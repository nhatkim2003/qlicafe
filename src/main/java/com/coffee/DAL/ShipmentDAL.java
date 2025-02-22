package com.coffee.DAL;

import com.coffee.DTO.Shipment;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAL extends Manager {
    public ShipmentDAL() {
        super("shipment",
                List.of("id",
                        "material_id",
                        "supplier_id",
                        "import_id",
                        "quantity",
                        "remain",
                        "mfg",
                        "exp"));
    }

    public List<Shipment> convertToShipments(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Shipment(
                        Integer.parseInt(row.get(0)),
                        Integer.parseInt(row.get(1)),
                        Integer.parseInt(row.get(2)),
                        Integer.parseInt(row.get(3)),
                        Double.parseDouble(row.get(4)),
                        Double.parseDouble(row.get(5)),
                        Date.valueOf(row.get(6)),
                        Date.valueOf(row.get(7))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ShipmentDAL.convertToShipment(): " + e.getMessage());
            }
            return new Shipment();
        });
    }

    public int addShipment(Shipment shipment) {
        try {
            return create(shipment.getId(),
                    shipment.getMaterial_id(),
                    shipment.getSupplier_id(),
                    shipment.getImport_id(),
                    shipment.getQuantity(),
                    shipment.getRemain(),
                    shipment.getMfg(),
                    shipment.getExp()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.addShipment(): " + e.getMessage());
        }
        return 0;
    }

    public int updateShipment(Shipment shipment) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(shipment.getId());
            updateValues.add(shipment.getMaterial_id());
            updateValues.add(shipment.getSupplier_id());
            updateValues.add(shipment.getImport_id());
            updateValues.add(shipment.getQuantity());
            updateValues.add(shipment.getRemain());
            updateValues.add(shipment.getMfg());
            updateValues.add(shipment.getExp());
            return update(updateValues, "id = " + shipment.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.updateShipment(): " + e.getMessage());
        }
        return 0;
    }

    public List<Shipment> searchShipments(String... conditions) {
        try {
            return convertToShipments(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.searchShipments(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
