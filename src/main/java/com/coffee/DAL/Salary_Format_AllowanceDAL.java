package com.coffee.DAL;

import com.coffee.DTO.Salary_Format_Allowance;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Salary_Format_AllowanceDAL extends Manager {
    public Salary_Format_AllowanceDAL() {
        super("salary_format_allowance",
                List.of("salary_format_id",
                        "allowance_id"));
    }

    public List<Salary_Format_Allowance> convertToSalary_Format_Allowances(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Salary_Format_Allowance(
                        Integer.parseInt(row.get(0)), // salary_format_allowance_id
                        Integer.parseInt(row.get(1)) // name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Salary_Format_AllowanceDAL.convertToSalary_Format_Allowances(): " + e.getMessage());
            }
            return new Salary_Format_Allowance();
        });
    }

    public int addSalary_Format_Allowance(Salary_Format_Allowance salary_format_allowance) {
        try {
            return create(salary_format_allowance.getSalary_format_id(),
                    salary_format_allowance.getAllowance_id()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_Format_AllowanceDAL.addSalary_Format_Allowance(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteSalary_Format_Allowance(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_Format_AllowanceDAL.deleteSalary_Format_Allowance(): " + e.getMessage());
        }
        return 0;
    }

    public List<Salary_Format_Allowance> searchSalary_Format_Allowances(String... conditions) {
        try {
            return convertToSalary_Format_Allowances(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_Format_AllowanceDAL.searchSalary_Format_Allowances(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


}



