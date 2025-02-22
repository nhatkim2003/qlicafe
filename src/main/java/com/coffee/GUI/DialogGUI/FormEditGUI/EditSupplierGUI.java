package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.SupplierBLL;
import com.coffee.DTO.Supplier;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.main.Cafe_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditSupplierGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeSupplier;
    private List<JTextField> jTextFieldSupplier;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private SupplierBLL supplierBLL = new SupplierBLL();
    private Supplier supplier;

    public EditSupplierGUI(Supplier supplier) {
        super();
        super.setTitle("Cập nhật thông tin nhà cung cấp");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        this.supplier = supplier;
        init(supplier);
        setVisible(true);
    }

    private void init(Supplier supplier) {
        titleName = new JLabel();
        attributeSupplier = new ArrayList<>();
        jTextFieldSupplier = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonEdit = new JButton("Cập nhật");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Cập nhật thông tin nhà cung cấp");
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

            if (string.equals("Tên nhà cung cấp")) {
                textField.setText(supplier.getName());
            }
            if (string.equals("Điện thoại")) {
                textField.setText(supplier.getPhone());
            }
            if (string.equals("Địa chỉ")) {
                textField.setText(supplier.getAddress());
            }
            if (string.equals("Email")) {
                textField.setText(supplier.getEmail());
            }

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

        buttonEdit.setPreferredSize(new Dimension(100, 30));
        buttonEdit.setBackground(new Color(1, 120, 220));
        buttonEdit.setForeground(Color.white);
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editSupplier();
            }
        });
        containerButton.add(buttonEdit);
    }

    private void editSupplier() {
        Pair<Boolean, String> result;
        int id;
        String name, phone, address, email;

        id = supplier.getId();
        name = jTextFieldSupplier.get(0).getText();
        phone = jTextFieldSupplier.get(1).getText();
        address = jTextFieldSupplier.get(2).getText();
        email = jTextFieldSupplier.get(3).getText();

        Supplier newsSupplier = new Supplier(id, name, phone, address, email, false); // false là tồn tại, true là đã xoá

        result = supplierBLL.updateSupplier(supplier, newsSupplier);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
