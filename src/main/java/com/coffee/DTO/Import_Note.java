package com.coffee.DTO;

import com.coffee.utils.VNString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Import_Note {
    private int id;
    private int staff_id;
    private BigDecimal total;
    private LocalDateTime received_date;

    public Import_Note() {
    }

    public Import_Note(int id, int staff_id, BigDecimal total, LocalDateTime received_date) {
        this.id = id;
        this.staff_id = staff_id;
        this.total = total;
        this.received_date = received_date;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getReceived_date() {
        return received_date;
    }

    public void setReceived_date(LocalDateTime received_date) {
        this.received_date = received_date;
    }

    @Override
    public String toString() {
        return id + " | " +
                staff_id + " | " +
                VNString.currency(Double.parseDouble(total.toString())) + " | " +
                received_date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
