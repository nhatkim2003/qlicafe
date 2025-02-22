package com.coffee.BLL;

import com.coffee.DAL.DecentralizationDAL;
import com.coffee.DAL.FunctionDAL;
import com.coffee.DTO.Decentralization;
import com.coffee.DTO.Export_Detail;
import com.coffee.DTO.Function;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionBLL extends Manager<Function>{
    private FunctionDAL functionDAL;

    public FunctionBLL() {
        functionDAL = new FunctionDAL();
    }

    public FunctionDAL getFunctionDAL() {
        return functionDAL;
    }

    public void setFunctionDAL(FunctionDAL functionDAL) {
        this.functionDAL = functionDAL;
    }

    public Object[][] getData() {
        return getData(functionDAL.searchFunctions());
    }

    public Pair<Boolean, String> addFunction(Function function) {
        Pair<Boolean, String> result = checkFunctionAll(function);

        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (functionDAL.addFunction(function) == 0)
            return new Pair<>(false, "Thêm chức năng không thành công.");

        return new Pair<>(true, "Thêm chức năng thành công.");
    }

    public Pair<Boolean, String> updateFunction(Function function) {
        Pair<Boolean, String> result = checkFunctionAll(function);

        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        if (functionDAL.updateFunction(function) == 0)
            return new Pair<>(false, "Cập nhật chức năng không thành công.");

        return new Pair<>(true, "Cập nhật chức năng thành công.");
    }

    public Pair<Boolean, String> deleteFunction(Function function) {
        Pair<Boolean, String> result;

        result = checkDecentralization(function);
        if(result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (functionDAL.deleteFunction("id = " + function.getId()) == 0)
            return new Pair<>(false, "Xoá chức năng không thành công.");

        return new Pair<>(true, "Xoá chức năng thành công.");
    }

    public List<Function> searchFunctions(String... conditions) {
        return functionDAL.searchFunctions(conditions);
    }

    public List<Function> findFunctions(String key, String value) {
        List<Function> list = new ArrayList<>();
        List<Function> functionList = functionDAL.searchFunctions();
        for (Function function : functionList) {
            if (getValueByKey(function, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(function);
            }
        }
        return list;
    }

    public List<Function> findFunctionsBy(Map<String, Object> conditions) {
        List<Function> functions = functionDAL.searchFunctions();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            functions = findObjectsBy(entry.getKey(), entry.getValue(), functions);
        return functions;
    }
    public  Pair<Boolean, String> checkFunctionAll(Function function) {
        Pair<Boolean, String> result;
        result = validateName(function.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(function);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        return new Pair<>(true,"");

    }

    private static Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên chức năng không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên chức năng không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên chức năng không được chứa số.");
        return new Pair<>(true, name);
    }
    public Pair<Boolean, String> exists(Function function) {
        List<Function> functions = findFunctionsBy(Map.of(
                "name", function.getName()
        ));

        if(!functions.isEmpty()){
            return new Pair<>(true, "Chức năng đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> checkDecentralization(Function function) {
        DecentralizationBLL decentralizationBLL = new DecentralizationBLL();
        List<Decentralization> decentralizationss = decentralizationBLL.findDecentralizationsBy(Map.of(
                "function_id", function.getId()
        ));

        if(!decentralizationss.isEmpty()){
            return new Pair<>(true, "Chức năng đã tồn tại trong phân quyền.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Function function, String key) {
        return switch (key) {
            case "id" -> function.getId();
            case "name" -> function.getName();
            default -> null;
        };
    }

    public static void main(String[] args) {
        FunctionBLL functionBLL = new FunctionBLL();
        Function function = new Function(8, "abc");
//        functionBLL.addFunction(function);
        function.setName("xyz");
//        functionBLL.updateFunction(function);
        functionBLL.deleteFunction(function);
    }
}
