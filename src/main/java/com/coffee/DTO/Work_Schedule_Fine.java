package com.coffee.DTO;

public class Work_Schedule_Fine {
    private int work_schedule_id;
    private String fine_name;
    private double fine_amount;
    private int quantity;
    private double fine_total;

    public Work_Schedule_Fine() {
    }

    public Work_Schedule_Fine(int work_schedule_id, String fine_name, double fine_amount, int quantity, double fine_total) {
        this.work_schedule_id = work_schedule_id;
        this.fine_name = fine_name;
        this.fine_amount = fine_amount;
        this.quantity = quantity;
        this.fine_total = fine_total;
    }

    public int getWork_schedule_id() {
        return work_schedule_id;
    }

    public void setWork_schedule_id(int work_schedule_id) {
        this.work_schedule_id = work_schedule_id;
    }

    public String getFine_name() {
        return fine_name;
    }

    public void setFine_name(String fine_name) {
        this.fine_name = fine_name;
    }

    public double getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(double fine_amount) {
        this.fine_amount = fine_amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getFine_total() {
        return fine_total;
    }

    public void setFine_total(double fine_total) {
        this.fine_total = fine_total;
    }

    @Override
    public String toString() {
        return work_schedule_id + " | " +
                fine_name + " | " +
                fine_amount + " | " +
                quantity + " | " +
                fine_total;
    }
}
