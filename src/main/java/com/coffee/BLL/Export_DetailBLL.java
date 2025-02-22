package com.coffee.BLL;

import com.coffee.DAL.Export_DetailDAL;
import com.coffee.DTO.Discount_Detail;
import com.coffee.DTO.Export_Detail;
import com.coffee.DTO.Material;
import com.coffee.DTO.Shipment;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Export_DetailBLL extends Manager<Export_Detail> {
    private Export_DetailDAL exportDetailDAL;

    public Export_DetailBLL() {
        exportDetailDAL = new Export_DetailDAL();
    }

    public Export_DetailDAL getExportDetailDAL() {
        return exportDetailDAL;
    }

    public void setExportDetailDAL(Export_DetailDAL exportDetailDAL) {
        this.exportDetailDAL = exportDetailDAL;
    }

    public Object[][] getData() {
        return getData(exportDetailDAL.searchExport_Detail());
    }

    public Pair<Boolean, String> addExport_Detail(Export_Detail exportDetail) {
//        Pair<Boolean, String> result;

//        result = exists(exportDetail);
//        if (result.getKey()) {
//            return new Pair<>(false, result.getValue());
//        }

        if (exportDetailDAL.addExport_Detail(exportDetail) == 0)
            return new Pair<>(false, "Xuất nguyên liệu không thành công.");

        Shipment shipment = new ShipmentBLL().findShipmentsBy(Map.of("id", exportDetail.getShipment_id())).get(0);
        shipment.setRemain(shipment.getRemain() - exportDetail.getQuantity());
        new ShipmentBLL().updateShipment(shipment);

        Material oldMaterial = new MaterialBLL().findMaterialsBy(Map.of("id", shipment.getMaterial_id())).get(0);
        Material newMaterial = new MaterialBLL().findMaterialsBy(Map.of("id", shipment.getMaterial_id())).get(0);
        newMaterial.setRemain_wearhouse(newMaterial.getRemain_wearhouse() - exportDetail.getQuantity());

        if (exportDetail.getReason().equals("Bán")) {
            newMaterial.setRemain(newMaterial.getRemain() + exportDetail.getQuantity());
        }
        new MaterialBLL().updateMaterial(newMaterial, oldMaterial);
        return new Pair<>(true, "Xuất nguyên liệu thành công.");
    }

    public List<Export_Detail> searchExport(String... conditions) {
        return exportDetailDAL.searchExport_Detail(conditions);
    }

    public List<Export_Detail> findExport(String key, String value) {
        List<Export_Detail> list = new ArrayList<>();
        List<Export_Detail> exportList = exportDetailDAL.searchExport_Detail();
        for (Export_Detail exportDetail : exportList) {
            if (getValueByKey(exportDetail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(exportDetail);
            }
        }
        return list;
    }

    public List<Export_Detail> findExportBy(Map<String, Object> conditions) {
        List<Export_Detail> exports = exportDetailDAL.searchExport_Detail();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            exports = findObjectsBy(entry.getKey(), entry.getValue(), exports);
        return exports;
    }

    public Pair<Boolean, String> exists(Export_Detail export) {
        List<Export_Detail> exports = findExportBy(Map.of(
                "export_id", export.getExport_id(),
                "shipment_id", export.getShipment_id()
        ));

        if (!exports.isEmpty()) {
            return new Pair<>(true, "Nguyên liệu đã được xuất.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Export_Detail exports, String key) {
        return switch (key) {
            case "export_id" -> exports.getExport_id();
            case "shipment_id" -> exports.getShipment_id();
            case "quantity" -> exports.getQuantity();
            case "reason" -> exports.getReason();
            default -> null;
        };
    }

    public static void main(String[] args) {
        Export_DetailBLL exportDetailBLL = new Export_DetailBLL();
        Export_Detail export_detail = new Export_Detail(1, 1, 20, "Ban");
        exportDetailBLL.addExport_Detail(export_detail);
    }
}
