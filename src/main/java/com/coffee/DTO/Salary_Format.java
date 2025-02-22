package com.coffee.DTO;

public class Salary_Format {
    private int id;
    private String name;

    public Salary_Format() {
    }

    public Salary_Format(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return id + " | " +
                name;
    }
}
