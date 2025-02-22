package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.Import_Note;
import com.coffee.DTO.Material;
import com.coffee.DTO.Shipment;
import com.coffee.DTO.Supplier;
import com.coffee.GUI.CreateWorkScheduleGUI;
import com.coffee.GUI.DialogGUI.DialogFormDetail;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.MaterialGUI;
import com.coffee.GUI.SaleGUI;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.MyTextFieldUnderLine;
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
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddImportGUI extends DialogFormDetail {
    private List<JLabel> attributeImport_Note;
    private JLabel titleName;
    private JLabel jLabelTotal;
    private JButton buttonAdd;
    private Import_NoteBLL import_NoteBLL = new Import_NoteBLL();
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private DataTable dataTable;
    private String[] columnNames;
    private RoundedScrollPane scrollPane;
    private Import_Note import_note = new Import_Note();
    private List<Shipment> shipmentList = new ArrayList<>();
    private JTextField jTextFieldQuantity = new MyTextFieldUnderLine();
    private JComboBox<String> jComboBoxSupplier = new JComboBox<>();
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private DatePicker[] datePicker;
    private JFormattedTextField[] editor;
    private int materialID = -1;
    private int import_id;
    private int shipment_id;
    private BigDecimal total = BigDecimal.valueOf(0);

    public AddImportGUI() {
        super();
        super.setTitle("Tạo Phiếu Nhập");
        super.setSize(new Dimension(1200, 700));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        import_id = import_NoteBLL.getAutoID(import_NoteBLL.searchImport());
        shipment_id = shipmentBLL.getAutoID(shipmentBLL.searchShipments()) - 1;
        init();
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText(data.getText());
                Material material = new MaterialBLL().findMaterialsBy(Map.of("name", data.getText())).get(0);
                materialID = material.getId();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
            }
        });
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        buttonAdd = new JButton("Tạo Phiếu Nhập");
        attributeImport_Note = new ArrayList<>();
        contenttop.setLayout(new MigLayout("",
                "50[]20[]20[]20[]20[]20[]20[]20[]20",
                "10[]10[]10"));
        contentbot.setLayout(new MigLayout("",
                "50[]20[]50",
                "10[]10[]10"));
        datePicker = new DatePicker[2];
        editor = new JFormattedTextField[2];

        titleName.setText("Tạo Phiếu Nhập");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Phiếu Nhập", "Nhân Viên", "Ngày Nhập"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeImport_Note.add(label);
            contenttop.add(label);
            JLabel textField = new JLabel();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            if (string.trim().equals("Ngày Nhập")) {
                import_note.setReceived_date(LocalDateTime.now());
                textField.setText(import_note.getReceived_date().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            }
            if (string.trim().equals("Mã Phiếu Nhập")) {
                import_note.setId(import_NoteBLL.getAutoID(import_NoteBLL.searchImport()));
                String import_noteId = Integer.toString(import_note.getId());
                textField.setText(import_noteId);
            }
            if (string.equals("Nhân Viên")) {
                import_note.setStaff_id(HomeGUI.staff.getId());
                String name = new StaffBLL().findStaffsBy(Map.of("id", import_note.getStaff_id())).get(0).getName();
                textField.setText(name);
            }
            contenttop.add(textField, "wrap");
        }

        super.remove(contentmid);
        super.remove(contentbot);
        super.remove(containerButton);

        JPanel jPanel = new JPanel(new MigLayout("", "[][][]20[][]20[][]20[]", ""));
        jPanel.setPreferredSize(new Dimension(1200, 150));
        jPanel.setBackground(new Color(245, 246, 250));

        JButton btnAddMaterial = new JButton();
        ImageIcon icon = new FlatSVGIcon("icon/add.svg");
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        btnAddMaterial.setIcon(icon);
        btnAddMaterial.setPreferredSize(new Dimension(30, 30));
        btnAddMaterial.setBackground(new Color(0, 182, 62));
        btnAddMaterial.setFont(new Font("Public Sans", Font.BOLD, 16));
        btnAddMaterial.setForeground(Color.WHITE);
        btnAddMaterial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddMaterial.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new AddMaterialGUI();
            }
        });
        jPanel.add(btnAddMaterial);

        int i = 0;

        for (String string : new String[]{"Tên Nguyên Liệu", "Ngày Sản Xuất", "Nhà Cung Cấp", "Ngày Hết Hạn", "SL Nhập"}) {
            if (string.equals("Nhà Cung Cấp")) {
                JButton btnAddSupplier = new JButton();
                icon = new ImageIcon(newImg);
                btnAddSupplier.setIcon(icon);
                btnAddSupplier.setPreferredSize(new Dimension(30, 30));
                btnAddSupplier.setBackground(new Color(0, 182, 62));
                btnAddSupplier.setFont(new Font("Public Sans", Font.BOLD, 16));
                btnAddSupplier.setForeground(Color.WHITE);
                btnAddSupplier.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnAddSupplier.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        new AddSupplierGUI();
                        jComboBoxSupplier.removeAllItems();
                        for (Supplier supplier : new SupplierBLL().searchSuppliers("deleted = 0"))
                            jComboBoxSupplier.addItem(supplier.getName());
                    }
                });
                jPanel.add(btnAddSupplier);
            }
            JLabel jLabelTitle = new JLabel(string);
            jLabelTitle.setFont((new Font("Public Sans", Font.BOLD, 12)));
            jPanel.add(jLabelTitle);

            if (string.equals("Tên Nguyên Liệu")) {
                txtSearch = new MyTextField();
                txtSearch.setPreferredSize(new Dimension(200, 30));

                txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        txtSearchMouseClicked(evt);
                    }
                });
                txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        txtSearchKeyPressed(evt);
                    }

                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        txtSearchKeyReleased(evt);
                    }
                });
                jPanel.add(txtSearch);
                continue;
            }
            if (string.equals("Nhà Cung Cấp")) {
                for (Supplier supplier : new SupplierBLL().searchSuppliers("deleted = 0"))
                    jComboBoxSupplier.addItem(supplier.getName());
                jComboBoxSupplier.setPreferredSize(new Dimension(150, 30));
                jComboBoxSupplier.setBackground(new Color(1, 120, 220));
                jComboBoxSupplier.setForeground(Color.white);
                jPanel.add(jComboBoxSupplier);
                continue;
            }
            if (string.equals("SL Nhập")) {
                jTextFieldQuantity.setPreferredSize(new Dimension(50, 30));
                jTextFieldQuantity.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
                jPanel.add(jTextFieldQuantity);
            } else {

                datePicker[i] = new DatePicker();
                editor[i] = new JFormattedTextField();

                datePicker[i].setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
                datePicker[i].setEditor(editor[i]);
                datePicker[i].setCloseAfterSelected(true);

                editor[i].setPreferredSize(new Dimension(200, 40));
                editor[i].setFont(new Font("Inter", Font.BOLD, 15));

                if (string.equals("Ngày Sản Xuất"))
                    jPanel.add(editor[i], "wrap");
                else
                    jPanel.add(editor[i]);
                i++;
            }
        }

        JButton btnThem = new JButton("Thêm lô");
        btnThem.setPreferredSize(new Dimension(100, 30));
        btnThem.setBackground(new Color(1, 120, 220));
        btnThem.setForeground(Color.white);
        btnThem.setFont(new Font("Public Sans", Font.BOLD, 12));
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnThem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addShipment();
            }

        });
        jPanel.add(btnThem);

        super.add(jPanel, "wrap");
        contentmid.setPreferredSize(new Dimension(1200, 350));
        super.add(contentmid, "wrap");
        contentbot.setPreferredSize(new Dimension(1200, 100));
        super.add(contentbot, "wrap");
        containerButton.setPreferredSize(new Dimension(1200, 100));
        super.add(containerButton, "wrap");


        columnNames = new String[]{"Nguyên Liệu", "Tên NCC", "SL Nhập", "SL Tồn", "MFG", "EXP",};
        columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
        columnNames[columnNames.length - 1] = "Xóa";
        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(), e -> changedQuantity(),
                false, false, true, 6, true, 2);

        dataTable.getColumnModel().getColumn(0).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(500);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(100);
        dataTable.getColumnModel().getColumn(4).setMaxWidth(250);
        dataTable.getColumnModel().getColumn(5).setMaxWidth(250);
        dataTable.setRowHeight(25);
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        contentmid.add(scrollPane, BorderLayout.CENTER);

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
                addImport_Note();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addImport_Note() {
        Pair<Boolean, String> result;

        if (shipmentList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng thêm đơn hàng!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        import_note.setTotal(total);
        result = import_NoteBLL.addImport(import_note);

        if (result.getKey()) {
            for (Shipment shipment : shipmentList) {
                shipment_id += 1;
                shipment.setId(shipment_id);
                shipmentBLL.addShipment(shipment); // cập nhập tồn kho của nguyên liệu trong lớp shipmentBLL.addShipment
            }
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (Cafe_Application.homeGUI.indexModuleMaterialGUI != -1) {
                MaterialGUI materialGUI = (MaterialGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleMaterialGUI];
                materialGUI.refresh();
            }
            loadSaleGUI();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changedQuantity() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        String quantity = model.getValueAt(indexRow, indexColumn).toString();
        if (VNString.checkUnsignedNumber(quantity)) {
            model.setValueAt(quantity, indexRow, indexColumn + 1);
            shipmentList.get(indexRow).setQuantity(Double.parseDouble(quantity));
            shipmentList.get(indexRow).setRemain(Double.parseDouble(quantity));
        } else {
            JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);

        }
        refresh();
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();
        if (indexColumn == 6) {
            shipmentList.remove(indexRow);
            refresh();
        }
    }

    private void addShipment() {
        int supplier_id;
        double quantity;
        java.util.Date mfg, exp;

        if (materialID == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nguyên liệu nhập!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (datePicker[0].getDateSQL_Single() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhập ngày sản xuất!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (datePicker[1].getDateSQL_Single() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhập ngày hết hạn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mfg = datePicker[0].getDateSQL_Single();
        exp = datePicker[1].getDateSQL_Single();
        if (!mfg.before(exp)) {
            JOptionPane.showMessageDialog(null, "Ngày sản xuất và ngày hết hạn không hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        supplier_id = jComboBoxSupplier.getSelectedIndex() + 1;
        for (Shipment shipment1 : shipmentList) {
            if (shipment1.getMaterial_id() == materialID && shipment1.getSupplier_id() == supplier_id) {
                if (shipment1.getMfg() == mfg && shipment1.getExp() == exp) {
                    JOptionPane.showMessageDialog(null, "Lô hàng trùng ngày sản xuất và ngày hết hạn!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        if (jTextFieldQuantity.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng nhập!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        quantity = Double.parseDouble(jTextFieldQuantity.getText());
        if (quantity == 0) {
            JOptionPane.showMessageDialog(null, "Số lượng nhập phải lớn hơn 0!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Shipment shipment = new Shipment(shipment_id, materialID, supplier_id, import_id, quantity, quantity, mfg, exp);
        shipmentList.add(shipment);
        refresh();
    }

    private void refresh() {
        txtSearch.setText("");
        jComboBoxSupplier.setSelectedIndex(0);
        jTextFieldQuantity.setText("");
        datePicker[0].clearSelectedDate();
        datePicker[1].clearSelectedDate();

        loadDataTable(shipmentBLL.getData(shipmentList));

    }

    private void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            jLabelTotal.setText(VNString.currency(0));
            return;
        }

        Object[][] data = new Object[objects.length][objects[0].length];
        total = BigDecimal.valueOf(0);
        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            int material_id = Integer.parseInt(data[i][1].toString());
            Material material = new MaterialBLL().findMaterialsBy(Map.of("id", material_id)).get(0);
            data[i][1] = "<html>" + material.getName() + "</html>";

            int supplier_id = Integer.parseInt(data[i][2].toString());
            data[i][2] = "<html>" + new SupplierBLL().findSuppliersBy(Map.of("id", supplier_id)).get(0).getName() + "</html>";

            JLabel iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            data[i] = Arrays.copyOf(data[i], data[i].length + 1);
            data[i][data[i].length - 1] = iconRemove;

            total = total.add(BigDecimal.valueOf(material.getUnit_price() * Double.parseDouble(data[i][4].toString())));
        }

        for (Object[] object : data) {
            Object[] objects1 = object;
            System.arraycopy(objects1, 0, object, 0, 2);
            System.arraycopy(objects1, 4, object, 3, 5);
            object = Arrays.copyOfRange(object, 1, 8);
            model.addRow(object);
        }
        jLabelTotal.setText(VNString.currency(Double.parseDouble(total.toString())));
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        materialID = -1;
        List<DataSearch> list = new ArrayList<>();
        List<Material> materials = new MaterialBLL().findMaterials("name", text);
        for (Material m : materials) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
        }
        return list;
    }

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
            } else {
                menu.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            txtSearch.setText(text);

        }
        menu.setVisible(false);

    }

    private void loadSaleGUI() {
        if (Cafe_Application.homeGUI.indexSaleGUI != -1) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexSaleGUI] = new SaleGUI(HomeGUI.account);
                }
            });
            thread.start();
        }
    }
}
