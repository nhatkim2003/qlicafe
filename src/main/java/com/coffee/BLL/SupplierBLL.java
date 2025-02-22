package com.coffee.BLL;

import com.coffee.DAL.SupplierDAL;
import com.coffee.DTO.Supplier;
import com.coffee.utils.VNString;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SupplierBLL extends Manager<Supplier> {
    private SupplierDAL supplierDAL;

    public SupplierBLL() {
        supplierDAL = new SupplierDAL();
    }

    public SupplierDAL getSupplierDAL() {
        return supplierDAL;
    }

    public void setSupplierDAL(SupplierDAL supplierDAL) {
        this.supplierDAL = supplierDAL;
    }

    public Object[][] getData() {
        return getData(supplierDAL.searchSuppliers("deleted = 0"));
    }

    public Pair<Boolean, String> addSupplier(Supplier supplier) {
        Pair<Boolean, String> result = validateSupplierAll(supplier);
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (supplierDAL.addSupplier(supplier) == 0)
            return new Pair<>(false, "Thêm nhà cung cấp không thành công.");

        return new Pair<>(true, "Thêm nhà cung cấp thành công.");

    }

    //    public Pair<Boolean, String> updateSupplier(Supplier supplier) {
//        Pair<Boolean, String> result = validateSupplierAll(supplier);
//
//        if (!result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }
//
//        if (supplierDAL.updateSupplier(supplier) == 0)
//            return new Pair<>(false, "Cập nhật nhà cung cấp không thành công.");
//
//        return new Pair<>(true, "Cập nhật nhà cung cấp thành công.");
//    }
    public Pair<Boolean, String> updateSupplier(Supplier oldSupplier, Supplier newSupplier) {

        List<String> errorMessages = new ArrayList<>();

        if (!Objects.equals(oldSupplier.getName(), newSupplier.getName())) {
            Pair<Boolean, String> nameResult = validateName(newSupplier.getName());
            if (!nameResult.getKey()) {
                errorMessages.add(nameResult.getValue());
            }
        }

        if (!Objects.equals(oldSupplier.getPhone(), newSupplier.getPhone())) {
            Pair<Boolean, String> phoneResult = validatePhone(newSupplier.getPhone());
            if (!phoneResult.getKey()) {
                errorMessages.add(phoneResult.getValue());
            }
        }

        if (!Objects.equals(oldSupplier.getEmail(), newSupplier.getEmail())) {
            Pair<Boolean, String> emailResult = validateEmail(newSupplier.getEmail());
            if (!emailResult.getKey()) {
                errorMessages.add(emailResult.getValue());
            }
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }

        if (supplierDAL.updateSupplier(newSupplier) == 0) {
            return new Pair<>(false, "Cập nhật nhà cung cấp không thành công.");
        }

        return new Pair<>(true, "Cập nhật nhà cung cấp thành công.");
    }

    public Pair<Boolean, String> deleteSupplier(Supplier supplier) {

        if (supplierDAL.deleteSupplier("id = " + supplier.getId()) == 0)
            return new Pair<>(false, "Xoá nhà cung cấp không thành công.");

        return new Pair<>(true, "Xoá nhà cung cấp thành công.");
    }

    public List<Supplier> searchSuppliers(String... conditions) {
        return supplierDAL.searchSuppliers(conditions);
    }

    public List<Supplier> findSuppliers(String key, String value) {
        List<Supplier> list = new ArrayList<>();
        List<Supplier> supplierList = searchSuppliers("deleted = 0");
        for (Supplier supplier : supplierList) {
            if (getValueByKey(supplier, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(supplier);
            }
        }
        return list;
    }

    public List<Supplier> findSuppliersBy(Map<String, Object> conditions) {
        List<Supplier> suppliers = searchSuppliers("deleted = 0");
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            suppliers = findObjectsBy(entry.getKey(), entry.getValue(), suppliers);
        return suppliers;
    }

    public Pair<Boolean, String> validateSupplierAll(Supplier supplier) {
        Pair<Boolean, String> result;
        List<String> errorMessages = new ArrayList<>();

        result = validateName(supplier.getName());
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }

        result = validatePhone(supplier.getPhone());
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }

        result = validateEmail(supplier.getEmail());
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        result = validateAddress(supplier.getAddress());
        if (!result.getKey()) {
            errorMessages.add(result.getValue());
        }
        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            return new Pair<>(false, errorMessage);
        }
        return new Pair<>(true, "");
    }


    public Pair<Boolean, String> exists(Supplier newSupplier) {
        List<Supplier> suppliers = supplierDAL.searchSuppliers("phone = '" + newSupplier.getPhone() + "'", "deleted = 0");
        if (!suppliers.isEmpty()) {
            return new Pair<>(true, "Số điện thoại nhà cung cấp đã tồn tại.");
        }
        suppliers = supplierDAL.searchSuppliers("email = '" + newSupplier.getEmail() + "'", "deleted = 0");
        if (!suppliers.isEmpty()) {
            return new Pair<>(true, "Email nhà cung cấp đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    private Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên nhà cung cấp không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên nhà cung cấp không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên nhà cung cấp không được chứa số.");
        List<Supplier> suppliers = supplierDAL.searchSuppliers("name = '" + name + "'", "deleted = 0");
        if (!suppliers.isEmpty()) {
            return new Pair<>(false, "Tên  nhà cung cấp đã tồn tại.");
        }
        return new Pair<>(true, name);
    }

    private  Pair<Boolean, String> validateAddress(String address) {
        if ( address.isBlank())
            return new Pair<>(false, "Địa chỉ  không được để trống.");
        return new Pair<>(true, address);
    }

    public Pair<Boolean, String> validatePhone(String phone) {
        if ( phone.isBlank())
            return new Pair<>(false, "Số điện thoại nhà cung cấp không được bỏ trống.");
        if (!VNString.checkFormatPhone(phone))
            return new Pair<>(false, "Số điện thoại nhà cung cấp phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc \\{\\\\3, 5, 7, 8, 9\\}\\\\.");

        List<Supplier> suppliers = supplierDAL.searchSuppliers("phone = '" + phone + "'", "deleted = 0");
        if (!suppliers.isEmpty()) {
            return new Pair<>(false, "Số điện thoại nhà cung cấp đã tồn tại.");
        }
        return new Pair<>(true, phone);
    }


    public Pair<Boolean, String> validateEmail(String email) {
        if ( email.isBlank())
            return new Pair<>(false, "Email nhà cung cấp không được để trống.");
        if (VNString.containsUnicode(email))
            return new Pair<>(false, "Email nhà cung cấp không được chứa unicode.");
        if (!VNString.checkFormatOfEmail(email))
            return new Pair<>(false, "Email nhà cung cấp phải theo định dạng (username@domain.name).");
        List<Supplier> suppliers = supplierDAL.searchSuppliers("email = '" + email + "'", "deleted = 0");
        if (!suppliers.isEmpty()) {
            return new Pair<>(false, "Email nhà cung cấp đã tồn tại.");
        }
        return new Pair<>(true, email);
    }
    @Override
    public Object getValueByKey(Supplier supplier, String key) {
        return switch (key) {
            case "id" -> supplier.getId();
            case "name" -> supplier.getName();
            case "phone" -> supplier.getPhone();
            case "address" -> supplier.getAddress();
            case "email" -> supplier.getEmail();
            default -> null;
        };
    }

    public static void main(String[] args) {
        SupplierBLL supplierBLL = new SupplierBLL();
        Supplier supplier = new Supplier(15, "abc", "0963333946", "514", "a@gmail.com", false);
        supplierBLL.addSupplier(supplier);
//        supplier.setName("xyz");
//        supplierBLL.updateSupplier(supplier);
//        supplierBLL.deleteSupplier(supplier);
    }
}
