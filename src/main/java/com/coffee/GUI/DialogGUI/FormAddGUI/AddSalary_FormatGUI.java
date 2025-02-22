package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditAllowanceGUI;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditDeductionGUI;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditStaffGUI;
import com.coffee.GUI.components.EventSwitchSelected;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.SwitchButton;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddSalary_FormatGUI extends JDialog {
    private JTextField jTextFieldName;
    private JPanel content;
    private JPanel allowancePanel;
    private JPanel deductionPanel;
    private SwitchButton switchButtonAllowance;
    private SwitchButton switchButtonDeduction;
    private int heightAllowancePanel = 150;
    private int heightDeductionPanel = 150;
    private JComboBox<String> jComboBoxAllowance;
    private JComboBox<String> jComboBoxDeduction;
    private java.util.List<Allowance> allowanceList;
    private List<Deduction> deductionList;
    private JButton buttonAddAllowance;
    private JButton buttonAddDeduction;
    public static boolean addAllowance = false;
    public static boolean addDeduction = false;

    public AddSalary_FormatGUI() {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(255, 255, 255));
        setTitle("Thêm Mẫu Lương");
        setLayout(new MigLayout("", "0[]0", "20[]10[]0"));
        setIconImage(new FlatSVGIcon("image/coffee_logo.svg").getImage());
        setSize(new Dimension(800, 700));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Cafe_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        init();
        setVisible(true);
    }

    private void init() {
        switchButtonAllowance = new SwitchButton();
        switchButtonDeduction = new SwitchButton();
        allowanceList = new ArrayList<>();
        deductionList = new ArrayList<>();

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        top.setPreferredSize(new Dimension(800, 70));
        top.setBackground(Color.WHITE);
        add(top, "wrap");

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(800, 510));
        add(scrollPane, "wrap");

        JPanel containerButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(Color.WHITE);
        containerButton.setPreferredSize(new Dimension(800, 50));
        add(containerButton, "wrap");

        JLabel jLabelName = new JLabel("Tên mẫu lương");
        jLabelName.setFont(new Font("Inter", Font.BOLD, 15));
        top.add(jLabelName);

        jTextFieldName = new MyTextFieldUnderLine();
        jTextFieldName.setPreferredSize(new Dimension(280, 50));
        jTextFieldName.setFont((new Font("Inter", Font.PLAIN, 14)));
        jTextFieldName.setBackground(new Color(245, 246, 250));
        top.add(jTextFieldName);

        content = new JPanel();
        content.setBackground(Color.white);
        content.setLayout(new MigLayout("", "0[]0", "0[]0"));
        scrollPane.setViewportView(content);

        JPanel jPanelLabelAllowance = new JPanel(new MigLayout("", "20[]0[]"));
        jPanelLabelAllowance.setBackground(Color.white);
        jPanelLabelAllowance.setPreferredSize(new Dimension(785, 50));
        content.add(jPanelLabelAllowance, "wrap");

        JLabel labelAllowance = new JLabel();
        labelAllowance.setPreferredSize(new Dimension(700, 50));
        labelAllowance.setText("Phụ cấp");
        labelAllowance.setFont((new Font("Inter", Font.BOLD, 15)));
        jPanelLabelAllowance.add(labelAllowance);

        switchButtonAllowance.addEventSelected(new EventSwitchSelected() {
            @Override
            public void onSelected(boolean selected) {
                if (selected) {
                    allowancePanel.setPreferredSize(new Dimension(785, heightAllowancePanel));
                    loadAllowance();
                    allowancePanel.setVisible(true);
                    content.repaint();
                    content.revalidate();
                }
                if (!selected) {
                    heightAllowancePanel = allowancePanel.getHeight();
                    allowancePanel.removeAll();
                    allowancePanel.setPreferredSize(new Dimension(785, 0));
                    allowancePanel.setVisible(false);
                    content.repaint();
                    content.revalidate();
                }
            }
        });
        jPanelLabelAllowance.add(switchButtonAllowance);

        allowancePanel = new JPanel();
        allowancePanel.setLayout(new MigLayout("", "20[]20"));
        allowancePanel.setBackground(Color.white);
        allowancePanel.setPreferredSize(new Dimension(785, 0));
        allowancePanel.setVisible(false);
        content.add(allowancePanel, "wrap");


        JPanel jPanelLabelDeduction = new JPanel(new MigLayout("", "20[]0[]"));
        jPanelLabelDeduction.setBackground(Color.white);
        jPanelLabelDeduction.setPreferredSize(new Dimension(785, 50));
        content.add(jPanelLabelDeduction, "wrap");

        JLabel labelDeduction = new JLabel();
        labelDeduction.setPreferredSize(new Dimension(700, 50));
        labelDeduction.setText("Giảm trừ");
        labelDeduction.setFont((new Font("Inter", Font.BOLD, 15)));
        jPanelLabelDeduction.add(labelDeduction);

        switchButtonDeduction.addEventSelected(new EventSwitchSelected() {
            @Override
            public void onSelected(boolean selected) {
                if (selected) {
                    deductionPanel.setPreferredSize(new Dimension(785, heightDeductionPanel));
                    loadDeduction();
                    deductionPanel.setVisible(true);
                    content.repaint();
                    content.revalidate();
                }
                if (!selected) {
                    heightDeductionPanel = deductionPanel.getHeight();
                    deductionPanel.removeAll();
                    deductionPanel.setPreferredSize(new Dimension(785, 0));
                    deductionPanel.setVisible(false);
                    content.repaint();
                    content.revalidate();
                }
            }
        });
        jPanelLabelDeduction.add(switchButtonDeduction);

        deductionPanel = new JPanel();
        deductionPanel.setLayout(new MigLayout("", "20[]20"));
        deductionPanel.setBackground(Color.white);
        deductionPanel.setPreferredSize(new Dimension(785, 0));
        deductionPanel.setVisible(false);
        content.add(deductionPanel, "wrap");

        JButton buttonCancel = new JButton("Huỷ");
        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Inter", Font.BOLD, 15));
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

        JButton buttonSet = new JButton("Thêm");
        buttonSet.setPreferredSize(new Dimension(100, 30));
        buttonSet.setBackground(new Color(1, 120, 220));
        buttonSet.setForeground(Color.white);
        buttonSet.setFont(new Font("Inter", Font.BOLD, 15));
        buttonSet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSalary_Format();
            }
        });
        containerButton.add(buttonSet);
    }

    private void addSalary_Format() {
        Pair<Boolean, String> result;

        if (jTextFieldName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên mẫu lương.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = new Salary_FormatBLL().getAutoID(new Salary_FormatBLL().searchSalary_Formats());
        result = new Salary_FormatBLL().addSalary_Format(new Salary_Format(id, jTextFieldName.getText()));
        if (result.getKey()) {
            for (Allowance allowance : allowanceList) {
                Salary_Format_Allowance salaryFormatAllowance = new Salary_Format_Allowance(id, allowance.getId());
                new Salary_Format_AllowanceBLL().addSalary_Format_Allowance(salaryFormatAllowance);
            }

            for (Deduction deduction : deductionList) {
                Salary_Format_Deduction salaryFormatDeduction = new Salary_Format_Deduction(id, deduction.getId());
                new Salary_Format_DeductionBLL().addSalary_Format_Deduction(salaryFormatDeduction);
            }

            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            EditStaffGUI.addSalary_Format = true;
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllowance() {
        allowancePanel.removeAll();

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(785, 30));
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
        jpanel1.add(titleAllowanceType, "left, span, wrap");
        allowancePanel.add(jpanel1, "span, wrap");

        jComboBoxAllowance = new JComboBox<>();
        jComboBoxAllowance.setPreferredSize(new Dimension(150, 30));
        jComboBoxAllowance.addItem("Chon loại phụ cấp");
        for (Allowance allowance : new AllowanceBLL().searchAllowances())
            jComboBoxAllowance.addItem(allowance.getName());

        for (Allowance allowance : allowanceList) {
            jComboBoxAllowance.removeItem(allowance.getName());

            JPanel jPanelAllowance = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
            jPanelAllowance.setBackground(Color.white);
            jPanelAllowance.setPreferredSize(new Dimension(785, 50));

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
            jPanelAllowance.add(jLabelAllowanceType);

            JLabel jLabelIconEdit = new JLabel(new FlatSVGIcon("icon/edit.svg"));
            jLabelIconEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconEdit.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setVisible(false);
                    new EditAllowanceGUI(allowance);
                    Allowance newAllowance = new AllowanceBLL().searchAllowances("id = " + allowance.getId()).get(0);
                    allowanceList.set(allowanceList.indexOf(allowance), newAllowance);
                    loadAllowance();
                    setVisible(true);
                }
            });
            jPanelAllowance.add(jLabelIconEdit);

            JLabel jLabelIconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            jLabelIconRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    allowanceList.remove(allowance);
                    heightAllowancePanel -= 50;
                    loadAllowance();
                }
            });
            jPanelAllowance.add(jLabelIconRemove, "wrap");

            allowancePanel.add(jPanelAllowance, "span, wrap");
            heightAllowancePanel += 50;
        }

        jComboBoxAllowance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jComboBoxAllowance.getSelectedIndex();
                if (selectedIndex != 0) {
                    Allowance allowance = new AllowanceBLL().searchAllowances("name = '" + jComboBoxAllowance.getSelectedItem() + "'").get(0);
                    allowanceList.add(allowance);
                    loadAllowance();
                }
            }
        });
        allowancePanel.add(jComboBoxAllowance, "span, wrap");

        buttonAddAllowance = new JButton("+ Thêm phụ cấp");
        buttonAddAllowance.setBackground(new Color(0, 182, 62));
        buttonAddAllowance.setForeground(Color.WHITE);
        buttonAddAllowance.setPreferredSize(new Dimension(150, 30));
