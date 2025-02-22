package com.coffee.main;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.CheckRemainWearHouse;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.LoginGUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Cafe_Application {
    public static LoginGUI loginGUI;
    public static HomeGUI homeGUI;

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatIntelliJLaf.setup();

        UIManager.put("ProgressBar.selectionForeground", Color.black);
        UIManager.put("ProgressBar.selectionBackground", Color.black);
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.track", new Color(220, 221, 225, 255));
        UIManager.put("PasswordField.showRevealButton", true);
        UIManager.put("PasswordField.capsLockIcon", new FlatSVGIcon("icon/capslock.svg"));
        UIManager.put("TitlePane.iconSize", new Dimension(25, 25));
        UIManager.put("TitlePane.iconMargins", new Insets(3, 5, 0, 20));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));

        Thread thread = new Thread(() -> homeGUI = new HomeGUI());
        thread.start();
        loginGUI = new LoginGUI();
        loginGUI.setVisible(true);


//        new CheckRemainWearHouse();

//        new EditStaffGUI(new StaffBLL().searchStaffs("id = 2").get(0));
//        new EditWorkScheduleGUI(new Work_ScheduleBLL().searchWork_schedules("id = 55").get(0));
//        new AddPayrollGUI();
//        new DetailPayroll_DetailGUI(new Payroll_DetailBLL().searchPayroll_Details().get(0), new PayrollBLL().searchPayrolls().get(0));

///       tao du lieu random cho hoa don

//
//        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime start = LocalDateTime.of(2024, 2, 6, 0, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 5, 6, 0, 0);
//
//        List<Product> productList = new ProductBLL().searchProducts("deleted = 0");
//        int id = 4003;
//        while (!start.equals(end)) {
//            for (int j = 0; j < 10; j++) {
//                Random random = new Random();
//
//                // Tạo thời điểm ngẫu nhiên trong ngày 1/1/2023
//                int gio = random.nextInt(24); // Giờ từ 0 đến 23
//                int phut = random.nextInt(60); // Phút từ 0 đến 59
//                int giay = random.nextInt(60); // Giây từ 0 đến 59
//
//                LocalDateTime invoice_date = start.withHour(gio).withMinute(phut).withSecond(giay);
//
//                List<Integer> chosenIndices = new ArrayList<>();
//                List<Product> randomProducts = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    int index;
//                    do {
//                        index = random.nextInt(productList.size());
//                    } while (chosenIndices.contains(index));
//                    chosenIndices.add(index);
//                    randomProducts.add(productList.get(index));
//                }
//
//                int staffId = random.nextInt(2) + 5;
//                Receipt receipt = new Receipt(id, staffId, invoice_date, 0, 0, 0, 0, 0, 0);
//
//                double total = 0;
//                List<Receipt_Detail> receipt_details = new ArrayList<>();
//                for (Product product : randomProducts) {
//                    Receipt_Detail receipt_detail = new Receipt_Detail();
//                    receipt_detail.setReceipt_id(receipt.getId());
//                    receipt_detail.setProduct_id(product.getId());
//                    receipt_detail.setSize(product.getSize());
//                    int quantity = random.nextInt(5) + 1;
//                    receipt_detail.setQuantity(quantity);
//                    receipt_detail.setPrice(quantity * product.getPrice());
//                    receipt_detail.setNotice(" ");
//                    receipt_detail.setPrice_discount(receipt_detail.getPrice());
//                    total += receipt_detail.getPrice();
//                    receipt_details.add(receipt_detail);
//                }
//
//                receipt.setTotal_price(total);
//                receipt.setTotal(total);
//                receipt.setReceived(total);
//
//                new ReceiptBLL().addReceipt(receipt);
//
//                for (Receipt_Detail receipt_detail : receipt_details) {
//                    new Receipt_DetailBLL().addReceipt_Detail(receipt_detail);
//                }
//
//                id += 1;
//            }
//            start = start.plusDays(1);
//        }


        // tinh gia von
//        for (Product product : new ProductBLL().searchProducts( "size = 'Không'","deleted = 0")) {
//            double capitalization_price = 0;
//            for (Recipe recipe : new RecipeBLL().searchRecipes("product_id = " + product.getId(), "size = '" + product.getSize() + "'")) {
//                Material material = new MaterialBLL().searchMaterials("id = " + recipe.getMaterial_id()).get(0);
//                capitalization_price += recipe.getQuantity() * material.getUnit_price() / 1000;
//            }
//            product.setCapital_price(capitalization_price);
//            new ProductBLL().updateProduct(product);
//        }

        // tao du lieu nhap
