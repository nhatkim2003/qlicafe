package com.coffee.BLL;

import com.coffee.DAL.Discount_DetailDAL;
import com.coffee.DTO.Decentralization;
import com.coffee.DTO.Discount_Detail;
import com.coffee.DTO.Supplier;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Discount_DetailBLL extends Manager<Discount_Detail> {
    private Discount_DetailDAL discount_detailDAL;

    public Discount_DetailBLL() {
        discount_detailDAL = new Discount_DetailDAL();
    }

    public Discount_DetailDAL getDiscount_DetailDAL() {
        return discount_detailDAL;
    }

    public void setDiscount_DetailDAL(Discount_DetailDAL discount_detailDAL) {
        this.discount_detailDAL = discount_detailDAL;
    }

    public Object[][] getData() {
        return getData(discount_detailDAL.searchDiscountDetails());
    }

    public Pair<Boolean, String> addDiscount_Detail(Discount_Detail discount_detail) {
        if (discount_detailDAL.addDiscountDetail(discount_detail) == 0)
            return new Pair<>(false, "Thêm sản phẩm giảm giá không thành công.");

        return new Pair<>(true,"Thêm sản phẩm giảm giá thành công.");
    }

    public Pair<Boolean, String> updateDiscount_Detail(Discount_Detail discount_detail) {

        if (discount_detailDAL.updateDiscountDetail(discount_detail) == 0)
            return new Pair<>(false, "Cập nhật sản phẩm giảm giá không thành công.");

        return new Pair<>(true,"Cập nhật sản phẩm giảm giá thành công.");
    }

    public List<Discount_Detail> searchDiscount_Details(String... conditions) {
        return discount_detailDAL.searchDiscountDetails(conditions);
    }

    public List<Discount_Detail> findDiscount_Details(String key, String value) {
        List<Discount_Detail> list = new ArrayList<>();
        List<Discount_Detail> discount_detailList = discount_detailDAL.searchDiscountDetails();
        for (Discount_Detail discount_detail : discount_detailList) {
            if (getValueByKey(discount_detail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(discount_detail);
            }
        }
        return list;
    }

    public List<Discount_Detail> findDiscount_DetailsBy(Map<String, Object> conditions) {
        List<Discount_Detail> discount_details = discount_detailDAL.searchDiscountDetails();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            discount_details = findObjectsBy(entry.getKey(), entry.getValue(), discount_details);
        return discount_details;
    }

//    public Pair<Boolean, String> exists(Discount_Detail discount_detail) {
//        List<Discount_Detail> discount_details = findDiscount_DetailsBy(Map.of(
//                "discount_id", discount_detail.getDiscount_id(),
//                "product_id", discount_detail.getProduct_id()
//        ));
//
//        if(!discount_details.isEmpty()){
//            return new Pair<>(true, "Sản phẩm đã được giảm giá.");
//        }
//        return new Pair<>(false, "");
//    }

    private static Pair<Boolean, String> validatePercent(String percent){
        if (percent.isBlank())
            return new Pair<>(false, "Phần trăm giảm giá không được để trống.");
        if (!VNString.checkRangeOfPercent(percent))
            return new Pair<>(false, "Phần trăm giảm giá phải trong khoảng (0, 100).");
        return new Pair<>(true, percent);
    }

    @Override
    public Object getValueByKey(Discount_Detail discount_detail, String key) {
        return switch (key) {
            case "discount_id" -> discount_detail.getDiscount_id();
            case "product_id" -> discount_detail.getProduct_id();
            case "percent" -> discount_detail.getPercent();
            default -> null;
        };
    }

    public static void main(String[] args) {
        Discount_DetailBLL discountDetailBLL = new Discount_DetailBLL();

//        Discount_Detail discountDetail = new Discount_Detail(1, 1, 10);
//        discountDetailBLL.addDiscount_Detail(discountDetail);
//        discountDetail.setPercent(20);
//        discountDetailBLL.updateDiscount_Detail(discountDetail);
    }
}
