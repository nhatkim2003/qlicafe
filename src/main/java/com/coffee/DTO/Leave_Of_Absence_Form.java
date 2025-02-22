package com.coffee.DTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Leave_Of_Absence_Form {
    private int id;
    private int staff_id;
    private Date date;
    private Date date_off;
    private String shifts;
    private String reason;
    private int status;

    public Leave_Of_Absence_Form() {
    }

    public Leave_Of_Absence_Form(int id, int staff_id, Date date, Date date_off, String shifts, String reason, int status) {
        this.id = id;
        this.staff_id = staff_id;
        this.date = date;
        this.date_off = date_off;
        this.shifts = shifts;
        this.reason = reason;
        this.status = status;
    }

    public Date getDate_off() {
        return date_off;
    }

    public void setDate_off(Date date_off) {
        this.date_off = date_off;
    }

    public String getShifts() {
        return shifts;
    }

    public void setShifts(String shifts) {
        this.shifts = shifts;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String status1 = status == 0 ? "Chưa duyệt" : status == 1 ? "Duyệt" : "Không duyệt";
        return id + " | " +
                staff_id + " | " +
                new SimpleDateFormat("dd/MM/yyyy").format(date) + " | " +
                new SimpleDateFormat("dd/MM/yyyy").format(date_off) + " | " +
                shifts + " | " +
                status1;
    }
}
