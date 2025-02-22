package com.coffee.DTO;

import com.coffee.utils.VNString;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Receipt {
    private int id;
    private int staff_id;
    private LocalDateTime invoice_date;
    private double total_price;
    private double total_discount;
    private double total;
    private double received;
    private double excess;
    private int discount_id;

    public Receipt() {
    }

    public Receipt(int id, int staff_id, LocalDateTime invoice_date, double total_price, double total_discount, double total, double received, double excess, int discount_id) {
        this.id = id;
        this.staff_id = staff_id;
        this.invoice_date = invoice_date;
        this.total_price = total_price;
        this.total_discount = total_discount;
        this.total = total;
        this.received = received;
        this.excess = excess;
        this.discount_id = discount_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(LocalDateTime invoice_date) {
        this.invoice_date = invoice_date;
    }

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public double getExcess() {
        return excess;
    }

    public void setExcess(double excess) {
        this.excess = excess;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(double total_discount) {
        this.total_discount = total_discount;
    }

    @Override
    public String toString() {
        return id + " | " +
                staff_id + " | " +
                VNString.currency(total) + " | " +
                invoice_date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

}
