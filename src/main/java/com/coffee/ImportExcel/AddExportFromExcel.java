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

public class AddExportFromExcel {

    private final MaterialBLL materialBLL = new MaterialBLL();

    private final Export_DetailBLL export_detailBLL = new Export_DetailBLL();
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private Export_NoteBLL export_NoteBLL = new Export_NoteBLL();

    public Pair<Boolean, String> addExportFromExcel(File file) throws IOException {
        List<Export_Detail> exportDetailList;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);

            Pair<List<Export_Detail>, String> export_detailResult = readExportDetailFromExcel(sheet1);
            exportDetailList = export_detailResult.getKey();
            errorAll.append(export_detailResult.getValue());

            if (errorAll.isEmpty()) {
                List<Pair<Export_Note, List<Export_Detail>>> pair = createExportNoteAndExportDetail(exportDetailList);
                process(pair);
                System.out.println(pair);
                addTodatabase(pair);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");

    }

    private void addTodatabase(List<Pair<Export_Note, List<Export_Detail>>> pair) {
        for (Pair<Export_Note, List<Export_Detail>> p : pair) {
            Export_Note export_note = p.getKey();
            List<Export_Detail> export_details = p.getValue();
            export_NoteBLL.addExport_Note(export_note);
            for (Export_Detail export_detail : export_details) {
                export_detailBLL.addExport_Detail(export_detail);
            }
        }
    }



    private BigDecimal UpdateTotal(List<Export_Detail> export_details_of_export) {
        BigDecimal total = BigDecimal.valueOf(0.0);
        for (Export_Detail export_detail : export_details_of_export) {
            Shipment shipment = shipmentBLL.searchShipments("id = " + export_detail.getShipment_id()).get(0);
            System.out.println("Danh sách lô hàng: " + shipment);
            Material material = materialBLL.searchMaterials("id  =" + shipment.getMaterial_id()).get(0);
            total = total.add(BigDecimal.valueOf(material.getUnit_price() * export_detail.getQuantity()));
        }
        return total;
    }

    private Pair<List<Export_Detail>, String> readExportDetailFromExcel(Sheet sheet) {
        List<Export_Detail> exportDetailList = new ArrayList<>();
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
                Map<String, Object> rowData = readExportDetailRowData(currentRow);
                validateAttributeExportDetail(rowData, errorRow);
                // nếu sau khi qua hàm validateAttributes mà errorRow vẫn rỗng
                // thì tức là không có lỗi về định dạng đầu vào thì sẽ tạo đối tượng  và và thêm vào list
                if (errorRow.isEmpty()) {
                    Export_Detail export_detail = createExportDetail(rowData);
                    exportDetailList.add(export_detail);
                } else {
                    errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }

        // nếu tất cả các dòng đều không có lỗi thì check lô hàng có bị trùng nhau không
        if (errorAll.isEmpty()) {
            checkForDuplicates(exportDetailList, errorAll);
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(exportDetailList, "");
        }
        return new Pair<>(exportDetailList, errorAll.toString());
    }
    private void process(List<Pair<Export_Note, List<Export_Detail>>> pair) {
        int export_id = export_NoteBLL.getAutoID(export_NoteBLL.searchExport_Note());
        for (Pair<Export_Note, List<Export_Detail>> p : pair) {
            Export_Note export_note = p.getKey();
            export_note.setId(export_id++);
            List<Export_Detail> export_details = p.getValue();
            BigDecimal total = UpdateTotal(export_details);
            export_note.setTotal(total);
        for(Export_Detail export_detail : export_details)
            export_detail.setExport_id(export_note.getId());
        }
    }
    private List<Pair<Export_Note, List<Export_Detail>>> createExportNoteAndExportDetail(List<Export_Detail> exportDetailList) {
        List<Pair<Export_Note, List<Export_Detail>>> pair = new ArrayList<>();
        List<Export_Note> export_notes = create_ListExportNote_from_ListExportDetail(exportDetailList);
        for (Export_Note export_note : export_notes) {
            List<Export_Detail> export_detail_of_export_note = new ArrayList<>();
            for (Export_Detail export_detail : exportDetailList)
                if (export_detail.getExport_id() == export_note.getId())
                    export_detail_of_export_note.add(export_detail);
            pair.add(new Pair<>(export_note, export_detail_of_export_note));
        }
        return pair;
    }

    private Export_Detail createExportDetail(Map<String, Object> rowData) {
        Export_Detail exportDetail = new Export_Detail();
        exportDetail.setExport_id((int) rowData.get("export_id"));
        exportDetail.setShipment_id((int) rowData.get("shipment_id"));
        exportDetail.setQuantity((int) rowData.get("quantity"));
        exportDetail.setReason((String) rowData.get("Reason"));
        return exportDetail;
    }

    public List<Integer> getUniqueExportID(List<Export_Detail> export_details) {
        Set<Integer> uniqueExportID = new HashSet<>();
        for (Export_Detail export_detail: export_details) {
            uniqueExportID.add(export_detail.getExport_id());
        }
        return new ArrayList<>(uniqueExportID);
    }