//        buttonAddAllowance.setIcon(new FlatSVGIcon("icon/add.svg"));
        buttonAddAllowance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAddAllowance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddAllowanceGUI();
                if (addAllowance) {
                    List<Allowance> allowanceList = new AllowanceBLL().searchAllowances();
                    Allowance allowance = allowanceList.get(allowanceList.size() - 1);
                    jComboBoxAllowance.addItem(allowance.getName());
                }
                addAllowance = false;
                setVisible(true);
            }
        });
        allowancePanel.add(buttonAddAllowance, "span, wrap");

        content.repaint();
        content.revalidate();
    }

    private void loadDeduction() {
        deductionPanel.removeAll();

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(785, 30));
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

        jpanel1.add(titleDeductionName);
        jpanel1.add(titleDeductionAmount);
        jpanel1.add(titleDeductionType, "left, span, wrap");
        deductionPanel.add(jpanel1, "span, wrap");

        jComboBoxDeduction = new JComboBox<>();
        jComboBoxDeduction.setPreferredSize(new Dimension(150, 30));
        jComboBoxDeduction.addItem("Chon loại giảm trừ");
        for (Deduction deduction : new DeductionBLL().searchDeductions())
            jComboBoxDeduction.addItem(deduction.getName());

        for (Deduction deduction : deductionList) {
            jComboBoxDeduction.removeItem(deduction.getName());

            JPanel jPanelDeduction = new JPanel(new MigLayout("", "0[]10[]10[]10[]0[]0", "0[]0"));
            jPanelDeduction.setBackground(Color.white);
            jPanelDeduction.setPreferredSize(new Dimension(785, 50));

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
            jPanelDeduction.add(jLabelDeductionType);

            JLabel jLabelIconEdit = new JLabel(new FlatSVGIcon("icon/edit.svg"));
            jLabelIconEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconEdit.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setVisible(false);
                    new EditDeductionGUI(deduction);
                    Deduction newDeduction = new DeductionBLL().searchDeductions("id = " + deduction.getId()).get(0);
                    deductionList.set(deductionList.indexOf(deduction), newDeduction);
                    loadDeduction();
                    setVisible(true);
                }
            });
            jPanelDeduction.add(jLabelIconEdit);

            JLabel jLabelIconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            jLabelIconRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    deductionList.remove(deduction);
                    heightDeductionPanel -= 50;
                    loadDeduction();
                }
            });
            jPanelDeduction.add(jLabelIconRemove, "wrap");

            deductionPanel.add(jPanelDeduction, "span, wrap");
            heightDeductionPanel += 50;
        }

        jComboBoxDeduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jComboBoxDeduction.getSelectedIndex();
                if (selectedIndex != 0) {
                    Deduction deduction = new DeductionBLL().searchDeductions("name = '" + jComboBoxDeduction.getSelectedItem() + "'").get(0);
                    deductionList.add(deduction);
                    loadDeduction();
                }
            }
        });
        deductionPanel.add(jComboBoxDeduction, "span, wrap");

        buttonAddDeduction = new JButton("+ Thêm giảm trừ");
        buttonAddDeduction.setBackground(new Color(0, 182, 62));
        buttonAddDeduction.setForeground(Color.WHITE);
        buttonAddDeduction.setPreferredSize(new Dimension(150, 30));
//        buttonAddDeduction.setIcon(new FlatSVGIcon("icon/add.svg"));
        buttonAddDeduction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAddDeduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddDeductionGUI();
                if (addDeduction) {
                    List<Deduction> deductionList = new DeductionBLL().searchDeductions();
                    Deduction deduction = deductionList.get(deductionList.size() - 1);
                    jComboBoxDeduction.addItem(deduction.getName());
                }
                addDeduction = false;
                setVisible(true);
            }
        });
        deductionPanel.add(buttonAddDeduction, "span, wrap");

        content.repaint();
        content.revalidate();
    }

    public void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1)
            dispose();
    }
}
