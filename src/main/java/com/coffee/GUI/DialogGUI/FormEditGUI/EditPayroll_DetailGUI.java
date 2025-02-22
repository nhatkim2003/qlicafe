package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.Payroll_DetailBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Payroll_Detail;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.main.Cafe_Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditPayroll_DetailGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributePayroll_Detail;
    private List<JTextField> jTextFieldPayroll_Detail;
    private JComboBox<String> jComboBox;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private Payroll_DetailBLL payroll_DetailBLL = new Payroll_DetailBLL();
    private Payroll_Detail payroll_Detail;

    public EditPayroll_DetailGUI(Payroll_Detail payroll_Detail) {
        super();
        super.setTitle("Cập nhật phiếu lương nhân viên");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        this.payroll_Detail = payroll_Detail;
        init(payroll_Detail);
        setVisible(true);
    }

    private void init(Payroll_Detail payroll_Detail) {
        titleName = new JLabel();
        attributePayroll_Detail = new ArrayList<>();
        jTextFieldPayroll_Detail = new ArrayList<>();
        jComboBox = new JComboBox<>(new String[]{"Tạm tính", "Đã trả"});
        buttonCancel = new JButton("Huỷ");
        buttonEdit = new JButton("Cập nhật");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Cập nhật phiếu lương nhân viên");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Nhân viên", "Tổng giờ làm", "Tiền thưởng", "Tiền phạt", "Tổng lương", "Trạng thái"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributePayroll_Detail.add(label);
            content.add(label);

            JTextField textField = new MyTextFieldUnderLine();

            if (string.equals("Nhân viên")) {
                Staff staff = new StaffBLL().searchStaffs("id = " + payroll_Detail.getStaff_id()).get(0);
                textField.setText(staff.getName());
                textField.setEditable(false);
            }
            if (string.equals("Tổng giờ làm")) {
                textField.setText(String.valueOf(payroll_Detail.getHours_amount()));
                textField.setEditable(false);
            }
            if (string.equals("Tiền thưởng")) {
                textField.setText(String.valueOf(payroll_Detail.getBonus_amount()));
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
            }
            if (string.equals("Tiền phạt")) {
                textField.setText(String.valueOf(payroll_Detail.getDeduction_amount()));
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
            }
            if (string.equals("Tổng lương")) {
                textField.setText(String.valueOf(payroll_Detail.getSalary_amount()));
                textField.setEditable(false);
            }
            if (string.equals("Trạng thái")) {
                jComboBox.setPreferredSize(new Dimension(1000, 30));
                jComboBox.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                jComboBox.setBackground(new Color(245, 246, 250));

                if (payroll_Detail.isStatus())
                    jComboBox.setSelectedIndex(1);
                else
                    jComboBox.setSelectedIndex(0);

                content.add(jComboBox, "wrap");
                continue;
            }

            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldPayroll_Detail.add(textField);
            content.add(textField, "wrap");

        }
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

        buttonEdit.setPreferredSize(new Dimension(100, 30));
        buttonEdit.setBackground(new Color(1, 120, 220));
        buttonEdit.setForeground(Color.white);
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editPayroll_Detail();
            }
        });
        containerButton.add(buttonEdit);
    }

    private void editPayroll_Detail() {
//        Pair<Boolean, String> result;
//        int id;
//        String name, phone, address, email;
//
//        id = payroll_Detail.getId();
//        name = jTextFieldPayroll_Detail.get(0).getText();
//        phone = jTextFieldPayroll_Detail.get(1).getText();
//        address = jTextFieldPayroll_Detail.get(2).getText();
//        email = jTextFieldPayroll_Detail.get(3).getText();
//
//        Payroll_Detail newsPayroll_Detail = new Payroll_Detail(id, name, phone, address, email, false); // false là tồn tại, true là đã xoá
//
//        result = payroll_DetailBLL.updatePayroll_Detail(payroll_Detail, newsPayroll_Detail);
//
//        if (result.getKey()) {
//            JOptionPane.showMessageDialog(null, result.getValue(),
//                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            dispose();
//        } else {
//            JOptionPane.showMessageDialog(null, result.getValue(),
//                    "Thông báo", JOptionPane.ERROR_MESSAGE);
//        }
    }

}
