package com.coffee.DTO;

import com.coffee.utils.VNString;

public class Allowance {
    private int id;
    private String name;
    private double allowance_amount;
    private int allowance_type;

    public Allowance() {
    }

    public Allowance(int id, String name, double allowance_amount, int allowance_type) {
        this.id = id;
        this.name = name;
        this.allowance_amount = allowance_amount;
        this.allowance_type = allowance_type;
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

    public double getAllowance_amount() {
        return allowance_amount;
    }

    public void setAllowance_amount(double allowance_amount) {
        this.allowance_amount = allowance_amount;
    }

    public int getAllowance_type() {
        return allowance_type;
    }

    public void setAllowance_type(int allowance_type) {
        this.allowance_type = allowance_type;
    }

    @Override
    public String toString() {
        return id + " | " +
                name + " | " +
                VNString.currency(allowance_amount) + " | " +
                allowance_type;
    }
}
