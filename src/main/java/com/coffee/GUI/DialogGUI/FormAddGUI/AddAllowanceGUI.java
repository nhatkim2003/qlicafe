package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.AllowanceBLL;
import com.coffee.DTO.Allowance;
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

public class AddAllowanceGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeAllowance;
    private List<JTextField> jTextFieldAllowance;
    private JComboBox<String> jComboBoxAllowanceType;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private AllowanceBLL allowanceBLL = new AllowanceBLL();

    public AddAllowanceGUI() {
        super();
        super.setTitle("Thêm phụ cấp");
        super.setSize(new Dimension(600, 320));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributeAllowance = new ArrayList<>();
        jTextFieldAllowance = new ArrayList<>();
        jComboBoxAllowanceType = new JComboBox<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Thêm phụ cấp");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Tên phụ cấp", "Số tiền phụ cấp", "Loại phụ cấp"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeAllowance.add(label);
            content.add(label);

            JTextField textField = new MyTextFieldUnderLine();
            if (string.equals("Số tiền phụ cấp")) {
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
                jComboBoxAllowanceType.addItem("Phụ cấp theo ngày làm");
                jComboBoxAllowanceType.addItem("Phụ cấp theo tháng làm");

                jComboBoxAllowanceType.setPreferredSize(new Dimension(1000, 30));
                jComboBoxAllowanceType.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                jComboBoxAllowanceType.setBackground(new Color(245, 246, 250));
                content.add(jComboBoxAllowanceType, "wrap");

                continue;
            }

            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldAllowance.add(textField);
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
                addAllowance();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addAllowance() {
        Pair<Boolean, String> result;
        int id, allowance_type;
        String name;
        double allowance_amount;

        id = allowanceBLL.getAutoID(allowanceBLL.searchAllowances()); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        name = jTextFieldAllowance.get(0).getText();
        if (jTextFieldAllowance.get(1).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền phụ cấp!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        allowance_amount = Double.parseDouble(jTextFieldAllowance.get(1).getText());
        allowance_type = jComboBoxAllowanceType.getSelectedIndex();

        Allowance allowance = new Allowance(id, name, allowance_amount, allowance_type); // false là tồn tại, true là đã xoá

        result = allowanceBLL.addAllowance(allowance);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            AddSalary_FormatGUI.addAllowance = true;
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
