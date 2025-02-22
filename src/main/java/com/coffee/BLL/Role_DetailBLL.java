package com.coffee.BLL;

import com.coffee.DAL.Role_DetailDAL;
import com.coffee.DTO.Role_Detail;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Role_DetailBLL extends Manager<Role_Detail> {
    private Role_DetailDAL role_detailDAL;

    public Role_DetailBLL() {
        role_detailDAL = new Role_DetailDAL();
    }

    public Role_DetailDAL getRole_detailDAL() {
        return role_detailDAL;
    }

    public void setRole_detailDAL(Role_DetailDAL role_detailDAL) {
        this.role_detailDAL = role_detailDAL;
    }

    public Object[][] getData() {
        return getData(role_detailDAL.searchRole_details());
    }

    public Pair<Boolean, String> addRole_detail(Role_Detail role_detail) {
        Pair<Boolean, String> result;

        result = exists(role_detail);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (role_detailDAL.addRole_detail(role_detail) == 0)
            return new Pair<>(false, "Cập nhật chức vụ nhân viên không thành công.");

        return new Pair<>(true, "Cập nhật chức vụ nhân viên thành công.");
    }

    public Pair<Boolean, String> updateRole_detail(Role_Detail role_detail) {
        if (role_detailDAL.updateRole_detail(role_detail) == 0)
            return new Pair<>(false, "Thiết lập chức vụ nhân viên không thành công.");

        return new Pair<>(true, "Thiết lập chức vụ nhân viên thành công.");
    }

    public List<Role_Detail> searchRole_details(String... conditions) {
        return role_detailDAL.searchRole_details(conditions);
    }

    public List<Role_Detail> searchRole_detailsByRole(int role_id, String end) {
        return role_detailDAL.searchRole_detailsByRole(role_id, end);
    }

    public List<Role_Detail> searchRole_detailsByStaff(int staff_id) {
        return role_detailDAL.searchRole_detailsByStaff(staff_id);
    }

    public List<Role_Detail> findRole_details(String key, String value) {
        List<Role_Detail> list = new ArrayList<>();
        List<Role_Detail> role_detailList = role_detailDAL.searchRole_details();
        for (Role_Detail role_detail : role_detailList) {
            if (getValueByKey(role_detail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(role_detail);
            }
        }
        return list;
    }

    public List<Role_Detail> findRole_detailsBy(Map<String, Object> conditions) {
        List<Role_Detail> role_details = role_detailDAL.searchRole_details();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            role_details = findObjectsBy(entry.getKey(), entry.getValue(), role_details);
        return role_details;
    }

    public Pair<Boolean, String> exists(Role_Detail role_detail) {
        List<Role_Detail> role_details = findRole_detailsBy(Map.of(
                "role_id", role_detail.getRole_id(),
                "staff_id", role_detail.getStaff_id(),
                "entry_date", role_detail.getEntry_date()
        ));

        if (!role_details.isEmpty()) {
            return new Pair<>(true, "Chi tiết chức vụ nhân viên nhân viên đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Role_Detail role_detail, String key) {
        return switch (key) {
            case "role_id" -> role_detail.getRole_id();
            case "staff_id" -> role_detail.getStaff_id();
            case "entry_date" -> role_detail.getEntry_date();
            case "salary" -> role_detail.getSalary();
            case "type_salary" -> role_detail.getType_salary();
            default -> null;
        };
    }
}
