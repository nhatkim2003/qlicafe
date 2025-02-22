package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.Import_Note;
import com.coffee.DTO.Material;
import com.coffee.DTO.Shipment;
import com.coffee.DTO.Supplier;
import com.coffee.GUI.DialogGUI.DialogFormDetail;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DetailMaterialGUI extends DialogFormDetail {
    private JLabel titleName;
    private JComboBox<String> jComboBoxRemain;
    private JComboBox<String> jComboBoxSupplier;
    private List<JLabel> attributeMaterial;
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private DataTable dataTable;
    private String[] columnNames;
    private RoundedScrollPane scrollPane;
    private Material material;
    private DatePicker datePicker;
    private JFormattedTextField editor;

    public DetailMaterialGUI(Material material) {
        super();
        super.setTitle("Thông Tin Nguyên Liệu");
        super.setSize(new Dimension(1100, 750));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        this.material = material;
        init(material);
        setVisible(true);
    }

    private void init(Material material) {
        titleName = new JLabel();
        jComboBoxRemain = new JComboBox<>(new String[]{"Tất cả", "Dưới mức tồn", "Vượt mức tồn", "Còn hàng", "Hết hàng", "Sắp hết hạn"});
        jComboBoxSupplier = new JComboBox<>();
        attributeMaterial = new ArrayList<>();
        datePicker = new DatePicker();
        editor = new JFormattedTextField();

        contenttop.setLayout(new MigLayout("",
                "100[]20[]20[]20[]100",
                "10[]10[]10"));

        super.remove(contentbot);
        super.remove(containerButton);

        titleName.setText("Thông Tin Nguyên Liệu");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Nguyên Liệu", "Tên Nguyên Liệu", "Tồn Quầy", "Tồn Kho", "Tồn Tối Thiểu", "Tồn Tối Đa", "Đơn Vị", "Giá Vốn"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeMaterial.add(label);
            contenttop.add(label);
            JLabel textField = new JLabel();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));

            if (string.trim().equals("Mã Nguyên Liệu")) {
                textField.setText(String.valueOf(material.getId()));
                contenttop.add(textField);
                continue;
            }
            if (string.trim().equals("Tên Nguyên Liệu")) {
                textField.setText(material.getName());
                contenttop.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Tồn Quầy")) {
                textField.setText(String.format("%.2f", material.getRemain()));
                contenttop.add(textField);
                continue;
            }
            if (string.trim().equals("Tồn Kho")) {
                textField.setText(String.format("%.2f", material.getRemain_wearhouse()));
                contenttop.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Tồn Tối Thiểu")) {
                textField.setText(String.valueOf(material.getMinRemain()));
                contenttop.add(textField);
                continue;
            }
            if (string.trim().equals("Tồn Tối Đa")) {
                textField.setText(String.valueOf(material.getMaxRemain()));
                contenttop.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Đơn Vị")) {
                textField.setText(material.getUnit());
                contenttop.add(textField);
                continue;
            }
            if (string.trim().equals("Giá Vốn")) {
                textField.setText(VNString.currency(material.getUnit_price()));
                contenttop.add(textField, "wrap");
            }


        }

        JPanel jPanel = new JPanel(new MigLayout("", "0[]20[]20[]20[]20[]"));
        jPanel.setBackground(new Color(242, 245, 250));
        jPanel.setPreferredSize(new Dimension(1000, 100));
        contentmid.add(jPanel, BorderLayout.NORTH);


        datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setEditor(editor);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                searchShipments();
            }
        });

        editor.setPreferredSize(new Dimension(280, 30));
        editor.setFont(new Font("Inter", Font.BOLD, 15));
        jPanel.add(editor, "span, wrap");

        JLabel label = new JLabel();
        label.setText("Trạng thái lô hàng");
        label.setFont((new Font("Public Sans", Font.BOLD, 15)));
        jPanel.add(label);

        jComboBoxRemain.setBackground(new Color(1, 120, 220));
        jComboBoxRemain.setPreferredSize(new Dimension(200, 30));
        jComboBoxRemain.setForeground(Color.white);
        jComboBoxRemain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchShipments();
            }
        });
        jPanel.add(jComboBoxRemain);

        JLabel jlabel = new JLabel();
        jlabel.setText("Nhà cung cấp");
        jlabel.setFont((new Font("Public Sans", Font.BOLD, 15)));
        jPanel.add(jlabel);

        jComboBoxSupplier.addItem("Tất cả");
        for (Supplier supplier : new SupplierBLL().searchSuppliers("deleted = 0"))
            jComboBoxSupplier.addItem(supplier.getName());
        jComboBoxSupplier.setPreferredSize(new Dimension(150, 30));
        jComboBoxSupplier.setBackground(new Color(1, 120, 220));
        jComboBoxSupplier.setForeground(Color.white);
        jComboBoxSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchShipments();
            }
        });

        jPanel.add(jComboBoxSupplier);
        JButton jButtonSearch = new JButton("Tìm kiếm");
        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonSearch.addActionListener(e -> searchShipments());
        jPanel.add(jButtonSearch);

        columnNames = new String[]{"Mã Lô", "Ngày Nhập", "Tên NCC", "SL Nhập", "SL Tồn", "MFG", "EXP",};
        dataTable = new DataTable(new Object[0][0], columnNames);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(4).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(5).setMaxWidth(250);
        dataTable.getColumnModel().getColumn(6).setMaxWidth(250);
        dataTable.setRowHeight(25);
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 400));
        contentmid.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(shipmentBLL.getData(shipmentBLL.findShipmentsBy(Map.of("material_id", material.getId()))));
    }

    private void searchShipments() {
        List<Shipment> shipmentList = new ArrayList<>();


        if (datePicker.getDateSQL_Between() != null) {
            Date startDate = datePicker.getDateSQL_Between()[0];
            Date endDate = datePicker.getDateSQL_Between()[1];
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Shipment shipment : shipmentBLL.findShipmentsBy(Map.of("material_id", material.getId()))) {
                Import_Note importNote = new Import_NoteBLL().findImportBy(Map.of("id", shipment.getImport_id())).get(0);

                if (importNote.getReceived_date().toLocalDate().isAfter(LocalDate.parse(startDate.toString(), myFormatObj)) && importNote.getReceived_date().toLocalDate().isBefore(LocalDate.parse(endDate.toString(), myFormatObj))) {
                    shipmentList.add(shipment);
                }
            }
        } else
            shipmentList = shipmentBLL.findShipmentsBy(Map.of("material_id", material.getId()));
        if (jComboBoxRemain.getSelectedIndex() == 1) {
            shipmentList.removeIf(shipment -> shipment.getRemain() >= material.getMinRemain());
        }
        if (jComboBoxRemain.getSelectedIndex() == 2) {
            shipmentList.removeIf(shipment -> shipment.getRemain() <= material.getMaxRemain());
        }
        if (jComboBoxRemain.getSelectedIndex() == 3) {
            shipmentList.removeIf(shipment -> shipment.getRemain() == 0);
        }
        if (jComboBoxRemain.getSelectedIndex() == 4) {
            shipmentList.removeIf(shipment -> shipment.getRemain() > 0);
        }
        if (jComboBoxRemain.getSelectedIndex() == 5) {
            shipmentList = shipmentBLL.searchShipments("material_id = " + material.getId(), "DATEDIFF(exp,NOW()) > 0", "DATEDIFF(exp,NOW()) <= 15");
        }
        if (jComboBoxSupplier.getSelectedIndex() != 0) {
            shipmentList.removeIf(shipment -> shipment.getSupplier_id() != jComboBoxSupplier.getSelectedIndex());
        }

        loadDataTable(shipmentBLL.getData(shipmentList));
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        Object[][] data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            int import_id = Integer.parseInt(data[i][3].toString());
            Import_Note importNote = new Import_NoteBLL().findImportBy(Map.of("id", import_id)).get(0);
            data[i][1] = importNote.getReceived_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            int supplier_id = Integer.parseInt(data[i][2].toString());
            data[i][2] = "<html>" + new SupplierBLL().findSuppliersBy(Map.of("id", supplier_id)).get(0).getName() + "</html>";


        }

        for (Object[] object : data) {
            Object[] objects1 = object;
            System.arraycopy(objects1, 0, object, 0, 3);
            System.arraycopy(objects1, 4, object, 3, 4);
            object = Arrays.copyOfRange(object, 0, 7);
            model.addRow(object);
        }
    }
}
