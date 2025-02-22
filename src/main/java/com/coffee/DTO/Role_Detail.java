package com.coffee.DTO;


import java.time.LocalDateTime;

public class Role_Detail {
    private int role_id;
    private int staff_id;
    private LocalDateTime entry_date;
    private double salary;
    private int type_salary;

    public Role_Detail() {
    }

    public Role_Detail(int role_id, int staff_id, LocalDateTime entry_date, double salary, int type_salary) {
        this.role_id = role_id;
        this.staff_id = staff_id;
        this.entry_date = entry_date;
        this.salary = salary;
        this.type_salary = type_salary;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public LocalDateTime getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(LocalDateTime entry_date) {
        this.entry_date = entry_date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getType_salary() {
        return type_salary;
    }

    public void setType_salary(int type_salary) {
        this.type_salary = type_salary;
    }

    @Override
    public String toString() {
        return role_id + " | " +
                staff_id + " | " +
                entry_date + " | " +
                salary + " | " +
                type_salary;
    }
}
