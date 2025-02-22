package com.coffee.GUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddStaffGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.*;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditStaffGUI;
import com.coffee.GUI.components.*;
import com.coffee.ImportExcel.AddEmployeeFromExcel;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static com.coffee.utils.Resource.chooseExcelFile;

public class StaffGUI extends Layout1 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private JComboBox<String> jComboBoxSearch;
    private JComboBox<String> jComboBoxRole;
    private List<Function> functions;
    private StaffBLL staffBLL = new StaffBLL();
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

    public StaffGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("edit")))
            edit = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("remove")))
            remove = true;
        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBoxSearch = new JComboBox<>(new String[]{"Tên", "CCCD"});
        jComboBoxRole = new JComboBox<>();
        columnNames = new String[]{"Mã Nhân Viên", "CCCD", "Tên", "Số Điện Thoại", " Chức Vụ"};
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
                detail, edit, remove, 5); // table hiển thị các thuộc tính "Mã NCC", "Tên NCC", "SĐT", "Email" nên điền 4
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
        jTextFieldSearch.setPreferredSize(new Dimension(300, 30));
        containerSearch.add(jTextFieldSearch);
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchStaffs();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchStaffs();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchStaffs();
            }
        });
//        jButtonSearch.setBackground(new Color(1, 120, 220));
//        jButtonSearch.setForeground(Color.white);
//        jButtonSearch.setPreferredSize(new Dimension(100, 30));
//        jButtonSearch.addActionListener(e -> searchStaffs());
//        SearchPanel.add(jButtonSearch);

        jComboBoxSearch.setBackground(new Color(1, 120, 220));
        jComboBoxSearch.setForeground(Color.white);
        jComboBoxSearch.setPreferredSize(new Dimension(120, 30));
        jComboBoxSearch.addActionListener(e -> searchStaffs());
        SearchPanel.add(jComboBoxSearch);

        jComboBoxRole.addItem("Tất cả");
        for (Role role : new RoleBLL().searchRoles("id != 0"))
            jComboBoxRole.addItem(role.getName());

        jComboBoxRole.setBackground(new Color(1, 120, 220));
        jComboBoxRole.setForeground(Color.white);
        jComboBoxRole.setPreferredSize(new Dimension(150, 30));
        jComboBoxRole.addActionListener(e -> searchStaffs());
        SearchPanel.add(jComboBoxRole);

        loadDataTable(staffBLL.getData(staffBLL.searchStaffs("deleted = 0")));

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
                    new AddStaffGUI();
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
                        Pair<Boolean, String> result = null;
                        try {
                            result = new AddEmployeeFromExcel().addStaffFromExcel(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công",
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

    private void refresh() {
        jTextFieldSearch.setText("");
        jComboBoxSearch.setSelectedIndex(0);
        loadDataTable(staffBLL.getData(staffBLL.searchStaffs("deleted = 0")));
    }

    private void searchStaffs() {
        List<Staff> staffList = new ArrayList<>();
        if (jComboBoxRole.getSelectedIndex() == 0) {
            staffList = staffBLL.searchStaffs("deleted = 0");
        } else {
            List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByRole(jComboBoxRole.getSelectedIndex(), new SimpleDateFormat("yyyy-MM-dd").format(Date.valueOf(LocalDate.now())));
            for (Role_Detail roleDetail : role_detailList)
                staffList.add(staffBLL.findStaffsBy(Map.of("id", roleDetail.getStaff_id())).get(0));
        }
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(staffBLL.getData(staffList));
        } else {
            if (jComboBoxSearch.getSelectedIndex() == 0) {
                staffList.removeIf(staff -> !staff.getName().toLowerCase().contains(jTextFieldSearch.getText().toLowerCase()));
            } else {
                staffList.removeIf(staff -> !staff.getStaffNo().contains(jTextFieldSearch.getText()));
            }
            loadDataTable(staffBLL.getData(staffList));

        }
    }


    private void searchStaffByName() {
        String searchText = jTextFieldSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadDataTable(staffBLL.getData(staffBLL.searchStaffs("deleted = 0")));
        } else {
            List<Staff> foundStaff = staffBLL.findStaffs("name", searchText);
            if (!foundStaff.isEmpty()) {
                loadDataTable(staffBLL.getData(foundStaff));
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên có tên '" + searchText + "'", "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void searchStaffByRole() {
        if (jTextFieldSearch.getText().isEmpty()) {

            loadDataTable(staffBLL.getData(staffBLL.searchStaffs("deleted = 0")));
        } else {
            String roleName = jTextFieldSearch.getText();
            List<Role> roles = new RoleBLL().searchRoles("name = '" + roleName + "'");

            if (!roles.isEmpty()) {
                int roleId = roles.get(0).getId();
                List<Role_Detail> roleDetails = new Role_DetailBLL().searchRole_details("role_id = " + roleId);

                if (!roleDetails.isEmpty()) {
                    List<Integer> staffIds = new ArrayList<>();
                    for (Role_Detail roleDetail : roleDetails) {
                        staffIds.add(roleDetail.getStaff_id());
                    }
                    List<Staff> staffs = new ArrayList<>();
                    for (Integer staffId : staffIds) {
                        List<Staff> tempStaffs = staffBLL.searchStaffs("id = " + staffId);
                        if (!tempStaffs.isEmpty()) {
                            staffs.add(tempStaffs.get(0));
                        }
                    }

                    loadDataTable(staffBLL.getData(staffs));
                } else {
                    JOptionPane.showMessageDialog(this, "Không có nhân viên nào có chức vụ '" + roleName + "'", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chức vụ '" + roleName + "'", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
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

            int staffId = Integer.parseInt((String) data[i][0]);
            List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(staffId);
            if (!role_detailList.isEmpty()) {
                Role_Detail roleDetail = role_detailList.get(0);
                Role role = new RoleBLL().searchRoles("id = " + roleDetail.getRole_id()).get(0);
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = role.getName();
            } else {
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = "Chưa có chức vụ";
            }


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
            new DetailStaffGUI(staffBLL.searchStaffs("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá

        if (edit && indexColumn == indexColumnEdit) {
            new EditStaffGUI(staffBLL.searchStaffs("id = " + data[indexRow][0]).get(0));
//            new EditStaffGUI(staffBLL.searchStaffs("id = " + data[indexRow][0]).get(0));
            refresh();
        }
        if (remove && indexColumn == indexColumnRemove)
            deleteStaff(staffBLL.searchStaffs("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá

    }

    private void deleteStaff(Staff staff) {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xoá.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá nhà nhân viên?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Pair<Boolean, String> result = staffBLL.deleteStaff(staff);
            if (result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}



