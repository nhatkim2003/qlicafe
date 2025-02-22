package com.coffee.GUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.DTO.Function;
import com.coffee.DTO.Material;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddMaterialGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailMaterialGUI;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditMaterialGUI;
import com.coffee.GUI.components.*;

import com.coffee.ImportExcel.AddImportFromExcel;
import com.coffee.ImportExcel.AddMaterialFromExcel;
import com.coffee.utils.PDF;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coffee.utils.Resource.chooseExcelFile;

public class MaterialGUI extends Layout2 {
    private final MaterialBLL materialBLL = new MaterialBLL();
    private List<Function> functions;
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private boolean detail = false;
    private boolean edit = false;
    private boolean remove = false;
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private int indexColumnEdit = -1;
    private int indexColumnRemove = -1;
    private String[] columnNames;
    private Object[][] data = new Object[0][0];
    private JComboBox<String> jComboBox;

    public MaterialGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("edit")))
            edit = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("remove")))
            remove = true;
        initComponents(functions);
    }

    public void initComponents(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBox = new JComboBox<>(new String[]{"Tất cả", "Tồn quầy dưới định mức", "Tồn kho dưới định mức", "Tồn kho vượt định mức", "Hết hàng trong quầy", "Hết hàng trong kho"});

        columnNames = new String[]{"Mã NL", "Tên nguyên liệu", "Tồn quầy", "Tồn kho", "Đơn vị", "Giá vốn"};
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

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(),
                detail, edit, remove, 6);
        dataTable.getColumnModel().getColumn(0).setMinWidth(130);
        dataTable.getColumnModel().getColumn(1).setMinWidth(350);
        dataTable.getColumnModel().getColumn(2).setMinWidth(130);
        dataTable.getColumnModel().getColumn(3).setMinWidth(130);
        dataTable.getColumnModel().getColumn(4).setMinWidth(100);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(130);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(350);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(130);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(130);
        dataTable.getColumnModel().getColumn(4).setMaxWidth(100);

        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        bottom.add(scrollPane, BorderLayout.CENTER);

        JLabel jLabelStatus = new JLabel("Tồn nguyên liệu: ");
        jLabelStatus.setFont(new Font("Lexend", Font.BOLD, 14));
        FilterDatePanel.add(jLabelStatus, BorderLayout.WEST);

        jComboBox.setBackground(new Color(1, 120, 220));
        jComboBox.setForeground(Color.white);
        jComboBox.setPreferredSize(new Dimension(200, 30));
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMaterials();
            }
        });
        FilterDatePanel.add(jComboBox, BorderLayout.WEST);

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(280, 40));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 10, 0, 10);
        SearchPanel.add(containerSearch, gbc);


        gbc.gridx++;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 10);
        SearchPanel.add(jButtonSearch, gbc);


        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(250, 30));


        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonSearch.addActionListener(e -> searchMaterials());
        SearchPanel.add(jButtonSearch);
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchMaterials();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchMaterials();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchMaterials();
            }
        });

        loadDataTable(materialBLL.getData(materialBLL.searchMaterials("deleted = 0")));
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
        });
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
                    new CheckRemainWearHouse();
                    refresh();
                }
            });
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Kiểm kho");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/clipboard-tick-svgrepo-com.svg"));
            roundedPanel.add(panel);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("add"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    new AddMaterialGUI();
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
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Nhập Excel");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/import.svg"));
            roundedPanel.add(panel);
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    File file = chooseExcelFile(null);
                    if (file != null) {
                        Pair<Boolean, String> result;
                        try {
                            result = new AddMaterialFromExcel().addMaterialFromExcel(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, result.getValue(),
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            refresh();
                        }
                    }
                    refresh();
                }
            });
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
                            result = new AddMaterialFromExcel().addMaterialFromExcel(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Thêm nguyên liệu thành công",
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
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    PDF.exportMaterialPDF(data, "src/main/resources/ExportPDF");
                    JOptionPane.showMessageDialog(null, "Xuất PDF danh sách nguyên liệu thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            roundedPanel.add(panel);
        }


    }

    public void refresh() {
        jComboBox.setSelectedIndex(0);
        jTextFieldSearch.setText("");
        loadDataTable(materialBLL.getData(materialBLL.searchMaterials("deleted = 0")));
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][6];

        for (int i = 0; i < objects.length; i++) {
            data[i][0] = objects[i][0];
            data[i][1] = objects[i][1];
            data[i][2] = String.format("%.2f", Double.parseDouble(objects[i][2].toString()));
            data[i][3] = String.format("%.2f", Double.parseDouble(objects[i][3].toString()));
            data[i][4] = objects[i][4];
            data[i][5] = objects[i][5];

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

        if (indexColumn == indexColumnDetail)
            new DetailMaterialGUI(materialBLL.searchMaterials("id = " + data[indexRow][0]).get(0));

        if (edit && indexColumn == indexColumnEdit) {
            new EditMaterialGUI(materialBLL.searchMaterials("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
            refresh();
        }

        if (indexColumn == indexColumnRemove) {
            deleteMaterial(materialBLL.searchMaterials("id = " + data[indexRow][0]).get(0));
        }
    }

    private void deleteMaterial(Material material) {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nguyên liệu cần xoá.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá nguyên liệu?", "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Pair<Boolean, String> result = materialBLL.deleteMaterial(material);
            if (result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchMaterials() {
        List<Material> materialList = materialBLL.searchMaterials();
        if (jComboBox.getSelectedIndex() == 0) {
            materialList = materialBLL.searchMaterials();
        }
        if (jComboBox.getSelectedIndex() == 1) {
            materialList = materialBLL.searchMaterials("remain < min_remain");
        }
        if (jComboBox.getSelectedIndex() == 2) {
            materialList = materialBLL.searchMaterials("remain_wearhouse < min_remain");
        }
        if (jComboBox.getSelectedIndex() == 3) {
            materialList = materialBLL.searchMaterials("remain_wearhouse > max_remain");
        }
        if (jComboBox.getSelectedIndex() == 4) {
            materialList = materialBLL.searchMaterials("remain = 0");
        }
        if (jComboBox.getSelectedIndex() == 5) {
            materialList = materialBLL.searchMaterials("remain_wearhouse = 0");
        }
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(materialBLL.getData(materialList));
        } else {
            List<Integer> materialIDList = new ArrayList<>();
            for (Material material : new MaterialBLL().findMaterials("name", jTextFieldSearch.getText()))
                materialIDList.add(material.getId());
            materialList.removeIf(material -> !materialIDList.contains(material.getId()));
            loadDataTable(materialBLL.getData(materialList));
        }
    }

}