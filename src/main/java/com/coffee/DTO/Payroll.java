package com.coffee.DTO;

import com.coffee.utils.VNString;

import java.util.Date;

public class Payroll {
    private int id;
    private String name;
    private Date entry_date;
    private int month;
    private int year;
    private double total_salary;
    private double paid;
    private double debt;

    public Payroll() {
    }

    public Payroll(int id, String name, Date entry_date, int month, int year, double total_salary, double paid, double debt) {
        this.id = id;
        this.name = name;
        this.entry_date = entry_date;
        this.month = month;
        this.year = year;
        this.total_salary = total_salary;
        this.paid = paid;
        this.debt = debt;
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

    public Date getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(Date entry_date) {
        this.entry_date = entry_date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotal_salary() {
        return total_salary;
    }

    public void setTotal_salary(double total_salary) {
        this.total_salary = total_salary;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    @Override
    public String toString() {
        return id + " | " +
                " " + name + " | " +
                month + "/" + year + " | " +
                VNString.currency(total_salary) + " | " +
                VNString.currency(paid) + " | " +
                VNString.currency(debt);
    }
}
