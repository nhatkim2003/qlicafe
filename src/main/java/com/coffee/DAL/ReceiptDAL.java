package com.coffee.DAL;

import com.coffee.DTO.Receipt;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDAL extends Manager {
    public ReceiptDAL() {
        super("receipt",
                List.of("id",
                        "staff_id",
                        "invoice_date",
                        "total_price",
                        "total_discount",
                        "total",
                        "received",
                        "excess",
                        "discount_id"));
    }

    public List<Receipt> convertToReceipts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Receipt(
                        Integer.parseInt(row.get(0)), // id
                        Integer.parseInt(row.get(1)), // staff_id
                        LocalDateTime.parse(row.get(2)), // invoice_date
                        Double.parseDouble(row.get(3)), // total_price
                        Double.parseDouble(row.get(4)), // total_discount
                        Double.parseDouble(row.get(5)), // total
                        Double.parseDouble(row.get(6)), // received
                        Double.parseDouble(row.get(7)), // excess
                        Integer.parseInt(row.get(8)) // discount_id
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ReceiptDAL.convertToReceipts(): " + e.getMessage());
            }
            return new Receipt();
        });
    }

    public int addReceipt(Receipt receipt) {
        try {
            return create(receipt.getId(),
                    receipt.getStaff_id(),
                    receipt.getInvoice_date(),
                    receipt.getTotal_price(),
                    receipt.getTotal_discount(),
                    receipt.getTotal(),
                    receipt.getReceived(),
                    receipt.getExcess(),
                    receipt.getDiscount_id()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ReceiptDAL.addReceipt(): " + e.getMessage());
        }
        return 0;
    }

    public List<Receipt> searchReceipts(String... conditions) {
        try {
            return convertToReceipts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ReceiptDAL.searchReceipts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
