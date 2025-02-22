package com.coffee.DAL;

import com.coffee.DTO.Payroll;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAL extends Manager {
    public PayrollDAL() {
        super("payroll",
                List.of("id",
                        "name",
                        "entry_date",
                        "month",
                        "year",
                        "total_Salary",
                        "paid",
                        "debt"));
    }

    public List<Payroll> convertToPayrolls(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Payroll(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        Date.valueOf(row.get(2)), //entry_date
                        Integer.parseInt(row.get(3)), // month
                        Integer.parseInt(row.get(4)), // year
                        Double.parseDouble(row.get(5)), // total_salary
                        Double.parseDouble(row.get(6)), // paid
                        Double.parseDouble(row.get(7)) // debt
                );
            } catch (Exception e) {
                System.out.println("Error occurred in RoleDAL.convertToPayroll(): " + e.getMessage());
            }
            return new Payroll();
        });
    }

    public int addPayroll(Payroll payroll) {
        try {
            return create(payroll.getId(),
                    payroll.getName(),
                    payroll.getEntry_date(),
                    payroll.getMonth(),
                    payroll.getYear(),
                    payroll.getTotal_salary(),
                    payroll.getPaid(),
                    payroll.getDebt()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PayrollDAL.addPayroll(): " + e.getMessage());
        }
        return 0;
    }

    public int updatePayroll(Payroll payroll) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(payroll.getId());
            updateValues.add(payroll.getName());
            updateValues.add(payroll.getEntry_date());
            updateValues.add(payroll.getMonth());
            updateValues.add(payroll.getYear());
            updateValues.add(payroll.getTotal_salary());
            updateValues.add(payroll.getPaid());
            updateValues.add(payroll.getDebt());
            return update(updateValues, "id = " + payroll.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PayrollDAL.updatePayroll(): " + e.getMessage());
        }
        return 0;
    }

    public int deletePayroll(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in RoleDAL.deleteRole(): " + e.getMessage());
        }
        return 0;
    }

    public List<Payroll> searchPayrolls(String... conditions) {
        try {
            return convertToPayrolls(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PayrollDAL.searchPayrolls(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
