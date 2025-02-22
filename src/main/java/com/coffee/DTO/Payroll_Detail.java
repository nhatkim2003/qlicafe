package com.coffee.DTO;

import com.coffee.utils.VNString;

import java.time.LocalDateTime;

public class Payroll_Detail {
    private int payroll_id;
    private int staff_id;
    private double hours_amount;
    private double allowance_amount;
    private double deduction_amount;
    private double bonus_amount;
    private double fine_amount;
    private double salary_amount;
    private boolean status;

    public Payroll_Detail() {
    }

    public Payroll_Detail(int payroll_id, int staff_id, double hours_amount, double allowance_amount, double deduction_amount, double bonus_amount, double fine_amount, double salary_amount, boolean status) {
        this.payroll_id = payroll_id;
        this.staff_id = staff_id;
        this.hours_amount = hours_amount;
        this.allowance_amount = allowance_amount;
        this.deduction_amount = deduction_amount;
        this.bonus_amount = bonus_amount;
        this.fine_amount = fine_amount;
        this.salary_amount = salary_amount;
        this.status = status;
    }

    public int getPayroll_id() {
        return payroll_id;
    }

    public void setPayroll_id(int payroll_id) {
        this.payroll_id = payroll_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public double getHours_amount() {
        return hours_amount;
    }

    public void setHours_amount(double hours_amount) {
        this.hours_amount = hours_amount;
    }

    public double getAllowance_amount() {
        return allowance_amount;
    }

    public void setAllowance_amount(double allowance_amount) {
        this.allowance_amount = allowance_amount;
    }

    public double getDeduction_amount() {
        return deduction_amount;
    }

    public void setDeduction_amount(double deduction_amount) {
        this.deduction_amount = deduction_amount;
    }

    public double getBonus_amount() {
        return bonus_amount;
    }

    public void setBonus_amount(double bonus_amount) {
        this.bonus_amount = bonus_amount;
    }

    public double getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(double fine_amount) {
        this.fine_amount = fine_amount;
    }

    public double getSalary_amount() {
        return salary_amount;
    }

    public void setSalary_amount(double salary_amount) {
        this.salary_amount = salary_amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
//        String status1 = status ? "Đã trả" : "Tạm tính";
        return payroll_id + " | " +
                staff_id + " | " +
                "staff_name" + " | " +
                VNString.currency(salary_amount) + " | " +
                status;
    }
}