    private List<Export_Note> create_ListExportNote_from_ListExportDetail(List<Export_Detail> export_details) {
        List<Integer> uniqueExportList = getUniqueExportID(export_details);
        List<Export_Note> export_notes = new ArrayList<>();
        for (Integer i : uniqueExportList) {
            Export_Note export_note = new Export_Note();
            export_note.setId(i);
            export_note.setStaff_id((HomeGUI.staff.getId()));
            export_note.setTotal(BigDecimal.valueOf(0));
            export_note.setInvoice_date(java.sql.Date.valueOf(LocalDate.now()));
            export_notes.add(export_note);
        }

        return export_notes;
    }


    private Map<String, Object> readExportDetailRowData(Row currentRow) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : currentRow) {
            int columnIndex = cell.getColumnIndex() + 1;

            switch (columnIndex) {
                case 1: // mã phiếu xuất
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("export_id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 2: // mã lô
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("shipment_id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 3: // tên nguyên liệu
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("material_name", cell.getStringCellValue());
                    }
                    break;

                case 4: // số lượng
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("quantity", (int) cell.getNumericCellValue());
                    }
                    break;
                case 5: // lý do
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("Reason", cell.getStringCellValue());
                    }
                    break;

                default:
                    break;
            }
        }

        return rowData;
    }

    private void validateAttributeExportDetail(Map<String, Object> rowData, StringBuilder errorRow) {
        if (!rowData.containsKey("export_id") || rowData.get("export_id") == null) {
            errorRow.append("Mã phiếu xuất bắt buộc phải là số nguyên.\n");
        }
        if (!rowData.containsKey("shipment_id") || rowData.get("shipment_id") == null) {
            errorRow.append("Mã lô bắt buộc phải là số nguyên.\n");
        }
        if (!rowData.containsKey("material_name") || rowData.get("material_name") == null) {
            errorRow.append("Tên nguyên liệu bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }
        if (!rowData.containsKey("quantity") || rowData.get("quantity") == null) {
            errorRow.append("Số lượng bắt buộc phải là số nguyên.\n");
        }
        if (!rowData.containsKey("Reason") || rowData.get("Reason") == null) {
            errorRow.append("Lý do bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }
        if (errorRow.isEmpty()) {
            int shipment_id = (int) rowData.get("shipment_id");
            List<Shipment> shipments = shipmentBLL.searchShipments("id = " + shipment_id);
            if (shipments.isEmpty())
                errorRow.append("Mã lô hàng không tồn tại trong hệ thống .\n");
        }
        if (errorRow.isEmpty()) {
            String material_name = (String) rowData.get("material_name");
            List<Material> materials = materialBLL.searchMaterials("name = '" + material_name + "'");
            if (materials.isEmpty())
                errorRow.append("Tên nguyên liệu không tồn tại trong hệ thống .\n");
            else {
                int material_id = materials.get(0).getId();
                List<Shipment> shipments = shipmentBLL.searchShipments("id = " + rowData.get("shipment_id"), "material_id = " + material_id);
                if (shipments.isEmpty())
                    errorRow.append(material_name).append(" không thuộc lô hàng ").append((int) rowData.get("shipment_id")).append(" .\n");
                else
                    rowData.put("material_id", materials.get(0).getId());
            }
        }
        if (errorRow.isEmpty()) {
            List<Shipment> shipments = shipmentBLL.searchShipments("id = " + rowData.get("shipment_id"));
            double remain = shipments.get(0).getRemain();
            if (remain <= 0)
                errorRow.append("Lô "+ rowData.get("shipment_id") +" đã hết hàng .\n");
            else if (remain < (int) rowData.get("quantity"))
                errorRow.append("Số lượng xuất vượt quá số lượng tồn trong lô hàng .\n");
        }
        if (errorRow.isEmpty()) {
            String reason = (String) rowData.get("Reason");
            if (!(reason.equals("Bán") || !reason.equals("Hủy")))
                errorRow.append("Lý do phài là Bán hoặc Hủy .\n");
        }


    }

    private boolean isDuplicate(Export_Detail shipment1, Export_Detail shipment2) {
        return shipment1.getShipment_id() == (shipment2.getShipment_id())
                && shipment1.getExport_id() == (shipment2.getExport_id());

    }

    // check xem giữa các dòng trong file excel có nhân viên bị  trùng lịch làm việc không
    private void checkForDuplicates(List<Export_Detail> shipments, StringBuilder errorAll) {
        for (int i = 0; i < shipments.size(); i++) {
            Export_Detail shipment1 = shipments.get(i);
            for (int j = i + 1; j < shipments.size(); j++) {
                Export_Detail shipment2 = shipments.get(j);
                if (isDuplicate(shipment1, shipment2)) {
                    errorAll.append("- Dòng ").append(i + 2).append("Lô hàng trùng với dòng ").append(j + 2).append("\n");
                }
            }
        }
    }

}
