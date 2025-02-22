package com.coffee.DTO;

import java.util.Date;

public class Discount {
    private int id;
    private String name;
    private Date start_date;
    private Date end_date;
    private boolean type;
    private boolean status;


    public Discount() {
    }

    public Discount(int id, String name, Date start_date, Date end_date, boolean type, boolean status) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String status1 = status ? "Ngừng áp dụng" : "Đang áp dụng";
        String type1 = type ? "Giảm theo hóa đơn" : "Giảm theo sản phẩm";
        return id + " | " +
                name + " | " +
                start_date + " | " +
                end_date + " | " +
                type1 + " | " +
                status1;
    }
}
