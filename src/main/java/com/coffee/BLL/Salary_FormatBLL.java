package com.coffee.BLL;

import com.coffee.DAL.Salary_FormatDAL;
import com.coffee.DTO.Salary_Format;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Salary_FormatBLL extends Manager<Salary_Format> {
    private Salary_FormatDAL salary_formatDAL;

    public Salary_FormatBLL() {
        salary_formatDAL = new Salary_FormatDAL();
    }

    public Salary_FormatDAL getSalary_FormatDAL() {
        return salary_formatDAL;
    }

    public void setSalary_FormatDAL(Salary_FormatDAL salary_formartDAL) {
        this.salary_formatDAL = salary_formartDAL;
    }

    public Object[][] getData() {
        return getData(salary_formatDAL.searchSalary_Formats());
    }

    public Pair<Boolean, String> addSalary_Format(Salary_Format salary_formart) {
        Pair<Boolean, String> result;

        result = validateName(salary_formart.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(salary_formart);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (salary_formatDAL.addSalary_Format(salary_formart) == 0)
            return new Pair<>(false, "Thêm mẫu lương không thành công.");

        return new Pair<>(true, "Thêm mẫu lương thành công.");
    }

    public Pair<Boolean, String> updateSalary_Format(Salary_Format salary_formart) {
        if (salary_formatDAL.updateSalary_Format(salary_formart) == 0)
            return new Pair<>(false, "Cập nhật mẫu lương không thành công.");

        return new Pair<>(true, "Cập nhật mẫu lương thành công.");
    }

    public Pair<Boolean, String> deleteSalary_Format(Salary_Format salary_formart) {
        Pair<Boolean, String> result;

        if (salary_formatDAL.deleteSalary_Format("id = " + salary_formart.getId()) == 0)
            return new Pair<>(false, "Xoá mẫu lương không thành công.");

        return new Pair<>(true, "Xoá mẫu lương thành công.");
    }

    public List<Salary_Format> searchSalary_Formats(String... conditions) {
        return salary_formatDAL.searchSalary_Formats(conditions);
    }

    public List<Salary_Format> findSalary_Formats(String key, String value) {
        List<Salary_Format> list = new ArrayList<>();
        List<Salary_Format> salary_formartList = salary_formatDAL.searchSalary_Formats();
        for (Salary_Format salary_formart : salary_formartList) {
            if (getValueByKey(salary_formart, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(salary_formart);
            }
        }
        return list;
    }

    public List<Salary_Format> findSalary_FormatsBy(Map<String, Object> conditions) {
        List<Salary_Format> salary_formarts = salary_formatDAL.searchSalary_Formats();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            salary_formarts = findObjectsBy(entry.getKey(), entry.getValue(), salary_formarts);
        return salary_formarts;
    }

    public Pair<Boolean, String> exists(Salary_Format salary_format) {
        List<Salary_Format> modules = findSalary_FormatsBy(Map.of("name", salary_format.getName()));

        if (!modules.isEmpty()) {
            return new Pair<>(true, "Mẫu lương đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    private Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên mẫu lương không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên mẫu lương không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên mẫu lương không được chứa số.");
        return new Pair<>(true, name);
    }

    @Override
    public Object getValueByKey(Salary_Format salary_formart, String key) {
        return switch (key) {
            case "id" -> salary_formart.getId();
            case "name" -> salary_formart.getName();
            default -> null;
        };
    }

    public static void main(String[] args) {
        Salary_FormatBLL salary_formartBLL = new Salary_FormatBLL();
//        Salary_Format salary_formart = new Salary_Format(15, "abc");
//        salary_formartBLL.addSalary_Format(salary_formart);
//        salary_formart.setName("xyz");
//        salary_formartBLL.updateSalary_Format(salary_formart);
//        salary_formartBLL.deleteSalary_Format(salary_formart);

        System.out.println(salary_formartBLL.searchSalary_Formats());
    }
}
