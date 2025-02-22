package com.coffee.BLL;

import com.coffee.DAL.ShipmentDAL;
import com.coffee.DTO.*;
import com.coffee.DTO.Module;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShipmentBLL extends Manager<Shipment> {
    private ShipmentDAL shipmentDAL;

    public ShipmentBLL() {
        shipmentDAL = new ShipmentDAL();
    }

    public ShipmentDAL getShipmentDAL() {
        return shipmentDAL;
    }

    public void setShipmentDAL(ShipmentDAL shipmentDAL) {
        this.shipmentDAL = shipmentDAL;
    }

    public Object[][] getData() {
        return getData(shipmentDAL.searchShipments());
    }

    public Pair<Boolean, String> addShipment(Shipment shipment) {
//        Pair<Boolean, String> result;
//        result = validateQuantity(String.valueOf(shipment.getQuantity()));
//        if (!result.getKey())
//            return new Pair<>(false, result.getValue());
//
//        result = exists(shipment);
//        if (result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }

//        result = validateDate(shipment.getMfg(), shipment.getExp());
//        if (!result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }
        if (shipmentDAL.addShipment(shipment) == 0)
            return new Pair<>(false, "Thêm lô hàng không thành công.");

        MaterialBLL materialBLL = new MaterialBLL();
        Material newMaterial = materialBLL.findMaterialsBy(Map.of("id", shipment.getMaterial_id())).get(0);
        newMaterial.setRemain_wearhouse(newMaterial.getRemain_wearhouse() + shipment.getQuantity());
        Material oldMaterial = materialBLL.findMaterialsBy(Map.of("id", shipment.getMaterial_id())).get(0);
        materialBLL.updateMaterial(newMaterial, oldMaterial);
        return new Pair<>(true, "Thêm lô hàng thành công.");
    }

    public Pair<Boolean, String> updateShipment(Shipment shipment) {
        Pair<Boolean, String> result;
        result = validateQuantity(String.valueOf(shipment.getQuantity()));
        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        if (!result.getKey())
            return new Pair<>(false, result.getValue());

        result = validateDate(shipment.getMfg(), shipment.getExp());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (shipmentDAL.updateShipment(shipment) == 0)
            return new Pair<>(false, "Cập nhật lô hàng không thành công.");

        return new Pair<>(true, "Cập nhật lô hàng thành công.");
    }

    public List<Shipment> searchShipments(String... conditions) {
        return shipmentDAL.searchShipments(conditions);
    }

    public List<Shipment> findShipments(String key, String value) {
        List<Shipment> list = new ArrayList<>();
        List<Shipment> shipmentList = shipmentDAL.searchShipments();
        for (Shipment shipment : shipmentList) {
            if (getValueByKey(shipment, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(shipment);
            }
        }
        return list;
    }

    public List<Shipment> findShipmentsBy(Map<String, Object> conditions) {
        List<Shipment> shipments = shipmentDAL.searchShipments();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            shipments = findObjectsBy(entry.getKey(), entry.getValue(), shipments);
        return shipments;
    }

    public Pair<Boolean, String> exists(Shipment shipment) {
        List<Shipment> shipments = findShipmentsBy(Map.of(
                "material_id", shipment.getMaterial_id(),
                "import_id", shipment.getImport_id()
        ));

        if (!shipments.isEmpty()) {
            return new Pair<>(true, "Nguyên liệu đã được nhập.");
        }
        return new Pair<>(false, "");
    }

    private static Pair<Boolean, String> validateDate(Date mfg, Date exp) {
        if (mfg == null)
            return new Pair<>(false, "Ngày sản xuất không được để trống.");
        if (exp == null)
            return new Pair<>(false, "Ngày hết hạn không được để trống.");
//        if (mfg.before(java.sql.Date.valueOf(LocalDate.now())))
//            return new Pair<>(false, "Ngày sản xuất phải sau ngày hiện tại.");
        if (mfg.after(exp))
            return new Pair<>(false, "Ngày sản xuất phải trước ngày hết hạn.");
        return new Pair<>(true, "Hạn sử dụng hợp lệ.");
    }

    private Pair<Boolean, String> validatePrice(String price) {
        if (price.isBlank())
            return new Pair<>(false, "Giá nhập không được để trống");
        if (!VNString.checkUnsignedNumber(price))
            return new Pair<>(false, "Giá nhập phải là số lớn hơn không");
        return new Pair<>(true, "Giá nhập hợp lệ");
    }

    private Pair<Boolean, String> validateQuantity(String quantity) {
        if (quantity.isBlank())
            return new Pair<>(false, "Số lượng không được để trống");
        if (!VNString.checkUnsignedNumber(quantity))
            return new Pair<>(false, "Số lượng phải là số lớn hơn không");
        return new Pair<>(true, "Số lượng hợp lệ");
    }

    @Override
    public Object getValueByKey(Shipment shipment, String key) {
        return switch (key) {
            case "id" -> shipment.getId();
            case "material_id" -> shipment.getMaterial_id();
            case "supplier_id" -> shipment.getSupplier_id();
            case "import_id" -> shipment.getImport_id();
            case "quantity" -> shipment.getQuantity();
            case "remain" -> shipment.getRemain();
            case "mfg" -> shipment.getMfg();
            case "exp" -> shipment.getExp();
            default -> null;
        };
    }

    public static void main(String[] args) {
//        ShipmentBLL shipmentBLL = new ShipmentBLL();
//        Shipment shipment = new Shipment(shipmentBLL.getAutoID(shipmentBLL.searchShipments()), 2, 1, 0, 0, java.sql.Date.valueOf("2024-02-07"), java.sql.Date.valueOf("2024-02-10"));
//        shipmentBLL.addShipment(shipment);
//
//        shipment.setQuantity(50);
//        shipmentBLL.updateShipment(shipment);
    }


}
