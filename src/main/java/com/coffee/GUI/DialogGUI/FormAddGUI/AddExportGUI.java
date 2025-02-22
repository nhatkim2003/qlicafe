package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.DialogFormDetail;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.MaterialGUI;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class AddExportGUI extends DialogFormDetail {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private JComboBox<String> jComboBoxSearch;
    private JComboBox<String> jComboBoxSearchImport;
    private JComboBox<String> jComboBoxSearchStatus;
    private List<JLabel> attributeExport_Note;
    private JLabel titleName;
    private JLabel jLabelTotal;
    private JButton buttonAdd;
    private Export_NoteBLL export_NoteBLL = new Export_NoteBLL();
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private DataTable dataTable;
    private String[] columnNames;
    private RoundedScrollPane scrollPane;
    private Export_Note export_note = new Export_Note();
    private List<Integer> shipmentList = new ArrayList<>();
    private List<Export_Detail> exportDetailList = new ArrayList<>();
    private int export_id;
    private BigDecimal total = BigDecimal.valueOf(0);

    public AddExportGUI() {
        super();
        super.setTitle("Tạo Phiếu Xuất");
        super.setSize(new Dimension(1400, 700));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        export_id = export_NoteBLL.getAutoID(export_NoteBLL.searchExport_Note());
        init();
        setVisible(true);
    }

    private void init() {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBoxSearch = new JComboBox<>(new String[]{"Nguyên liệu", "Nhà cung cấp"});
        jComboBoxSearchStatus = new JComboBox<>(new String[]{"Tất cả", "Sắp hết hạn", "Đã hết hạn", "Đã chọn"});
        jComboBoxSearchImport = new JComboBox<>();
        titleName = new JLabel();
        buttonAdd = new JButton("Tạo Phiếu Xuất");
        attributeExport_Note = new ArrayList<>();
        contenttop.setLayout(new MigLayout("",
                "50[]20[]20[]20[]20[]20[]20[]20[]20",
                "10[]10[]10"));
        contentbot.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));

        titleName.setText("Tạo Phiếu Xuất");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Phiếu Xuất", "Nhân Viên", "Ngày Xuất"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeExport_Note.add(label);
            contenttop.add(label);
            JLabel textField = new JLabel();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            if (string.trim().equals("Ngày Xuất")) {
                export_note.setInvoice_date(Date.valueOf(LocalDate.now()));
                textField.setText(export_note.getInvoice_date().toString());
            }
            if (string.trim().equals("Mã Phiếu Xuất")) {
                export_note.setId(export_id);
                String export_noteId = Integer.toString(export_note.getId());
                textField.setText(export_noteId);
            }
            if (string.equals("Nhân Viên")) {
                export_note.setStaff_id(HomeGUI.staff.getId());
                String name = new StaffBLL().findStaffsBy(Map.of("id", export_note.getStaff_id())).get(0).getName();
                textField.setText(name);
            }
            contenttop.add(textField, "wrap");
        }

        super.remove(contentmid);
        super.remove(contentbot);
        super.remove(containerButton);

        JPanel jPanel = new JPanel(new FlowLayout());
        jPanel.setPreferredSize(new Dimension(1400, 100));
        jPanel.setBackground(new Color(245, 246, 250));

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(255, 255, 255));
        containerSearch.setPreferredSize(new Dimension(280, 40));
        jPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(255, 255, 255));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(300, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchShipments();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchShipments();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchShipments();
            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchShipments());
        jPanel.add(jButtonSearch);

        jComboBoxSearch.setBackground(new Color(1, 120, 220));
        jComboBoxSearch.setForeground(Color.white);
        jComboBoxSearch.setPreferredSize(new Dimension(120, 30));
        jComboBoxSearch.addActionListener(e -> searchShipments());
        jPanel.add(jComboBoxSearch);

        JLabel jLabel = new JLabel("Mã phiếu nhập");
        jLabel.setFont((new Font("Public Sans", Font.BOLD, 12)));
        jPanel.add(jLabel);

        jComboBoxSearchImport.addItem("Tất cả");
        for (Import_Note importNote : new Import_NoteBLL().searchImport())
            jComboBoxSearchImport.addItem(String.valueOf(importNote.getId()));

        jComboBoxSearchImport.setBackground(new Color(1, 120, 220));
        jComboBoxSearchImport.setForeground(Color.white);
        jComboBoxSearchImport.setPreferredSize(new Dimension(120, 30));
        jComboBoxSearchImport.addActionListener(e -> searchShipments());
        jPanel.add(jComboBoxSearchImport);

        JLabel jLabel1 = new JLabel("Trạng thái lô hàng");
        jLabel1.setFont((new Font("Public Sans", Font.BOLD, 12)));
        jPanel.add(jLabel1);

        jComboBoxSearchStatus.setBackground(new Color(1, 120, 220));
        jComboBoxSearchStatus.setForeground(Color.white);
        jComboBoxSearchStatus.setPreferredSize(new Dimension(120, 30));
        jComboBoxSearchStatus.addActionListener(e -> searchShipments());
        jPanel.add(jComboBoxSearchStatus);

        super.add(jPanel, "wrap");
        contentmid.setPreferredSize(new Dimension(1400, 350));
        super.add(contentmid, "wrap");
        contentbot.setPreferredSize(new Dimension(1400, 100));
        super.add(contentbot, "wrap");
        containerButton.setPreferredSize(new Dimension(1400, 100));
        super.add(containerButton, "wrap");


        columnNames = new String[]{"Mã Lô", "Nguyên Liệu", "Tên NCC", "Mã Nhập", "SL Tồn", "MFG", "EXP", "SL Xuất", "", "Lý Do"};
        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(), e -> changedQuantity(), 7, 8, 9
        );

        dataTable.getColumnModel().getColumn(0).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(4).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(5).setMaxWidth(250);
        dataTable.getColumnModel().getColumn(6).setMaxWidth(250);
        dataTable.getColumnModel().getColumn(7).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(8).setMaxWidth(50);
        dataTable.getColumnModel().getColumn(9).setMaxWidth(100);
        dataTable.setRowHeight(25);
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1400, 680));
        contentmid.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(shipmentBLL.getData(shipmentBLL.searchShipments("remain > 0")));

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(170, 30));
        label.setText("Tổng Tiền");
        label.setFont((new Font("Public Sans", Font.BOLD, 16)));
        contentbot.add(label);

        jLabelTotal = new JLabel();
        jLabelTotal.setText(VNString.currency(0));
        jLabelTotal.setPreferredSize(new Dimension(1000, 30));
        jLabelTotal.setFont((new Font("Public Sans", Font.PLAIN, 14)));
        jLabelTotal.setBackground(new Color(245, 246, 250));
        contentbot.add(jLabelTotal, "wrap");

        buttonAdd.setPreferredSize(new Dimension(200, 30));
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(Color.white);
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addExport_Note();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void searchShipments() {
        List<Shipment> shipmentList = shipmentBLL.searchShipments("remain > 0");
        if (jComboBoxSearchStatus.getSelectedIndex() == 1) {
            shipmentList = shipmentBLL.searchShipments("remain > 0", "DATEDIFF(exp,NOW()) > 0", "DATEDIFF(exp,NOW()) <= 15");

        }
        if (jComboBoxSearchStatus.getSelectedIndex() == 2) {
            shipmentList = shipmentBLL.searchShipments("remain > 0", "exp < NOW()");

        }
        if (jComboBoxSearchStatus.getSelectedIndex() == 3) {
            shipmentList.removeIf(shipment -> !this.shipmentList.contains(shipment.getId()));
        }
        if (jComboBoxSearchImport.getSelectedIndex() != 0) {
            shipmentList.removeIf(shipment -> shipment.getImport_id() != Integer.parseInt(Objects.requireNonNull(jComboBoxSearchImport.getSelectedItem()).toString()));
        }

        if (jComboBoxSearch.getSelectedIndex() == 0) {
            List<Integer> materialID = new ArrayList<>();
            for (Material material : new MaterialBLL().findMaterials("name", jTextFieldSearch.getText())) {
                materialID.add(material.getId());
            }
            shipmentList.removeIf(shipment -> !materialID.contains(shipment.getMaterial_id()));
        }
        if (jComboBoxSearch.getSelectedIndex() == 1) {
            List<Integer> supplierID = new ArrayList<>();
            for (Supplier supplier : new SupplierBLL().findSuppliers("name", jTextFieldSearch.getText())) {
                supplierID.add(supplier.getId());
            }
            shipmentList.removeIf(shipment -> !supplierID.contains(shipment.getSupplier_id()));
        }

        loadDataTable(shipmentBLL.getData(shipmentList));
    }

    private void addExport_Note() {
        Pair<Boolean, String> result;

        if (shipmentList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn lô hàng cần xuất!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Export_Detail exportDetail : exportDetailList) {
            if (exportDetail.getQuantity() == 0) {
                JOptionPane.showMessageDialog(null, "Số lượng xuất của lô hàng '" + exportDetail.getShipment_id() + "' phải lớn hơn 0!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (exportDetail.getReason().equals("--Lý do--")) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn lý do xuất của lô hàng '" + exportDetail.getShipment_id() + "'",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        export_note.setTotal(total);
        result = export_NoteBLL.addExport_Note(export_note);

        if (result.getKey()) {
            for (Export_Detail exportDetail : exportDetailList) {
                new Export_DetailBLL().addExport_Detail(exportDetail); // cập nhập tồn kho của nguyên liệu trong lớp Export_DetailBLL().addExport_Detail
            }
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (Cafe_Application.homeGUI.indexModuleMaterialGUI != -1) {
                MaterialGUI materialGUI = (MaterialGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleMaterialGUI];
                materialGUI.refresh();
            }
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changedQuantity() {
        int indexRow = dataTable.getSelectedRow();

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        if (model.getValueAt(indexRow, 8).equals(false))
            return;

        String quantity = model.getValueAt(indexRow, 7).toString();
        if (VNString.checkUnsignedNumber(quantity)) {
            String reason = model.getValueAt(indexRow, 9).toString();
            String shipment_id = model.getValueAt(indexRow, 0).toString();
            int index = shipmentList.indexOf(Integer.parseInt(shipment_id));
            Shipment shipment = shipmentBLL.findShipmentsBy(Map.of("id", Integer.parseInt(shipment_id))).get(0);
            if (Double.parseDouble(quantity) > shipment.getRemain()) {
                JOptionPane.showMessageDialog(null, "Số lượng xuất không được lớn hơn số lượng tồn!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                model.setValueAt("0", indexRow, 7);
                return;
            }
            exportDetailList.get(index).setQuantity(Double.parseDouble(quantity));
            exportDetailList.get(index).setReason(reason);
            calculateTotal();
        } else {
            JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();
        if (indexColumn == 8) {
            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            Object[] rowData = model.getDataVector().elementAt(dataTable.getSelectedRow()).toArray();

            if (rowData[8].equals(false)) {
                shipmentList.add(Integer.parseInt(rowData[0].toString()));
                Export_Detail exportDetail = new Export_Detail();
                exportDetail.setExport_id(export_note.getId());
                exportDetail.setShipment_id(Integer.parseInt(rowData[0].toString()));
                exportDetailList.add(exportDetail);
                model.setValueAt(true, indexRow, indexColumn);
                model.setValueAt("0", indexRow, indexColumn - 1);
                model.setValueAt("--Lý do--", indexRow, indexColumn + 1);
            } else {
                model.setValueAt(false, indexRow, indexColumn);
                model.setValueAt("", indexRow, indexColumn - 1);
                model.setValueAt("--Lý do--", indexRow, indexColumn + 1);
                int index = shipmentList.indexOf(Integer.parseInt(rowData[0].toString()));
                shipmentList.remove((Object) Integer.parseInt(rowData[0].toString()));
                exportDetailList.remove(index);
                calculateTotal();
            }
        }
    }

    private void calculateTotal() {
        total = BigDecimal.valueOf(0);
        for (Export_Detail exportDetail : exportDetailList) {
            int shipment_id = exportDetail.getShipment_id();
            int material_id = shipmentBLL.findShipmentsBy(Map.of("id", shipment_id)).get(0).getMaterial_id();
            double unit_price = new MaterialBLL().findMaterialsBy(Map.of("id", material_id)).get(0).getUnit_price();
            total = total.add(BigDecimal.valueOf(unit_price * exportDetail.getQuantity()));
        }
        jLabelTotal.setText(VNString.currency(Double.parseDouble(total.toString())));
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            jLabelTotal.setText(VNString.currency(0));
            return;
        }

        Object[][] data = new Object[objects.length][objects[0].length];
        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            int material_id = Integer.parseInt(data[i][1].toString());
            Material material = new MaterialBLL().searchMaterials("id = " + material_id).get(0);
            data[i][1] = "<html>" + material.getName() + "</html>";

            int supplier_id = Integer.parseInt(data[i][2].toString());
            data[i][2] = "<html>" + new SupplierBLL().findSuppliersBy(Map.of("id", supplier_id)).get(0).getName() + "</html>";

            int shipment_id = Integer.parseInt(data[i][0].toString());
            if (shipmentList.contains(shipment_id)) {
                int index = shipmentList.indexOf(shipment_id);

                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = exportDetailList.get(index).getQuantity();

                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = true;

                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = exportDetailList.get(index).getReason();
            } else {
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = "";

                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = false;

                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = "--Lý do--";
            }
        }

        for (Object[] object : data) {
            Object[] objects1 = object;
            System.arraycopy(objects1, 0, object, 0, 4);
            System.arraycopy(objects1, 5, object, 4, 6);
            object = Arrays.copyOfRange(object, 0, 10);
            model.addRow(object);
        }
    }
}
