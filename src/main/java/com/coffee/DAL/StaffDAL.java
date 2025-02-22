package com.coffee.DAL;

import com.coffee.DTO.Discount;
import com.coffee.DTO.Staff;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StaffDAL extends Manager {
    public StaffDAL() {
        super("staff",
                List.of("id",
                        "no",
                        "name",
                        "gender",
                        "birthdate",
                        "phone",
                        "address",
                        "email",
                        "deleted",
                        "salary_format_id"));
    }

    public List<Staff> convertToStaffs(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Staff(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // no
                        row.get(2), // name
                        Boolean.parseBoolean(row.get(3)), // gender
                        Date.valueOf(row.get(4)), // birthday
                        row.get(5), // phone
                        row.get(6), // address
                        row.get(7), // email
                        Boolean.parseBoolean(row.get(8)), // deleted
                        Integer.parseInt(row.get(9))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in StaffDAL.convertToStaffs(): " + e.getMessage());
            }
            return new Staff();
        });
    }

    public int addStaff(Staff staff) {
        try {
            return create(staff.getId(),
                    staff.getStaffNo(),
                    staff.getName(),
                    staff.isGender(),
                    staff.getBirthdate(),
                    staff.getPhone(),
                    staff.getAddress(),
                    staff.getEmail(),
                    false,
                    staff.getSalary_format_id()
            ); // staff khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.addStaff(): " + e.getMessage());
        }
        return 0;
    }

    public int updateStaff(Staff staff) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(staff.getId());
            updateValues.add(staff.getStaffNo());
            updateValues.add(staff.getName());
            updateValues.add(staff.isGender());
            updateValues.add(staff.getBirthdate());
            updateValues.add(staff.getPhone());
            updateValues.add(staff.getAddress());
            updateValues.add(staff.getEmail());
            updateValues.add(staff.isDeleted());
            updateValues.add(staff.getSalary_format_id());
            return update(updateValues, "id = " + staff.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.updateStaff(): " + e.getMessage());
        }
        return 0;
    }


    public int deleteStaff(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.deleteStaff(): " + e.getMessage());
        }
        return 0;
    }

    public List<Staff> searchStaffs(String... conditions) {
        try {
            return convertToStaffs(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.searchStaffs(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {

        StaffDAL staffDAL = new StaffDAL();


        List<Staff> staffs = staffDAL.searchStaffs();

        // In thông tin của mỗi đối tượng Discount
        for (Staff staff : staffs) {
            System.out.println(staff.getId() + " | " +
                    staff.getStaffNo() + " | " +
                    staff.getName() + " | " +
                    staff.isGender() + " | " +
                    staff.getBirthdate() + " | " +
                    staff.getPhone() + " | " +
                    staff.getAddress() + " | " +
                    staff.getEmail() + " | " +
                    staff.isDeleted());
        }

    }
}
