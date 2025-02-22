package com.coffee.BLL;

import com.coffee.DAL.Payroll_DetailDAL;
import com.coffee.DTO.Payroll_Detail;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Payroll_DetailBLL extends Manager<Payroll_Detail> {
    private Payroll_DetailDAL payroll_DetailDAL;

    public Payroll_DetailBLL() {
        payroll_DetailDAL = new Payroll_DetailDAL();
    }

    public Payroll_DetailDAL getPayroll_DetailDAL() {
        return payroll_DetailDAL;
    }

    public void setPayroll_DetailDAL(Payroll_DetailDAL payroll_DetailDAL) {
        this.payroll_DetailDAL = payroll_DetailDAL;
    }

    public Object[][] getData() {
        return getData(payroll_DetailDAL.searchPayroll_Details());
    }

    public Pair<Boolean, String> addPayroll_Detail(Payroll_Detail payroll_Detail) {
        Pair<Boolean, String> result;

        result = exists(payroll_Detail);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (payroll_DetailDAL.addPayroll_Detail(payroll_Detail) == 0)
            return new Pair<>(false, "Thêm phiếu lương không thành công.");

        return new Pair<>(true, "Thêm phiếu lương thành công.");
    }

    public Pair<Boolean, String> updatePayroll_Detail(Payroll_Detail payroll_Detail) {
        if (payroll_DetailDAL.updatePayroll_Detail(payroll_Detail) == 0)
            return new Pair<>(false, "Cập nhật phiếu lương không thành công.");

        return new Pair<>(true, "Cập nhật phiếu lương thành công.");
    }

    public List<Payroll_Detail> searchPayroll_Details(String... conditions) {
        return payroll_DetailDAL.searchPayroll_Details(conditions);
    }

    public List<Payroll_Detail> findPayroll_Details(String key, String value) {
        List<Payroll_Detail> list = new ArrayList<>();
        List<Payroll_Detail> payroll_DetailList = payroll_DetailDAL.searchPayroll_Details();
        for (Payroll_Detail payroll_Detail : payroll_DetailList) {
            if (getValueByKey(payroll_Detail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(payroll_Detail);
            }
        }
        return list;
    }

    public List<Payroll_Detail> findPayroll_DetailsBy(Map<String, Object> conditions) {
        List<Payroll_Detail> payroll_Details = payroll_DetailDAL.searchPayroll_Details();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            payroll_Details = findObjectsBy(entry.getKey(), entry.getValue(), payroll_Details);
        return payroll_Details;
    }

    public Pair<Boolean, String> exists(Payroll_Detail payroll_Detail) {
        List<Payroll_Detail> payroll_Details = findPayroll_DetailsBy(Map.of(
                "payroll_id", payroll_Detail.getPayroll_id(),
                "staff_id", payroll_Detail.getStaff_id()
        ));

        if (!payroll_Details.isEmpty()) {
            return new Pair<>(true, "Phiếu lương nhân viên trong tháng này đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Payroll_Detail payroll_Detail, String key) {
        return switch (key) {
            case "payroll_id" -> payroll_Detail.getPayroll_id();
            case "staff_id" -> payroll_Detail.getStaff_id();
            case "hours_amount" -> payroll_Detail.getHours_amount();
            case "allowance_amount" -> payroll_Detail.getAllowance_amount();
            case "deduction_amount" -> payroll_Detail.getDeduction_amount();
            case "bonus_amount" -> payroll_Detail.getBonus_amount();
            case "fine_amount" -> payroll_Detail.getFine_amount();
            case "salary_amount" -> payroll_Detail.getSalary_amount();
            case "status" -> payroll_Detail.isStatus();
            default -> null;
        };
    }
}
