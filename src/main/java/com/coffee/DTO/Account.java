package com.coffee.DTO;

public class Account {
    private int id;
    private String username;
    private String password;
    private int staff_id;

    public Account() {
    }

    public Account(int id, String username, int staff_id) {
        this.id = id;
        this.username = username;
        this.staff_id = staff_id;
    }

    public Account(int id, String username, String password, int staff_id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.staff_id = staff_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    @Override
    public String toString() {
        return id + " | " +
                username + " | " +
                staff_id;
    }
}
