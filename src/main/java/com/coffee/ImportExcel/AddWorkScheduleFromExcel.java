package com.coffee.ImportExcel;

import com.coffee.BLL.StaffBLL;
import com.coffee.BLL.Work_ScheduleBLL;
import com.coffee.DTO.Staff;
import com.coffee.DTO.Work_Schedule;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddWorkScheduleFromExcel {
    Work_ScheduleBLL work_ScheduleBLL = new Work_ScheduleBLL();

    public Pair<Boolean, String> addWorkScheduleFromExcel(File file) throws IOException {
        List<Work_Schedule> work_schedules;
        StringBuilder errorAll = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheetAt(0);

            Pair<List<Work_Schedule>, String> Work_SchedulesResult = readWork_ScheduleFromExcel(sheet1);
            work_schedules = Work_SchedulesResult.getKey();
            errorAll.append(Work_SchedulesResult.getValue());


            // Thêm dữ liệu vào cơ sở dữ liệu nếu không có bất kì lỗi nào
            if (errorAll.isEmpty()) {
                // vì đã check hết rồi nên hàm thêm này chắc chắn đúng nên không cần check ở đây nữa mà cứ thêm vào database
                work_ScheduleBLL.addWork_schedule(work_schedules);
            } else {
                return new Pair<>(false, errorAll.toString());
            }

        } catch (IOException e) {
            return new Pair<>(false, "Lỗi khi đọc file Excel.");
        }
        return new Pair<>(true, "");

    }


    private Pair<List<Work_Schedule>, String> readWork_ScheduleFromExcel(Sheet sheet) {
        List<Work_Schedule> work_schedules = new ArrayList<>();
        StringBuilder errorAll = new StringBuilder();

        List<Work_Schedule> work_schedules1 = work_ScheduleBLL.searchWork_schedules();
        work_schedules1.sort(Comparator.comparing(Work_Schedule::getId));
        int work_schedule_id = work_ScheduleBLL.getAutoID(work_schedules1);

        Iterator<Row> iterator = sheet.iterator();

        if (iterator.hasNext()) {
            iterator.next(); // Bỏ qua dòng tiêu đề
        }

        // duyệt và kiểm tra từng dòng trong file để tạo ra list work_schedule
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            if (currentRow.getPhysicalNumberOfCells() > 0) {
                int rowNum = currentRow.getRowNum() + 1; // Số dòng bắt đầu từ 1
                StringBuilder errorRow = new StringBuilder();
                Map<String, Object> rowData = readWork_ScheduleRowData(currentRow);
                validateAttributeWork_schedule(rowData, errorRow);
                // nếu sau khi qua hàm validateAttributeWork_schedule mà errorRow vẫn rỗng
                // thì tức là không có lỗi về định dạng đầu vào thì sẽ tạo đối tượng work_schedule và và thêm vào list
                if (errorRow.isEmpty()) {
                    Work_Schedule work_schedule = createWorkSchedule(rowData);
                    work_schedule.setId(work_schedule_id);
                    work_schedules.add(work_schedule);
                    work_schedule_id += 1;
                } else {
                    errorAll.append(" - Dòng").append(rowNum).append(":\n").append(errorRow).append("\n");
                }
            }
        }

        // nếu tất cả các dòng đều không có lỗi thì check thêm ca làm việc có bị trùng nhau không
        if (errorAll.isEmpty()) {
            checkForDuplicates(work_schedules, errorAll);
            checkExistDatabase(work_schedules, errorAll);
        }
        if (errorAll.isEmpty()) {
            return new Pair<>(work_schedules, "");
        }
        return new Pair<>(work_schedules, errorAll.toString());
    }

    private Work_Schedule createWorkSchedule(Map<String, Object> rowData) {
        Integer staff_id = (Integer) rowData.get("staff_id");
        Date date = (Date) rowData.get("date");
        Integer shift = (Integer) rowData.get("shift");
        return new Work_Schedule(0, staff_id, date, null, null, shift, "Không");
    }

    private Map<String, Object> readWork_ScheduleRowData(Row currentRow) {
        Map<String, Object> rowData = new HashMap<>();
        for (Cell cell : currentRow) {
            int columnIndex = cell.getColumnIndex() + 1;

            switch (columnIndex) {
                case 1: // id
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("staff_id", (int) cell.getNumericCellValue());
                    }
                    break;
                case 2: // Name
                    if (cell.getCellType() == CellType.STRING) {
                        rowData.put("staff_name", cell.getStringCellValue());
                    }
                    break;
                case 3: // Date
                    if (cell.getCellType() == CellType.NUMERIC) {
                        Date date = cell.getDateCellValue();
                        date = new java.sql.Date(date.getTime());
                        rowData.put("date", date);
                    } else if (cell.getCellType() == CellType.STRING) {
                        String dateString = cell.getStringCellValue();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            rowData.put("date", date);
                        } catch (ParseException ignored) {

                        }
                    }
                    break;

                case 4: // shift
                    if (cell.getCellType() == CellType.NUMERIC) {
                        rowData.put("shift", (int) cell.getNumericCellValue());
                    }
                    break;
                default:
                    break;
            }
        }

        return rowData;
    }

    private void validateAttributeWork_schedule(Map<String, Object> rowData, StringBuilder errorRow) {
        List<Staff> staffs;
        String name = "";
        if (!rowData.containsKey("staff_id") || rowData.get("staff_id") == null) {
            errorRow.append("Mã nhân viên bắt buộc phải là kiểu số nguyên và  không được để trống .\n");
        } else {
            staffs = new StaffBLL().searchStaffs("id = '" + rowData.get("staff_id") + "'");
            if (!staffs.isEmpty())
                // lấy ra tên nhân viên để xuống dưới kiểm tra xem tên nhân viên có đúng với mã nhân viên không
                name = staffs.get(0).getName();
            else errorRow.append("Mã nhân viên không tồn tại trong hệ thống .\n");
        }

        if (!rowData.containsKey("staff_name") || rowData.get("staff_name") == null) {
            errorRow.append("Tên nhân viên bắt buộc phải là kiểu chuỗi và không được để trống .\n");
        } else {
            if (!rowData.get("staff_name").equals(name))
                errorRow.append("Tên nhân viên này không thuộc về mã nhân viên ").append(rowData.get("staff_id")).append(".\n");
        }

        if (!rowData.containsKey("date") || rowData.get("date") == null) {
            errorRow.append("Ngày làm việc hợp lệ.\n");
        }

        List<Integer> shifts = List.of(1, 2, 3);
        if (!rowData.containsKey("shift") || rowData.get("shift") == null) {
            errorRow.append("Ca làm việc bắt buộc phải là số nguyên: 1,2,3.\n");
        } else {
            Integer s = (int) rowData.get("shift");
            if (!shifts.contains(s)) {
                errorRow.append("\"Ca làm việc bắt buộc phải là số nguyên: 1,2,3..\n");
            }
        }

    }

    private boolean isDuplicate(Work_Schedule work_schedule1, Work_Schedule work_schedule2) {
        return work_schedule1.getStaff_id() == (work_schedule2.getStaff_id())
                && work_schedule1.getDate().equals(work_schedule2.getDate())
                && work_schedule1.getShift() == (work_schedule2.getShift());
    }

    // check xem giữa các dòng trong file excel có nhân viên bị  trùng lịch làm việc không
    private void checkForDuplicates(List<Work_Schedule> workSchedules, StringBuilder errorAll) {
        for (int i = 0; i < workSchedules.size(); i++) {
            Work_Schedule work_schedule1 = workSchedules.get(i);
            for (int j = i + 1; j < workSchedules.size(); j++) {
                Work_Schedule work_schedule2 = workSchedules.get(j);
                if (isDuplicate(work_schedule1, work_schedule2)) {
                    errorAll.append("- Dòng ").append(i + 2).append(" bị trùng ca làm việc với dòng ").append(j + 2).append("\n");
                }
            }
        }
    }

    // check xem nhân viên đó đã có ca làm việc trong hệ thống chưa
    private void checkExistDatabase(List<Work_Schedule> workSchedules, StringBuilder errorAll) {
        for (int i = 0; i < workSchedules.size(); i++) {
            Work_Schedule work_schedule = workSchedules.get(i);
            Pair<Boolean, String> result = work_ScheduleBLL.exists(work_schedule);
            if (result.getKey())
                errorAll.append("- Dòng ").append(i + 2).append(" Nhân viên đã có  ca làm việc trong hệ thống ").append("\n");
        }
    }
}
