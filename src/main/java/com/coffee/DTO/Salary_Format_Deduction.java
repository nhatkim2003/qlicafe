package com.coffee.DTO;

public class Salary_Format_Deduction {
    private int salary_format_id;
    private int deduction_id;

    public Salary_Format_Deduction(int salary_format_id, int deduction_id) {
        this.salary_format_id = salary_format_id;
        this.deduction_id = deduction_id;
    }

    public Salary_Format_Deduction() {
    }

    public int getSalary_format_id() {
        return salary_format_id;
    }

    public void setSalary_format_id(int salary_format_id) {
        this.salary_format_id = salary_format_id;
    }

    public int getDeduction_id() {
        return deduction_id;
    }

    public void setDeduction_id(int deduction_id) {
        this.deduction_id = deduction_id;
    }

    @Override
    public String toString() {
        return salary_format_id + " | " + deduction_id;
    }
}
