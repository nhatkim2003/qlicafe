package com.coffee.DAL;

import com.coffee.DTO.Module;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleDAL extends Manager {
    public ModuleDAL() {
        super("module", List.of("id", "name"));
    }

    public List<Module> convertToModules(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Module(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1) // name
                );
            } catch (Exception e) {
                System.out.println("Error occurred in RoleDAL.convertToRoles(): " + e.getMessage());
            }
            return new Module();
        });
    }

    public int addModule(Module module) {
        try {
            return create(module.getId(),
                    module.getName()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ModuleDAL.addRole(): " + e.getMessage());
        }
        return 0;
    }

    public int updateModule(Module module) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(module.getId());
            updateValues.add(module.getName());
            return update(updateValues, "id = " + module.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ModuleDAL.updateModule(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteModule(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ModuleDAL.deleteDecentralization() " + e.getMessage());
        }
        return 0;
    }

    public List<Module> searchModules(String... conditions) {
        try {
            return convertToModules(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ModuleDAL.searchModules(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
