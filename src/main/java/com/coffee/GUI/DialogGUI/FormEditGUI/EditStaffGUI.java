package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.CreateWorkScheduleGUI;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddRoleGUI;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddSalary_FormatGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailPayroll_DetailGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailStaffGUI;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class EditStaffGUI extends JDialog {
    public static boolean changeRole = false;
    private Staff staff;
    private JTabbedPane jTabbedPane;
    private JPanel infoPanel;
    private JPanel rolePanel;
    private JPanel configSalaryPanel;
    private JPanel payrollPanel;
    private JScrollPane scrollPane;
    private JComboBox<String> jComboBoxSalary_format = new JComboBox<>();
    public static boolean addSalary_Format = false;
    private DataTable dataTable;
    private Object[][] data;

    public EditStaffGUI(Staff staff) {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(191, 198, 208));
        setTitle("Cập Nhật Nhân Viên");
        setLayout(new BorderLayout());
        setIconImage(new FlatSVGIcon("image/coffee_logo.svg").getImage());
        setSize(new Dimension(800, 500));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Cafe_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });

        this.staff = staff;
        init(staff);
        setVisible(true);
    }

    private void init(Staff staff) {
        jTabbedPane = new JTabbedPane();
        infoPanel = new JPanel();
        rolePanel = new JPanel();
        configSalaryPanel = new JPanel();
        payrollPanel = new JPanel();

        jTabbedPane.setPreferredSize(new Dimension(800, 500));
        jTabbedPane.setBackground(Color.white);

        jTabbedPane.addTab("Thông tin", infoPanel);
        jTabbedPane.addTab("Chức vụ", rolePanel);
        jTabbedPane.addTab("Thiết lập lương", configSalaryPanel);
        jTabbedPane.addTab("Phiếu lương", payrollPanel);
        add(jTabbedPane, BorderLayout.CENTER);


        infoPanel.setBackground(new Color(255, 255, 255));
        infoPanel.setLayout(new MigLayout("", "0[]0", "0[]10[]0"));

        rolePanel.setBackground(new Color(255, 255, 255));
        rolePanel.setLayout(new MigLayout("", "0[]0", "0[]10[]0"));

        configSalaryPanel.setBackground(Color.white);
        configSalaryPanel.setLayout(new MigLayout());

        payrollPanel.setBackground(new Color(255, 255, 255));
        payrollPanel.setLayout(new BorderLayout());

        initInfoPanel();
        initRolePanel();
        initConfigSalaryPanel();
        initPayrollPanel();
    }

    private void initPayrollPanel() {
        dataTable = new DataTable(new Object[0][0], new String[]{"Mã Phiếu", "Kỳ Làm Việc", "Thực Lãnh", "Trạng thái", "Xem"}, e -> selectFunction(), true, false, false, 4);
        JScrollPane scrollPane1 = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setPreferredSize(new Dimension(800, 400));

        payrollPanel.add(scrollPane1, BorderLayout.CENTER);

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        Object[][] objects = new Payroll_DetailBLL().getData(new Payroll_DetailBLL().findPayroll_DetailsBy(Map.of("staff_id", staff.getId())));
        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][5];

        for (int i = 0; i < objects.length; i++) {
            data[i][0] = objects[i][0];
            Payroll payroll = new PayrollBLL().findPayrollsBy(Map.of("id", Integer.parseInt(objects[i][0].toString()))).get(0);
            data[i][1] = payroll.getMonth() + "/" + payroll.getYear();
            data[i][2] = objects[i][3];

            data[i][3] = Boolean.parseBoolean(objects[i][4].toString()) ? "Đã trả" : "Tạm tính";
            JLabel iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
            data[i][4] = iconDetail;
        }

        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (indexColumn == 4)
            new DetailPayroll_DetailGUI(new Payroll_DetailBLL().searchPayroll_Details("payroll_id = " + data[indexRow][0], "staff_id = " + staff.getId()).get(0), new PayrollBLL().searchPayrolls("id = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá

    }

    private void initConfigSalaryPanel() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setPreferredSize(new Dimension(800, 350));
        configSalaryPanel.add(content, "wrap");

        JPanel containerButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(Color.WHITE);
        containerButton.setPreferredSize(new Dimension(800, 50));
        configSalaryPanel.add(containerButton, "wrap");

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(800, 350));
        scrollPane.setBackground(Color.gray);

        content.add(scrollPane, BorderLayout.CENTER);

        loadSalary_format();

        JButton buttonCancel = new JButton("Huỷ");
        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        JButton buttonSet = new JButton("Thiết lập");
        buttonSet.setPreferredSize(new Dimension(100, 30));
        buttonSet.setBackground(new Color(1, 120, 220));
        buttonSet.setForeground(Color.white);
        buttonSet.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonSet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (staff.getSalary_format_id() == 0) {
                    String[] options = new String[]{"Huỷ", "Xác nhận"};
                    int choice = JOptionPane.showOptionDialog(null, "Nhân viên chưa được thiết lập lương?",
                            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
                    if (choice == 0)
                        return;
                }
                Pair<Boolean, String> result;
                result = new StaffBLL().updateStaff1(staff);
                if (result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        containerButton.add(buttonSet);
    }

    private void loadSalary_format() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);

        if (staff.getSalary_format_id() == 0) {
//            panel.setPreferredSize(new Dimension(750, 300));
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

            JButton buttonAddSalary_format = new JButton("Tạo mới mẫu lương");
            buttonAddSalary_format.setBackground(new Color(0, 182, 62));
            buttonAddSalary_format.setForeground(Color.WHITE);
            buttonAddSalary_format.setPreferredSize(new Dimension(200, 40));
            buttonAddSalary_format.setIcon(new FlatSVGIcon("icon/add.svg"));
            buttonAddSalary_format.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonAddSalary_format.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AddSalary_FormatGUI();
                    if (addSalary_Format) {
                        List<Salary_Format> salaryFormatList = new Salary_FormatBLL().searchSalary_Formats("id != 0");
                        Salary_Format allowance = salaryFormatList.get(salaryFormatList.size() - 1);
                        jComboBoxSalary_format.addItem(allowance.getName());
                    }
                    addSalary_Format = false;
                }
            });
            panel.add(buttonAddSalary_format);

            panel.add(new JLabel("   Hoặc   "));

            jComboBoxSalary_format = new JComboBox<>();
            jComboBoxSalary_format.addItem("Chọn mẫu lương hiện có");
            jComboBoxSalary_format.setSelectedIndex(0);
            for (Salary_Format salaryFormat : new Salary_FormatBLL().searchSalary_Formats("id != 0")) {
                jComboBoxSalary_format.addItem(salaryFormat.getName());
            }
            jComboBoxSalary_format.setPreferredSize(new Dimension(200, 40));
            jComboBoxSalary_format.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jComboBoxSalary_format.getSelectedIndex() == 0) {
                        staff.setSalary_format_id(0);
                    } else {
                        staff.setSalary_format_id(jComboBoxSalary_format.getSelectedIndex());
                    }
                    loadSalary_format();
                }
            });
            panel.add(jComboBoxSalary_format);

        } else {
            jComboBoxSalary_format = new JComboBox<>();
            jComboBoxSalary_format.addItem("Chọn mẫu lương hiện có");
            for (Salary_Format salaryFormat : new Salary_FormatBLL().searchSalary_Formats("id != 0")) {
                jComboBoxSalary_format.addItem(salaryFormat.getName());
            }
            Salary_Format salaryFormat = new Salary_FormatBLL().searchSalary_Formats("id = " + staff.getSalary_format_id()).get(0);
            jComboBoxSalary_format.setPreferredSize(new Dimension(200, 40));
            jComboBoxSalary_format.setSelectedItem(salaryFormat.getName());
            jComboBoxSalary_format.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jComboBoxSalary_format.getSelectedIndex() == 0) {
                        staff.setSalary_format_id(0);
                    } else {
                        staff.setSalary_format_id(jComboBoxSalary_format.getSelectedIndex());
                    }
                    loadSalary_format();
                }
            });

            panel.setLayout(new MigLayout("", "10[]10", "10[]10"));

            JPanel jpanel3 = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
            jpanel3.setBackground(new Color(255, 255, 255));
            jpanel3.setPreferredSize(new Dimension(730, 30));
            jpanel3.add(jComboBoxSalary_format);

            JLabel jLabelIconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            jLabelIconRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    String[] options = new String[]{"Huỷ", "Xác nhận"};
                    int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá mẫu lương \"" + salaryFormat.getName() + "\"",
                            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
                    if (choice == 1) {
                        Pair<Boolean, String> result;

                        staff.setSalary_format_id(0);
                        new StaffBLL().updateStaff1(staff);

                        result = new Salary_FormatBLL().deleteSalary_Format(salaryFormat);
                        if (result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(),
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            loadSalary_format();
                        } else {
                            JOptionPane.showMessageDialog(null, result.getValue(),
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
            });
            jpanel3.add(jLabelIconRemove, "wrap");
            panel.add(jpanel3, "wrap");


            JLabel labelAllowance = new JLabel();
            labelAllowance.setPreferredSize(new Dimension(730, 50));
            labelAllowance.setText("Phụ cấp");
            labelAllowance.setFont((new Font("Inter", Font.BOLD, 15)));
            panel.add(labelAllowance, "wrap");

            JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
            jpanel1.setBackground(new Color(227, 242, 250));
            jpanel1.setPreferredSize(new Dimension(730, 30));

            JLabel titleAllowanceName = new JLabel();
            JLabel titleAllowanceAmount = new JLabel();
            JLabel titleAllowanceType = new JLabel();

            titleAllowanceName.setText("Tên phụ cấp");
            titleAllowanceAmount.setText("Số tiền phụ cấp");
            titleAllowanceType.setText("Loại phụ cấp");

            titleAllowanceName.setPreferredSize(new Dimension(200, 30));
            titleAllowanceName.setFont((new Font("Inter", Font.BOLD, 13)));

            titleAllowanceAmount.setPreferredSize(new Dimension(200, 30));
            titleAllowanceAmount.setFont((new Font("Inter", Font.BOLD, 13)));

            titleAllowanceType.setPreferredSize(new Dimension(200, 30));
            titleAllowanceType.setFont((new Font("Inter", Font.BOLD, 13)));
            jpanel1.add(titleAllowanceName);
            jpanel1.add(titleAllowanceAmount);
            jpanel1.add(titleAllowanceType, "wrap");
            panel.add(jpanel1, "wrap");


            List<Allowance> allowanceList = new ArrayList<>();
            for (Salary_Format_Allowance salaryFormatAllowance : new Salary_Format_AllowanceBLL().searchSalary_Format_Allowances("salary_format_id = " + salaryFormat.getId())) {
                allowanceList.add(new AllowanceBLL().searchAllowances("id = " + salaryFormatAllowance.getAllowance_id()).get(0));
            }

            for (Allowance allowance : allowanceList) {
                JPanel jPanelAllowance = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
                jPanelAllowance.setBackground(new Color(255, 255, 255));
                jPanelAllowance.setPreferredSize(new Dimension(730, 50));

                JTextField jLabelAllowanceName = new MyTextFieldUnderLine();
                jLabelAllowanceName.setText(allowance.getName());
                jLabelAllowanceName.setPreferredSize(new Dimension(200, 30));
                jLabelAllowanceName.setFont((new Font("Inter", Font.PLAIN, 13)));
                jLabelAllowanceName.setEditable(false);
                jPanelAllowance.add(jLabelAllowanceName);

                JTextField jLabelAllowanceAmount = new MyTextFieldUnderLine();
                jLabelAllowanceAmount.setText((VNString.currency(allowance.getAllowance_amount())));
                jLabelAllowanceAmount.setPreferredSize(new Dimension(200, 30));
                jLabelAllowanceAmount.setFont((new Font("Inter", Font.PLAIN, 13)));
                jLabelAllowanceAmount.setEditable(false);
                jPanelAllowance.add(jLabelAllowanceAmount);

                JTextField jLabelAllowanceType = new MyTextFieldUnderLine();
                if (allowance.getAllowance_type() == 0)
                    jLabelAllowanceType.setText("Phụ cấp theo ngày làm");
                if (allowance.getAllowance_type() == 1)
                    jLabelAllowanceType.setText("Phụ cấp theo tháng làm");
                jLabelAllowanceType.setPreferredSize(new Dimension(250, 30));
                jLabelAllowanceType.setFont((new Font("Inter", Font.PLAIN, 13)));
                jLabelAllowanceType.setEditable(false);
                jPanelAllowance.add(jLabelAllowanceType, "wrap");

                panel.add(jPanelAllowance, "wrap");
            }

            JLabel labelDeduction = new JLabel();
            labelDeduction.setPreferredSize(new Dimension(730, 50));
            labelDeduction.setText("Giảm trừ");
            labelDeduction.setFont((new Font("Inter", Font.BOLD, 15)));
            panel.add(labelDeduction, "wrap");

            JPanel jpanel2 = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
            jpanel2.setBackground(new Color(227, 242, 250));
            jpanel2.setPreferredSize(new Dimension(730, 30));

            JLabel titleDeductionName = new JLabel();
            JLabel titleDeductionAmount = new JLabel();
            JLabel titleDeductionType = new JLabel();

            titleDeductionName.setText("Tên giảm trừ");
            titleDeductionAmount.setText("Số tiền giảm trừ");
            titleDeductionType.setText("Loại giảm trừ");

            titleDeductionName.setPreferredSize(new Dimension(200, 30));
            titleDeductionName.setFont((new Font("Inter", Font.BOLD, 13)));

            titleDeductionAmount.setPreferredSize(new Dimension(200, 30));
            titleDeductionAmount.setFont((new Font("Inter", Font.BOLD, 13)));

            titleDeductionType.setPreferredSize(new Dimension(200, 30));
            titleDeductionType.setFont((new Font("Inter", Font.BOLD, 13)));
            jpanel2.add(titleDeductionName);
            jpanel2.add(titleDeductionAmount);
            jpanel2.add(titleDeductionType, "wrap");
            panel.add(jpanel2, "wrap");


            List<Deduction> deductionList = new ArrayList<>();
            for (Salary_Format_Deduction salaryFormatDeduction : new Salary_Format_DeductionBLL().searchSalary_Format_Deductions("salary_format_id = " + salaryFormat.getId())) {
                deductionList.add(new DeductionBLL().searchDeductions("id = " + salaryFormatDeduction.getDeduction_id()).get(0));
            }

            for (Deduction deduction : deductionList) {
                JPanel jPanelDeduction = new JPanel(new MigLayout("", "0[]10[]10[]0", "0[]0"));
                jPanelDeduction.setBackground(new Color(255, 255, 255));
                jPanelDeduction.setPreferredSize(new Dimension(730, 50));

                JTextField jLabelDeductionName = new MyTextFieldUnderLine();
                jLabelDeductionName.setText(deduction.getName());
                jLabelDeductionName.setPreferredSize(new Dimension(200, 30));
                jLabelDeductionName.setFont((new Font("Inter", Font.PLAIN, 13)));
                jLabelDeductionName.setEditable(false);
                jPanelDeduction.add(jLabelDeductionName);

                JTextField jLabelDeductionAmount = new MyTextFieldUnderLine();
                jLabelDeductionAmount.setText((VNString.currency(deduction.getDeduction_amount())));
                jLabelDeductionAmount.setPreferredSize(new Dimension(200, 30));
                jLabelDeductionAmount.setFont((new Font("Inter", Font.PLAIN, 13)));
                jLabelDeductionAmount.setEditable(false);
                jPanelDeduction.add(jLabelDeductionAmount);

                JTextField jLabelDeductionType = new MyTextFieldUnderLine();
                if (deduction.getDeduction_type() == 0)
                    jLabelDeductionType.setText("Giảm trừ đi muộn");
                if (deduction.getDeduction_type() == 1)
                    jLabelDeductionType.setText("Giảm trừ về sớm");
                if (deduction.getDeduction_type() == 2)
                    jLabelDeductionType.setText("Giảm trừ cố định");
                if (deduction.getDeduction_type() == 3)
                    jLabelDeductionType.setText("Giảm trừ nghỉ làm");
                jLabelDeductionType.setPreferredSize(new Dimension(250, 30));
                jLabelDeductionType.setFont((new Font("Inter", Font.PLAIN, 13)));
                jLabelDeductionType.setEditable(false);
                jPanelDeduction.add(jLabelDeductionType, "wrap");

                panel.add(jPanelDeduction, "wrap");
            }
        }


        scrollPane.setViewportView(panel);
    }

    private void initRolePanel() {
        JPanel content = new JPanel(new MigLayout("", "150[]0[]0[]100", "50[]"));
        content.setBackground(Color.WHITE);
        content.setPreferredSize(new Dimension(800, 350));
        rolePanel.add(content, "wrap");

        JPanel containerButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(Color.WHITE);
        containerButton.setPreferredSize(new Dimension(800, 50));
        rolePanel.add(containerButton, "wrap");

        List<JLabel> attributeRole_detail = new ArrayList<>();
        JComboBox<String> jComboBoxRole = new JComboBox<>();
        JComboBox<String> jComboBoxTypeSalary = new JComboBox<>();
        JTextField textFieldSalary = new MyTextFieldUnderLine();
        JLabel jLabelTypeSalary = new JLabel("          ");

        List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
        Role_Detail roleDetail;
        if (!role_detailList.isEmpty()) {
            roleDetail = role_detailList.get(0);
        } else {
            roleDetail = null;
        }


        for (String string : new String[]{"Chức vụ", "Loại lương", ""}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeRole_detail.add(label);
            content.add(label);

            if (string.equals("Chức vụ")) {
                for (Role role : new RoleBLL().searchRoles("id > 1"))
                    jComboBoxRole.addItem(role.getName());

                if (roleDetail != null) {
                    Role role = new RoleBLL().searchRoles("id = " + roleDetail.getRole_id()).get(0);
                    jComboBoxRole.setSelectedItem(role.getName());
                }

                jComboBoxRole.setPreferredSize(new Dimension(200, 30));
                jComboBoxRole.setFont((new Font("Public Sans", Font.PLAIN, 14)));


                content.add(jComboBoxRole);

                JButton btnAddRole = new JButton();
                ImageIcon icon = new FlatSVGIcon("icon/add.svg");
                Image image = icon.getImage();
                Image newImg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                btnAddRole.setIcon(icon);
                btnAddRole.setPreferredSize(new Dimension(30, 30));
                btnAddRole.setBackground(new Color(0, 182, 62));
                btnAddRole.setFont(new Font("Public Sans", Font.BOLD, 16));
                btnAddRole.setForeground(Color.WHITE);
                btnAddRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnAddRole.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        new AddRoleGUI();
                        jComboBoxRole.removeAllItems();
                        for (Role role : new RoleBLL().searchRoles("id > 1"))
                            jComboBoxRole.addItem(role.getName());
                    }
                });
                content.add(btnAddRole, "wrap");

            }

            if (string.equals("Loại lương")) {
                jComboBoxTypeSalary.addItem("-- Chọn loại lương --");
                jComboBoxTypeSalary.addItem("1. Cố định");
                jComboBoxTypeSalary.addItem("2. Theo giờ làm việc");

                if (roleDetail != null) {
                    if (roleDetail.getType_salary() == 1)
                        jComboBoxTypeSalary.setSelectedIndex(1);

                    else if (roleDetail.getType_salary() == 2)
                        jComboBoxTypeSalary.setSelectedIndex(2);
                } else
                    jComboBoxTypeSalary.setSelectedIndex(0);

                jComboBoxTypeSalary.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jComboBoxTypeSalary.getSelectedIndex() == 0) {
                            attributeRole_detail.get(attributeRole_detail.size() - 1).setText("");
                            textFieldSalary.setVisible(false);
                            jLabelTypeSalary.setText("         ");
                        } else {
                            attributeRole_detail.get(attributeRole_detail.size() - 1).setText("Mức lương");
                            textFieldSalary.setVisible(true);

                            if (jComboBoxTypeSalary.getSelectedIndex() == 1)
                                jLabelTypeSalary.setText("/kỳ lương");

                            if (jComboBoxTypeSalary.getSelectedIndex() == 2)
                                jLabelTypeSalary.setText("/giờ");
                        }
                    }
                });

                jComboBoxTypeSalary.setPreferredSize(new Dimension(200, 30));
                jComboBoxTypeSalary.setFont((new Font("Public Sans", Font.PLAIN, 14)));

                content.add(jComboBoxTypeSalary, "wrap");
            }

            if (string.isEmpty()) {
                textFieldSalary.setPreferredSize(new Dimension(200, 30));
                textFieldSalary.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                if (roleDetail != null) {
                    textFieldSalary.setText(VNString.currency(roleDetail.getSalary()));
                }
                textFieldSalary.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
                content.add(textFieldSalary);
            }

        }
        jLabelTypeSalary.setFont((new Font("Public Sans", Font.PLAIN, 13)));
        content.add(jLabelTypeSalary, "wrap");

        if (jComboBoxTypeSalary.getSelectedIndex() == 0) {
            attributeRole_detail.get(attributeRole_detail.size() - 1).setText("");
            textFieldSalary.setVisible(false);
            jLabelTypeSalary.setText("         ");
        } else {
            attributeRole_detail.get(attributeRole_detail.size() - 1).setText("Mức lương");
            textFieldSalary.setVisible(true);

            if (jComboBoxTypeSalary.getSelectedIndex() == 1)
                jLabelTypeSalary.setText("/kỳ lương");

            if (jComboBoxTypeSalary.getSelectedIndex() == 2)
                jLabelTypeSalary.setText("/giờ");
        }

        JButton buttonCancel = new JButton("Huỷ");
        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        JButton buttonSet = new JButton("Cập nhật");
        buttonSet.setPreferredSize(new Dimension(100, 30));
        buttonSet.setBackground(new Color(1, 120, 220));
        buttonSet.setForeground(Color.white);
        buttonSet.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonSet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSet.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateRole_detail();
            }

            private void updateRole_detail() {
                Pair<Boolean, String> result;
                int role_id, staff_id, type_salary;
                LocalDateTime entry_date;
                double salary;

                role_id = jComboBoxRole.getSelectedIndex() + 2;
                if (roleDetail != null) {
                    if (role_id != roleDetail.getRole_id()) {
                        LocalDate currentDate = LocalDate.now();
                        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
                        if (!firstDayOfMonth.equals(LocalDate.now())) {
                            Role role = new RoleBLL().searchRoles("id = " + roleDetail.getRole_id()).get(0);
                            jComboBoxRole.setSelectedItem(role.getName());
                            JOptionPane.showMessageDialog(null, "Vui lòng thay đổi chức vụ vào ngày đầu tiên của tháng.",
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        changeRole = true;
                    }
                } else {
                    changeRole = true;
                }

                staff_id = staff.getId();
                entry_date = LocalDateTime.now();
                type_salary = jComboBoxTypeSalary.getSelectedIndex();
                salary = Double.parseDouble(textFieldSalary.getText().replaceAll("\\.", "").split("₫")[0]);
                Role_Detail role_detail = new Role_Detail(role_id, staff_id, entry_date, salary, type_salary); // false là tồn tại, true là đã xoá

                result = new Role_DetailBLL().addRole_detail(role_detail);

                if (result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    if (changeRole && staff.getId() == HomeGUI.staff.getId()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập lại.",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        Cafe_Application.homeGUI.dispose();
                        System.gc();
                        Cafe_Application.loginGUI.setVisible(true);
                        return;
                    }
                    if (Cafe_Application.homeGUI.indexModuleCreateWorkScheduleGUI != -1) {
                        CreateWorkScheduleGUI createWorkScheduleGUI = (CreateWorkScheduleGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleCreateWorkScheduleGUI];
                        createWorkScheduleGUI.refresh();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        containerButton.add(buttonSet);
    }

    private void initInfoPanel() {
        JPanel content = new JPanel(new MigLayout("", "20[]10[]20", "50[]"));
        content.setBackground(Color.WHITE);
        content.setPreferredSize(new Dimension(800, 350));
        infoPanel.add(content, "wrap");

        JPanel containerButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(Color.WHITE);
        containerButton.setPreferredSize(new Dimension(800, 50));
        infoPanel.add(containerButton, "wrap");

        List<JTextField> jTextFieldsStaff = new ArrayList<>();

        for (String string : new String[]{"Mã Nhân Viên", "Tên Nhân Viên", "CCCD", "Giới Tính",
                "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Email"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            content.add(label);
            JTextField textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));

            if (string.trim().equals("Ngày Sinh")) {
                textField.setText(new SimpleDateFormat("dd/MM/yyyy").format(staff.getBirthdate()));
                textField.setEditable(false);
                content.add(textField);
                continue;
            } else {
                if (string.trim().equals("Mã Nhân Viên")) {
                    String staffId = Integer.toString(staff.getId());
                    textField.setText(staffId);
                    textField.setEditable(false);
                    content.add(textField);
                }
                if (string.trim().equals("Tên Nhân Viên")) {
                    textField.setText(staff.getName());
                    textField.setEditable(false);
                    content.add(textField, "wrap");
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(staff.getStaffNo());
                    textField.setEditable(false);
                    content.add(textField);
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = staff.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                    textField.setEditable(false);
                    content.add(textField, "wrap");
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(staff.getPhone());
                    content.add(textField, "wrap");
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(staff.getAddress());
                    content.add(textField);
                }
                if (string.trim().equals("Email")) {
                    textField.setText(staff.getEmail());
                    content.add(textField, "wrap");
                }
            }
            jTextFieldsStaff.add(textField);
        }

        JButton buttonCancel = new JButton("Huỷ");
        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        JButton buttonEdit = new JButton("Cập nhật");
        buttonEdit.setPreferredSize(new Dimension(100, 30));
        buttonEdit.setBackground(new Color(1, 120, 220));
        buttonEdit.setForeground(Color.white);
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Pair<Boolean, String> result;
                int id;
                String staffNo, name, phone, address, email;
                boolean gender;
                Date birthdate;
                id = staff.getId();
                staffNo = staff.getStaffNo();
                name = staff.getName();
                gender = staff.isGender(); // Chuyển đổi giá trị boolean từ text field
                birthdate = staff.getBirthdate(); // Lấy ngày tháng từ JDateChooser


                phone = jTextFieldsStaff.get(4).getText().trim();
                address = jTextFieldsStaff.get(5).getText().trim();
                email = jTextFieldsStaff.get(6).getText().trim();


                Staff newStaff = new Staff(id, staffNo, name, gender, birthdate, phone, address, email, false, staff.getSalary_format_id());
                // false là tồn tại, true là đã xoá

                result = new StaffBLL().updateStaff(staff, newStaff);

                if (result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        containerButton.add(buttonEdit);
    }

    public void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1)
            dispose();
    }
}