//        LocalDate start = LocalDate.of(2023, 1, 6);
//        LocalDate end = LocalDate.of(2024, 6, 6);
//
//        List<Material> materialList = new MaterialBLL().searchMaterials("deleted = 0");
//        List<Supplier> supplierList = new SupplierBLL().searchSuppliers("deleted = 0");
//        int id = 1;
//        int idex = 1;
//        int idship = 1;
//        while (!start.equals(end)) {
//            List<Integer> chosenIndices = new ArrayList<>();
//            for (int j = 0; j < 5; j++) {
//                Random random = new Random();
//
////                Date received_date = java.sql.Date.valueOf(start);
//
//                List<Material> randomMaterials = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    int index;
//                    do {
//                        index = random.nextInt(materialList.size());
//                    } while (chosenIndices.contains(index));
//                    chosenIndices.add(index);
//                    randomMaterials.add(materialList.get(index));
//                }
//
//                int staffId = 4;
//
//                LocalDateTime invoice_date = start.atStartOfDay();
//                Import_Note importNote = new Import_Note(id, staffId, new BigDecimal(0), invoice_date);
//
//                double total = 0;
//                List<Shipment> shipmentList = new ArrayList<>();
//                for (Material material : randomMaterials) {
//                    Shipment shipment = new Shipment();
//                    shipment.setId(idship);
//                    shipment.setMaterial_id(material.getId());
//                    int index = random.nextInt(supplierList.size());
//                    Supplier supplier = supplierList.get(index);
//                    shipment.setSupplier_id(supplier.getId());
//                    int quantity = random.nextInt(20) + 1;
//                    shipment.setQuantity(quantity);
//                    shipment.setRemain(quantity);
//                    shipment.setImport_id(importNote.getId());
//                    shipment.setMfg(java.sql.Date.valueOf(start.plusMonths(-1)));
//                    shipment.setExp(java.sql.Date.valueOf(start.plusMonths(6)));
//
//                    total += quantity * material.getUnit_price();
//                    shipmentList.add(shipment);
//                    idship += 1;
//                }
//
//                importNote.setTotal(BigDecimal.valueOf(total));
//
//                new Import_NoteBLL().addImport(importNote);
//
//                for (Shipment shipment : shipmentList) {
//                    new ShipmentBLL().addShipment(shipment);
//                }
//                id += 1;
//            }
//
//            for (int j = 0; j < 5; j++) {
//                Random random = new Random();
//
//                Date received_date = java.sql.Date.valueOf(start.plusDays(5));
//
//                List<Shipment> shipmentList = new ShipmentBLL().searchShipments("remain > 0");
//                List<Shipment> randomShipments = new ArrayList<>();
//                List<Integer> chosenIndices1 = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    int index;
//                    do {
//                        index = random.nextInt(shipmentList.size());
//                    } while (chosenIndices1.contains(index));
//                    chosenIndices1.add(index);
//                    randomShipments.add(shipmentList.get(index));
//                }
//
//                int staffId = 7;
//                Export_Note exportNote = new Export_Note(idex, staffId, new BigDecimal(0), received_date);
//
//                double total = 0;
//                List<Export_Detail> export_details = new ArrayList<>();
//                for (Shipment shipment : randomShipments) {
//                    Material material = new MaterialBLL().searchMaterials("id  = " + shipment.getMaterial_id()).get(0);
//                    Export_Detail exportDetail = new Export_Detail();
//                    exportDetail.setExport_id(exportNote.getId());
//                    exportDetail.setShipment_id(shipment.getId());
//                    int quantity = random.nextInt((int) shipment.getRemain()) + 1;
//                    exportDetail.setQuantity(quantity);
//                    int random1 = random.nextInt(2);
//                    exportDetail.setReason(random1 == 0 ? "Bán" : "Huỷ");
//
//                    total += quantity * material.getUnit_price();
//                    export_details.add(exportDetail);
//                }
//
//                exportNote.setTotal(BigDecimal.valueOf(total));
//
//                new Export_NoteBLL().addExport_Note(exportNote);
//
//                for (Export_Detail exportDetail : export_details) {
//                    new Export_DetailBLL().addExport_Detail(exportDetail);
//                }
//
//                idex += 1;
//            }
//
//            start = start.plusMonths(1);
//        }
    }

    public static void exit(int status) {
        System.exit(status);
    }

}
