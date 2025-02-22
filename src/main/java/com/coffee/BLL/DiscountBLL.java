package com.coffee.BLL;

import com.coffee.DAL.DiscountDAL;
import com.coffee.DTO.Discount;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;

public class DiscountBLL extends Manager<Discount> {
    private final DiscountDAL discountDAL;


    public DiscountBLL() {
        discountDAL = new DiscountDAL();
    }


    public Object[][] getData() {
        return getData(discountDAL.searchDiscounts("id != 0"));
    }

    public Pair<Boolean, String> addDiscount(Discount discount) {
        if (discountDAL.addDiscount(discount) == 0) return new Pair<>(false, "Thêm đợt giảm giá không thành công.");

        return new Pair<>(true, "Thêm đợt giảm giá thành công.");
    }

    public Pair<Boolean, String> updateDiscount(Discount discount) {
//        Pair<Boolean, String> result;
//
//
//        result = validateDate(discount.getStart_date(), discount.getEnd_date());
//        if (!result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }

        if (discountDAL.updateDiscount(discount) == 0)
            return new Pair<>(false, "Cập nhật đợt giảm giá không thành công.");

        return new Pair<>(true, "Cập nhật giảm giá thành công.");

    }

    public Pair<Boolean, String> updateStatusDiscount(Discount discount) {
        if (discountDAL.updateDiscount(discount) == 0)
            return new Pair<>(false, "Cập nhật đợt giảm giá không thành công.");

        return new Pair<>(true, "Cập nhật giảm giá thành công.");

    }

    public List<Discount> searchDiscounts(String... conditions) {
        return discountDAL.searchDiscounts(conditions);
    }

    public List<Discount> findDiscounts(String key, String value) {
        List<Discount> list = new ArrayList<>();
        List<Discount> discountList = discountDAL.searchDiscounts("id != 0");
        for (Discount discount : discountList) {
            if (getValueByKey(discount, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(discount);
            }
        }
        return list;
    }

    public Pair<Boolean, String> validateDate(Date start_date, Date end_date) {
        if (start_date == null) return new Pair<>(false, "Ngày bắt đầu không được để trống.");
        if (end_date == null) return new Pair<>(false, "Ngày kết thúc không được để trống.");
        if (start_date.before(java.sql.Date.valueOf(LocalDate.now())))
            return new Pair<>(false, "Ngày bắt đầu phải sau ngày hiện tại.");
        if (start_date.after(end_date)) return new Pair<>(false, "Ngày bắt đầu phải trước ngày kết thúc.");
        return new Pair<>(true, "Thời gian khuyến mãi hợp lệ.");
    }

    public Pair<Boolean, String> validateName(String name) {
        if (name.isBlank()) return new Pair<>(false, "Tên chương trình không được để trống.");
        return new Pair<>(true, name);
    }

    @Override
    public Object getValueByKey(Discount discount, String key) {
        return switch (key) {
            case "id" -> discount.getId();
            case "name" -> discount.getName();
            case "start_date" -> discount.getStart_date();
            case "end_date" -> discount.getEnd_date();
            case "type" -> discount.isType();
            case "status" -> discount.isStatus();
            default -> null;
        };
    }

    public static void main(String[] args) {
        DiscountBLL discountBLL = new DiscountBLL();

        System.out.println(discountBLL.searchDiscounts());
    }
}
