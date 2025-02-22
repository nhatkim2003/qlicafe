package com.coffee.DAL;

import com.coffee.DTO.Supplier;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAL extends Manager{
    public SupplierDAL() {
        super("supplier",
                List.of("id",
                        "name",
                        "phone",
                        "address",
                        "email",
                        "deleted"));
    }

    public List<Supplier> convertToSuppliers(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Supplier(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        row.get(2), // phone
                        row.get(3), // address
                        row.get(4), // email
                        Boolean.parseBoolean(row.get(5)) // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in SupplierDAL.convertToSuppliers(): " + e.getMessage());
            }
            return new Supplier();
        });
    }

    public int addSupplier(Supplier supplier) {
        try {
            return create(supplier.getId(),
                    supplier.getName(),
                    supplier.getPhone(),
                    supplier.getAddress(),
                    supplier.getEmail(),
                    false
            ); // supplier khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in SupplierDAL.addSupplier(): " + e.getMessage());
        }
        return 0;
    }

    public int updateSupplier(Supplier supplier) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(supplier.getId());
            updateValues.add(supplier.getName());
            updateValues.add(supplier.getPhone());
            updateValues.add(supplier.getAddress());
            updateValues.add(supplier.getEmail());
            updateValues.add(supplier.isDeleted());
            return update(updateValues, "id = " + supplier.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in SupplierDAL.updateSupplier(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteSupplier(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in SupplierDAL.deleteSupplier(): " + e.getMessage());
        }
        return 0;
    }

    public List<Supplier> searchSuppliers(String... conditions) {
        try {
            return convertToSuppliers(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in SupplierDAL.searchSuppliers(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        SupplierDAL s = new SupplierDAL();
        System.out.println(s.searchSuppliers());
    }
}
