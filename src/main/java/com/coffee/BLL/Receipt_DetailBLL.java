package com.coffee.BLL;

import com.coffee.DAL.Receipt_DetailDAL;
import com.coffee.DTO.Export_Detail;
import com.coffee.DTO.Receipt;
import com.coffee.DTO.Receipt_Detail;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Receipt_DetailBLL extends Manager<Receipt_Detail> {
    private Receipt_DetailDAL receipt_DetailDAL;

    public Receipt_DetailBLL() {
        receipt_DetailDAL = new Receipt_DetailDAL();
    }

    public Receipt_DetailDAL getReceipt_DetailDAL() {
        return receipt_DetailDAL;
    }

    public void setReceipt_DetailDAL(Receipt_DetailDAL receipt_DetailDAL) {
        this.receipt_DetailDAL = receipt_DetailDAL;
    }

    public Object[][] getData() {
        return getData(receipt_DetailDAL.searchReceipt_Details());
    }

    public Pair<Boolean, String> addReceipt_Detail(Receipt_Detail receipt_Detail) {
        Pair<Boolean, String> result;

        result = validateQuantity(String.valueOf(receipt_Detail.getQuantity()));
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

//        result = exists(receipt_Detail);
//        if (result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }

        if (receipt_DetailDAL.addReceipt_Detail(receipt_Detail) == 0)
            return new Pair<>(false, "Thêm sản phẩm vào hoá đơn không thành công.");

        return new Pair<>(true, "Thêm sản phẩm vào hoá đơn thành công.");
    }

    public List<Receipt_Detail> searchReceipt_Details(String... conditions) {
        return receipt_DetailDAL.searchReceipt_Details(conditions);
    }

    public List<Receipt_Detail> findReceipt_Details(String key, String value) {
        List<Receipt_Detail> list = new ArrayList<>();
        List<Receipt_Detail> receipt_DetailList = receipt_DetailDAL.searchReceipt_Details();
        for (Receipt_Detail receipt_Detail : receipt_DetailList) {
            if (getValueByKey(receipt_Detail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(receipt_Detail);
            }
        }
        return list;
    }

    public List<Receipt_Detail> findReceipt_DetailsBy(Map<String, Object> conditions) {
        List<Receipt_Detail> receipt_Details = receipt_DetailDAL.searchReceipt_Details();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            receipt_Details = findObjectsBy(entry.getKey(), entry.getValue(), receipt_Details);
        return receipt_Details;
    }

    public Pair<Boolean, String> exists(Receipt_Detail receipt_Detail) {
        List<Receipt_Detail> receipt_Details = findReceipt_DetailsBy(Map.of(
                "receipt_id", receipt_Detail.getProduct_id(),
                "product_id", receipt_Detail.getQuantity(),
                "size", receipt_Detail.getSize()
        ));

        if (!receipt_Details.isEmpty()) {
            return new Pair<>(true, "Sản phẩm đã được thêm vào hoá đơn.");
        }
        return new Pair<>(false, "");
    }

    private Pair<Boolean, String> validateQuantity(String quantity) {
        if (quantity.isBlank())
            return new Pair<>(false, "Số lượng không được để trống");
        if (!VNString.checkUnsignedNumber(quantity))
            return new Pair<>(false, "Số lượng phải là số lớn hơn không");
        return new Pair<>(true, "Số lượng hợp lệ");
    }

    @Override
    public Object getValueByKey(Receipt_Detail receipt_Detail, String key) {
        return switch (key) {
            case "receipt_id" -> receipt_Detail.getReceipt_id();
            case "product_id" -> receipt_Detail.getProduct_id();
            case "size" -> receipt_Detail.getSize();
            case "quantity" -> receipt_Detail.getQuantity();
            case "price" -> receipt_Detail.getPrice();
            case "notice" -> receipt_Detail.getNotice();
            default -> null;
        };
    }

    public static void main(String[] args) {
        Receipt_DetailBLL receiptNoteBLL = new Receipt_DetailBLL();
//        Receipt_Detail receiptNote = new Receipt_Detail(1, 2, 0, 0);
//        receiptNoteBLL.addReceipt_Detail(receiptNote);
    }
}
