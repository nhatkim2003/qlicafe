package com.coffee.GUI;

import com.coffee.BLL.SupplierBLL;
import com.coffee.DTO.Function;
import com.coffee.DTO.Supplier;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddSupplierGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailSupplierGUI;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditSupplierGUI;
import com.coffee.GUI.components.*;
import com.coffee.ImportExcel.AddSupplierFromExcel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.coffee.utils.Resource.chooseExcelFile;

public class SupplierGUI extends Layout1 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private JComboBox<String> jComboBoxSearch;
    private List<Function> functions;
    private SupplierBLL supplierBLL = new SupplierBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private int indexColumnEdit = -1;
    private int indexColumnRemove = -1;
    private boolean detail = false;
    private boolean edit = false;
    private boolean remove = false;
    private String[] columnNames;
    private Object[][] data = new Object[0][0];

    public SupplierGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view"))) detail = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("edit"))) edit = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("remove"))) remove = true;
        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBoxSearch = new JComboBox<>(new String[]{"Tên NCC", "SĐT", "Email"});

        columnNames = new String[]{"Mã NCC", "Tên NCC", "SĐT", "Email"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        if (edit) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnEdit = columnNames.length - 1;
            columnNames[indexColumnEdit] = "Sửa";
        }

        if (remove) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnRemove = columnNames.length - 1;
            columnNames[indexColumnRemove] = "Xoá";
        }

        dataTable = new DataTable(new Object[0][0], columnNames, e -> selectFunction(), detail, edit, remove, 4); // table hiển thị các thuộc tính  nên điền 4
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        bottom.add(scrollPane, BorderLayout.CENTER);

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(280, 40));
        SearchPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(280, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchSuppliers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchSuppliers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchSuppliers();
            }
        });

        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchSuppliers());
        SearchPanel.add(jButtonSearch);

        jComboBoxSearch.setBackground(new Color(1, 120, 220));
        jComboBoxSearch.setForeground(Color.white);
        jComboBoxSearch.setPreferredSize(new Dimension(110, 30));
        jComboBoxSearch.addActionListener(e -> selectSearchFilter());
        SearchPanel.add(jComboBoxSearch);


        loadDataTable(supplierBLL.getData(supplierBLL.searchSuppliers("deleted = 0")));

        RoundedPanel refreshPanel = new RoundedPanel();
        refreshPanel.setLayout(new GridBagLayout());
        refreshPanel.setPreferredSize(new Dimension(130, 40));
        refreshPanel.setBackground(new Color(1, 120, 220));
        refreshPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                refresh();
            }
        }); // button refesh
        FunctionPanel.add(refreshPanel);

        JLabel refreshLabel = new JLabel("Làm mới");
        refreshLabel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        refreshLabel.setForeground(Color.white);
        refreshLabel.setIcon(new FlatSVGIcon("icon/refresh.svg"));
        refreshPanel.add(refreshLabel);

        if (functions.stream().anyMatch(f -> f.getName().equals("add"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    new AddSupplierGUI();
                    refresh();
                }
            });

            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Thêm mới");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/add.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("excel"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    File file = chooseExcelFile(null);
                    if (file != null) {
                        Pair<Boolean, String> result;
                        try {
                            result = new AddSupplierFromExcel().addSupplierFromExcel(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thành công",
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            refresh();
                        }
                    }
                }
            });

            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Nhập Excel");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/import.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("pdf"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Xuất PDF");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/export.svg"));
            roundedPanel.add(panel);
        }
    }

    public void refresh() {
        jTextFieldSearch.setText("");
        jComboBoxSearch.setSelectedIndex(0);
        loadDataTable(supplierBLL.getData(supplierBLL.searchSuppliers("deleted = 0")));
    }

    private void searchSuppliers() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(supplierBLL.getData(supplierBLL.searchSuppliers("deleted = 0")));
        } else {
            selectSearchFilter();
        }
    }

    private void selectSearchFilter() {
        if (Objects.requireNonNull(jComboBoxSearch.getSelectedItem()).toString().contains("SĐT")) {
            searchSuppliersByPhone();
        } else {
            if (Objects.requireNonNull(jComboBoxSearch.getSelectedItem()).toString().contains("Email")) {
                searchSuppliersByEmail();
            } else {
                loadDataTable(supplierBLL.getData(supplierBLL.findSuppliers("name", jTextFieldSearch.getText())));
            }
        }
    }

    private void searchSuppliersByPhone() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(supplierBLL.getData(supplierBLL.searchSuppliers("deleted = 0")));
        } else {
            loadDataTable(supplierBLL.getData(supplierBLL.findSuppliers("phone", jTextFieldSearch.getText())));
        }
    }

    private void searchSuppliersByEmail() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(supplierBLL.getData(supplierBLL.searchSuppliers("deleted = 0")));
        } else {
            loadDataTable(supplierBLL.getData(supplierBLL.findSuppliers("email", jTextFieldSearch.getText())));
        }
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            if (detail) {
                JLabel iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconDetail;
            }
            if (edit) {
                JLabel iconEdit = new JLabel(new FlatSVGIcon("icon/edit.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconEdit;
            }
            if (remove) {
                JLabel iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconRemove;
            }
        }

        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (detail && indexColumn == indexColumnDetail)
            new DetailSupplierGUI(supplierBLL.searchSuppliers("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá

        if (edit && indexColumn == indexColumnEdit) {
            new EditSupplierGUI(supplierBLL.searchSuppliers("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
            refresh();
        }

        if (remove && indexColumn == indexColumnRemove)
            deleteSupplier(supplierBLL.searchSuppliers("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá

    }

    private void deleteSupplier(Supplier supplier) {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp cần xoá.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá nhà cung cấp?", "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Pair<Boolean, String> result = supplierBLL.deleteSupplier(supplier);
            if (result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
