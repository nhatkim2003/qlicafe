package com.coffee.DTO;

public class Export_Detail {
    private int export_id;
    private int shipment_id;
    private double quantity;
    private String reason;

    public Export_Detail() {
    }

    public Export_Detail(int export_id, int shipment_id, double quantity, String reason) {
        this.export_id = export_id;
        this.shipment_id = shipment_id;
        this.quantity = quantity;
        this.reason = reason;
    }

    public int getExport_id() {
        return export_id;
    }

    public void setExport_id(int export_id) {
        this.export_id = export_id;
    }

    public int getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(int shipment_id) {
        this.shipment_id = shipment_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return export_id + " | " +
                shipment_id + " | " +
                "Name" + " | " +
                quantity + " | " +
                reason;
    }
}
