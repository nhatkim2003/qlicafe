package com.coffee.DAL;

import com.coffee.DTO.Role;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAL extends Manager {
    public RoleDAL() {
        super("role",
                List.of("id",
                        "name"));
    }

    public List<Role> convertToRoles(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Role(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1) // name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in RoleDAL.convertToRoles(): " + e.getMessage());
            }
            return new Role();
        });
    }

    public int addRole(Role role) {
        try {
            return create(role.getId(),
                    role.getName()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in RoleDAL.addRole(): " + e.getMessage());
        }
        return 0;
    }

    public int updateRole(Role role) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(role.getId());
            updateValues.add(role.getName());
            return update(updateValues, "id = " + role.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in RoleDAL.updateRole(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteRole(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in RoleDAL.deleteRole(): " + e.getMessage());
        }
        return 0;
    }

    public List<Role> searchRoles(String... conditions) {
        try {
            return convertToRoles(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in RoleDAL.searchRoles(): " + e.getMessage());
        }
        return new ArrayList<>();
    }


}



