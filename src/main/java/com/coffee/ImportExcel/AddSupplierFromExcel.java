package com.coffee.ImportExcel;

import com.coffee.BLL.SupplierBLL;
import com.coffee.DTO.Supplier;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class AddSupplierFromExcel {
    SupplierBLL supplierBLL = new SupplierBLL();
    public Pair<Boolean, String> addSupplierFromExcel(File file) throws IOException {
        List<Supplier> suppliers;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);

            Pair<List<Supplier>, String> suppliersResult = readSupplierFromExcel(sheet1);
            suppliers = suppliersResult.getKey();
            errorAll.append(suppliersResult.getValue());


            // Thêm dữ liệu vào cơ sở dữ liệu nếu không có lỗi
            if (errorAll.isEmpty()) {
                addToDatabase(suppliers);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");

    }


    private Pair<List<Supplier>, String> readSupplierFromExcel(Sheet sheet) {
        List<Supplier> suppliers = new ArrayList<>();
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
                Supplier supplier = readSupplierRowData(currentRow);

                validateAttributeSupplier(supplier, errorRow);
                if (errorRow.isEmpty()) {
                    suppliers.add(supplier);
                } else {
                        errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(suppliers, "");
        }
        return new Pair<>(suppliers, errorAll.toString());
    }

    private Supplier readSupplierRowData(Row currentRow) {
       Supplier supplier = new Supplier();
        for (Cell cell : currentRow) {
            int columnIndex = cell.getColumnIndex() + 1;

            switch (columnIndex) {
                case 1: // Name
                    if (cell.getCellType() == CellType.STRING) {
                      supplier.setName(cell.getStringCellValue());
                    }

                    break;
                case 2: // phone
                    if (cell.getCellType() == CellType.STRING) {
                        String phone = cell.getStringCellValue();
                        supplier.setPhone(phone);
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        String phone = "0" + (int) cell.getNumericCellValue();
                        supplier.setPhone(phone);
                    }
                    else supplier.setPhone(null);
                    break;
                case 3: //address
                    if (cell.getCellType() == CellType.STRING) {
                        String address = cell.getStringCellValue();
                        supplier.setAddress(address);
                    }

                    break;
                case 4: // email
                    if (cell.getCellType() == CellType.STRING) {
                        String email = cell.getStringCellValue();
                        supplier.setEmail(email);
                    }
                    break;
                default:
                    break;
            }
        }

        return supplier;
    }

    private void validateAttributeSupplier(Supplier supplier, StringBuilder errorRow) {
        // nếu ô trong excel để trống hoặc không phải là kiểu chuỗi thì sẽ thông báo này
        if (supplier.getName() == null) {
            errorRow.append("Tên nhà cung cấp bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }
        if (supplier.getPhone() == null) {
            errorRow.append("Số điện thoại nhà cung cấp bắt buộc phải là kiểu chuỗi và không được để trống\n");
        }

        if (supplier.getAddress() == null) {
            errorRow.append("Địa chỉ nhà cung cấpbắt buộc phải là kiểu chuỗi và không được để trống.\n");
        }

        if (supplier.getEmail() == null) {
            errorRow.append("Email nhà cung cấp bắt buộc phải là kiểu chuỗi và không được để trống\n");
        }

        if(errorRow.isEmpty()){
            Pair<Boolean,String> result = supplierBLL.validateSupplierAll(supplier);
            if(!result.getKey())
                errorRow.append(result.getValue()).append("\n");
        }
    }

    private void addToDatabase(List<Supplier> suppliers) {
        int supplier_id = supplierBLL.getAutoID(supplierBLL.searchSuppliers());
        for (Supplier supplier : suppliers){
            supplier.setId(supplier_id);
            supplierBLL.addSupplier(supplier);
            supplier_id+=1;
        }

    }
}
