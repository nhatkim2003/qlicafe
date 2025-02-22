package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.SupplierBLL;
import com.coffee.DTO.Supplier;

import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
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

public class AddSupplierGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeSupplier;
    private List<JTextField> jTextFieldSupplier;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private SupplierBLL supplierBLL = new SupplierBLL();

    public AddSupplierGUI() {
        super();
        super.setTitle("Thêm nhà cung cấp");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributeSupplier = new ArrayList<>();
        jTextFieldSupplier = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Thêm nhà cung cấp");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Tên nhà cung cấp", "Điện thoại", "Địa chỉ", "Email"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeSupplier.add(label);
            content.add(label);

            JTextField textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldSupplier.add(textField);
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

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(Color.white);
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addSupplier();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addSupplier() {
        Pair<Boolean, String> result;
        int id;
        String name, phone, address, email;

        id = supplierBLL.getAutoID(supplierBLL.searchSuppliers("deleted = 0")); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        name = jTextFieldSupplier.get(0).getText();
        phone = jTextFieldSupplier.get(1).getText();
        address = jTextFieldSupplier.get(2).getText();
        email = jTextFieldSupplier.get(3).getText();

        Supplier supplier = new Supplier(id, name, phone, address, email, false); // false là tồn tại, true là đã xoá

        result = supplierBLL.addSupplier(supplier);

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
