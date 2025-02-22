package com.coffee.DAL;

import com.coffee.DTO.Deduction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeductionDAL extends Manager {
    public DeductionDAL() {
        super("deduction",
                List.of("id",
                        "name",
                        "deduction_amount",
                        "deduction_type"));
    }

    public List<Deduction> convertToDeductions(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Deduction(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        Double.parseDouble(row.get(2)), // deduction_amount
                        Integer.parseInt(row.get(3)) // deduction_type
                );
            } catch (Exception e) {
                System.out.println("Error occurred in DeductionDAL.convertToDeductions(): " + e.getMessage());
            }
            return new Deduction();
        });
    }

    public int addDeduction(Deduction deduction) {
        try {
            return create(deduction.getId(),
                    deduction.getName(),
                    deduction.getDeduction_amount(),
                    deduction.getDeduction_type()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DeductionDAL.addDeduction(): " + e.getMessage());
        }
        return 0;
    }

    public int updateDeduction(Deduction deduction) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(deduction.getId());
            updateValues.add(deduction.getName());
            updateValues.add(deduction.getDeduction_amount());
            updateValues.add(deduction.getDeduction_type());
            return update(updateValues, "id = " + deduction.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DeductionDAL.updateDeduction(): " + e.getMessage());
        }
        return 0;
    }

    public List<Deduction> searchDeductions(String... conditions) {
        try {
            return convertToDeductions(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DeductionDAL.searchDeductions(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
