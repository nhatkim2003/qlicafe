package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.AccountBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Account;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.AutocompleteJComboBox;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.StringSearchable;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.main.Cafe_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddAccountGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeAccount;
    private List<JTextField> jTextFieldAccount;
    private AutocompleteJComboBox combo;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private StaffBLL staffBLL = new StaffBLL();
    private AccountBLL accountBLL = new AccountBLL();
    private List<String> staffList;

    public AddAccountGUI() {
        super();
        super.setTitle("Thêm tài khoản");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
        setVisible(true);

    }

    private void init() {
        titleName = new JLabel();
        attributeAccount = new ArrayList<>();
        jTextFieldAccount = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        staffList = new ArrayList<String>();
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Thêm tài khoản");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Tên Đăng Nhập", "Nhân Viên"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeAccount.add(label);
            content.add(label);


            if (string.equals("Nhân Viên")) {
                for (Staff staff : staffBLL.searchStaffs("deleted = 0")) {
                    staffList.add(String.valueOf(staff.getId()));
                }

                for (Account account : accountBLL.searchAccounts())
                    staffList.remove(String.valueOf(account.getStaff_id()));

                staffList.replaceAll(s -> staffBLL.findStaffsBy(Map.of("id", Integer.valueOf(s))).get(0).getName() + " - " + s);
                StringSearchable searchable = new StringSearchable(staffList);

                combo = new AutocompleteJComboBox(searchable);
                combo.setPreferredSize(new Dimension(1000, 30));
                combo.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                combo.setBackground(new Color(245, 246, 250));
                content.add(combo, "wrap");
            } else {
                JTextField textField = new MyTextFieldUnderLine();
                textField.setPreferredSize(new Dimension(1000, 30));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(new Color(245, 246, 250));
                jTextFieldAccount.add(textField);
                content.add(textField, "wrap");
            }


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

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(Color.white);
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addAccount();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addAccount() {
        Pair<Boolean, String> result;

        int id, staff_id;
        String username;

        if (combo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!staffList.contains(combo.getSelectedItem())) {
            JOptionPane.showMessageDialog(null, "Nhân viên không hợp lệ!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        id = accountBLL.getAutoID(accountBLL.searchAccounts());
        username = jTextFieldAccount.get(0).getText();
        staff_id = Integer.parseInt(Objects.requireNonNull(combo.getSelectedItem()).toString().split(" - ")[1]);


        Account account = new Account(id, username, staff_id);

        result = accountBLL.addAccount(account);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
