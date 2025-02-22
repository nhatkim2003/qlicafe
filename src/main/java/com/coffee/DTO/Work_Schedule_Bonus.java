package com.coffee.DTO;

public class Work_Schedule_Bonus {
    private int work_schedule_id;
    private String bonus_name;
    private double bonus_amount;
    private int quantity;
    private double bonus_total;

    public Work_Schedule_Bonus() {
    }

    public Work_Schedule_Bonus(int work_schedule_id, String bonus_name, double bonus_amount, int quantity, double bonus_total) {
        this.work_schedule_id = work_schedule_id;
        this.bonus_name = bonus_name;
        this.bonus_amount = bonus_amount;
        this.quantity = quantity;
        this.bonus_total = bonus_total;
    }

    public int getWork_schedule_id() {
        return work_schedule_id;
    }

    public void setWork_schedule_id(int work_schedule_id) {
        this.work_schedule_id = work_schedule_id;
    }

    public String getBonus_name() {
        return bonus_name;
    }

    public void setBonus_name(String bonus_name) {
        this.bonus_name = bonus_name;
    }

    public double getBonus_amount() {
        return bonus_amount;
    }

    public void setBonus_amount(double bonus_amount) {
        this.bonus_amount = bonus_amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getBonus_total() {
        return bonus_total;
    }

    public void setBonus_total(double bonus_total) {
        this.bonus_total = bonus_total;
    }

    @Override
    public String toString() {
        return work_schedule_id + " | " +
                bonus_name + " | " +
                bonus_amount + " | " +
                quantity + " | " +
                bonus_total;
    }
}
