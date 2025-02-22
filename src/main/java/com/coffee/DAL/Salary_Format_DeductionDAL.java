package com.coffee.DAL;

import com.coffee.DTO.Salary_Format_Deduction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Salary_Format_DeductionDAL extends Manager {
    public Salary_Format_DeductionDAL() {
        super("salary_format_deduction",
                List.of("salary_format_id",
                        "deduction_id"));
    }

    public List<Salary_Format_Deduction> convertToSalary_Format_Deductions(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Salary_Format_Deduction(
                        Integer.parseInt(row.get(0)), // salary_format_deduction_id
                        Integer.parseInt(row.get(1)) // name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Salary_Format_DeductionDAL.convertToSalary_Format_Deductions(): " + e.getMessage());
            }
            return new Salary_Format_Deduction();
        });
    }

    public int addSalary_Format_Deduction(Salary_Format_Deduction salary_format_deduction) {
        try {
            return create(salary_format_deduction.getSalary_format_id(),
                    salary_format_deduction.getDeduction_id()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_Format_DeductionDAL.addSalary_Format_Deduction(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteSalary_Format_Deduction(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_Format_DeductionDAL.deleteSalary_Format_Deduction(): " + e.getMessage());
        }
        return 0;
    }

    public List<Salary_Format_Deduction> searchSalary_Format_Deductions(String... conditions) {
        try {
            return convertToSalary_Format_Deductions(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_Format_DeductionDAL.searchSalary_Format_Deductions(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


}



