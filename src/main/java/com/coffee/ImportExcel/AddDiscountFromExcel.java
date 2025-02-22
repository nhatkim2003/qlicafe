package com.coffee.ImportExcel;

import com.coffee.BLL.DiscountBLL;
import com.coffee.BLL.Discount_DetailBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.DTO.Discount;
import com.coffee.DTO.Discount_Detail;
import com.coffee.DTO.Product;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddDiscountFromExcel {

    private final ProductBLL productBLL = new ProductBLL();
    private final Discount_DetailBLL discountDetailBLL = new Discount_DetailBLL();
    private final DiscountBLL discountBLL = new DiscountBLL();

    public Pair<Boolean, String> addDiscountsFromExcel(File file) throws IOException {
        List<Discount> discounts;
        List<Discount_Detail> discountDetails;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet1 = workbook.getSheetAt(0);
            Sheet sheet2 = workbook.getSheetAt(1);

            // Đọc dữ liệu từ file Excel và tổng hợp các lỗi kiểm tra đầu vào: vd sai định dạng ô hoặc để trống ...
            Pair<List<Discount>, String> discountsResult = readDiscountsFromExcel(sheet1);
            discounts = discountsResult.getKey();
            errorAll.append(discountsResult.getValue());

            Pair<List<Discount_Detail>, String> detailsResult = readDiscountDetailsFromExcel(sheet2);
            discountDetails = detailsResult.getKey();
            errorAll.append(detailsResult.getValue());

            // Xử lý và kiểm tra các lỗi logic và trả về các discount và discount_detail tương ứng của discount đó
            List<Pair<Discount, List<Discount_Detail>>> discountDatas = new ArrayList<>();
            if (errorAll.isEmpty()) {
                discountDatas = processAndValidateData(discounts, discountDetails, errorAll);
            }

            // Thêm dữ liệu vào cơ sở dữ liệu nếu không có lỗi
            if (errorAll.isEmpty()) {
                addToDatabase(discountDatas);
            } else {
                return new Pair<>(false, errorAll.toString());
            }
        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");
    }

    private List<Pair<Discount, List<Discount_Detail>>> processAndValidateData(List<Discount> discounts, List<Discount_Detail> discountDetails, StringBuilder errorAll) {

        List<Pair<Discount, List<Discount_Detail>>> discountDatas = new ArrayList<>();
        int id = discountBLL.getAutoID(discountBLL.searchDiscounts());

        // Lọc và cập nhật id cho các discount_details theo discount
        for (Discount discount : discounts) {
            List<Discount_Detail> discountDetails_of_discount = new ArrayList<>();

            for (int i = 0; i < discountDetails.size(); i++) {
                if (discountDetails.get(i).getDiscount_id() == discount.getId()) {
                    discountDetails.get(i).setDiscount_id(id);
                    discountDetails_of_discount.add(discountDetails.get(i));
                    discountDetails.remove(i);
                    i--;
                }
            }

            discount.setId(id);
            id += 1;

            // Kiểm tra xem discount có chi tiết giảm giá không
            if (discountDetails_of_discount.isEmpty()) {
                errorAll.append("Mã giảm giá ").append(discount.getId()).append(" chưa có chi tiết giảm giá.\n");
            }

            discountDatas.add(new Pair<>(discount, discountDetails_of_discount));
        }

        // Kiểm tra xem có chi tiết giảm giá nào không có discount tương ứng không
        if (!discountDetails.isEmpty()) {
            discountDetails.forEach(discountDetail -> errorAll.append("Mã giảm giá ").append(discountDetail.getDiscount_id()).append(" không tồn tại phiếu giảm giá.\n"));

        }
        return discountDatas;
    }

    private void addToDatabase(List<Pair<Discount, List<Discount_Detail>>> discountDatas) {
        // Duyệt qua danh sách discount và chi tiết giảm giá của từng discount và thêm vào database
        for (Pair<Discount, List<Discount_Detail>> discountData : discountDatas) {
            Discount discount = discountData.getKey();
            List<Discount_Detail> details = discountData.getValue();
            updateStatusDicount();// trước khi thêm mã giảm giá mới cần tắt tất cả các mã giảm giá đang áp dụng
            discountBLL.addDiscount(discount);

            for (Discount_Detail detail : details) {
                discountDetailBLL.addDiscount_Detail(detail);
            }
        }
    }
    private void updateStatusDicount() {
        List<Discount> discountList = discountBLL.searchDiscounts("status =0");

        for (Discount discount : discountList) {
            discount.setStatus(true);
            Pair<Boolean, String> result = discountBLL.updateStatusDiscount(discount);
            if (!result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public Pair<List<Discount>, String> readDiscountsFromExcel(Sheet sheet) {
        List<Discount> discounts = new ArrayList<>();
        StringBuilder errorAll = new StringBuilder();

        Iterator<Row> iterator = sheet.iterator();

        if (iterator.hasNext()) {
            iterator.next(); // Bỏ qua dòng tiêu đề
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            if (currentRow.getPhysicalNumberOfCells() > 0) {
                int rowNum = currentRow.getRowNum() + 1; // Số dòng bắt đầu từ 1
                StringBuilder errorRow = new StringBuilder();
                Map<String, Object> rowData = processDiscountRowData(currentRow, errorRow);

                validateAttributeDiscount(rowData, errorRow);
                if (errorRow.isEmpty()) {
                    Discount discount = createDiscount(rowData);
                    discounts.add(discount);
                } else {
                    if(errorAll.isEmpty())
                        errorAll.append("Lỗi ở sheet Giảm giá ").append(":\n").append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                    else
                        errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");

                }
            }
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(discounts, "");
        }
        return new Pair<>(discounts, errorAll.toString());
    }

    private Map<String, Object> processDiscountRowData(Row row, StringBuilder errorRow) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex() + 1; // Số cột bắt đầu từ 1

            switch (columnIndex) {
                case 1: // ID
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("id", (int) cell.getNumericCellValue());
                    } else {
                        errorRow.append("\t\tô ID: Giá trị không hợp lệ \n");
                    }

                    break;
                case 2: // Name
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("name", cell.getStringCellValue());
                    } else {
                        errorRow.append("\t\tô Name: Định dạng ô không phải kiểu chuỗi. \n ");
                    }
                    break;
                case 3: // Start_date
                    if (cell.getCellType() == CellType.NUMERIC) {
                        Date start_date = cell.getDateCellValue();
                        start_date = new java.sql.Date(start_date.getTime());
                        rowData.put("start_date", start_date);
                    } else if (cell.getCellType() == CellType.STRING) {
                        String startDateString = cell.getStringCellValue();
                        try {
                            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
                            rowData.put("start_date", startDate);
                        } catch (ParseException e) {
                            errorRow.append("\t\tô Start_date: Định dạng ngày không hợp lệ \n ");
                        }
                    }
                    break;
                case 4: // End_date
                    if (cell.getCellType() == CellType.NUMERIC) {
                        Date end_date = cell.getDateCellValue();
                        end_date = new java.sql.Date(end_date.getTime());
                        rowData.put("end_date", end_date);
                    } else if (cell.getCellType() == CellType.STRING) {
                        String endDateString = cell.getStringCellValue();
                        try {
                            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
                            rowData.put("end_date", endDate);
                        } catch (ParseException e) {
                            errorRow.append("\t\tô End_date: Định dạng ngày không hợp lệ. \n");
                        }
                    }

                    break;
                case 5: // Type
                    if (cell.getCellType() == CellType.STRING) {
                        String typeString = cell.getStringCellValue();
                        Boolean type = null;
                        if (typeString.equals("Giảm theo sản phẩm")) {
                            type = false;
                        } else if (typeString.equals("Giảm theo hóa đơn")) {
                            type = true;
                        } else {
                            errorRow.append("\t\tô Type: Giá trị không hợp lệ\n");
                        }
                        rowData.put("type", type);
                    } else {
                        errorRow.append("\t\tô hình thức: Không phải kiểu chuỗi. \n");
                    }
                    break;
                case 6: // Status
                    if (cell.getCellType() == CellType.STRING) {
                        String statusString = cell.getStringCellValue();
                        Boolean status = null;
                        if (statusString.equals("Đang áp dụng")) {
                            status = false;
                        } else if (statusString.equals("Ngưng áp dụng")) {
                            status = true;
                        } else {
                            errorRow.append("\t\tô trạng thái: Giá trị không hợp lệ\n");
                        }
                        rowData.put("status", status);
                    } else {
                        errorRow.append("\t\tô trạng thái: Không phải kiểu chuỗi. \n");
                    }
                    break;
                default:
                    break;
            }
        }
        return rowData;
    }

    private void validateAttributeDiscount(Map<String, Object> rowData, StringBuilder errorRow) {
        if (!rowData.containsKey("id") || rowData.get("id") == null) {
            errorRow.append("Mã giảm giá không hợp lệ.\n");
        }

        if (!rowData.containsKey("name") || rowData.get("name") == null || ((String) rowData.get("name")).isEmpty()) {
            errorRow.append("Tên giảm giá không hợp lệ.\n");
        }

        if (!rowData.containsKey("start_date") || rowData.get("start_date") == null) {
            errorRow.append("Ngày bắt đầu không hợp lệ.\n");
        }

        if (!rowData.containsKey("end_date") || rowData.get("end_date") == null) {
            errorRow.append("Ngày kết thúc không hợp lệ.\n");
        }

        if (!rowData.containsKey("type") || rowData.get("type") == null) {
            errorRow.append("Loại giảm giá không hợp lệ.\n");
        }

        if (!rowData.containsKey("status") || rowData.get("status") == null) {
            errorRow.append("Trạng thái giảm giá không hợp lệ.\n");
        }

        Pair<Boolean, String> result = discountBLL.validateDate((Date) rowData.get("start_date"), (Date) rowData.get("end_date"));
        if (!result.getKey()) {
            errorRow.append("Ngày bắt đầu và kết thúc: ").append(result.getValue()).append("\n");
        }
    }

    private Discount createDiscount(Map<String, Object> rowData) {
        Integer id = (Integer) rowData.get("id");
        String name = (String) rowData.get("name");
        Date startDate = (Date) rowData.get("start_date");
        Date endDate = (Date) rowData.get("end_date");
        Boolean type = (Boolean) rowData.get("type");
        Boolean status = (Boolean) rowData.get("status");

        return new Discount(id, name, startDate, endDate, type, status);
    }

    public Pair<List<Discount_Detail>, String> readDiscountDetailsFromExcel(Sheet sheet) {
        List<Discount_Detail> discountDetails = new ArrayList<>();
        StringBuilder errorAll = new StringBuilder();
        // Bỏ qua dòng tiêu đề
        Iterator<Row> iterator = sheet.iterator();
        if (iterator.hasNext()) {
            iterator.next();
        }
        int rowNum = 1;
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            rowNum++;

            Map<String, Object> rowData = processDiscountDetailRow(currentRow);
            StringBuilder errorRow = new StringBuilder();

            validateAttributesDetail(rowData, errorRow);

            if (errorRow.isEmpty()) {
                Discount_Detail discount_detail = createDiscountDetail(rowData);
                discountDetails.add(discount_detail);
            } else {
                if(errorAll.isEmpty())
                    errorAll.append("Lỗi ở Sheet chi tiết giảm giá ").append("\n - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                else
                    errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
            }
        }
            checkForDuplicates(discountDetails, errorAll);
        // Kiểm tra sự trùng lặp giữa các chi tiết giảm giá

        return new Pair<>(discountDetails, errorAll.toString());
    }

    private Map<String, Object> processDiscountDetailRow(Row row) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex() + 1; // Số cột bắt đầu từ 1

            switch (columnIndex) {
                case 1: // discount_id
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 2: // Tên sản phẩm
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("name", cell.getStringCellValue());
                    }
                    break;
                case 3: // size
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("size", cell.getStringCellValue());
                    }
                    break;
                case 4: //quantity
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("quantity", cell.getNumericCellValue());
                    }
                    break;
                case 5: // percent
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("percent", cell.getNumericCellValue());
                    }
                    break;
                case 6: // discountBill
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("discountBill", cell.getNumericCellValue());
                    }
                    break;
                default:
                    break;
            }
        }
        return rowData;
    }

    private void validateAttributesDetail(Map<String, Object> rowData, StringBuilder errorRow) {
        if (!rowData.containsKey("id") || rowData.get("id") == null) errorRow.append("Mã giảm giá không hợp lệ.\n");

        if (!rowData.containsKey("name") || rowData.get("name") == null || ((String) rowData.get("name")).isEmpty()) {
            errorRow.append("Tên sản phẩm không hợp lệ.\n");
        } else {
            String name = (String) rowData.get("name");
            if (name.equals("Không")) rowData.put("product_id", 0);
            else {
                List<Product> products = productBLL.searchProducts("name ='" + name + "'");
                if (products.isEmpty()) errorRow.append("Tên sản phẩm không tồn tại trong hệ thống.\n");
                else rowData.put("product_id", products.get(0).getId());
            }
        }

        if (!rowData.containsKey("size") || rowData.get("size") == null || ((String) rowData.get("size")).isEmpty())
            errorRow.append("Kích thước không hợp lệ.\n");

        if (!rowData.containsKey("quantity") || rowData.get("quantity") == null || (Double) rowData.get("quantity") < 0)
            errorRow.append("Số lượng không hợp lệ.\n");

        if (!rowData.containsKey("percent") || rowData.get("percent") == null || (Double) rowData.get("percent") <= 0 || (Double) rowData.get("percent") > 100)
            errorRow.append("Phần trăm giảm giá không hợp lệ.\n");

        if (!rowData.containsKey("discountBill") || rowData.get("discountBill") == null || (Double) rowData.get("discountBill") < 0)
            errorRow.append("Giá giảm giá không hợp lệ.\n");

    }

    private Discount_Detail createDiscountDetail(Map<String, Object> rowData) {
        Integer id = (Integer) rowData.get("id");
        Integer product_id = (Integer) rowData.get("product_id");
        String size = (String) rowData.get("size");
        Double quantity = (Double) rowData.get("quantity");
        Double percent = (Double) rowData.get("percent");
        Double discountBill = (Double) rowData.get("discountBill");

        return new Discount_Detail(id, product_id, size, quantity, percent, discountBill);
    }

    private boolean isDuplicate(Discount_Detail detail1, Discount_Detail detail2) {
        if (detail1.getDiscount_id() == detail2.getDiscount_id() && detail1.getProduct_id() == 0 && detail2.getProduct_id() == 0) {
            return detail1.getDiscountBill() == detail2.getDiscountBill();
        } else {
            return detail1.getDiscount_id() == detail2.getDiscount_id() && detail1.getProduct_id() == detail2.getProduct_id() && detail1.getSize().equals(detail2.getSize()) && detail1.getQuantity() == detail2.getQuantity();
        }
    }

    private void checkForDuplicates(List<Discount_Detail> discountDetails, StringBuilder errorAll) {
        for (int i = 0; i < discountDetails.size(); i++) {
            Discount_Detail detail1 = discountDetails.get(i);
            for (int j = i + 1; j < discountDetails.size(); j++) {
                Discount_Detail detail2 = discountDetails.get(j);
                if (isDuplicate(detail1, detail2)) {
                        if (errorAll.isEmpty())
                            errorAll.append("- Lỗi sheet Chi tiêt giảm giá \n").append("- Dòng ").append(i + 2).append(" bị trùng điều kiện với dòng ").append(j + 2).append("\n");
                        else
                            errorAll.append("- Dòng ").append(i + 2).append(" bị trùng điều kiện với dòng ").append(j + 2).append("\n");
                }
            }
        }
    }
}
