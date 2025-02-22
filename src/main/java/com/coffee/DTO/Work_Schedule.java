package com.coffee.DTO;

import java.util.Date;

public class Work_Schedule {
    private int id;
    private int staff_id;
    private Date date;
    private String check_in;
    private String check_out;
    private int shift;
    private String notice;

    public Work_Schedule() {
    }

    public Work_Schedule(int id, int staff_id, Date date, String check_in, String check_out, int shift, String notice) {
        this.id = id;
        this.staff_id = staff_id;
        this.date = date;
        this.check_in = check_in;
        this.check_out = check_out;
        this.shift = shift;
        this.notice = notice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return id + " | " +
                staff_id + " | " +
                date + " | " +
                shift + " | " +
                check_in + " | " +
                check_out;
    }
}
