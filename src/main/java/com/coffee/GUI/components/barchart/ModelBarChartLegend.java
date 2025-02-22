package com.coffee.GUI.components.barchart;

import java.awt.*;

public class ModelBarChartLegend {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ModelBarChartLegend(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public ModelBarChartLegend() {
    }

    private String name;
    private Color color;
}
