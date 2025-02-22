package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.PayrollBLL;
import com.coffee.DTO.Payroll;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.main.Cafe_Application;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddPayrollGUI extends DialogForm {
    private JLabel titleName;
    private JComboBox<String> jComboBox;
    private List<JLabel> attributePayroll;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private PayrollBLL payrollBLL = new PayrollBLL();

    public AddPayrollGUI() {
        super();
        super.setTitle("Thêm Bảng Lương");
        super.setSize(new Dimension(600, 200));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributePayroll = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        content.setLayout(new MigLayout("",
                "50[]20[]20[]"));

        titleName.setText("Thêm Bảng Lương");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        content.setBackground(new Color(242, 245, 250));

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(170, 30));
        label.setText("Chọn kỳ làm việc");
        label.setFont((new Font("Public Sans", Font.BOLD, 16)));
        attributePayroll.add(label);
        content.add(label);

//        jMonthChooser = new JMonthChooser();
//        jMonthChooser.setPreferredSize(new Dimension(1000, 30));
////        jMonthChooser.setBackground(new Color(245, 246, 250));
//        jMonthChooser.setMonth(LocalDate.now().getMonth().getValue() - 1);
//        content.add(jMonthChooser);
//
//        jYearChooser = new JYearChooser();
//        jYearChooser.setPreferredSize(new Dimension(1000, 30));
////        jYearChooser.setBackground(new Color(245, 246, 250));
//        jYearChooser.setYear(LocalDate.now().getYear());
//        content.add(jYearChooser);

        jComboBox = new JComboBox<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int index = 0, i = 0;
        // Lặp qua từ năm 2021 đến năm 2025
        for (int year = 2021; year <= 2025; year++) {
            for (int month = 1; month <= 12; month++) {
                if (year == currentYear && month == currentMonth)
                    index = i;
                LocalDate startDate = LocalDate.of(year, month, 1);
                LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

                // In ra dữ liệu theo mẫu "01/04/yyyy - 30/04/yyyy"
                String dateRange = startDate.format(dateFormatter) + " - " + endDate.format(dateFormatter);
                jComboBox.addItem(dateRange);
                i += 1;
            }
        }

        jComboBox.setSelectedIndex(index);
        jComboBox.setPreferredSize(new Dimension(300, 35));
        content.add(jComboBox);

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
                addPayroll();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addPayroll() {
        Pair<Boolean, String> result;
        int id, month, year;
        String name;
        Date entry_date;
        double total_salary, paid, debt;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Phân tích cú pháp chuỗi
        String[] parts = Objects.requireNonNull(jComboBox.getSelectedItem()).toString().split(" - ");

        // Lấy tháng và năm từ phần bắt đầu và kết thúc của chuỗi
        LocalDate startDate = LocalDate.parse(parts[0], dateFormatter);

        month = startDate.getMonthValue();
        year = startDate.getYear();

        if (month > LocalDate.now().getMonthValue() || year > LocalDate.now().getYear()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn thời gian hiện tại hoặc trước đó!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        id = payrollBLL.getAutoID(payrollBLL.searchPayrolls()); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        YearMonth yearMonth = YearMonth.of(year, month);

        name = "Bảng lương " + yearMonth.format(DateTimeFormatter.ofPattern("MM/yyyy"));
        entry_date = java.sql.Date.valueOf(LocalDate.now());
        total_salary = 0;
        paid = 0;
        debt = 0;
        Payroll payroll = new Payroll(id, name, entry_date, month, year, total_salary, paid, debt); // false là tồn tại, true là đã xoá

        result = payrollBLL.addPayroll(payroll);

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
