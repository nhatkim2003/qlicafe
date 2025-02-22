package com.coffee.GUI.components.line_chart;

/**
 * @author RAVEN
 */
public class ModelData {

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(double withdrawal) {
        this.withdrawal = withdrawal;
    }

    public double getTransfer() {
        return transfer;
    }

    public void setTransfer(double transfer) {
        this.transfer = transfer;
    }

    public ModelData(String month, double deposit, double withdrawal, double transfer) {
        this.month = month;
        this.deposit = deposit;
        this.withdrawal = withdrawal;
        this.transfer = transfer;
    }

    public ModelData() {
    }

    private String month;
    private double deposit;
    private double withdrawal;
    private double transfer;
}
