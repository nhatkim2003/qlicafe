package com.coffee.BLL;

import com.coffee.DAL.ModuleDAL;
import com.coffee.DTO.Decentralization;
import com.coffee.DTO.Function;
import com.coffee.DTO.Module;
import com.coffee.DTO.Supplier;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleBLL extends Manager<Module>{
    private ModuleDAL moduleDAL;

    public ModuleBLL() {
        moduleDAL = new ModuleDAL();
    }

    public ModuleDAL getModuleDAL() {
        return moduleDAL;
    }

    public void setModuleDAL(ModuleDAL moduleDAL) {
        this.moduleDAL = moduleDAL;
    }

    public Object[][] getData() {
        return getData(moduleDAL.searchModules());
    }

    public Pair<Boolean, String> addModule(Module module) {
        Pair<Boolean, String> result = validateModuleAll(module);
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (moduleDAL.addModule(module) == 0)
            return new Pair<>(false, "Thêm module không thành công.");

        return new Pair<>(true, "Thêm module thành công.");
    }

    public Pair<Boolean, String> updateModule(Module module) {
        Pair<Boolean, String> result = validateModuleAll(module);
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        if (moduleDAL.updateModule(module) == 0)
            return new Pair<>(false, "Cập nhật module không thành công.");

        return new Pair<>(true, "Cập nhật module thành công.");
    }

    public Pair<Boolean, String> deleteModule(Module module) {
        Pair<Boolean, String> result;

        result = checkDecentralization(module);
        if(result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (moduleDAL.deleteModule("id = " + module.getId()) == 0)
            return new Pair<>(false, "Xoá module không thành công.");

        return new Pair<>(true, "Xoá module thành công.");
    }

    public List<Module> searchModules(String... conditions) {
        return moduleDAL.searchModules(conditions);
    }

    public List<Module> findModules(String key, String value) {
        List<Module> list = new ArrayList<>();
        List<Module> moduleList = moduleDAL.searchModules();
        for (Module module : moduleList) {
            if (getValueByKey(module, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(module);
            }
        }
        return list;
    }

    public List<Module> findModulesBy(Map<String, Object> conditions) {
        List<Module> modules =moduleDAL.searchModules();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            modules = findObjectsBy(entry.getKey(), entry.getValue(), modules);
        return modules;
    }
    public Pair<Boolean, String> validateModuleAll(Module module) {
        Pair<Boolean, String> result;

        result = validateName(module.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        result = exists(module);
        if(result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        return new Pair<>(true,"");
    }
    private static Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên module không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên nmodule không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên nmodule không được chứa số.");
        return new Pair<>(true, "");
    }
    public Pair<Boolean, String> exists(Module module) {
        List<Module> modules = findModulesBy(Map.of(
                "name", module.getName()
        ));

        if(!modules.isEmpty()){
            return new Pair<>(true, "Module đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> checkDecentralization(Module module) {
        DecentralizationBLL decentralizationBLL = new DecentralizationBLL();
        List<Decentralization> decentralizationss = decentralizationBLL.findDecentralizationsBy(Map.of(
                "module_id", module.getId()
        ));

        if(!decentralizationss.isEmpty()){
            return new Pair<>(true, "Module đã tồn tại trong phân quyền.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Module module, String key) {
        return switch (key) {
            case "id" -> module.getId();
            case "name" -> module.getName();
            default -> null;
        };
    }

    public static void main(String[] args) {
        ModuleBLL moduleBLL = new ModuleBLL();
        Module module = new Module(15, "abc");
//        moduleBLL.addModule(module);
        module.setName("xyz");
//        moduleBLL.updateModule(module);
        moduleBLL.deleteModule(module);
    }
}
