package com.coffee.DTO;

import com.coffee.utils.VNString;

public class Material {
    private int id;
    private String name;
    private double remain;
    private double minRemain;
    private double maxRemain;
    private String unit;
    private Boolean sell;
    private double unit_price;
    private boolean deleted;
    private double remain_wearhouse;

    public Material() {
    }

    public Material(int id, String name, double remain, double minRemain, double maxRemain, String unit, double unit_price, Boolean sell, boolean deleted, double remain_wearhouse) {
        this.id = id;
        this.name = name;
        this.remain = remain;
        this.minRemain = minRemain;
        this.maxRemain = maxRemain;
        this.unit = unit;
        this.unit_price = unit_price;
        this.sell = sell;
        this.deleted = deleted;
        this.remain_wearhouse = remain_wearhouse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }

    public Double getMinRemain() {
        return minRemain;
    }

    public void setMinRemain(double minRemain) {
        this.minRemain = minRemain;
    }

    public Double getMaxRemain() {
        return maxRemain;
    }

    public void setMaxRemain(double maxRemain) {
        this.maxRemain = maxRemain;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public Boolean isSell() {
        return sell;
    }

    public void setSell(Boolean sell) {
        this.sell = sell;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getRemain_wearhouse() {
        return remain_wearhouse;
    }

    public void setRemain_wearhouse(double remain_wearhouse) {
        this.remain_wearhouse = remain_wearhouse;
    }

    @Override
    public String toString() {
        return id + " | " +
                name + " | " +
                remain + " | " +
                remain_wearhouse + " | " +
                unit + " | " +
                VNString.currency(unit_price) + " | " +
                deleted;
    }
}
