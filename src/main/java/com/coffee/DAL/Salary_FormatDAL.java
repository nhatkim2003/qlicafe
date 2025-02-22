package com.coffee.DAL;

import com.coffee.DTO.Salary_Format;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Salary_FormatDAL extends Manager {
    public Salary_FormatDAL() {
        super("salary_format",
                List.of("id",
                        "name"));
    }

    public List<Salary_Format> convertToSalary_Formats(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Salary_Format(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1) // name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Salary_FormatDAL.convertToSalary_Formats(): " + e.getMessage());
            }
            return new Salary_Format();
        });
    }

    public int addSalary_Format(Salary_Format salary_format) {
        try {
            return create(salary_format.getId(),
                    salary_format.getName()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_FormatDAL.addSalary_Format(): " + e.getMessage());
        }
        return 0;
    }

    public int updateSalary_Format(Salary_Format salary_format) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(salary_format.getId());
            updateValues.add(salary_format.getName());
            return update(updateValues, "id = " + salary_format.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_FormatDAL.updateSalary_Format(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteSalary_Format(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_FormatDAL.deleteSalary_Format(): " + e.getMessage());
        }
        return 0;
    }

    public List<Salary_Format> searchSalary_Formats(String... conditions) {
        try {
            return convertToSalary_Formats(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Salary_FormatDAL.searchSalary_Formats(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


}



