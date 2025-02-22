package com.coffee.DTO;

public class Salary_Format_Allowance {
    private int salary_format_id;
    private int allowance_id;

    public Salary_Format_Allowance(int salary_format_id, int allowance_id) {
        this.salary_format_id = salary_format_id;
        this.allowance_id = allowance_id;
    }

    public Salary_Format_Allowance() {
    }

    public int getSalary_format_id() {
        return salary_format_id;
    }

    public void setSalary_format_id(int salary_format_id) {
        this.salary_format_id = salary_format_id;
    }

    public int getAllowance_id() {
        return allowance_id;
    }

    public void setAllowance_id(int allowance_id) {
        this.allowance_id = allowance_id;
    }

    @Override
    public String toString() {
        return salary_format_id + " | " + allowance_id;
    }
}
