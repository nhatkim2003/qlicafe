package com.coffee.DTO;

public class Product {
    private int id;
    private String name;
    private String size;
    private String category;
    private double capital_price;
    private double price;
    private String image;
    private boolean deleted;

    public Product() {
    }

    public Product(int id, String name, String size, String category, double capital_price, double price, String image, boolean deleted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.capital_price = capital_price;
        this.price = price;
        this.size = size;
        this.image = image;
        this.deleted = deleted;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCapital_price() {
        return capital_price;
    }

    public void setCapital_price(double capital_price) {
        this.capital_price = capital_price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return id + " | " +
                name + " | " +
                category + " | " +
                price + " | " +
                size + " | " +
                image + " | " +
                deleted;
    }
    public Product clone() {
        return new Product(id, name, size, category, capital_price, price, image, deleted);
    }
}
