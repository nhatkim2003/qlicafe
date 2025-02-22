package com.coffee.DTO;

import com.coffee.utils.VNString;

public class Deduction {
    private int id;
    private String name;
    private double deduction_amount;
    private int deduction_type;

    public Deduction() {
    }

    public Deduction(int id, String name, double deduction_amount, int deduction_type) {
        this.id = id;
        this.name = name;
        this.deduction_amount = deduction_amount;
        this.deduction_type = deduction_type;
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

    public double getDeduction_amount() {
        return deduction_amount;
    }

    public void setDeduction_amount(double deduction_amount) {
        this.deduction_amount = deduction_amount;
    }

    public int getDeduction_type() {
        return deduction_type;
    }

    public void setDeduction_type(int deduction_type) {
        this.deduction_type = deduction_type;
    }

    @Override
    public String toString() {
        return id + " | " +
                name + " | " +
                VNString.currency(deduction_amount) + " | " +
                deduction_type;
    }
}
