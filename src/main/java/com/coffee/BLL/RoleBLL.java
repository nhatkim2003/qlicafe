package com.coffee.BLL;

import com.coffee.DAL.RoleDAL;
import com.coffee.DTO.Account;
import com.coffee.DTO.Decentralization;
import com.coffee.DTO.Module;
import com.coffee.DTO.Role;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleBLL extends Manager<Role> {
    private RoleDAL roleDAL;

    public RoleBLL() {
        roleDAL = new RoleDAL();
    }

    public RoleDAL getRoleDAL() {
        return roleDAL;
    }

    public void setRoleDAL(RoleDAL roleDAL) {
        this.roleDAL = roleDAL;
    }

    public Object[][] getData() {
        return getData(roleDAL.searchRoles());
    }

    public Pair<Boolean, String> addRole(Role role) {
        Pair<Boolean, String> result;

        result = validateName(role.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(role);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (roleDAL.addRole(role) == 0)
            return new Pair<>(false, "Thêm chức vụ không thành công.");

        return new Pair<>(true, "Thêm chức vụ thành công.");
    }

    public Pair<Boolean, String> updateRole(Role role) {
        if (roleDAL.updateRole(role) == 0)
            return new Pair<>(false, "Cập nhật chức vụ không thành công.");

        return new Pair<>(true, "Cập nhật chức vụ thành công.");
    }

    public Pair<Boolean, String> deleteRole(Role role) {
        Pair<Boolean, String> result;

        result = checkDecentralization(role);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (roleDAL.deleteRole("id = " + role.getId()) == 0)
            return new Pair<>(false, "Xoá chức vụ không thành công.");

        return new Pair<>(true, "Xoá chức vụ thành công.");
    }

    public List<Role> searchRoles(String... conditions) {
        return roleDAL.searchRoles(conditions);
    }

    public List<Role> findRoles(String key, String value) {
        List<Role> list = new ArrayList<>();
        List<Role> roleList = roleDAL.searchRoles("id != 0");
        for (Role role : roleList) {
            if (getValueByKey(role, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(role);
            }
        }
        return list;
    }

    public List<Role> findRolesBy(Map<String, Object> conditions) {
        List<Role> roles = roleDAL.searchRoles("id != 0");
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            roles = findObjectsBy(entry.getKey(), entry.getValue(), roles);
        return roles;
    }

    public Pair<Boolean, String> exists(Role role) {
        List<Role> modules = findRolesBy(Map.of("name", role.getName()));

        if (!modules.isEmpty()) {
            return new Pair<>(true, "Chức vụ đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    private Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên chức vụ không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên chức vụ không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên chức vụ không được chứa số.");
        return new Pair<>(true, name);
    }

    public Pair<Boolean, String> checkDecentralization(Role role) {
        DecentralizationBLL decentralizationBLL = new DecentralizationBLL();
        List<Decentralization> decentralizationss = decentralizationBLL.findDecentralizationsBy(Map.of(
                "role_id", role.getId()
        ));

        if (!decentralizationss.isEmpty()) {
            return new Pair<>(true, "Chức vụ đã tồn tại trong phân quyền.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Role role, String key) {
        return switch (key) {
            case "id" -> role.getId();
            case "name" -> role.getName();
            default -> null;
        };
    }

    public static void main(String[] args) {
        RoleBLL roleBLL = new RoleBLL();
//        Role role = new Role(15, "abc");
//        roleBLL.addRole(role);
//        role.setName("xyz");
//        roleBLL.updateRole(role);
//        roleBLL.deleteRole(role);

        System.out.println(roleBLL.searchRoles());
    }
}
