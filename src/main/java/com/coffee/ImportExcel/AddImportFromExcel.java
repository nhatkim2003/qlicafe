package com.coffee.ImportExcel;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.HomeGUI;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AddImportFromExcel {

    private final MaterialBLL materialBLL = new MaterialBLL();

    private final SupplierBLL supplierBLL = new SupplierBLL();
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private Import_NoteBLL import_NoteBLL = new Import_NoteBLL();

    public Pair<Boolean, String> addImportFromExcel(File file) throws IOException {
        List<Shipment> shipments_excel;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);

            Pair<List<Shipment>, String> shipementsResult = readShipmentFromExcel(sheet1);
            shipments_excel = shipementsResult.getKey();
            errorAll.append(shipementsResult.getValue());

            if (errorAll.isEmpty()) {
                List<Pair<Import_Note, List<Shipment>>> pair = createImportNoteAndShipment(shipments_excel);
                // gọi hàm cập nhật lại total cho phiếu nhập và mã phiếu nhập
                process(pair);
                // thêm danh sách phiếu nhập và danh sách lô hàng
                addTodatabase(pair);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");

    }

    private void addTodatabase(List<Pair<Import_Note, List<Shipment>>> pair) {
        for (Pair<Import_Note, List<Shipment>> p : pair) {
            Import_Note import_note = p.getKey();
            List<Shipment> shipments = p.getValue();
            import_NoteBLL.addImport(import_note);
            for (Shipment shipment : shipments) {
                shipmentBLL.addShipment(shipment);
            }
        }
    }

    private void process(List<Pair<Import_Note, List<Shipment>>> pair) {
        int import_id = import_NoteBLL.getAutoID(import_NoteBLL.searchImport());
        int shipment_id = shipmentBLL.getAutoID(shipmentBLL.searchShipments());
        for (Pair<Import_Note, List<Shipment>> p : pair) {
            Import_Note import_note = p.getKey();
            import_note.setId(import_id++);
            List<Shipment> shipments = p.getValue();
            BigDecimal total = UpdateTotal(shipments);
            import_note.setTotal(total);

            for (Shipment shipment : shipments) {
                shipment.setId(shipment_id++);
                shipment.setImport_id(import_note.getId());
            }
        }
    }

    private BigDecimal UpdateTotal(List<Shipment> shipmet_of_import) {
        BigDecimal total = BigDecimal.valueOf(0.0);
        for (Shipment shipment : shipmet_of_import) {
            Material material = materialBLL.searchMaterials("id  ='" + shipment.getMaterial_id() + "'").get(0);
            total = total.add(BigDecimal.valueOf(material.getUnit_price() * Double.parseDouble(shipment.getQuantity().toString())));
        }
        return total;
    }

    private Pair<List<Shipment>, String> readShipmentFromExcel(Sheet sheet) {
        List<Shipment> shipments = new ArrayList<>();
        StringBuilder errorAll = new StringBuilder();

        Iterator<Row> iterator = sheet.iterator();

        if (iterator.hasNext()) {
            iterator.next(); // Bỏ qua dòng tiêu đề
        }

        // duyệt và kiểm tra từng dòng trong file để tạo ra list shipment
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            if (currentRow.getPhysicalNumberOfCells() > 0) {
                int rowNum = currentRow.getRowNum() + 1; // Số dòng bắt đầu từ 1
                StringBuilder errorRow = new StringBuilder();
                Map<String, Object> rowData = readShipmentRowData(currentRow);
                validateAttributeShipment(rowData, errorRow);
                // nếu sau khi qua hàm validateAttributes mà errorRow vẫn rỗng
                // thì tức là không có lỗi về định dạng đầu vào thì sẽ tạo đối tượng  và và thêm vào list
                if (errorRow.isEmpty()) {
                    Shipment shipment = createShipmet(rowData);
                    shipments.add(shipment);
                } else {
                    errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }

        // nếu tất cả các dòng đều không có lỗi thì check lô hàng có bị trùng nhau không
        if (errorAll.isEmpty()) {
            checkForDuplicates(shipments, errorAll);
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(shipments, "");
        }
        return new Pair<>(shipments, errorAll.toString());
    }

    private List<Pair<Import_Note, List<Shipment>>> createImportNoteAndShipment(List<Shipment> shipments) {
        List<Pair<Import_Note, List<Shipment>>> pair = new ArrayList<>();
        List<Import_Note> import_notes = create_ListImportNote_from_ListShipment(shipments);
        for (Import_Note import_note : import_notes) {
            List<Shipment> shipment_of_import = new ArrayList<>();
            for (Shipment shipment : shipments)
                if (shipment.getImport_id() == import_note.getId())
                    shipment_of_import.add(shipment);
            pair.add(new Pair<>(import_note, shipment_of_import));
        }
        return pair;
    }

    private Shipment createShipmet(Map<String, Object> rowData) {
        Shipment shipment = new Shipment();
        shipment.setImport_id((int) rowData.get("import_id"));
        shipment.setMaterial_id((int) rowData.get("material_id"));
        shipment.setSupplier_id((int) rowData.get("supplier_id"));
        shipment.setMfg((Date) rowData.get("mfg"));
        shipment.setExp((Date) rowData.get("exp"));
        shipment.setQuantity((int) rowData.get("quantity"));
        shipment.setRemain((int) rowData.get("quantity"));
        return shipment;
    }

    // lấy ra danh sách mã phiếu nhập từ danh sách lô hàng
    public List<Integer> getUniqueImportIds(List<Shipment> shipments) {
        Set<Integer> uniqueImportIds = new HashSet<>();
        for (Shipment shipment : shipments) {
            uniqueImportIds.add(shipment.getImport_id());
        }
        return new ArrayList<>(uniqueImportIds);
    }

    // tạo danh sách phiếu nhập từ danh sách lô hàng
    private List<Import_Note> create_ListImportNote_from_ListShipment(List<Shipment> shipments) {
        List<Integer> uniqueImportIdList = getUniqueImportIds(shipments);
        List<Import_Note> import_notes = new ArrayList<>();
        for (Integer i : uniqueImportIdList) {
            Import_Note import_note = new Import_Note();
            import_note.setId(i);
            import_note.setStaff_id((HomeGUI.staff.getId()));
            import_note.setTotal(BigDecimal.valueOf(0));
            import_note.setReceived_date(LocalDateTime.now());
            import_notes.add(import_note);
        }

        return import_notes;
    }


    private Map<String, Object> readShipmentRowData(Row currentRow) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : currentRow) {
            int columnIndex = cell.getColumnIndex() + 1;

            switch (columnIndex) {
                case 1: // mã phiếu nhập
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("import_id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 2: // tên nguyên liệu
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("material_name", cell.getStringCellValue());
                    }
                    break;

                case 3: // nhà cung cấp
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("supplier_name", cell.getStringCellValue());
                    }
                    break;
                case 4: // Ngày sản xuất
                    if (cell.getCellType() == CellType.NUMERIC) {
                        Date date = cell.getDateCellValue();
                        date = new java.sql.Date(date.getTime());
                        rowData.put("mfg", date);
                    } else if (cell.getCellType() == CellType.STRING) {
                        String dateString = cell.getStringCellValue();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            rowData.put("mfg", date);
                        } catch (ParseException ignored) {

                        }
                    }
                    break;

                case 5: // ngày hết hạn
                    if (cell.getCellType() == CellType.NUMERIC) {
                        Date date = cell.getDateCellValue();
                        date = new java.sql.Date(date.getTime());
                        rowData.put("exp", date);
                    } else if (cell.getCellType() == CellType.STRING) {
                        String dateString = cell.getStringCellValue();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            rowData.put("exp", date);
                        } catch (ParseException ignored) {

                        }
                    }
                    break;
                case 6: // số lượng
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("quantity", (int) cell.getNumericCellValue());
                    }
                    break;
                default:
                    break;
            }
        }

        return rowData;
    }

    private void validateAttributeShipment(Map<String, Object> rowData, StringBuilder errorRow) {
        if (!rowData.containsKey("import_id") || rowData.get("import_id") == null) {
            errorRow.append("Mã phiếu nhập bắt buộc phải là số nguyên.\n");
        }
        if (!rowData.containsKey("material_name") || rowData.get("material_name") == null) {
            errorRow.append("Tên nguyên liệu bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        } else {
            List<Material> materials = materialBLL.searchMaterials("name = '" + rowData.get("material_name") + "'");
            if (materials.isEmpty())
                errorRow.append("Tên nguyên liệu không tồn tại trong hệ thống .\n");
            else
                rowData.put("material_id", materials.get(0).getId());
        }
        if (!rowData.containsKey("supplier_name") || rowData.get("supplier_name") == null) {
            errorRow.append("Tên nhà cung cấp bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        } else {
            List<Supplier> suppliers = supplierBLL.searchSuppliers("name = '" + rowData.get("supplier_name") + "'");
            if (suppliers.isEmpty())
                errorRow.append("Tên nhà cung cấp không tồn tại trong hệ thống .\n");
            else
                rowData.put("supplier_id", suppliers.get(0).getId());
        }
        if (!rowData.containsKey("mfg") || rowData.get("mfg") == null) {
            errorRow.append("Ngày sản xuất hợp lệ.\n");
        }
        if (!rowData.containsKey("exp") || rowData.get("exp") == null) {
            errorRow.append("Ngày hết hạn hợp lệ.\n");
        }

    }

    private boolean isDuplicate(Shipment shipment1, Shipment shipment2) {
        return shipment1.getMaterial_id() == (shipment2.getMaterial_id())
                && shipment1.getSupplier_id() == (shipment2.getSupplier_id());

    }

    // check xem giữa các dòng trong file excel có nhân viên bị  trùng lịch làm việc không
    private void checkForDuplicates(List<Shipment> shipments, StringBuilder errorAll) {
        for (int i = 0; i < shipments.size(); i++) {
            Shipment shipment1 = shipments.get(i);
            for (int j = i + 1; j < shipments.size(); j++) {
                Shipment shipment2 = shipments.get(j);
                if (isDuplicate(shipment1, shipment2)) {
                    errorAll.append("- Dòng ").append(i + 2).append("Lô hàng trùng ngày sản xuất và ngày hết hạn với dòng ").append(j + 2).append("\n");
                }
            }
        }
    }


}
