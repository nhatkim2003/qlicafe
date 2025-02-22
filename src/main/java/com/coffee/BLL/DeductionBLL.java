package com.coffee.BLL;

import com.coffee.DAL.DeductionDAL;
import com.coffee.DTO.Deduction;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeductionBLL extends Manager<Deduction> {
    private DeductionDAL deductionDAL;

    public DeductionBLL() {
        deductionDAL = new DeductionDAL();
    }

    public DeductionDAL getDeductionDAL() {
        return deductionDAL;
    }

    public void setDeductionDAL(DeductionDAL deductionDAL) {
        this.deductionDAL = deductionDAL;
    }

    public Object[][] getData() {
        return getData(deductionDAL.searchDeductions());
    }

    public Pair<Boolean, String> addDeduction(Deduction deduction) {
        Pair<Boolean, String> result;

        result = validateName(deduction.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(deduction);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (deductionDAL.addDeduction(deduction) == 0)
            return new Pair<>(false, "Thêm giảm trừ không thành công.");

        return new Pair<>(true, "Thêm giảm trừ thành công.");
    }

    public Pair<Boolean, String> updateDeduction(Deduction deduction) {
        Pair<Boolean, String> result;

        result = validateName(deduction.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (deductionDAL.updateDeduction(deduction) == 0)
            return new Pair<>(false, "Cập nhật giảm trừ không thành công.");

        return new Pair<>(true, "Cập nhật giảm trừ thành công.");
    }

    public List<Deduction> searchDeductions(String... conditions) {
        return deductionDAL.searchDeductions(conditions);
    }

    public List<Deduction> findDeductions(String key, String value) {
        List<Deduction> list = new ArrayList<>();
        List<Deduction> deductionList = deductionDAL.searchDeductions();
        for (Deduction deduction : deductionList) {
            if (getValueByKey(deduction, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(deduction);
            }
        }
        return list;
    }

    public List<Deduction> findDeductionsBy(Map<String, Object> conditions) {
        List<Deduction> deductions = deductionDAL.searchDeductions();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            deductions = findObjectsBy(entry.getKey(), entry.getValue(), deductions);
        return deductions;
    }

    public Pair<Boolean, String> exists(Deduction deduction) {
        List<Deduction> modules = findDeductionsBy(Map.of("name", deduction.getName()));

        if (!modules.isEmpty()) {
            return new Pair<>(true, "Giảm trừ đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    private Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên giảm trừ không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên giảm trừ không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên giảm trừ không được chứa số.");
        return new Pair<>(true, name);
    }

    @Override
    public Object getValueByKey(Deduction deduction, String key) {
        return switch (key) {
            case "id" -> deduction.getId();
            case "name" -> deduction.getName();
            case "deduction_amount" -> deduction.getDeduction_amount();
            case "deduction_type" -> deduction.getDeduction_type();
            default -> null;
        };
    }

    public static void main(String[] args) {
        DeductionBLL deductionBLL = new DeductionBLL();
//        Deduction deduction = new Deduction(15, "abc");
//        deductionBLL.addDeduction(deduction);
//        deduction.setName("xyz");
//        deductionBLL.updateDeduction(deduction);
//        deductionBLL.deleteDeduction(deduction);

        System.out.println(deductionBLL.searchDeductions());
    }
}
