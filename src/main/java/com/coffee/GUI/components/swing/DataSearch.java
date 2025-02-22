package com.coffee.GUI.components.swing;

public class DataSearch {

    public String getText() {
        return text;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public void setText(String text) {
        this.text = text;
    }
    public DataSearch(String text) {
        this.text = text;
    }

    public DataSearch() {
    }

    public DataSearch(String text, String text1,String price) {
        this.text = text;
        this.text1 = text1;
        this.price = price;
    }

    private String text;
    private String text1;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
}
