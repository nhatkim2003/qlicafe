package com.coffee.ImportExcel;

import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Staff;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AddEmployeeFromExcel {
    StaffBLL staffBLL = new StaffBLL();

    public Pair<Boolean, String> addStaffFromExcel(File file) throws IOException {
        List<Staff> staffs;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);

            Pair<List<Staff>, String> staffsResult = readStaffFromExcel(sheet1);
            staffs = staffsResult.getKey();
            errorAll.append(staffsResult.getValue());


            // Thêm dữ liệu vào cơ sở dữ liệu nếu không có lỗi
            if (errorAll.isEmpty()) {
                addToDatabase(staffs);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");

    }


    private Pair<List<Staff>, String> readStaffFromExcel(Sheet sheet) {
        List<Staff> staffs = new ArrayList<>();
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
                Staff staff = readStaffRowData(currentRow);

                validateAttributeStaff(staff, errorRow);
                if (errorRow.isEmpty()) {
                    staffs.add(staff);
                } else {
                    errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(staffs, "");
        }
        return new Pair<>(staffs, errorAll.toString());
    }

    private Staff readStaffRowData(Row currentRow) {
        Staff staff = new Staff();
        for (Cell cell : currentRow) {
            int columnIndex = cell.getColumnIndex() + 1;

            switch (columnIndex) {
                case 1: // Name
                    if (cell.getCellType() == CellType.STRING) {
                        staff.setName(cell.getStringCellValue());
                    }
                    break;
                case 2: // No
                    if (cell.getCellType() == CellType.STRING) {
                        String no = cell.getStringCellValue();
                        staff.setStaffNo(no);
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        String no = "0" + (long) cell.getNumericCellValue();
                        staff.setStaffNo(no);
                    }
                    break;
                case 3: // Gender
                    if (cell.getCellType() == CellType.STRING) {
                        String gender = cell.getStringCellValue();
                        if (gender.equals("Nam"))
                            staff.setGender(false);
                        else if (gender.equals("Nữ"))
                            staff.setGender(true);

                    }
                    break;
                case 4: // birth_date
                    if (cell.getCellType() == CellType.NUMERIC) {
                        Date birth_date = cell.getDateCellValue();
                        birth_date = new java.sql.Date(birth_date.getTime());
                        staff.setBirthdate(birth_date);
                    } else if (cell.getCellType() == CellType.STRING) {
                        String birthDateString = cell.getStringCellValue();
                        try {
                            Date birth_date = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateString);
                            staff.setBirthdate(birth_date);
                        } catch (ParseException ignored) {

                        }
                    }
                    break;
                case 5: // phone
                    if (cell.getCellType() == CellType.STRING) {
                        String phone = cell.getStringCellValue();
                        staff.setPhone(phone);
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        String phone = "0" + (int) cell.getNumericCellValue();
                        staff.setPhone(phone);
                    }
                    break;
                case 6: //address
                    if (cell.getCellType() == CellType.STRING) {
                        String address = cell.getStringCellValue();
                        staff.setAddress(address);
                    }
                    break;
                case 7: // email
                    if (cell.getCellType() == CellType.STRING) {
                        String email = cell.getStringCellValue();
                        staff.setEmail(email);
                    }
                    break;
                default:
                    break;
            }
        }

        return staff;
    }

    private void validateAttributeStaff(Staff staff, StringBuilder errorRow) {

        if (staff.getName() == null) {
            errorRow.append("Tên nhân viên bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }
        if (staff.getStaffNo() == null) {
            errorRow.append("Số căng cước bắt buộc phải là kiểu chuỗi hoặc kiểu số và không được để trống .\n");
        }

        if (staff.isGender() == null) {
            errorRow.append("Giới tính của nhân viên bắt buộc phải là Nam hoặc Nữ\n");
        }
        if (staff.getBirthdate() == null)
            errorRow.append("Ngày sinh của nhân viên không hợp lệ\n");

        if (staff.getPhone() == null) {
            errorRow.append("Số điện thoại bắt buộc phải là kiểu chuỗi hoặc kiểu số và không được để trống .\n");
        }

        if (staff.getAddress() == null) {
            errorRow.append("Địa chỉ bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }
        if (staff.getEmail() == null) {
            errorRow.append("Email bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        }

        if (errorRow.isEmpty()) {
            Pair<Boolean, String> result = staffBLL.validateStaffAll(staff);
            if (!result.getKey())
                errorRow.append(result.getValue()).append("\n");
        }
    }

    private void addToDatabase(List<Staff> staffs) {
        int supplier_id = staffBLL.getAutoID(staffBLL.searchStaffs());
        for (Staff staff : staffs) {
            staff.setId(supplier_id);
            staffBLL.addStaff(staff);
            supplier_id += 1;
        }

    }
}
