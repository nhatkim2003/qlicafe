package com.coffee.DAL;

import com.coffee.DTO.Discount;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAL extends Manager{
    public DiscountDAL() {
        super("discount",
                List.of("id",
                        "name",
                        "start_date",
                        "end_date",
                        "type",
                        "status"
                ));
    }

    public List<Discount> convertToDiscounts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Discount(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // name
                        Date.valueOf(row.get(2)), // start_date
                        Date.valueOf(row.get(3)), // end_date
                        Boolean.parseBoolean(row.get(4)), // type
                        Boolean.parseBoolean(row.get(5)) // status
                );
            } catch (Exception e) {
                System.out.println("Error occurred in DiscountDAL.convertToDiscounts(): " + e.getMessage());
            }
            return new Discount();
        });
    }

    public int addDiscount(Discount discount) {
        try {
            return create(discount.getId(),
                    discount.getName(),
                    discount.getStart_date(),
                    discount.getEnd_date(),
                    discount.isType(),
                    discount.isStatus()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DiscountDAL.addDiscount(): " + e.getMessage());
        }
        return 0;
    }

    public int updateDiscount (Discount discount) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(discount.getId());
            updateValues.add(discount.getName());
            updateValues.add(discount.getStart_date());
            updateValues.add(discount.getEnd_date());
            updateValues.add(discount.isType());
            updateValues.add(discount.isStatus());
            return update(updateValues, "id = " + discount.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DiscountDAL.updateDiscount(): " + e.getMessage());
        }
        return 0;
    }

    public List<Discount> searchDiscounts(String... conditions) {
        try {
            return convertToDiscounts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DiscountDAL.searchDiscounts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


        public static void main(String[] args) {
            // Tạo một đối tượng DiscountDAL
            DiscountDAL discountDAL = new DiscountDAL();

            // Gọi phương thức searchDiscounts với điều kiện là null để lấy tất cả các dữ liệu
            List<Discount> discounts = discountDAL.searchDiscounts();

            // In thông tin của mỗi đối tượng Discount
            for (Discount discount : discounts) {
                System.out.println(discount.getId() + " | " +
                        discount.getStart_date() + " | " +
                        discount.getEnd_date() + " | " +
                        discount.isStatus());
            }

    }



}
