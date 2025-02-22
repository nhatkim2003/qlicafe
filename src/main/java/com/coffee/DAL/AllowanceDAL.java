package com.coffee.DAL;

import com.coffee.DTO.Allowance;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllowanceDAL extends Manager {
    public AllowanceDAL() {
        super("allowance",
                List.of("id",
                        "name",
                        "allowance_amount",
                        "allowance_type"));
    }

    public List<Allowance> convertToAllowances(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Allowance(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        Double.parseDouble(row.get(2)), // allowance_amount
                        Integer.parseInt(row.get(3)) // allowance_type
                );
            } catch (Exception e) {
                System.out.println("Error occurred in AllowanceDAL.convertToAllowances(): " + e.getMessage());
            }
            return new Allowance();
        });
    }

    public int addAllowance(Allowance allowance) {
        try {
            return create(allowance.getId(),
                    allowance.getName(),
                    allowance.getAllowance_amount(),
                    allowance.getAllowance_type()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AllowanceDAL.addAllowance(): " + e.getMessage());
        }
        return 0;
    }

    public int updateAllowance(Allowance allowance) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(allowance.getId());
            updateValues.add(allowance.getName());
            updateValues.add(allowance.getAllowance_amount());
            updateValues.add(allowance.getAllowance_type());
            return update(updateValues, "id = " + allowance.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Allowance.updateAllowance(): " + e.getMessage());
        }
        return 0;
    }

    public List<Allowance> searchAllowances(String... conditions) {
        try {
            return convertToAllowances(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AllowanceDAL.searchAllowances(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
