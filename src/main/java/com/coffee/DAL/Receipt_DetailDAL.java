package com.coffee.DAL;

import com.coffee.DTO.Receipt_Detail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Receipt_DetailDAL extends Manager {
    public Receipt_DetailDAL() {
        super("receipt_detail",
                List.of("receipt_id",
                        "product_id",
                        "size",
                        "quantity",
                        "price", "notice", "price_discount"));
    }

    public List<Receipt_Detail> convertToReceipt_Details(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Receipt_Detail(
                        Integer.parseInt(row.get(0)), // receipt_id
                        Integer.parseInt(row.get(1)), // product_id
                        row.get(2), // size
                        Double.parseDouble(row.get(3)), // quantity
                        Double.parseDouble(row.get(4)), // price
                        row.get(5)
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Receipt_DetailDAL.convertToReceipt_Details(): " + e.getMessage());
            }
            return new Receipt_Detail();
        });
    }

    public int addReceipt_Detail(Receipt_Detail receipt_detail) {
        try {
            return create(receipt_detail.getReceipt_id(),
                    receipt_detail.getProduct_id(),
                    receipt_detail.getSize(),
                    receipt_detail.getQuantity(),
                    receipt_detail.getPrice(),
                    receipt_detail.getNotice(),
                    receipt_detail.getPrice_discount()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Receipt_DetailDAL.addReceipt_Detail(): " + e.getMessage());
        }
        return 0;
    }

    public List<Receipt_Detail> searchReceipt_Details(String... conditions) {
        try {
            return convertToReceipt_Details(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Receipt_DetailDAL.searchReceipt_Details(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
