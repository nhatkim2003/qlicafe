package com.coffee.GUI;

import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Account;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddLeave_Of_Absence_FormGUI;
import com.coffee.GUI.components.InfoPanel;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.swing.MyTextField;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoGUI extends InfoPanel {
    private JLabel jLabelAccount;
    private JLabel jLabelStaff;
    private List<JTextField> jTextFieldsAccount;
    private List<JTextField> jTextFieldsStaff;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private JButton buttonOffDay;
    private Account account;
    private Staff staff;

    public InfoGUI(Account account, Staff staff) {
        super();
        this.account = account;
        this.staff = staff;
        init();
    }

    private void init() {
        jLabelAccount = new JLabel("    Thông Tin Đăng Nhập");
        jLabelStaff = new JLabel("  Thông Tin Cá Nhân");
        jTextFieldsAccount = new ArrayList<>();
        jTextFieldsStaff = new ArrayList<>();
        buttonEdit = new JButton("Lưu");
        buttonCancel = new JButton("Huỷ");
        buttonOffDay = new JButton("Tạo đơn nghỉ phép");

        jLabelAccount.setFont(new Font("Lexend", Font.BOLD, 18));
        TitleInfoAccount.add(jLabelAccount, BorderLayout.WEST);

        jLabelStaff.setFont(new Font("Lexend", Font.BOLD, 18));
        TitleInfoStaff.add(jLabelStaff, BorderLayout.WEST);

        JLabel jLabelUsername = new JLabel("Tên đăng nhập");
        jLabelUsername.setFont(new Font("Lexend", Font.BOLD, 16));
        InfoAccountPanel.add(jLabelUsername);

        JTextField textFieldUsername = new JTextField();
        textFieldUsername.setText(account.getUsername());
        textFieldUsername.setPreferredSize(new Dimension(300, 45));
        textFieldUsername.setFont(new Font("Lexend", Font.PLAIN, 14));
        jTextFieldsAccount.add(textFieldUsername);
        InfoAccountPanel.add(textFieldUsername, "wrap");

        JLabel jLabelPassword = new JLabel("Mật khẩu");
        jLabelPassword.setFont(new Font("Lexend", Font.BOLD, 16));
        InfoAccountPanel.add(jLabelPassword);

        JTextField jPasswordField = new JTextField("⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫");
        jPasswordField.setPreferredSize(new Dimension(300, 45));
        jPasswordField.setFont(new Font("Lexend", Font.BOLD, 14));
        jPasswordField.setEditable(false);
        InfoAccountPanel.add(jPasswordField);

        JLabel iconChangePasswd = new JLabel(new FlatSVGIcon("icon/edit.svg"));
        iconChangePasswd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconChangePasswd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new ChangePasswordGUI().setVisible(true);
            }
        });
        InfoAccountPanel.add(iconChangePasswd, "wrap");

        for (String string : new String[]{"Tên Nhân Viên", "CCCD", "Giới Tính", "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Email"}) {
            JLabel label = new JLabel();
            label.setText(string);
            label.setFont(new Font("Lexend", Font.BOLD, 16));
            InfoStaffPanel.add(label);

            JTextField textField = new MyTextFieldUnderLine();
            if (string.equals("Tên Nhân Viên")) {
                JLabel jLabel = new JLabel(staff.getName());
                jLabel.setFont(new Font("Lexend", Font.PLAIN, 14));
                InfoStaffPanel.add(jLabel, "wrap");
                continue;
            }
            if (string.equals("CCCD")) {
                JLabel jLabel = new JLabel(staff.getStaffNo());
                jLabel.setFont(new Font("Lexend", Font.PLAIN, 14));
                InfoStaffPanel.add(jLabel, "wrap");
                continue;
            }
            if (string.equals("Giới Tính")) {
                boolean gender = staff.isGender();
                String gender1 = !gender ? "Nam" : "Nữ";
                JLabel jLabel = new JLabel(gender1);
                jLabel.setFont(new Font("Lexend", Font.PLAIN, 14));
                InfoStaffPanel.add(jLabel, "wrap");
                continue;
            }

            if (string.equals("Ngày Sinh")) {
                Date birthDate = staff.getBirthdate();
                JLabel jLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(birthDate));
                jLabel.setFont(new Font("Lexend", Font.PLAIN, 14));
                InfoStaffPanel.add(jLabel, "wrap");
                continue;
            }
            if (string.equals("Số Điện Thoại")) {
                textField.setText(staff.getPhone());
            }
            if (string.equals("Địa Chỉ")) {
                textField.setText(staff.getAddress());
            }
            if (string.equals("Email")) {
                textField.setText(staff.getEmail());
            }
            textField.setPreferredSize(new Dimension(300, 45));
            textField.setFont(new Font("Lexend", Font.PLAIN, 14));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldsStaff.add(textField);
            InfoStaffPanel.add(textField, "wrap");
        }

        buttonCancel.setPreferredSize(new Dimension(150, 30));
        buttonCancel.setFont(new Font("Lexend", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }
        });
        InfoStaffPanel.add(buttonCancel);

        buttonEdit.setPreferredSize(new Dimension(150, 30));
        buttonEdit.setBackground(new Color(1, 120, 220));
        buttonEdit.setForeground(Color.white);
        buttonEdit.setFont(new Font("Lexend", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editStaff();
            }
        });
        InfoStaffPanel.add(buttonEdit, "span, wrap");

        buttonOffDay.setPreferredSize(new Dimension(150, 30));
        buttonOffDay.setBackground(new Color(1, 120, 220));
        buttonOffDay.setForeground(Color.white);
        buttonOffDay.setFont(new Font("Lexend", Font.BOLD, 15));
        buttonOffDay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonOffDay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new AddLeave_Of_Absence_FormGUI(staff);
            }
        });
        InfoStaffPanel.add(buttonOffDay, "span, wrap");
    }

    private void editStaff() {
        Pair<Boolean, String> result;
        String phone, address, email;

        phone = jTextFieldsStaff.get(0).getText();
        address = jTextFieldsStaff.get(1).getText();
        email = jTextFieldsStaff.get(2).getText();

        Staff newstaff = new Staff(staff.getId(), staff.getStaffNo(), staff.getName(), staff.isGender(), staff.getBirthdate(), phone, address, email, false, staff.getSalary_format_id()); // false là tồn tại, true là đã xoá

        result = new StaffBLL().updateStaff(staff, newstaff);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            staff = newstaff;
            refresh();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refresh() {
        jTextFieldsStaff.get(0).setText(staff.getPhone());
        jTextFieldsStaff.get(1).setText(staff.getAddress());
        jTextFieldsStaff.get(2).setText(staff.getEmail());
    }
}
