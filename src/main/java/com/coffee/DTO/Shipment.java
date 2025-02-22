package com.coffee.DTO;

import java.util.Date;

public class Shipment {
    private int id;
    private int material_id;
    private int supplier_id;
    private int import_id;
    private double quantity;
    private double remain;
    private Date mfg;
    private Date exp;

    public Shipment() {
    }

    public Shipment(int id, int material_id, int supplier_id, int import_id, double quantity, double remain, Date mfg, Date exp) {
        this.id = id;
        this.material_id = material_id;
        this.supplier_id = supplier_id;
        this.import_id = import_id;
        this.quantity = quantity;
        this.remain = remain;
        this.mfg = mfg;
        this.exp = exp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getImport_id() {
        return import_id;
    }

    public void setImport_id(int import_id) {
        this.import_id = import_id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }

    public Date getMfg() {
        return mfg;
    }

    public void setMfg(Date mfg) {
        this.mfg = mfg;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return id + " | " +
                material_id + " | " +
                supplier_id + " | " +
                import_id + " | " +
                quantity + " | " +
                remain + " | " +
                mfg + " | " +
                exp;
    }
}
