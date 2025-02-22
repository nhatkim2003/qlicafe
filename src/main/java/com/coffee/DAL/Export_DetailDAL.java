package com.coffee.DAL;

import com.coffee.DTO.Export_Detail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Export_DetailDAL extends Manager{
    public Export_DetailDAL() {
        super("export_detail",
                List.of("export_id",
                        "shipment_id",
                        "quantity",
                        "reason"));
    }

    public List<Export_Detail> convertToExport_Details(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Export_Detail(
                        Integer.parseInt(row.get(0)), // export_id
                        Integer.parseInt(row.get(1)), // shipment_id
                        Double.parseDouble(row.get(2)), // quantity
                        row.get(3) // reason
                );
            } catch (Exception e) {
                System.out.println("Error occurred in StaffDAL.convertToStaffs(): " + e.getMessage());
            }
            return new Export_Detail();
        });
    }

    public int addExport_Detail(Export_Detail exportDetail) {
        try {
            return create(exportDetail.getExport_id(),
                    exportDetail.getShipment_id(),
                    exportDetail.getQuantity(),
                    exportDetail.getReason()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_detailDAL.addExport_detail(): " + e.getMessage());
        }
        return 0;
    }

    public List<Export_Detail> searchExport_Detail(String... conditions) {
        try {
            return convertToExport_Details(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_DetailDAL.searchExport_Detail(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
