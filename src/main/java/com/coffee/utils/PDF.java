package com.coffee.utils;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.components.DataTable;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDImmutableRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import static com.coffee.GUI.components.WorkSchedulePanel.getDaysBetween;

public class PDF {
    private PDDocument document;
    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private PDType0Font regularFont;
    private PDType0Font boldFont;
    private PDType0Font italicFont;
    public float pageWidth;
    public float pageHeight;
    public float TAB;
    public float LINE;

    public PDF(float numberOfTabs, float numberOfLines, float tabSize, float lineHeight) {
        Configurator.setLevel("org.apache.fontbox.ttf.gsub.GlyphSubstitutionDataExtractor", org.apache.logging.log4j.Level.OFF);
        Configurator.setLevel("org.apache.pdfbox.pdmodel.PDDocument", org.apache.logging.log4j.Level.OFF);
        Configurator.setAllLevels(LogManager.getRootLogger().getName(), org.apache.logging.log4j.Level.OFF);
        Logger.getLogger("org.apache.fontbox.ttf.gsub.GlyphSubstitutionDataExtractor").setLevel(java.util.logging.Level.OFF);
        Logger.getLogger("org.apache.pdfbox.pdmodel.PDDocument").setLevel(java.util.logging.Level.OFF);
        TAB = tabSize;
        LINE = lineHeight;
        document = new PDDocument();
        currentPage = new PDPage(new PDImmutableRectangle(numberOfTabs * TAB, numberOfLines * LINE));
        document.addPage(currentPage);
        pageWidth = currentPage.getMediaBox().getWidth();
        pageHeight = currentPage.getMediaBox().getHeight();
        regularFont = newFont("Roboto/Roboto-Regular.ttf");
        boldFont = newFont("Roboto/Roboto-Bold.ttf");
        italicFont = newFont("Roboto/Roboto-Italic.ttf");
        try {
            contentStream = new PDPageContentStream(document, currentPage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeDocument(File file) {
        try {
            contentStream.close();
            document.save(file);
            document.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addText(String text, float x, float y, PDFont font, float fontSize) {
        try {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(x, pageHeight - y);
            contentStream.showText(text);
            contentStream.endText();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void addTextAt(String text, float tabs, float lines, PDFont font, float fontSize) {
        addText(text, tabs * TAB, lines * LINE, font, fontSize);
    }

    public void addLine(float x1, float y1, float x2, float y2, float lineWidth) {
        try {
            contentStream.setLineWidth(lineWidth);
            contentStream.moveTo(x1, pageHeight - y1);
            contentStream.lineTo(x2, pageHeight - y2);
            contentStream.stroke();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLineAt(float tabs1, float lines1, float tabs2, float lines2, float lineWidth) {
        addLine(tabs1 * TAB, lines1 * LINE, tabs2 * TAB, lines2 * LINE, lineWidth);
    }

    public void addTable(List<String[]> data, float fontSize, float startLine, float firstRow, float[] columns) {
        int rows = data.size();

        for (int i = 0; i <= rows; ++i)
            addLineAt(columns[0], startLine + i, columns[columns.length - 1], startLine + i, 0.5F);
        for (float column : columns)
            addLineAt(column, startLine, column, startLine + rows, 0.5F);
        for (int j = 0; j < columns.length - 1; ++j)
            addTextAt(data.get(0)[j], columns[j] + 0.1F, firstRow, boldFont, fontSize);
        for (int i = 1; i < rows; ++i)
            for (int j = 0; j < columns.length - 1; ++j)
                addTextAt(data.get(i)[j], columns[j] + 0.1F, firstRow + i, regularFont, fontSize - 1);
    }


    public void addTableWorkSchedule(List<String[]> data, float fontSize, float startLine, float firstRow, float[] columns) {
        int rows = data.size();

        for (int i = 0; i <= rows; ++i)
            addLineAt(columns[0], startLine + i, columns[columns.length - 1], startLine + i, 0.5F);
        for (float column : columns)
            addLineAt(column, startLine, column, startLine + rows, 0.5F);
        for (int j = 0; j < columns.length - 1; ++j)
            addTextAt(data.get(0)[j], columns[j] + 0.1F, firstRow, boldFont, fontSize);
        for (int i = 1; i < rows; ++i)
            for (int j = 0; j < columns.length - 1; ++j)
                addTextAt("data.get(i)[j]", columns[j] + 0.1F, firstRow + i, regularFont, fontSize - 1);
    }

    public static String getFileNameDatetodate(String path, String name, Date from, Date to) {
        int max = 0;
        File[] files = new File(path).listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.getName().startsWith(name)) {
                String numStr = file.getName().substring(name.length(), file.getName().indexOf("_"));
                int num = Integer.parseInt(numStr);
                if (num > max)
                    max = num;
            }
        }
        return String.format("%s/%s%03d_%s_%s.pdf", path, name, max + 1, from.toString(), to.toString());
    }

    public static String getFileName(String path, String name) {
        int max = 0;
        File[] files = new File(path).listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.getName().startsWith(name)) {
                String numStr = file.getName().substring(name.length(), file.getName().indexOf("_"));
                int num = Integer.parseInt(numStr);
                if (num > max)
                    max = num;
            }
        }
        return String.format("%s/%s%03d.pdf", path, name, max + 1);
    }


    public static String getFileNameDate(String path, String name, Date date) {
        int max = 0;
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(name)) {
                    String fileName = file.getName();
                    int underscoreIndex = fileName.lastIndexOf('_');
                    if (underscoreIndex != -1) {
                        String numStr = fileName.substring(name.length(), underscoreIndex);
                        try {
                            int num = Integer.parseInt(numStr);
                            if (num > max) {
                                max = num;
                            }
                        } catch (NumberFormatException e) {
                            //ignore file
                        }
                    }
                }
            }
        }
        max++;
        return String.format("%s/%s%03d_%s.pdf", path, name, max, date.toString());
    }


    public static boolean exportExportDetailsPDF(Export_Note exportNote, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        List<Export_Detail> exports = new Export_DetailBLL().searchExport("export_id = " + exportNote.getId());
        File file = new File(getFileNameDate(path, "Export_Details", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(8, 12 + exports.size(), 125F, exports.size() + 40F);

        pdf.addTextAt("BẢNG CHI TIẾT PHIẾU XUẤT", 3.3F, 2, pdf.boldFont, 25);
        pdf.addTextAt(" Mã Phiếu Xuất : " + exportNote.getId(), 0.5F, 3, pdf.italicFont, 20);

        int staffId = exportNote.getStaff_id();
        List<Staff> staff = new StaffBLL().searchStaffs("id = '" + staffId + "'");

        pdf.addTextAt(" Tên Nhân Viên : " + staff.get(0).getName(), 0.5F, 4, pdf.italicFont, 20);
        pdf.addTextAt(" Ngày Tạo : " + exportNote.getInvoice_date(), 0.5F, 5, pdf.italicFont, 20);
//            pdf.addTextAt("Từ ngày " + from + " đến ngày " + to, 2.5F,4, pdf.italicFont, 20);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Mã Lô",
                "Nguyên Liệu",
                "Số Lượng",
                "Lý Do").toArray(new String[0]));


        for (Export_Detail export_detail : exports) {
            String[] data = new String[4];
            data[0] = String.valueOf(export_detail.getShipment_id());

            int shipmentId = export_detail.getShipment_id();
            Shipment shipment = new ShipmentBLL().findShipmentsBy(Map.of("id", shipmentId)).get(0);

            int materialId = shipment.getMaterial_id();
            Material material = new MaterialBLL().findMaterialsBy(Map.of("id", materialId)).get(0);

            data[1] = String.valueOf(material.getName());
            data[2] = String.valueOf(export_detail.getQuantity());
            data[3] = String.valueOf(export_detail.getReason());
            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 6.5F, 7.1F, new float[]{0.5F, 1.5F, 4F, 5F, 6F});

        String formattedTotal = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(exportNote.getTotal());
        pdf.addTextAt(" Tổng tiền: " + formattedTotal, 0.5F, exports.size() + 10, pdf.italicFont, 20);

        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportImportDetailsPDF(Import_Note importNote, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        List<Shipment> shipments = new ShipmentBLL().searchShipments("import_id = " + importNote.getId());
        File file = new File(getFileNameDate(path, "Import_Details", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(8, 12 + shipments.size(), 180F, shipments.size() + 40F);

        pdf.addTextAt("BẢNG CHI TIẾT PHIẾU NHẬP", 3.3F, 2, pdf.boldFont, 25);
        pdf.addTextAt(" Mã Phiếu Nhập : " + importNote.getId(), 0.5F, 3, pdf.italicFont, 20);

        int staffId = importNote.getStaff_id();
        List<Staff> staff = new StaffBLL().searchStaffs("id = '" + staffId + "'");

        pdf.addTextAt(" Tên Nhân Viên : " + staff.get(0).getName(), 0.5F, 4, pdf.italicFont, 20);
        pdf.addTextAt(" Ngày Nhập : " + importNote.getReceived_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 0.5F, 5, pdf.italicFont, 20);
//            pdf.addTextAt("Từ ngày " + from + " đến ngày " + to, 2.5F,4, pdf.italicFont, 20);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Mã Lô",
                "Nguyên Liệu", "Tên Nhà Cung Cấp",
                "Số Lượng Nhập ", "Số Lượng Tồn ",
                "Ngày Sản Xuất", "Ngày Hết Hạn ").toArray(new String[0]));


        for (Shipment shipment : shipments) {
            String[] data = new String[7];
            data[0] = String.valueOf(shipment.getImport_id());

            int materialId = shipment.getMaterial_id();
            Material material = new MaterialBLL().findMaterialsBy(Map.of("id", materialId)).get(0);
            data[1] = String.valueOf(material.getName());
            int supplierId = shipment.getSupplier_id();
            Supplier supplier = new SupplierBLL().findSuppliersBy(Map.of("id", supplierId)).get(0);
            data[2] = String.valueOf(supplier.getName());

            data[3] = String.valueOf(shipment.getQuantity());
            data[4] = String.valueOf(shipment.getRemain());
            data[5] = String.valueOf(shipment.getMfg());
            data[6] = String.valueOf(shipment.getExp());
            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 6.5F, 7.1F, new float[]{0.5F, 1F, 2.2F, 4F, 5F, 6F, 7F, 8F});

        String formattedTotal = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(importNote.getTotal());
        pdf.addTextAt(" Tổng tiền: " + formattedTotal, 0.5F, shipments.size() + 10, pdf.italicFont, 20);
        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportMaterialPDF(Object[][] objects, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        File file = new File(getFileNameDate(path, "Material", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(6, 10 + objects.length, 167F, 30F);
        pdf.addTextAt("BẢNG NGUYÊN LIỆU ", 2.2F, 2, pdf.boldFont, 25);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Id",
                "Tên Nguyên Liệu",
                " Tồn Kho ", " Đơn Vị ",
                "Giá Vốn").toArray(new String[0]));

        for (int i = 0; i < objects.length; i++) {
            String[] data = new String[5];
            data[0] = objects[i][0].toString();
            data[1] = objects[i][1].toString();
            data[2] = objects[i][2].toString();
            data[3] = objects[i][3].toString();
            data[4] = objects[i][4].toString();
            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 5F, 5.6F, new float[]{0.5F, 1F, 2.5F, 3.5F, 4F, 5F});

        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportReceiptDetialsPDF(Receipt receipt, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        List<Receipt_Detail> receipts = new Receipt_DetailBLL().searchReceipt_Details("receipt_id = " + receipt.getId());
        File file = new File(getFileNameDate(path, "Receipt_Details", java.sql.Date.valueOf(LocalDate.now())));

        PDF pdf = new PDF(8, 18 + receipts.size(), 125F, 30F); // note
        pdf.addTextAt("BẢNG CHI TIẾT HÓA ĐƠN", 3.3F, 2, pdf.boldFont, 25);

        pdf.addTextAt(" Mã Hóa Đơn : " + receipt.getId(), 0.5F, 3, pdf.italicFont, 20);
        int staffId = receipt.getStaff_id();
        List<Staff> staff = new StaffBLL().searchStaffs("id = '" + staffId + "'");
        pdf.addTextAt(" Tên Nhân Viên : " + staff.get(0).getName(), 0.5F, 4, pdf.italicFont, 20);
        pdf.addTextAt(" Ngày Tạo : " + receipt.getInvoice_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 0.5F, 5, pdf.italicFont, 20);

        List<String[]> tableData = new ArrayList<>();

        tableData.add(List.of(
                " Sản Phẩm ",
                " Size ",
                " Số Lượng ",
                " Giá ",
                " Ghi Chú").toArray(new String[0]));

        for (Receipt_Detail receipt_detail : receipts) {
            String[] data = new String[5];
            int productId = receipt_detail.getProduct_id();
            List<Product> products = new ProductBLL().searchProducts("id = '" + productId + "'");

            data[0] = String.valueOf(products.get(0).getName());
            data[1] = receipt_detail.getSize();
            data[2] = String.valueOf(receipt_detail.getQuantity());
            data[3] = VNString.currency(receipt_detail.getPrice());
            data[4] = receipt_detail.getNotice();
            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 6.5F, 7.1F, new float[]{0.5F, 2F, 3F, 4F, 5F, 7.0F});

        pdf.addTextAt(" Tổng Cộng: " + VNString.currency(receipt.getTotal_price()), 0.5F, receipts.size() + 9, pdf.italicFont, 20);
        if (receipt.getDiscount_id() == 0)
            pdf.addTextAt(" Khuyến Mãi: " + VNString.currency(receipt.getTotal_discount()), 0.5F, receipts.size() + 10, pdf.italicFont, 20);

        else
            pdf.addTextAt(" Khuyến Mãi: " + "-" + VNString.currency(receipt.getTotal_discount()) + " (Mã giảm giá: " + receipt.getDiscount_id() + ")", 0.5F, receipts.size() + 10, pdf.italicFont, 20);

        pdf.addTextAt(" Thành Tiền: " + VNString.currency(receipt.getTotal()), 0.5F, receipts.size() + 11, pdf.italicFont, 20);
        pdf.addTextAt(" Tiền Nhận: " + VNString.currency(receipt.getReceived()), 0.5F, receipts.size() + 12, pdf.italicFont, 20);
        pdf.addTextAt(" Tiền Thừa: " + VNString.currency(receipt.getExcess()), 0.5F, receipts.size() + 13, pdf.italicFont, 20);

        pdf.closeDocument(file);
        return true;
    }


    public static boolean exportReceiptsPDF(Object[][] objects, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        File file = new File(getFileNameDate(path, "Receipts", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(6, 10 + objects.length, 167F, 30F);
        pdf.addTextAt("BẢNG HÓA ĐƠN ", 2.2F, 2, pdf.boldFont, 25);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Mã Hóa Đơn",
                "Nhân Viên",
                " Tổng Tiền ",
                "Ngày Lập").toArray(new String[0]));

        for (int i = 0; i < objects.length; i++) {
            String[] data = new String[4];
            data[0] = objects[i][0].toString();
            data[1] = objects[i][1].toString();
            data[2] = objects[i][2].toString();
            data[3] = objects[i][3].toString();

            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 6F, 6.6F, new float[]{0.5F, 1.5F, 2.5F, 3.5F, 4.5F});

        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportExportNotePDF(Object[][] objects, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        File file = new File(getFileNameDate(path, "Export_Note", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(6, 10 + objects.length, 167F, 30F);
        pdf.addTextAt("BẢNG PHIẾU XUẤT HÀNG", 2.2F, 2, pdf.boldFont, 25);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Mã Phiếu Xuất ",
                "Nhân Viên",
                "Tổng Tiền ",
                "Ngày Xuất ").toArray(new String[0]));

        for (int i = 0; i < objects.length; i++) {
            String[] data = new String[4];
            data[0] = objects[i][0].toString();
            data[1] = objects[i][1].toString();
            data[2] = objects[i][2].toString();
            data[3] = objects[i][3].toString();

            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 6F, 6.6F, new float[]{0.5F, 1.5F, 2.5F, 3.5F, 4.5F});

        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportImportNotePDF(Object[][] objects, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        File file = new File(getFileNameDate(path, "Import_Note", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(6, 10 + objects.length, 167F, 30F);
        pdf.addTextAt("BẢNG PHIẾU NHẬP HÀNG", 2.2F, 2, pdf.boldFont, 25);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Mã Phiếu Nhập ",
                "Nhân Viên",
                " Tổng Tiền ",
                "Ngày Nhập ").toArray(new String[0]));

        for (int i = 0; i < objects.length; i++) {
            String[] data = new String[4];
            data[0] = objects[i][0].toString();
            data[1] = objects[i][1].toString();
            data[2] = objects[i][2].toString();
            data[3] = objects[i][3].toString();

            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 6F, 6.6F, new float[]{0.5F, 1.5F, 2.5F, 3.5F, 4.5F});

        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportPayrollPDF(Object[][] objects, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        File file = new File(getFileNameDate(path, "Pay_Roll", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(6, 10 + objects.length, 220F, 30F);
        pdf.addTextAt("BẢNG TỔNG LƯƠNG ", 2.2F, 2, pdf.boldFont, 25);

        List<String[]> tableData = new ArrayList<>();
        tableData.add(List.of(
                "Mã ",
                "Kì Hạn Bảng Lương",
                " Kỳ Làm Việc ",
                "Tổng Lương ",
                " Đã Trả",
                " Còn Lại").toArray(new String[0]));

        for (int i = 0; i < objects.length; i++) {
            String[] data = new String[6];
            data[0] = objects[i][0].toString();
            data[1] = objects[i][1].toString();
            data[2] = objects[i][2].toString();
            data[3] = objects[i][3].toString();
            data[4] = objects[i][4].toString();
            data[5] = objects[i][5].toString();
            tableData.add(data);
        }

        pdf.addTable(tableData, 16F, 4F, 4.6F, new float[]{0.5F, 1F, 2.7F, 3.5F, 4.3F, 5.1F, 5.9F});

        pdf.closeDocument(file);
        return true;
    }

    public static boolean exportPayrollDetailPDF(Payroll payroll, Payroll_Detail payrollDetail, String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        File file = new File(getFileNameDate(path, "PayRoll_Detail", java.sql.Date.valueOf(LocalDate.now())));
        PDF pdf = new PDF(6, 20F, 180F, 30F);

        pdf.addTextAt("BẢNG LƯƠNG CHI TIẾT THÁNG  " + payroll.getMonth() + "/" + payroll.getYear(), 2.2F, 2, pdf.boldFont, 25);

        pdf.addTextAt(" Mã Nhân Viên     :   " + payrollDetail.getStaff_id(), 0.5F, 4, pdf.italicFont, 20);

        Staff staff = new StaffBLL().searchStaffs("id = " + payrollDetail.getStaff_id()).get(0);

        pdf.addTextAt(" Tên Nhân Viên    :  " + staff.getName(), 0.5F, 5, pdf.italicFont, 20);

        Role_Detail role_detail = new Role_DetailBLL().searchRole_detailsByStaff(payrollDetail.getStaff_id()).get(0);
        Role role = new RoleBLL().searchRoles("id = " + role_detail.getRole_id()).get(0);
        pdf.addTextAt(" Chức Vụ          :    " + role.getName(), 0.5F, 6, pdf.italicFont, 20);

        pdf.addTextAt(" Lương Chính Thức :    " + VNString.currency(role_detail.getSalary()) + (role_detail.getType_salary() == 1 ? " /kỳ lương" : " /giờ"),
                0.5F, 7, pdf.italicFont, 20);

        List<String[]> tableData1 = new ArrayList<>();
        List<String[]> tableData2 = new ArrayList<>();
        List<String[]> tableData3 = new ArrayList<>();


        String[] data1 = new String[2];
        data1[0] = "Tổng Phụ Cấp                  :   " + VNString.currency(payrollDetail.getAllowance_amount());
        data1[1] = "Tổng Giảm Trừ               :   " + VNString.currency(payrollDetail.getDeduction_amount());

        tableData1.add(data1);
        pdf.addTable(tableData1, 16F, 9F, 9.6F, new float[]{0.5F, 2.5F, 5F});
        String[] data2 = new String[2];
        data2[0] = "Tổng Thưởng                   :   " + VNString.currency(payrollDetail.getBonus_amount());
        data2[1] = "Tổng Phạt Vi Phạm       :   " + VNString.currency(payrollDetail.getFine_amount());

        tableData2.add(data2);
        pdf.addTable(tableData2, 16F, 10F, 10.6F, new float[]{0.5F, 2.5F, 5F});

        String[] data3 = new String[2];

        if (role_detail.getType_salary() == 2)
            data3[0] = "Giờ Công Thực Tế           :   " + String.format("%.2f", payrollDetail.getHours_amount()) + " giờ";
        else
            data3[0] = "Giờ Công Thực Tế           :   " + String.valueOf(payrollDetail.getHours_amount()).split("\\.")[0] + " ngày";
        data3[1] = "Thực Lãnh                       :   " + VNString.currency(payrollDetail.getSalary_amount());

        tableData3.add(data3);
        pdf.addTable(tableData3, 16F, 11F, 11.6F, new float[]{0.5F, 2.5F, 5F});

        pdf.addTextAt("Trạng Thái          :    " + (payrollDetail.isStatus() ? "Đã trả lương" : "Tạm tính"), 0.5F, 14, pdf.italicFont, 20);


        pdf.closeDocument(file);
        return true;
    }


    public static void exportWorkSchedulePDF(Date from, Date to, String path) {
        List<Date> dates = getDaysBetween(from, to);
        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH);
        DateTimeFormatter dtfOutput = DateTimeFormatter.ofPattern("EEEE", new Locale("vi", "VN"));
        DateTimeFormatter dtfDayMonth = DateTimeFormatter.ofPattern("d/M", new Locale("vi", "VN"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] columns = new String[]{"       ",
                LocalDate.parse(dateFormat.format(dates.get(0)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(0)), dtfInput).format(dtfDayMonth) + ")",
                LocalDate.parse(dateFormat.format(dates.get(1)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(1)), dtfInput).format(dtfDayMonth) + ")",
                LocalDate.parse(dateFormat.format(dates.get(2)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(2)), dtfInput).format(dtfDayMonth) + ")",
                LocalDate.parse(dateFormat.format(dates.get(3)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(3)), dtfInput).format(dtfDayMonth) + ")",
                LocalDate.parse(dateFormat.format(dates.get(4)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(4)), dtfInput).format(dtfDayMonth) + ")",
                LocalDate.parse(dateFormat.format(dates.get(5)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(5)), dtfInput).format(dtfDayMonth) + ")",
                LocalDate.parse(dateFormat.format(dates.get(6)), dtfInput).format(dtfOutput) +
                        " (" + LocalDate.parse(dateFormat.format(dates.get(6)), dtfInput).format(dtfDayMonth) + ")"};

        DataTable dataTable = new DataTable(new Object[0][0], columns);
        Object[][] data = new Object[12][8];

        Object[] row = new Object[]{"Quản Lý", "", "", "", "", "", "", ""};
        data[0] = row;

        row = new Object[]{"Ca 1", "", "", "", "", "", "", ""};
        List<Integer> managerIDList = new ArrayList<>();
        List<Role_Detail> managerrole_detailList = new Role_DetailBLL().searchRole_detailsByRole(2, new SimpleDateFormat("yyyy-MM-dd").format(java.sql.Date.valueOf(LocalDate.now())));
        for (Role_Detail roleDetail : managerrole_detailList)
            managerIDList.add(roleDetail.getStaff_id());

        List<Work_Schedule> managerWork_schedules = new ArrayList<>();
        for (Integer id : managerIDList) {
            managerWork_schedules.addAll(new Work_ScheduleBLL().searchWork_schedules("staff_id = " + id,
                    "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(from) + "'",
                    "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(to) + "'"));
        }

        for (Work_Schedule work_schedule : managerWork_schedules) {
            if (work_schedule.getShift() == 1) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[1] = row;

        row = new Object[]{"Ca 2", "", "", "", "", "", "", ""};
        for (Work_Schedule work_schedule : managerWork_schedules) {
            if (work_schedule.getShift() == 2) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[2] = row;

        row = new Object[]{"Ca 3", "", "", "", "", "", "", ""};
        for (Work_Schedule work_schedule : managerWork_schedules) {
            if (work_schedule.getShift() == 3) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[3] = row;

        row = new Object[]{"Kho", "", "", "", "", "", "", ""};
        data[4] = row;

        row = new Object[]{"Ca 1", "", "", "", "", "", "", ""};
        List<Integer> staffWearHouseIDList = new ArrayList<>();
        List<Role_Detail> staffWearHouserole_detailList = new Role_DetailBLL().searchRole_detailsByRole(3, new SimpleDateFormat("yyyy-MM-dd").format(java.sql.Date.valueOf(LocalDate.now())));
        for (Role_Detail roleDetail : staffWearHouserole_detailList)
            staffWearHouseIDList.add(roleDetail.getStaff_id());

        List<Work_Schedule> staffWearHouseWork_schedules = new ArrayList<>();
        for (Integer id : staffWearHouseIDList) {
            staffWearHouseWork_schedules.addAll(new Work_ScheduleBLL().searchWork_schedules("staff_id = " + id,
                    "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(from) + "'",
                    "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(to) + "'"));
        }

        for (Work_Schedule work_schedule : staffWearHouseWork_schedules) {
            if (work_schedule.getShift() == 1) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[5] = row;

        row = new Object[]{"Ca 2", "", "", "", "", "", "", ""};
        for (Work_Schedule work_schedule : staffWearHouseWork_schedules) {
            if (work_schedule.getShift() == 2) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[6] = row;

        row = new Object[]{"Ca 3", "", "", "", "", "", "", ""};
        for (Work_Schedule work_schedule : staffWearHouseWork_schedules) {
            if (work_schedule.getShift() == 3) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[7] = row;

        row = new Object[]{"Bán Hàng", "", "", "", "", "", "", ""};
        data[8] = row;

        row = new Object[]{"Ca 1", "", "", "", "", "", "", ""};
        List<Integer> staffSalesIDList = new ArrayList<>();
        List<Role_Detail> staffSalesrole_detailList = new Role_DetailBLL().searchRole_detailsByRole(4, new SimpleDateFormat("yyyy-MM-dd").format(java.sql.Date.valueOf(LocalDate.now())));
        for (Role_Detail roleDetail : staffSalesrole_detailList)
            staffSalesIDList.add(roleDetail.getStaff_id());

        List<Work_Schedule> staffSalesWork_schedules = new ArrayList<>();
        for (Integer id : staffSalesIDList) {
            staffSalesWork_schedules.addAll(new Work_ScheduleBLL().searchWork_schedules("staff_id = " + id,
                    "date >= '" + new SimpleDateFormat("yyyy-MM-dd").format(from) + "'",
                    "date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(to) + "'"));
        }

        for (Work_Schedule work_schedule : staffSalesWork_schedules) {
            if (work_schedule.getShift() == 1) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[9] = row;

        row = new Object[]{"Ca 2", "", "", "", "", "", "", ""};
        for (Work_Schedule work_schedule : staffSalesWork_schedules) {
            if (work_schedule.getShift() == 2) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[10] = row;

        row = new Object[]{"Ca 3", "", "", "", "", "", "", ""};
        for (Work_Schedule work_schedule : staffSalesWork_schedules) {
            if (work_schedule.getShift() == 3) {
                int index = dates.indexOf(work_schedule.getDate());
                String staffName = new StaffBLL().searchStaffs("id = " + work_schedule.getStaff_id()).get(0).getName();
                row[index + 1] += staffName + "\n" + "\n";
            }
        }
        data[11] = row;

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        for (Object[] object : data) {
            model.addRow(object);
        }
        PDF.createWorkSchedulePDF("Lịch Làm Việc", dataTable, "src/main/resources/ExportPDF", from, to);
    }

    public static void createWorkSchedulePDF(String title, JTable table, String dest, Date from, Date to) {
        try {
            // Create PDF document
            Document document = new Document(new RectangleReadOnly(PageSize.A4.getHeight(), PageSize.A4.getWidth()));

            // Initialize PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(getFileNameDate(dest, "workSchedule", java.sql.Date.valueOf(LocalDate.now()))));

            // Open document
            document.open();
            String fontPath = "font/Roboto/Roboto-Regular.ttf"; // adjust the path as needed
            Font regularFont = getCustomFont(fontPath);
            regularFont.setSize(9F);

            fontPath = "font/Roboto/Roboto-Bold.ttf";
            Font boldFont = getCustomFont(fontPath);

            fontPath = "font/Roboto/Roboto-Italic.ttf";
            Font italicFont = getCustomFont(fontPath);

            // Add title to the document
            boldFont.setSize(25);
            Paragraph titleParagraph = new Paragraph(30F, title, boldFont);
            titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titleParagraph);


            boldFont.setSize(20);
            Paragraph dateParagraph = new Paragraph(30f, new SimpleDateFormat("dd/MM/yyy").format(from) + " - " + new SimpleDateFormat("dd/MM/yyy").format(to), boldFont);
            dateParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(dateParagraph);
            document.add(new Paragraph(30f, " "));

            // Convert JTable to PDF table
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100); // Set table width to 100% of page width
            pdfTable.setWidths(new float[]{30f, 60f, 60f, 60f, 60f, 60f, 60f, 60f});

            boldFont.setSize(10F);
            for (int i = 0; i < table.getColumnCount(); i++) {
                pdfTable.addCell(new Phrase(table.getColumnName(i), boldFont));
            }
            List<String> strings = List.of("Quản Lý", "Kho", "Bán Hàng");
            for (int i = 0; i < table.getRowCount(); i++) {
                boolean flag = table.getValueAt(i, 0) != null && strings.contains(table.getValueAt(i, 0).toString());
                for (int j = 0; j < table.getColumnCount(); j++) {
                    if (table.getValueAt(i, j) == null)
                        pdfTable.addCell(new Phrase(" ", regularFont));
                    else
                        pdfTable.addCell(new Phrase(table.getValueAt(i, j).toString(), regularFont));

                }
                if (flag) {
                    for (int j = 1; j < table.getColumnCount(); j++) {
                        PdfPCell cell = pdfTable.getRow(i + 1).getCells()[j];
                        cell.setBackgroundColor(BaseColor.GRAY);
                    }
                }
            }

            // Add PDF table to document
            document.add(pdfTable);

            // Close document
            document.close();

            System.out.println("PDF created successfully.");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    PDType0Font newFont(String path) {
        try (InputStream fontFile = Resource.loadInputStream("font/" + path)) {
            return PDType0Font.load(document, fontFile);
        } catch (Exception e) {
            return null;
        }
    }

    public static Font getCustomFont(String fontPath) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return new Font(bf, 12);
    }

    public static void main(String[] args) {
//            List<Receipt> receipts = new ReceiptBLL().searchReceipts();
//            PDF.exportReceiptsPDF(receipts, java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()),
//                    "C:\\Users\\MI\\OneDrive\\Documents\\GitHub\\cafe_application\\src\\main\\java\\com\\coffee\\Export" );

//            List<Export_Note> exportNotes = new Export_NoteBLL().searchExport_Note();
//            PDF.exportBillDetailsPDF(exportNotes.get(0),"C:\\Users\\MI\\OneDrive\\Documents\\GitHub\\cafe_application\\src\\main\\java\\com\\coffee\\Export" );
//
//            List<Import_Note> importNotes = new Import_NoteBLL().searchImport();
//            PDF.importBillDetailsPDF(importNotes.get(0),"C:\\Users\\MI\\OneDrive\\Documents\\GitHub\\cafe_application\\src\\main\\java\\com\\coffee\\Export" );
//

        // PDF.exportWorkSchedulePDF(java.sql.Date.valueOf("2024-03-11"), java.sql.Date.valueOf("2024-03-17"), "src/main/resources/ExportPDF");

//        List<Payroll> payrolls = new PayrollBLL().searchPayrolls();
//        PDF.exportPayrollDetailPDF(payrolls.get(0), "src/main/resources/ExportPDF");
    }

}


