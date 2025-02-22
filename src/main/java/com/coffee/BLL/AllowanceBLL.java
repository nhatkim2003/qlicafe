package com.coffee.BLL;

import com.coffee.DAL.AllowanceDAL;
import com.coffee.DTO.Allowance;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllowanceBLL extends Manager<Allowance> {
    private AllowanceDAL allowanceDAL;

    public AllowanceBLL() {
        allowanceDAL = new AllowanceDAL();
    }

    public AllowanceDAL getAllowanceDAL() {
        return allowanceDAL;
    }

    public void setAllowanceDAL(AllowanceDAL allowanceDAL) {
        this.allowanceDAL = allowanceDAL;
    }

    public Object[][] getData() {
        return getData(allowanceDAL.searchAllowances());
    }

    public Pair<Boolean, String> addAllowance(Allowance allowance) {
        Pair<Boolean, String> result;

        result = validateName(allowance.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(allowance);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (allowanceDAL.addAllowance(allowance) == 0)
            return new Pair<>(false, "Thêm phụ cấp không thành công.");

        return new Pair<>(true, "Thêm phụ cấp thành công.");
    }

    public Pair<Boolean, String> updateAllowance(Allowance allowance) {
        Pair<Boolean, String> result;

        result = validateName(allowance.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (allowanceDAL.updateAllowance(allowance) == 0)
            return new Pair<>(false, "Cập nhật phụ cấp không thành công.");

        return new Pair<>(true, "Cập nhật phụ cấp thành công.");
    }

    public List<Allowance> searchAllowances(String... conditions) {
        return allowanceDAL.searchAllowances(conditions);
    }

    public List<Allowance> findAllowances(String key, String value) {
        List<Allowance> list = new ArrayList<>();
        List<Allowance> allowanceList = allowanceDAL.searchAllowances();
        for (Allowance allowance : allowanceList) {
            if (getValueByKey(allowance, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(allowance);
            }
        }
        return list;
    }

    public List<Allowance> findAllowancesBy(Map<String, Object> conditions) {
        List<Allowance> allowanceses = allowanceDAL.searchAllowances();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            allowanceses = findObjectsBy(entry.getKey(), entry.getValue(), allowanceses);
        return allowanceses;
    }

    public Pair<Boolean, String> exists(Allowance allowance) {
        List<Allowance> modules = findAllowancesBy(Map.of("name", allowance.getName()));

        if (!modules.isEmpty()) {
            return new Pair<>(true, "Phụ cấp đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    private Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên phụ cấp không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên phụ cấp không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên phụ cấp không được chứa số.");
        return new Pair<>(true, name);
    }

    @Override
    public Object getValueByKey(Allowance allowance, String key) {
        return switch (key) {
            case "id" -> allowance.getId();
            case "name" -> allowance.getName();
            case "allowance_amount" -> allowance.getAllowance_amount();
            case "allowance_type" -> allowance.getAllowance_type();
            default -> null;
        };
    }

    public static void main(String[] args) {
        AllowanceBLL allowanceBLL = new AllowanceBLL();
//        Allowance allowance = new Allowance(15, "abc");
//        allowanceBLL.addAllowance(allowance);
//        allowance.setName("xyz");
//        allowanceBLL.updateAllowance(allowance);
//        allowanceBLL.deleteAllowance(allowance);

        System.out.println(allowanceBLL.searchAllowances());
    }
}
