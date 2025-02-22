package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.DeductionBLL;
import com.coffee.DTO.Deduction;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.main.Cafe_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditDeductionGUI extends DialogForm {
    private JLabel titleName;
    private java.util.List<JLabel> attributeDeduction;
    private List<JTextField> jTextFieldDeduction;
    private JComboBox<String> jComboBoxDeductionType;

    private JButton buttonCancel;
    private JButton buttonEdit;
    private DeductionBLL deductionBLL = new DeductionBLL();
    private Deduction deduction;

    public EditDeductionGUI(Deduction deduction) {
        super();
        super.setTitle("Cập nhật thông tin phụ cấp");
        super.setSize(new Dimension(600, 320));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        this.deduction = deduction;
        init(deduction);
        setVisible(true);
    }

    private void init(Deduction deduction) {
        titleName = new JLabel();
        attributeDeduction = new ArrayList<>();
        jTextFieldDeduction = new ArrayList<>();
        jComboBoxDeductionType = new JComboBox<>();
        buttonCancel = new JButton("Huỷ");
        buttonEdit = new JButton("Cập nhật");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Cập nhật thông tin phụ cấp");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Tên phụ cấp", "Số tiền phụ cấp", "Loại phụ cấp"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeDeduction.add(label);
            content.add(label);

            JTextField textField = new MyTextFieldUnderLine();

            if (string.equals("Tên phụ cấp")) {
                textField.setText(deduction.getName());
            }
            if (string.equals("Số tiền phụ cấp")) {
                textField.setText(String.valueOf(deduction.getDeduction_amount()));
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
            }
            if (string.equals("Loại phụ cấp")) {
                jComboBoxDeductionType.addItem("Giảm trừ đi muộn");
                jComboBoxDeductionType.addItem("Giảm trừ về sớm");
                jComboBoxDeductionType.addItem("Giảm trừ cố định");
                jComboBoxDeductionType.addItem("Giảm trừ nghỉ làm");

                jComboBoxDeductionType.setSelectedIndex(deduction.getDeduction_type());

                jComboBoxDeductionType.setPreferredSize(new Dimension(1000, 30));
                jComboBoxDeductionType.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                jComboBoxDeductionType.setBackground(new Color(245, 246, 250));
                content.add(jComboBoxDeductionType, "wrap");

                continue;
            }

            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldDeduction.add(textField);
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
                editDeduction();
            }
        });
        containerButton.add(buttonEdit);
    }

    private void editDeduction() {
        Pair<Boolean, String> result;
        int id, deduction_type;
        String name;
        double deduction_amount;

        id = deduction.getId();
        name = jTextFieldDeduction.get(0).getText();
        if (jTextFieldDeduction.get(1).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền giảm trừ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        deduction_amount = Double.parseDouble(jTextFieldDeduction.get(1).getText());
        deduction_type = jComboBoxDeductionType.getSelectedIndex();

        Deduction deduction = new Deduction(id, name, deduction_amount, deduction_type); // false là tồn tại, true là đã xoá

        result = deductionBLL.updateDeduction(deduction);

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
