package com.coffee.BLL;

import com.coffee.DAL.MaterialDAL;
import com.coffee.DTO.*;
import com.coffee.DTO.Material;
import com.coffee.DTO.Module;
import com.coffee.utils.VNString;
import javafx.util.Pair;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MaterialBLL extends Manager<Material> {
    private MaterialDAL materialDAL;
    private SupplierBLL supplierBLL = new SupplierBLL();

    public MaterialBLL() {
        materialDAL = new MaterialDAL();
    }

    public MaterialDAL getMaterialDAL() {
        return materialDAL;
    }

    public void setMaterialDAL(MaterialDAL materialDAL) {
        this.materialDAL = materialDAL;
    }

    public Object[][] getData() {
        return getData(materialDAL.searchMaterials("deleted = 0"));
    }

    public Pair<Boolean, String> addMaterial(Material material) {
        Pair<Boolean, String> result;

        result = validateName(material.getName());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = validateUnit(material.getUnit());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }


        if (materialDAL.addMaterial(material) == 0)
            return new Pair<>(false, "Thêm nguyên liệu không thành công.");

        return new Pair<>(true, "Thêm nguyên liệu thành công.");
    }


    public Pair<Boolean, String> updateMaterial(Material newMaterial, Material oldMaterial) {
        List<String> errorMessages = new ArrayList<>();

        if (!Objects.equals(oldMaterial.getName(), newMaterial.getName())) {
            Pair<Boolean, String> nameResult = validateName(newMaterial.getName());
            if (!nameResult.getKey()) {
                errorMessages.add(nameResult.getValue());
            }
        }

        if (!Objects.equals(oldMaterial.getUnit(), newMaterial.getUnit())) {
            Pair<Boolean, String> phoneResult = validateUnit(newMaterial.getUnit());
            if (!phoneResult.getKey()) {
                errorMessages.add(phoneResult.getValue());
            }
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }

        if (materialDAL.updateMaterial(newMaterial) == 0)
            return new Pair<>(false, "Cập nhật nguyên liệu không thành công.");

        return new Pair<>(true, "Cập nhật nguyên liệu thành công.");
    }

    public Pair<Boolean, String> updateMaterial(Material material) {
        if (materialDAL.updateMaterial(material) == 0)
            return new Pair<>(false, "Cập nhật nguyên liệu không thành công.");

        return new Pair<>(true, "Cập nhật nguyên liệu thành công.");
    }

    public Pair<Boolean, String> deleteMaterial(Material material) {
        Pair<Boolean, String> result;

        result = checkMaterial(material);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (materialDAL.deleteMaterial("id = " + material.getId()) == 0)
            return new Pair<>(false, "Xoá nguyên liệu không thành công.");

        return new Pair<>(true, "Xoá nguyên liệu thành công.");
    }

    public List<Material> searchMaterials(String... conditions) {
        return materialDAL.searchMaterials(conditions);
    }

    public List<Material> findMaterials(String key, String value) {
        List<Material> list = new ArrayList<>();
        List<Material> materialList = materialDAL.searchMaterials("deleted = 0");
        for (Material material : materialList) {
            if (getValueByKey(material, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(material);
            }
        }
        return list;
    }


    public List<Material> findMaterialsBySell(String key, String value, boolean sell) {
        List<Material> list = new ArrayList<>();
        List<Material> materialList = materialDAL.searchMaterials("deleted = 0");
        for (Material material : materialList) {
            if (getValueByKey(material, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(material);
            }
        }
        list.removeIf(material -> material.isSell() != sell);
        return list;
    }

    public List<Material> findMaterialsBy(Map<String, Object> conditions) {
        List<Material> materials = materialDAL.searchMaterials("deleted = 0");
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            materials = findObjectsBy(entry.getKey(), entry.getValue(), materials);
        return materials;
    }


    public Pair<Boolean, String> validateName(String name) {
        if (name == null || name.isBlank())
            return new Pair<>(false, "Tên nguyên liệu không  được để trống.");
        List<Material> materials = materialDAL.searchMaterials("name = '" + name + "'", "deleted = 0");
        if (!materials.isEmpty()) {
            return new Pair<>(false, "Nguyên liệu đã tồn tại.");
        }
        return new Pair<>(true, name);
    }

    public Pair<Boolean, String> exists(Material newMaterial) {
        List<Material> materials = materialDAL.searchMaterials("name = '" + newMaterial.getName() + "'");
        if (!materials.isEmpty()) {
            return new Pair<>(true, "Nguyên liệu đã tồn tại.");
        }

        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> checkMaterial(Material material) {
        RecipeBLL recipeBLL = new RecipeBLL();
        List<Recipe> recipes = recipeBLL.findRecipesBy(Map.of(
                "material_id", material.getId()
        ));

        if (!recipes.isEmpty()) {
            return new Pair<>(true, "Nguyên liệu đã tồn tại trong công thức.");
        }
        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> validateQuantity(String quantity, String title) {
        if (quantity == null || quantity.isBlank())
            return new Pair<>(false, title + " không được để trống");
        if (!VNString.checkUnsignedNumber(quantity))
            return new Pair<>(false, title + " phải là số lớn hơn không");
        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> validateUnit(String unit) {
        if (unit == null || unit.isBlank())
            return new Pair<>(false, "Đơn vị không được để trống.");
        if (VNString.containsSpecial(unit))
            return new Pair<>(false, "Đơn vị không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(unit))
            return new Pair<>(false, "Đơn không được chứa số.");
        return new Pair<>(true, unit);
    }

    public Pair<Boolean, String> validateAll(Material material) {
        Pair<Boolean, String> result;
        List<String> errorMessages = new ArrayList<>();
        result = validateName(material.getName());
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        result = validateQuantity(material.getMinRemain() + "", "Tồn tối thiểu");
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        result = validateQuantity(material.getMaxRemain() + "", "Tồn tối đa");
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        result = validateUnit(material.getUnit());
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        result = validateQuantity(material.getUnit_price() + "", "Giá vốn");
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }
        return new Pair<>(true, "");

    }

    public Pair<Boolean, String> validateSupplier(String id) {
        List<Supplier> suppliers = supplierBLL.findSuppliers("id", id);
        if (suppliers.isEmpty())
            return new Pair<>(false, "Nhà cung cấp không tồn tại!");
        return new Pair<>(true, "");
    }

    @Override
    public Object getValueByKey(Material material, String key) {
        return switch (key) {
            case "id" -> material.getId();
            case "name" -> material.getName();
            case "remain" -> material.getRemain();
            case "remain_wearhouse" -> material.getRemain_wearhouse();
            case "unit" -> material.getUnit();
            default -> null;
        };
    }

    public void sub(int id, String size, int quantity) {
        materialDAL.sub(id, size, quantity);
    }

    public void plus(int id, String size, int quantity) {
        materialDAL.plus(id, size, quantity);
    }

    public static void main(String[] args) {
//        MaterialBLL materialBLL = new MaterialBLL();
//        Material material = new Material(materialBLL.getAutoID(materialBLL.searchMaterials()), "xzy", 1, 1, "d", false);

//        System.out.println(materialBLL.addMaterial(material).getValue());

//        material.setId(2);

//        materialBLL.updateMaterial(material);
//        materialBLL.deleteMaterial(material);
    }
}
