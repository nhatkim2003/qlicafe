package com.coffee.DAL;

import com.coffee.DTO.Payroll_Detail;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Payroll_DetailDAL extends Manager {
    public Payroll_DetailDAL() {
        super("payroll_detail",
                List.of("payroll_id",
                        "staff_id",
                        "hours_amount",
                        "allowance_amount",
                        "deduction_amount",
                        "bonus_amount",
                        "fine_amount",
                        "salary_amount",
                        "status"));
    }

    public List<Payroll_Detail> convertToPayroll_Details(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Payroll_Detail(
                        Integer.parseInt(row.get(0)), // payroll_id
                        Integer.parseInt(row.get(1)), // staff_id
                        Double.parseDouble(row.get(2)), // hours_amount
                        Double.parseDouble(row.get(3)), // allowance_amount
                        Double.parseDouble(row.get(4)), // deduction_amount
                        Double.parseDouble(row.get(5)), // bonus_amount
                        Double.parseDouble(row.get(6)), // fine_amount
                        Double.parseDouble(row.get(7)), // salary_amount
                        Boolean.parseBoolean(row.get(8)) // status
                );
            } catch (Exception e) {
                System.out.println("Error occurred in RoleDAL.convertToPayroll(): " + e.getMessage());
            }
            return new Payroll_Detail();
        });
    }

    public int addPayroll_Detail(Payroll_Detail payroll_Detail) {
        try {
            return create(payroll_Detail.getPayroll_id(),
                    payroll_Detail.getStaff_id(),
                    payroll_Detail.getHours_amount(),
                    payroll_Detail.getAllowance_amount(),
                    payroll_Detail.getDeduction_amount(),
                    payroll_Detail.getBonus_amount(),
                    payroll_Detail.getFine_amount(),
                    payroll_Detail.getSalary_amount(),
                    false
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PayrollDAL.addPayroll_Detail(): " + e.getMessage());
        }
        return 0;
    }

    public int updatePayroll_Detail(Payroll_Detail payroll_Detail) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(payroll_Detail.getPayroll_id());
            updateValues.add(payroll_Detail.getStaff_id());
            updateValues.add(payroll_Detail.getHours_amount());
            updateValues.add(payroll_Detail.getAllowance_amount());
            updateValues.add(payroll_Detail.getDeduction_amount());
            updateValues.add(payroll_Detail.getBonus_amount());
            updateValues.add(payroll_Detail.getFine_amount());
            updateValues.add(payroll_Detail.getSalary_amount());
            updateValues.add(payroll_Detail.isStatus());
            return update(updateValues, "payroll_id = " + payroll_Detail.getPayroll_id(), "staff_id = " + payroll_Detail.getStaff_id());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PayrollDAL.updatePayroll_Detail(): " + e.getMessage());
        }
        return 0;
    }

    public List<Payroll_Detail> searchPayroll_Details(String... conditions) {
        try {
            return convertToPayroll_Details(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PayrollDAL.searchPayroll_Details(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
