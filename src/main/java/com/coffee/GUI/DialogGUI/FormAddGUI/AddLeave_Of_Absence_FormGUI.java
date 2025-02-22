package com.coffee.GUI.DialogGUI.FormAddGUI;


import com.coffee.BLL.Leave_Of_Absence_FormBLL;
import com.coffee.BLL.Work_ScheduleBLL;
import com.coffee.DTO.Leave_Of_Absence_Form;
import com.coffee.DTO.Staff;
import com.coffee.DTO.Work_Schedule;
import com.coffee.GUI.Leave_Of_Absence_FormGUI;
import com.coffee.GUI.MaterialGUI;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.main.Cafe_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class AddLeave_Of_Absence_FormGUI extends JDialog {
    private Staff staff;
    private List<JLabel> attributeLeaveOfAbsenceForm;
    private JTextField textField;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private JTextArea jTextArea;
    private JButton buttonCreate;
    private Leave_Of_Absence_FormBLL leaveOfAbsenceFormBLL = new Leave_Of_Absence_FormBLL();
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox2;
    private JCheckBox jCheckBox3;
    private JComboBox<String> jComboBox;

    public AddLeave_Of_Absence_FormGUI(Staff staff) {
        super((Frame) null, "", true);
        this.staff = staff;
        getContentPane().setBackground(new Color(242, 245, 250));
        setTitle("Tạo Đơn Nghỉ Phép");
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
        textField = new JTextField();
        datePicker = new DatePicker();
        editor = new JFormattedTextField();
        jTextArea = new JTextArea();
        buttonCreate = new JButton("Tạo");
        jCheckBox1 = new JCheckBox();
        jCheckBox2 = new JCheckBox();
        jCheckBox3 = new JCheckBox();
        jComboBox = new JComboBox<>(new String[]{"Lý do", "1. Nghỉ ốm", "2. Nghỉ phép năm (12 ngày/năm)", "3. Nghỉ kết hôn (3 ngày)", "4. Nghỉ con cái kết hôn (1 ngày)", "5. Nghỉ khác"});

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

        JLabel jLabelTitle = new JLabel("Tạo Đơn Nghỉ Phép");
        jLabelTitle.setHorizontalAlignment(JLabel.CENTER);
        jLabelTitle.setFont(new Font("Lexend", Font.BOLD, 18));
        roundedPanelTitle.add(jLabelTitle);

        for (String string : new String[]{"Họ tên", "Ngày nghỉ", "Ca", "Lý do"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.PLAIN, 16)));
            attributeLeaveOfAbsenceForm.add(label);
            center.add(label);

            if (string.equals("Họ tên")) {
                textField.setText(staff.getName());
                textField.setPreferredSize(new Dimension(1000, 50));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(Color.white);
                textField.setEditable(false);
                center.add(textField, "wrap");
            }
            if (string.equals("Ngày nghỉ")) {
                datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
                datePicker.setEditor(editor);
                datePicker.setCloseAfterSelected(true);

                editor.setPreferredSize(new Dimension(280, 40));
                editor.setFont(new Font("Inter", Font.BOLD, 15));

                center.add(editor, "wrap");
                continue;
            }
            if (string.equals("Ca")) {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.setPreferredSize(new Dimension(1000, 30));
                jPanel.setBackground(new Color(242, 245, 250));

                jCheckBox1.setText("1: 6h - 12h");
                jCheckBox2.setText("2: 12h - 18h");
                jCheckBox3.setText("3: 18h - 23h");

                jPanel.add(jCheckBox1);
                jPanel.add(jCheckBox2);
                jPanel.add(jCheckBox3);
                center.add(jPanel, "wrap");
                continue;
            }
            if (string.equals("Lý do")) {
                jComboBox.setPreferredSize(new Dimension(1000, 50));
                jComboBox.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                jComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jComboBox.getSelectedIndex() != 5 && jComboBox.getSelectedIndex() != 0) {
                            jTextArea.setText(Objects.requireNonNull(jComboBox.getSelectedItem()).toString().split("\\. ")[1].split(" \\(")[0]);
                            jTextArea.setEditable(false);
                        } else if (jComboBox.getSelectedIndex() == 0) {
                            jTextArea.setEditable(true);
                            jTextArea.setText("");
                        } else {
                            jTextArea.setEditable(true);
                            jTextArea.setText("");
                            jTextArea.setRequestFocusEnabled(true);
                        }
                    }
                });
                center.add(jComboBox, "wrap");

                center.add(new JLabel());

                jTextArea.setBackground(Color.white);
                jTextArea.setPreferredSize(new Dimension(1000, 200));
                center.add(jTextArea, "wrap");
            }
        }

        buttonCreate.setPreferredSize(new Dimension(100, 30));
        buttonCreate.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCreate.setBackground(new Color(1, 120, 220));
        buttonCreate.setForeground(Color.white);
        buttonCreate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addLeave_Of_Absence_form();
            }
        });
        bot.add(buttonCreate);
    }

    private void addLeave_Of_Absence_form() {
        Pair<Boolean, String> result;

        int id, staff_id;
        Date date, date_off;
        String shifts, reason;

        id = leaveOfAbsenceFormBLL.getAutoID(leaveOfAbsenceFormBLL.searchLeave_Of_Absence_Forms());
        staff_id = staff.getId();

        if (datePicker.getDateSQL_Single() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày nghỉ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (datePicker.getDateSQL_Single().before(java.sql.Date.valueOf(LocalDate.now()))) {
            JOptionPane.showMessageDialog(null, "Không được tạo đơn nghỉ phép trước ngày hiện tại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!jCheckBox1.isSelected() && !jCheckBox2.isSelected() && !jCheckBox3.isSelected()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ca nghỉ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        date = java.sql.Date.valueOf(LocalDate.now());
        date_off = datePicker.getDateSQL_Single();

        if (jTextArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập lý do.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        reason = jTextArea.getText();

        String[] string = new String[0];
        if (jCheckBox1.isSelected()) {
            string = Arrays.copyOf(string, string.length + 1);
            string[string.length - 1] = "1";
        }
        if (jCheckBox2.isSelected()) {
            string = Arrays.copyOf(string, string.length + 1);
            string[string.length - 1] = "2";
        }
        if (jCheckBox3.isSelected()) {
            string = Arrays.copyOf(string, string.length + 1);
            string[string.length - 1] = "3";
        }
        shifts = String.join(", ", string);

        Leave_Of_Absence_Form leaveOfAbsenceForm = new Leave_Of_Absence_Form(id, staff_id, date, date_off, shifts, reason, 0);

        String[] shiftsIntegers = leaveOfAbsenceForm.getShifts().split(", ");
        for (String s : shiftsIntegers) {
            List<Work_Schedule> work_schedules = new Work_ScheduleBLL().searchWork_schedules("staff_id = " + leaveOfAbsenceForm.getStaff_id(), "date ='" + leaveOfAbsenceForm.getDate_off() + "'", "shift = " + s);
            if (work_schedules.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nhân viên không có lịch làm việc vào ngày " + leaveOfAbsenceForm.getDate_off() + ", ca " + s,
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

        }

        result = leaveOfAbsenceFormBLL.addLeave_Of_Absence_Form(leaveOfAbsenceForm);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (Cafe_Application.homeGUI.indexModuleLeaveOffGUI != -1) {
                Leave_Of_Absence_FormGUI leaveOfAbsenceFormGUI = (Leave_Of_Absence_FormGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleLeaveOffGUI];
                leaveOfAbsenceFormGUI.refresh();
            }
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
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
