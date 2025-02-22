package com.coffee.BLL;

import com.coffee.DAL.Salary_Format_DeductionDAL;
import com.coffee.DTO.Salary_Format_Deduction;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Salary_Format_DeductionBLL extends Manager<Salary_Format_Deduction> {
    private Salary_Format_DeductionDAL salary_format_deductionDAL;

    public Salary_Format_DeductionBLL() {
        salary_format_deductionDAL = new Salary_Format_DeductionDAL();
    }

    public Salary_Format_DeductionDAL getSalary_Format_DeductionDAL() {
        return salary_format_deductionDAL;
    }

    public void setSalary_Format_DeductionDAL(Salary_Format_DeductionDAL salary_format_deductionDAL) {
        this.salary_format_deductionDAL = salary_format_deductionDAL;
    }

    public Object[][] getData() {
        return getData(salary_format_deductionDAL.searchSalary_Format_Deductions());
    }

    public Pair<Boolean, String> addSalary_Format_Deduction(Salary_Format_Deduction salary_format_deduction) {
        if (salary_format_deductionDAL.addSalary_Format_Deduction(salary_format_deduction) == 0)
            return new Pair<>(false, "Thêm giảm trừ vào mẫu lương không thành công.");

        return new Pair<>(true, "Thêm giảm trừ vào mẫu lương thành công.");
    }

    public Pair<Boolean, String> deleteSalary_Format_Deduction(Salary_Format_Deduction salary_format_deduction) {
        Pair<Boolean, String> result;

        if (salary_format_deductionDAL.deleteSalary_Format_Deduction("salary_format_id = " + salary_format_deduction.getSalary_format_id(), "deduction_id = " + salary_format_deduction.getDeduction_id()) == 0)
            return new Pair<>(false, "Xoá giảm trừ khỏi mẫu lương không thành công.");

        return new Pair<>(true, "Xoá giảm trừ khỏi mẫu lương thành công.");
    }

    public List<Salary_Format_Deduction> searchSalary_Format_Deductions(String... conditions) {
        return salary_format_deductionDAL.searchSalary_Format_Deductions(conditions);
    }

    public List<Salary_Format_Deduction> findSalary_Format_Deductions(String key, String value) {
        List<Salary_Format_Deduction> list = new ArrayList<>();
        List<Salary_Format_Deduction> salary_format_deductionList = salary_format_deductionDAL.searchSalary_Format_Deductions();
        for (Salary_Format_Deduction salary_format_deduction : salary_format_deductionList) {
            if (getValueByKey(salary_format_deduction, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(salary_format_deduction);
            }
        }
        return list;
    }

    public List<Salary_Format_Deduction> findSalary_Format_DeductionsBy(Map<String, Object> conditions) {
        List<Salary_Format_Deduction> salary_format_deductions = salary_format_deductionDAL.searchSalary_Format_Deductions();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            salary_format_deductions = findObjectsBy(entry.getKey(), entry.getValue(), salary_format_deductions);
        return salary_format_deductions;
    }

    @Override
    public Object getValueByKey(Salary_Format_Deduction salary_format_deduction, String key) {
        return switch (key) {
            case "salary_format_id" -> salary_format_deduction.getSalary_format_id();
            case "deduction_id" -> salary_format_deduction.getDeduction_id();
            default -> null;
        };
    }

    public static void main(String[] args) {
//        Salary_Format_Deduction_DeductionBLL salary_format_deductionBLL = new Salary_Format_Deduction_DeductionBLL();
//        Salary_Format_Deduction salary_format_deduction = new Salary_Format_Deduction(15, "abc");
//        salary_format_deductionBLL.addSalary_Format_Deduction(salary_format_deduction);
//        salary_format_deduction.setName("xyz");
//        salary_format_deductionBLL.updateSalary_Format_Deduction(salary_format_deduction);
//        salary_format_deductionBLL.deleteSalary_Format_Deduction(salary_format_deduction);

//        System.out.println(salary_format_deductionBLL.searchSalary_Format_Deductions());
    }
}
