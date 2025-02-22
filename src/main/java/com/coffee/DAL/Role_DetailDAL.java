package com.coffee.DAL;

import com.coffee.DTO.Role_Detail;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Role_DetailDAL extends Manager {
    public Role_DetailDAL() {
        super("role_detail",
                List.of("role_id",
                        "staff_id",
                        "entry_date",
                        "salary",
                        "type_salary"));
    }

    public List<Role_Detail> convertToRole_details(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Role_Detail(
                        Integer.parseInt(row.get(0)), // role_id
                        Integer.parseInt(row.get(1)), // staff_id
                        LocalDateTime.parse(row.get(2)), // entry_date
                        Double.parseDouble(row.get(3)), // salary
                        Integer.parseInt(row.get(4)) // type_salary
                );
            } catch (Exception e) {
                System.out.println("Error occurred in RoleDAL.convertToRole_detail(): " + e.getMessage());
            }
            return new Role_Detail();
        });
    }

    public int addRole_detail(Role_Detail role_detail) {
        try {
            return create(role_detail.getRole_id(),
                    role_detail.getStaff_id(),
                    role_detail.getEntry_date(),
                    role_detail.getSalary(),
                    role_detail.getType_salary()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Role_DetailDAL.addRole_detail(): " + e.getMessage());
        }
        return 0;
    }

    public int updateRole_detail(Role_Detail role_detail) {
        try {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(role_detail.getRole_id());
            updateValues.add(role_detail.getStaff_id());
            updateValues.add(role_detail.getEntry_date());
            updateValues.add(role_detail.getSalary());
            updateValues.add(role_detail.getType_salary());
            return update(updateValues, "role_id = " + role_detail.getRole_id(), "staff_id = " + role_detail.getStaff_id(), "entry_date = '" + role_detail.getEntry_date().format(myFormatObj) + "'");
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Role_DetailDAL.updateRole_detail(): " + e.getMessage());
        }
        return 0;
    }

    public List<Role_Detail> searchRole_details(String... conditions) {
        try {
            return convertToRole_details(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Role_DetailDAL.searchRole_details(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Role_Detail> searchRole_detailsByRole(int role_id, String end) {
        try {
            return convertToRole_details(executeQuery("SELECT rd.role_id, rd.staff_id, rd.entry_date, rd.salary, rd.type_salary \n" +
                    "FROM (SELECT staff_id, MAX(entry_date) as entry_date\n" +
                    "\t\t\tFROM `role_detail` \n" +
                    "\t\t\tWHERE entry_date <= '" + end + "'\n" +
                    "\t\t\tGROUP BY staff_id) tb1 JOIN role_detail rd on tb1.staff_id = rd.staff_id AND tb1.entry_date = rd.entry_date\n" +
                    "WHERE rd.role_id = " + role_id));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Role_DetailDAL.searchRole_details(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Role_Detail> searchRole_detailsByStaff(int staff_id) {
        try {
            return convertToRole_details(executeQuery("SELECT rd.role_id, rd.staff_id, rd.entry_date, rd.salary, rd.type_salary \n" +
                    "FROM (SELECT staff_id, MAX(entry_date) as entry_date\n" +
                    "\t\t\tFROM `role_detail` \n" +
                    "\t\t\tGROUP BY staff_id) tb1 JOIN role_detail rd on tb1.staff_id = rd.staff_id AND tb1.entry_date = rd.entry_date\n" +
                    "WHERE rd.staff_id = " + staff_id));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Role_DetailDAL.searchRole_details(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
