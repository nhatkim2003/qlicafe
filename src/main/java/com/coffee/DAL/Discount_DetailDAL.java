package com.coffee.DAL;

import com.coffee.DTO.Discount_Detail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Discount_DetailDAL extends Manager{
    public Discount_DetailDAL() {
        super("discount_detail",
                List.of("discount_id",
                        "product_id",
                        "size",
                        "quantity",
                        "percent",
                        "discountBill"
                ));
    }

    public List<Discount_Detail> convertToDiscountDetails(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Discount_Detail(
                        Integer.parseInt(row.get(0)), // discount_id
                        Integer.parseInt(row.get(1)), // product_id
                        String.valueOf(row.get(2)), // size
                        Integer.parseInt(row.get(3)), // quantity
                        Double.parseDouble(row.get(4)), // percent
                        Double.parseDouble(row.get(5)) // discountBill
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Discount_DetailDAL.convertToDiscountDetails(): " + e.getMessage());
            }
            return new Discount_Detail();
        });
    }

    public int addDiscountDetail(Discount_Detail discount_detail) {
        try {
            return create(discount_detail.getDiscount_id(),
                    discount_detail.getProduct_id(),
                    discount_detail.getSize(),
                    discount_detail.getQuantity(),
                    discount_detail.getPercent(),
                    discount_detail.getDiscountBill()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Discount_DetailDAL.addDiscountDetail(): " + e.getMessage());
        }
        return 0;
    }

    public int updateDiscountDetail(Discount_Detail discount_detail) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(discount_detail.getDiscount_id());
            updateValues.add(discount_detail.getProduct_id());
            updateValues.add(discount_detail.getSize());
            updateValues.add(discount_detail.getQuantity());
            updateValues.add(discount_detail.getPercent());
            updateValues.add(discount_detail.getDiscountBill());
            return update(updateValues,
                    "discount_id = " + discount_detail.getDiscount_id(),
                    "product_id = " +discount_detail.getProduct_id(),
                    "size = " + discount_detail.getSize(),
                    "quantity = " + discount_detail.getQuantity());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Discount_DetailDAL.updateDiscountDetail(): " + e.getMessage());
        }
        return 0;
    }

    public List<Discount_Detail> searchDiscountDetails(String... conditions) {
        try {
            return convertToDiscountDetails(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Discount_detailsDAL.searchDiscountDetails(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
