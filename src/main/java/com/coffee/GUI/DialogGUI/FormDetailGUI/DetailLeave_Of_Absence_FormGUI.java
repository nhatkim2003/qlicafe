package com.coffee.GUI.DialogGUI.FormDetailGUI;


import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.Leave_Of_Absence_Form;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.main.Cafe_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailLeave_Of_Absence_FormGUI extends JDialog {
    private List<JLabel> attributeLeaveOfAbsenceForm;
    private JTextArea jTextArea;
    private Leave_Of_Absence_Form leaveOfAbsenceForm;

    public DetailLeave_Of_Absence_FormGUI(Leave_Of_Absence_Form leaveOfAbsenceForm) {
        super((Frame) null, "", true);
        this.leaveOfAbsenceForm = leaveOfAbsenceForm;
        getContentPane().setBackground(new Color(242, 245, 250));
        setTitle("Thông tin đơn nghỉ phép");
        setLayout(new BorderLayout());
        setIconImage(new FlatSVGIcon("image/coffee_logo.svg").getImage());
        setSize(new Dimension(600, 500));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Cafe_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        attributeLeaveOfAbsenceForm = new ArrayList<>();
        jTextArea = new JTextArea();

        RoundedPanel top = new RoundedPanel();
        top.setLayout(new GridBagLayout());
        top.setBackground(new Color(242, 245, 250));
        top.setPreferredSize(new Dimension(600, 50));
        add(top, BorderLayout.NORTH);

        RoundedPanel center = new RoundedPanel();
        center.setBackground(new Color(242, 245, 250));
        center.setPreferredSize(new Dimension(600, 400));
        center.setLayout(new MigLayout("",
                "20[]20[]20",
                "20[]20[]20"));
        add(center, BorderLayout.CENTER);

        RoundedPanel bot = new RoundedPanel();
        bot.setLayout(new GridBagLayout());
        bot.setBackground(new Color(242, 245, 250));
        bot.setPreferredSize(new Dimension(600, 50));
        add(bot, BorderLayout.SOUTH);

        RoundedPanel roundedPanelTitle = new RoundedPanel();
        roundedPanelTitle.setBackground(new Color(242, 245, 250));
        roundedPanelTitle.setLayout(new BorderLayout());
        roundedPanelTitle.setPreferredSize(new Dimension(250, 40));
        top.add(roundedPanelTitle);

        JLabel jLabelTitle = new JLabel("Thông Tin Đơn Nghỉ Phép");
        jLabelTitle.setHorizontalAlignment(JLabel.CENTER);
        jLabelTitle.setFont(new Font("Lexend", Font.BOLD, 18));
        roundedPanelTitle.add(jLabelTitle);

        for (String string : new String[]{"Họ tên", "Ngày tạo", "Ngày nghỉ", "Ca", "Lý do"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeLeaveOfAbsenceForm.add(label);
            center.add(label);

            JTextField textField = new MyTextFieldUnderLine();
            if (string.equals("Họ tên")) {
                textField.setText(new StaffBLL().findStaffsBy(Map.of("id", leaveOfAbsenceForm.getStaff_id())).get(0).getName());
                textField.setPreferredSize(new Dimension(1000, 50));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(Color.white);

            }
            if (string.equals("Ngày tạo")) {
                textField.setText(new SimpleDateFormat("dd/MM/yyyy").format(leaveOfAbsenceForm.getDate()));
                textField.setPreferredSize(new Dimension(1000, 50));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(Color.white);
            }
            if (string.equals("Ngày nghỉ")) {
                textField.setText(new SimpleDateFormat("dd/MM/yyyy").format(leaveOfAbsenceForm.getDate_off()));
                textField.setPreferredSize(new Dimension(1000, 50));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(Color.white);
            }
            if (string.equals("Ca")) {
                textField.setText(leaveOfAbsenceForm.getShifts());
                textField.setPreferredSize(new Dimension(1000, 50));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(Color.white);
            }
            if (string.equals("Lý do")) {
                jTextArea.setBackground(new Color(255, 255, 255));
                jTextArea.setPreferredSize(new Dimension(1000, 200));
                jTextArea.setText(leaveOfAbsenceForm.getReason());
                jTextArea.setEditable(false);
                center.add(jTextArea, "wrap");
                continue;
            }
            textField.setEditable(false);
            center.add(textField, "wrap");
        }
    }

    private void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1)
            dispose();
    }
}
