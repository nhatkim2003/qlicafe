package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.SupplierBLL;
import com.coffee.DTO.Material;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.main.Cafe_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddMaterialGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeMaterial;
    private List<JTextField> jTextFieldMaterial;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JComboBox<String> listUnit;

    private MaterialBLL materialBLL = new MaterialBLL();
    private JCheckBox saleCheckbox;

    public AddMaterialGUI() {
        super();
        super.setTitle("Thêm nguyên liệu");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributeMaterial = new ArrayList<>();
        jTextFieldMaterial = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Thêm Nguyên Liệu");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Tên Nguyên Liệu", "Tồn Kho Tối Thiểu", "Tồn Kho Tối Đa", "Đơn Vị", "Giá Vốn"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeMaterial.add(label);
            content.add(label);

            if (string.equals("Đơn Vị")) {
                listUnit = new JComboBox<>();
                listUnit.setPreferredSize(new Dimension(1000, 30));
                listUnit.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                listUnit.setBackground(new Color(245, 246, 250));

                String[] units = {"Chọn đơn vị", "kg", "lít", "cái"};

                for (String unit : units) {
                    listUnit.addItem(unit);
                }
                content.add(listUnit, "wrap");
                continue;
            }
            JTextField textField = new MyTextFieldUnderLine();
            if (string.equals("Tồn Kho Tối Thiểu") || string.equals("Tồn Kho Tối Đa") || string.equals("Giá Vốn")) {
                textField.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
            }

            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldMaterial.add(textField);
            content.add(textField, "wrap");

        }
        saleCheckbox = new JCheckBox("Bán trực tiếp");
        content.add(saleCheckbox);

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
                addMaterial();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addMaterial() {
        Pair<Boolean, String> result;
        int id;
        String name, unit;
        double min_remain, max_remain, unit_price;

        id = materialBLL.getAutoID(materialBLL.searchMaterials());
        name = jTextFieldMaterial.get(0).getText();

        unit = Objects.requireNonNull(listUnit.getSelectedItem()).toString();

        if (jTextFieldMaterial.get(1).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tồn kho tối thiểu!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jTextFieldMaterial.get(2).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tồn kho tối đa!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        min_remain = Double.parseDouble(jTextFieldMaterial.get(1).getText());
        max_remain = Double.parseDouble(jTextFieldMaterial.get(2).getText());

        if (unit.equals("Chọn đơn vị")) {
            JOptionPane.showMessageDialog(null, "Chưa chọn đơn vị",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jTextFieldMaterial.get(3).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập giá vốn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        unit_price = Double.parseDouble(jTextFieldMaterial.get(3).getText());
        boolean sell = saleCheckbox.isSelected();
        Material material = new Material(id, name, 0, min_remain, max_remain, unit, unit_price, sell, false, 0);
        result = materialBLL.addMaterial(material);
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
