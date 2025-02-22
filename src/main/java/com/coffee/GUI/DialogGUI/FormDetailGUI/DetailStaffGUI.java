package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.RoleBLL;
import com.coffee.BLL.Role_DetailBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Role;
import com.coffee.DTO.Role_Detail;
import com.coffee.DTO.Staff;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.main.Cafe_Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailStaffGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeStaff;


    private List<JTextField> jTextFieldsStaff;

    private StaffBLL staffBLL = new StaffBLL();
    private Staff staff = new Staff();

    public DetailStaffGUI(Staff staff) {
        super();
        super.setTitle("Thông Tin Nhân Viên ");
        super.setSize(new Dimension(600, 700));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init(staff);
        setVisible(true);
    }

    private void init(Staff staff) {
        titleName = new JLabel();
        attributeStaff = new ArrayList<>();
        jTextFieldsStaff = new ArrayList<>();
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Thông Tin Nhân Viên");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Nhân Viên", "Tên Nhân Viên", "CCCD", "Giới Tính",
                "Ngày Sinh", "Chức Vụ", "Số Điện Thoại", "Địa Chỉ", "Email"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeStaff.add(label);
            content.add(label);
            JTextField textField = new MyTextFieldUnderLine();
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));

            if (string.trim().equals("Ngày Sinh")) {
                textField.setText(new SimpleDateFormat("dd/MM/yyyy").format(staff.getBirthdate()));
                content.add(textField, "wrap");
            } else {
                if (string.trim().equals("Mã Nhân Viên")) {
                    String staffId = Integer.toString(staff.getId());
                    textField.setText(staffId);
                }
                if (string.trim().equals("Tên Nhân Viên")) {
                    textField.setText(staff.getName());
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(staff.getStaffNo());
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = staff.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(staff.getPhone());
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(staff.getAddress());
                }
                if (string.trim().equals("Email")) {
                    textField.setText(staff.getEmail());
                }
                if (string.trim().equals("Chức Vụ")) {
                    List<Role_Detail> roleDetails = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
                    if (roleDetails.isEmpty()) {
                        textField.setText("Chưa có chức vụ");
                    } else {
                        Role role = new RoleBLL().searchRoles("id = " + roleDetails.get(0).getRole_id()).get(0);
                        textField.setText(role.getName());
                    }
                }
                jTextFieldsStaff.add(textField);
                content.add(textField, "wrap");
            }
        }

    }
}

