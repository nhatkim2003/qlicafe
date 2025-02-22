package com.coffee.DTO;

public class Discount_Detail {
    private int discount_id;
    private int product_id;
    private String size;
    private double quantity;
    private double percent;
    private double discountBill;

    public Discount_Detail() {
    }

    public Discount_Detail(int discount_id, int product_id, String size, double quantity, double percent, double discountBill) {
        this.discount_id = discount_id;
        this.product_id = product_id;
        this.size = size;
        this.quantity = quantity;
        this.percent = percent;
        this.discountBill = discountBill;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getDiscountBill() {
        return discountBill;
    }

    public void setDiscountBill(double discountBill) {
        this.discountBill = discountBill;
    }

    @Override
    public String toString() {
        return discount_id + " | " +
                product_id + " | " +
                percent;
    }
}
