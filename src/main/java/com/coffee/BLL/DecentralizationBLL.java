package com.coffee.BLL;

import com.coffee.DAL.DecentralizationDAL;
import com.coffee.DTO.Decentralization;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecentralizationBLL extends Manager<Decentralization>{
    private DecentralizationDAL decentralizationDAL;

    public DecentralizationBLL() {
        decentralizationDAL = new DecentralizationDAL();
    }

    public DecentralizationDAL getDecentralizationDAL() {
        return decentralizationDAL;
    }

    public void setDecentralizationDAL(DecentralizationDAL decentralizationDAL) {
        this.decentralizationDAL = decentralizationDAL;
    }

    public Object[][] getData() {
        return getData(decentralizationDAL.searchDecentralizations());
    }

    public Pair<Boolean, String> addDecentralization(Decentralization decentralization) {
        Pair<Boolean, String> result;

        result = exists(decentralization);
        if (result.getKey()) {
            return new Pair<>(false,result.getValue());
        }

        if (decentralizationDAL.addDecentralization(decentralization) == 0)
            return new Pair<>(false, "Thêm phân quyền không thành công.");

        return new Pair<>(true,"Thêm phân quyền thành công.");
    }

    public Pair<Boolean, String> updateDecentralization(Decentralization old_decentralization, Decentralization new_decentralization2) {
        if (decentralizationDAL.updateDecentralization(old_decentralization, new_decentralization2) == 0)
            return new Pair<>(false, "Cập nhật phân quyền không thành công.");

        return new Pair<>(true,"Cập nhật phân quyền thành công.");
    }

    public Pair<Boolean, String> deleteDecentralization(Decentralization decentralization) {
        if (decentralizationDAL.deleteDecentralization(
                "role_id = " + decentralization.getRole_id(),
                "module_id = " + decentralization.getModule_id(),
                "function_id = " + decentralization.getFunction_id()) == 0)
            return new Pair<>(false, "Xoá phân quyền không thành công.");

        return new Pair<>(true,"Xoá phân quyền thành công.");
    }

    public List<Decentralization> searchDecentralizations(String... conditions) {
        return decentralizationDAL.searchDecentralizations(conditions);
    }

    public List<Decentralization> findDecentralizations(String key, String value) {
        List<Decentralization> list = new ArrayList<>();
        List<Decentralization> decentralizationList = decentralizationDAL.searchDecentralizations();
        for (Decentralization decentralization : decentralizationList) {
            if (getValueByKey(decentralization, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(decentralization);
            }
        }
        return list;
    }

    public List<Decentralization> findDecentralizationsBy(Map<String, Object> conditions) {
        List<Decentralization> decentralizations = decentralizationDAL.searchDecentralizations();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            decentralizations = findObjectsBy(entry.getKey(), entry.getValue(), decentralizations);
        return decentralizations;
    }

    public Pair<Boolean, String> exists(Decentralization decentralization) {
        List<Decentralization> decentralizations = findDecentralizationsBy(Map.of(
                "role_id", decentralization.getRole_id(),
                "module_id", decentralization.getModule_id(),
                "function_id",decentralization.getFunction_id()
        ));

        if(!decentralizations.isEmpty()){
            return new Pair<>(true, "Phân quyền đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Decentralization decentralization, String key) {
        return switch (key) {
            case "role_id" -> decentralization.getRole_id();
            case "module_id" -> decentralization.getModule_id();
            case "function_id" -> decentralization.getFunction_id();
            default -> null;
        };
    }

    public static void main(String[] args) {
        DecentralizationBLL decentralizationBLL = new DecentralizationBLL();
        Decentralization decentralization1 = new Decentralization(3, 10, 1);
//        decentralizationBLL.addDecentralization(decentralization);
        Decentralization decentralization2 = new Decentralization(3, 10, 1);
        decentralization2.setFunction_id(4);
//        decentralizationBLL.updateDecentralization(decentralization1, decentralization2);

        decentralizationBLL.deleteDecentralization(decentralization2);
    }
}
