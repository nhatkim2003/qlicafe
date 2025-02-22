package com.coffee.BLL;

import com.coffee.DAL.Salary_Format_AllowanceDAL;
import com.coffee.DTO.Salary_Format_Allowance;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Salary_Format_AllowanceBLL extends Manager<Salary_Format_Allowance> {
    private Salary_Format_AllowanceDAL salary_format_allowanceDAL;

    public Salary_Format_AllowanceBLL() {
        salary_format_allowanceDAL = new Salary_Format_AllowanceDAL();
    }

    public Salary_Format_AllowanceDAL getSalary_Format_AllowanceDAL() {
        return salary_format_allowanceDAL;
    }

    public void setSalary_Format_AllowanceDAL(Salary_Format_AllowanceDAL salary_format_allowanceDAL) {
        this.salary_format_allowanceDAL = salary_format_allowanceDAL;
    }

    public Object[][] getData() {
        return getData(salary_format_allowanceDAL.searchSalary_Format_Allowances());
    }

    public Pair<Boolean, String> addSalary_Format_Allowance(Salary_Format_Allowance salary_format_allowance) {
        if (salary_format_allowanceDAL.addSalary_Format_Allowance(salary_format_allowance) == 0)
            return new Pair<>(false, "Thêm phụ cấp vào mẫu lương không thành công.");

        return new Pair<>(true, "Thêm phụ cấp vào mẫu lương thành công.");
    }

    public Pair<Boolean, String> deleteSalary_Format_Allowance(Salary_Format_Allowance salary_format_allowance) {
        Pair<Boolean, String> result;

        if (salary_format_allowanceDAL.deleteSalary_Format_Allowance("salary_format_id = " + salary_format_allowance.getSalary_format_id(), "allowance_id = " + salary_format_allowance.getAllowance_id()) == 0)
            return new Pair<>(false, "Xoá phụ cấp khỏi mẫu lương không thành công.");

        return new Pair<>(true, "Xoá phụ cấp khỏi mẫu lương thành công.");
    }

    public List<Salary_Format_Allowance> searchSalary_Format_Allowances(String... conditions) {
        return salary_format_allowanceDAL.searchSalary_Format_Allowances(conditions);
    }

    public List<Salary_Format_Allowance> findSalary_Format_Allowances(String key, String value) {
        List<Salary_Format_Allowance> list = new ArrayList<>();
        List<Salary_Format_Allowance> salary_format_allowanceList = salary_format_allowanceDAL.searchSalary_Format_Allowances();
        for (Salary_Format_Allowance salary_format_allowance : salary_format_allowanceList) {
            if (getValueByKey(salary_format_allowance, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(salary_format_allowance);
            }
        }
        return list;
    }

    public List<Salary_Format_Allowance> findSalary_Format_AllowancesBy(Map<String, Object> conditions) {
        List<Salary_Format_Allowance> salary_format_allowances = salary_format_allowanceDAL.searchSalary_Format_Allowances();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            salary_format_allowances = findObjectsBy(entry.getKey(), entry.getValue(), salary_format_allowances);
        return salary_format_allowances;
    }

    @Override
    public Object getValueByKey(Salary_Format_Allowance salary_format_allowance, String key) {
        return switch (key) {
            case "salary_format_id" -> salary_format_allowance.getSalary_format_id();
            case "allowance_id" -> salary_format_allowance.getAllowance_id();
            default -> null;
        };
    }

    public static void main(String[] args) {
//        Salary_Format_Allowance_AllowanceBLL salary_format_allowanceBLL = new Salary_Format_Allowance_AllowanceBLL();
//        Salary_Format_Allowance salary_format_allowance = new Salary_Format_Allowance(15, "abc");
//        salary_format_allowanceBLL.addSalary_Format_Allowance(salary_format_allowance);
//        salary_format_allowance.setName("xyz");
//        salary_format_allowanceBLL.updateSalary_Format_Allowance(salary_format_allowance);
//        salary_format_allowanceBLL.deleteSalary_Format_Allowance(salary_format_allowance);

//        System.out.println(salary_format_allowanceBLL.searchSalary_Format_Allowances());
    }
}
