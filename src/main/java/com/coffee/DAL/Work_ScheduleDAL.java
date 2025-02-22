package com.coffee.DAL;

import com.coffee.DTO.Work_Schedule;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Work_ScheduleDAL extends Manager {
    public Work_ScheduleDAL() {
        super("work_schedule",
                List.of("id",
                        "staff_id",
                        "date",
                        "check_in",
                        "check_out",
                        "shift",
                        "notice"));
    }

    public List<Work_Schedule> convertToWork_schedules(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Work_Schedule(
                        Integer.parseInt(row.get(0)), // id
                        Integer.parseInt(row.get(1)), // staff_id
                        Date.valueOf(row.get(2)), // date
                        row.get(3), // check_in
                        row.get(4), // check_out
                        Integer.parseInt(row.get(5)), // shift
                        row.get(6)
                );
            } catch (Exception e) {
                System.out.println("Error occurred in RoleDAL.convertToWork_schedule(): " + e.getMessage());
            }
            return new Work_Schedule();
        });
    }

    public int addWork_schedule(Work_Schedule work_schedule) {
        try {
            return create(work_schedule.getId(),
                    work_schedule.getStaff_id(),
                    work_schedule.getDate(),
                    work_schedule.getCheck_in(),
                    work_schedule.getCheck_out(),
                    work_schedule.getShift(),
                    work_schedule.getNotice()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_scheduleDAL.addWork_schedule(): " + e.getMessage());
        }
        return 0;
    }

    public int updateWork_schedule(Work_Schedule work_schedule) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(work_schedule.getId());
            updateValues.add(work_schedule.getStaff_id());
            updateValues.add(work_schedule.getDate());
            updateValues.add(work_schedule.getCheck_in());
            updateValues.add(work_schedule.getCheck_out());
            updateValues.add(work_schedule.getShift());
            updateValues.add(work_schedule.getNotice());
            return update(updateValues, "id = " + work_schedule.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_scheduleDAL.updateWork_schedule(): " + e.getMessage());
        }
        return 0;
    }

    public int updateChamCong(int staff_id, java.util.Date date, int shift, String checkin, String checkout) {
        try {
            String query = "UPDATE `" + getTableName() + "` SET check_in = '" + checkin + "', check_out = '" + checkout + "' WHERE staff_id = " + staff_id + " AND date = '" + date + "' AND shift = " + shift + ";";
            return executeUpdate(query);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_scheduleDAL.updateChamCong(): " + e.getMessage());
        }
        return 0;
    }

//    public static void main(String[] args) {
//        new Work_ScheduleDAL().updateChamCong(3, Date.valueOf("2024-04-20"), 1, "22:20", "23:00");
//    }

    public int deleteWork_schedule(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.deleteAccount(): " + e.getMessage());
        }
        return 0;
    }

    public List<Work_Schedule> searchWork_schedules(String... conditions) {
        try {
            List<Work_Schedule> work_schedules = convertToWork_schedules(read(conditions));
            work_schedules.sort(Comparator.comparing(Work_Schedule::getDate));
            return work_schedules;
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_scheduleDAL.searchWork_schedules(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Work_Schedule> searchWork_schedulesByStaff(int staff_id, int year, int month) {
        try {
            return convertToWork_schedules(executeQuery("SELECT * FROM `work_schedule`WHERE staff_id = " + staff_id + " and YEAR(date) = " + year + " and MONTH(date) = " + month));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Work_scheduleDAL.searchWork_schedules(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
