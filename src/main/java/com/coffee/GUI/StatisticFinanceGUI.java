package com.coffee.GUI;

import com.coffee.DAL.MySQL;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.line_chart.ModelData;
import com.coffee.GUI.components.line_chart.chart.CurveLineChart;
import com.coffee.GUI.components.line_chart.chart.ModelLineChart;
import com.coffee.utils.VNString;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StatisticFinanceGUI extends JPanel {
    private JPanel content;
    private JScrollPane scrollPane;
    private int concern = 0;
    private int displayType = 0;

    public StatisticFinanceGUI() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 700));
        setLayout(new BorderLayout());
        init();
        setVisible(true);
    }

    private void init() {
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 700));
        add(scrollPane, BorderLayout.EAST);
        initRightBar();

        content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.LEFT));
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(238, 238, 238));
        add(content, BorderLayout.CENTER);

        byMonthChart();
    }

    private void initRightBar() {
        JPanel jPanel = new JPanel(new MigLayout("", "5[]5", "10[]10"));
        jPanel.setBackground(new Color(238, 238, 238));
        scrollPane.setViewportView(jPanel);

        displayType = 0;

        RoundedPanel displayTypePanel = new RoundedPanel();
        displayTypePanel.setBackground(new Color(255, 255, 255));
        displayTypePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        displayTypePanel.setPreferredSize(new Dimension(247, 100));
        jPanel.add(displayTypePanel, "wrap");

        ButtonGroup btnGroupDisplayType = new ButtonGroup();
        JLabel labelDisplayType = new JLabel("Chọn hiển thị");
        labelDisplayType.setFont(new Font("Inter", Font.BOLD, 14));
        displayTypePanel.add(labelDisplayType, "wrap");

        JRadioButton chartRadioButton = createRadioButton("Biểu đồ");
        chartRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayType = 0;
                loadData();
            }
        });
        chartRadioButton.setSelected(true);
        btnGroupDisplayType.add(chartRadioButton);
        displayTypePanel.add(chartRadioButton, "wrap");

        JRadioButton reportRadioButton = createRadioButton("Báo cáo");
        reportRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayType = 1;
                loadData();
            }
        });
        btnGroupDisplayType.add(reportRadioButton);
        displayTypePanel.add(reportRadioButton, "wrap");

        RoundedPanel timePanel = new RoundedPanel();
        timePanel.setBackground(new Color(255, 255, 255));
        timePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        timePanel.setPreferredSize(new Dimension(247, 120));
        jPanel.add(timePanel, "wrap");

        JLabel labelTime = new JLabel("Thời gian");
        labelTime.setFont(new Font("Inter", Font.BOLD, 14));
        timePanel.add(labelTime, "wrap");

        JRadioButton radio1 = createRadioButton("Theo Tháng");
        if (concern == 0)
            radio1.setSelected(true);
        radio1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                concern = 0;
                initRightBar();
                loadData();
            }
        });

        JRadioButton radio2 = createRadioButton("Theo Quý");
        if (concern == 1)
            radio2.setSelected(true);
        radio2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                concern = 1;
                initRightBar();
                loadData();
            }
        });

        JRadioButton radio3 = createRadioButton("Theo Năm");
        if (concern == 2)
            radio3.setSelected(true);
        radio3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                concern = 2;
                initRightBar();
                loadData();
            }
        });

        ButtonGroup btnGroupTime = new ButtonGroup();
        btnGroupTime.add(radio1);
        btnGroupTime.add(radio2);
        btnGroupTime.add(radio3);

        timePanel.add(radio1, "wrap");
        timePanel.add(radio2, "wrap");
        timePanel.add(radio3, "wrap");
    }

    private void loadData() {
        if (concern == 0) {
            if (displayType == 0)
                byMonthChart();
            if (displayType == 1)
                byMonthReport();
        }
        if (concern == 1) {
            if (displayType == 0)
                byQuarterChart();
            if (displayType == 1)
                byQuarterReport();
        }
        if (concern == 2) {
            if (displayType == 0)
                byYearChart();
            if (displayType == 1)
                byYearReport();
        }
    }

    private void byYearReport() {
        List<List<String>> dataSale_Discount = MySQL.getSale_DiscountByYear();
        List<List<String>> dataCapitalPrice = MySQL.getCapitalPriceByYear();
        List<List<String>> dataSalary_Allowance_Bonus_Deduction_Fine = MySQL.getSalary_Allowance_Bonus_Deduction_FineByYear();

        List<Integer> yearList = new ArrayList<>();

        String[] columns = new String[]{" "};
        // Lặp qua từ tháng 1 đến tháng hiện tại
        for (List<String> strings : dataSale_Discount) {
            yearList.add(Integer.parseInt(strings.get(0)));
            columns = Arrays.copyOf(columns, columns.length + 1);
            columns[columns.length - 1] = strings.get(0);
        }

        columns = Arrays.copyOf(columns, columns.length + 1);
        columns[columns.length - 1] = "Tổng";

        List<List<String>> data = new ArrayList<>();

        for (Integer year : yearList) {
            List<String> list = new ArrayList<>();
            list.add(year.toString());

            boolean check = false;
            double value1 = 0;
            for (List<String> strings : dataSale_Discount) {
                if (strings.get(0).equals(year.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    value1 = Double.parseDouble(strings.get(1)) - Double.parseDouble(strings.get(2));
                    list.add(String.valueOf(value1));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
            }

            check = false;
            double value2 = 0;
            for (List<String> strings : dataCapitalPrice) {
                if (strings.get(0).equals(year.toString())) {
                    list.add(strings.get(1));
                    value2 = value1 - Double.parseDouble(strings.get(1));
                    list.add(String.valueOf(value2));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
            }

            check = false;
            double value3 = 0;
            double value4 = 0;
            double value5 = 0;
            double value6 = 0;
            for (List<String> strings : dataSalary_Allowance_Bonus_Deduction_Fine) {
                if (strings.get(0).equals(year.toString())) {
                    value3 = Double.parseDouble(strings.get(1)) + Double.parseDouble(strings.get(2)) + Double.parseDouble(strings.get(3));
                    list.add(String.valueOf(value3));
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    list.add(strings.get(3));
                    value4 = value2 - value3;
                    list.add(String.valueOf(value4));

                    value5 = Double.parseDouble(strings.get(4)) + Double.parseDouble(strings.get(5));
                    list.add(String.valueOf(value5));
                    list.add(strings.get(4));
                    list.add(strings.get(5));
                    value6 = value4 + value5;
                    list.add(String.valueOf(value6));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add(String.valueOf(value2));
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add(String.valueOf(value2));
            }

            data.add(list);
        }

        content.removeAll();

        initTopContent("Báo cáo kết quả hoạt động kinh doanh theo năm");

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(885, 610));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);


        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        salesLabelPanel.setPreferredSize(new Dimension(Math.max(885, (120 * columns.length - 1) + 200), 40));
        addLabelsToPanel(salesLabelPanel, columns, new Dimension(120, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        List<List<String>> convertData = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            convertData.add(new ArrayList<>());
        }
        for (int i = 0; i < data.size(); i++) {  // Duyệt qua hàng
            for (int j = 1; j < 15; j++) {  // Duyệt qua cột
                convertData.get(j - 1).add(data.get(i).get(j));
            }
        }

        int i = 1;
        for (List<String> list : convertData) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(Math.max(885, (120 * columns.length - 1) + 200), 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel label = new JLabel();
            label.setFont(new Font("Inter", Font.BOLD, 13));
            label.setPreferredSize(new Dimension(200, 50));
            panel.add(label);
            if (i == 1) {
                label.setText("Doanh thu bán hàng (1)");
            }
            if (i == 2) {
                label.setText("Chiết khấu hoá đơn (2)");
            }
            if (i == 3) {
                label.setText("Doanh thu thuần (3 = 1-2)");
            }
            if (i == 4) {
                label.setText("Giá vốn sản phẩm (4)");
            }
            if (i == 5) {
                label.setText("Lợi nhuận gộp bán hàng (5 = 3-4)");
            }
            if (i == 6) {
                label.setText("Chi phí (6)");
            }
            if (i == 7) {
                label.setText("     Tiền lương nhân viên");
            }
            if (i == 8) {
                label.setText("     Tiền phụ cấp");
            }
            if (i == 9) {
                label.setText("     Tiền thưởng");
            }
            if (i == 10) {
                label.setText("Lợi nhuận kinh doanh (7 = 5-6)");
            }
            if (i == 11) {
                label.setText("Thu nhập khác (8)");
            }
            if (i == 12) {
                label.setText("     Tiền thu giảm trừ");
            }
            if (i == 13) {
                label.setText("     Tiền thu phạt");
            }
            if (i == 14) {
                label.setText("Lợi nhuận thuần (9 = 7+8)");
            }
            double total = 0;
            for (String string : list) {
                total += Double.parseDouble(string);

                JLabel labelValue = new JLabel(VNString.currency(Double.parseDouble(string)));
                labelValue.setFont(new Font("Inter", Font.PLAIN, 13));
                labelValue.setPreferredSize(new Dimension(120, 50));
                labelValue.setHorizontalAlignment(JLabel.RIGHT);
                panel.add(labelValue);
            }

            JLabel labelTotal = new JLabel(VNString.currency(total));
            labelTotal.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotal.setHorizontalAlignment(JLabel.RIGHT);
            labelTotal.setPreferredSize(new Dimension(120, 50));
            panel.add(labelTotal);

            centerPanel.add(panel, "wrap");
            System.out.println(list);
            i++;
        }

        content.repaint();
        content.revalidate();
    }

    private void byYearChart() {
        List<List<String>> dataSale_Discount = MySQL.getSale_DiscountByYear();
        List<List<String>> dataCapitalPrice = MySQL.getCapitalPriceByYear();
        List<List<String>> dataSalary_Allowance_Bonus_Deduction_Fine = MySQL.getSalary_Allowance_Bonus_Deduction_FineByYear();

        List<Integer> yearList = new ArrayList<>();

        // Lặp qua từ tháng 1 đến tháng hiện tại
        for (List<String> strings : dataSale_Discount) {
            yearList.add(Integer.parseInt(strings.get(0)));
        }

        List<List<String>> data = new ArrayList<>();

        for (Integer year : yearList) {
            List<String> list = new ArrayList<>();
            list.add(year.toString());

            boolean check = false;
            for (List<String> strings : dataSale_Discount) {
                if (strings.get(0).equals(year.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
                list.add("0");
            }

            check = false;
            for (List<String> strings : dataCapitalPrice) {
                if (strings.get(0).equals(year.toString())) {
                    list.add(strings.get(1));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
            }

            check = false;
            for (List<String> strings : dataSalary_Allowance_Bonus_Deduction_Fine) {
                if (strings.get(0).equals(year.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    list.add(strings.get(3));
                    list.add(strings.get(4));
                    list.add(strings.get(5));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
                list.add("0");
                list.add("0");
                list.add("0");
                list.add("0");
            }

            data.add(list);
        }

        content.removeAll();

        JLabel jLabelTile1 = new JLabel("Lợi nhuận theo năm");
        jLabelTile1.setPreferredSize(new Dimension(890, 30));
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        jLabelTile1.setVerticalAlignment(JLabel.CENTER);
        jLabelTile1.setHorizontalAlignment(JLabel.CENTER);
        content.add(jLabelTile1, "wrap");

        CurveLineChart chart = new CurveLineChart();
//        chart.setTitle("Lợi nhuận theo tháng");
        chart.addLegend("Lợi nhuận thuần", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Lợi nhuận từ hoạt động kinh doanh (Lợi nhuận bán hàng - Chi phí nhân viên)", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Thu nhập khác", Color.decode("#0099F7"), Color.decode("#F11712"));
//        chart.addLegend("Thu nhập khác", Color.decode("#0099F5"), Color.decode("#F11710"));
        chart.setForeground(new Color(0x919191));
        chart.setFillColor(true);

        JPanel panelShadow = new JPanel();
        panelShadow.setPreferredSize(new Dimension(890, 650));
        panelShadow.setBackground(new Color(255, 255, 255));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow.setLayout(new BorderLayout());
        panelShadow.add(chart, BorderLayout.CENTER);
        content.add(panelShadow, "wrap");

        List<ModelData> lists = new ArrayList<>();
        for (List<String> list : data) {
            double sale = Double.parseDouble(list.get(1)) - Double.parseDouble(list.get(2)) - Double.parseDouble(list.get(3));
            double excess = Double.parseDouble(list.get(4)) + Double.parseDouble(list.get(5)) + Double.parseDouble(list.get(6));
            double other = Double.parseDouble(list.get(7)) + Double.parseDouble(list.get(8));
            double profit = sale - excess + other;
            lists.add(new ModelData("Năm " + list.get(0), profit, sale - excess, other));

        }

        for (ModelData d : lists) {
            chart.addData(new ModelLineChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
        }
        chart.start();

        content.repaint();
        content.revalidate();
    }

    private void byQuarterReport() {
        List<List<String>> dataSale_Discount = MySQL.getSale_DiscountByQuarter();
        List<List<String>> dataCapitalPrice = MySQL.getCapitalPriceByQuarter();
        List<List<String>> dataSalary_Allowance_Bonus_Deduction_Fine = MySQL.getSalary_Allowance_Bonus_Deduction_FineByQuarter();

        List<Integer> quarterList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Lặp qua từ tháng 1 đến tháng hiện tại
        String[] columns = new String[]{" "};
        for (Month month : Month.values()) {
            LocalDate monthStart = LocalDate.of(today.getYear(), month, 1);
            int quarter = (monthStart.getMonthValue() - 1) / 3 + 1;

            // Nếu tháng hiện tại hoặc trước tháng hiện tại, thêm quý vào danh sách
            if (monthStart.isBefore(today) || monthStart.getMonth() == today.getMonth()) {
                if (!quarterList.contains(quarter)) {
                    quarterList.add(quarter);
                    columns = Arrays.copyOf(columns, columns.length + 1);
                    columns[columns.length - 1] = "Quý " + quarter + "/" + LocalDate.now().getYear();
                }
            }
        }

        columns = Arrays.copyOf(columns, columns.length + 1);
        columns[columns.length - 1] = "Tổng";

        List<List<String>> data = new ArrayList<>();

        for (Integer quarter : quarterList) {
            List<String> list = new ArrayList<>();
            list.add(quarter.toString());

            boolean check = false;
            double value1 = 0;
            for (List<String> strings : dataSale_Discount) {
                if (strings.get(0).equals(quarter.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    value1 = Double.parseDouble(strings.get(1)) - Double.parseDouble(strings.get(2));
                    list.add(String.valueOf(value1));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
            }

            check = false;
            double value2 = 0;
            for (List<String> strings : dataCapitalPrice) {
                if (strings.get(0).equals(quarter.toString())) {
                    list.add(strings.get(1));
                    value2 = value1 - Double.parseDouble(strings.get(1));
                    list.add(String.valueOf(value2));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
            }

            check = false;
            double value3 = 0;
            double value4 = 0;
            double value5 = 0;
            double value6 = 0;
            for (List<String> strings : dataSalary_Allowance_Bonus_Deduction_Fine) {
                if (strings.get(0).equals(quarter.toString())) {
                    value3 = Double.parseDouble(strings.get(1)) + Double.parseDouble(strings.get(2)) + Double.parseDouble(strings.get(3));
                    list.add(String.valueOf(value3));
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    list.add(strings.get(3));
                    value4 = value2 - value3;
                    list.add(String.valueOf(value4));

                    value5 = Double.parseDouble(strings.get(4)) + Double.parseDouble(strings.get(5));
                    list.add(String.valueOf(value5));
                    list.add(strings.get(4));
                    list.add(strings.get(5));
                    value6 = value4 + value5;
                    list.add(String.valueOf(value6));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add(String.valueOf(value2));
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add(String.valueOf(value2));
            }

            data.add(list);
        }

        content.removeAll();

        initTopContent("Báo cáo kết quả hoạt động kinh doanh theo quý");

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(885, 610));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);


        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        salesLabelPanel.setPreferredSize(new Dimension(Math.max(885, (120 * columns.length - 1) + 200), 40));
        addLabelsToPanel(salesLabelPanel, columns, new Dimension(120, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        List<List<String>> convertData = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            convertData.add(new ArrayList<>());
        }
        for (int i = 0; i < data.size(); i++) {  // Duyệt qua hàng
            for (int j = 1; j < 15; j++) {  // Duyệt qua cột
                convertData.get(j - 1).add(data.get(i).get(j));
            }
        }

        int i = 1;
        for (List<String> list : convertData) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(Math.max(885, (120 * columns.length - 1) + 200), 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel label = new JLabel();
            label.setFont(new Font("Inter", Font.BOLD, 13));
            label.setPreferredSize(new Dimension(200, 50));
            panel.add(label);
            if (i == 1) {
                label.setText("Doanh thu bán hàng (1)");
            }
            if (i == 2) {
                label.setText("Chiết khấu hoá đơn (2)");
            }
            if (i == 3) {
                label.setText("Doanh thu thuần (3 = 1-2)");
            }
            if (i == 4) {
                label.setText("Giá vốn sản phẩm (4)");
            }
            if (i == 5) {
                label.setText("Lợi nhuận gộp bán hàng (5 = 3-4)");
            }
            if (i == 6) {
                label.setText("Chi phí (6)");
            }
            if (i == 7) {
                label.setText("     Tiền lương nhân viên");
            }
            if (i == 8) {
                label.setText("     Tiền phụ cấp");
            }
            if (i == 9) {
                label.setText("     Tiền thưởng");
            }
            if (i == 10) {
                label.setText("Lợi nhuận kinh doanh (7 = 5-6)");
            }
            if (i == 11) {
                label.setText("Thu nhập khác (8)");
            }
            if (i == 12) {
                label.setText("     Tiền thu giảm trừ");
            }
            if (i == 13) {
                label.setText("     Tiền thu phạt");
            }
            if (i == 14) {
                label.setText("Lợi nhuận thuần (9 = 7+8)");
            }
            double total = 0;
            for (String string : list) {
                total += Double.parseDouble(string);

                JLabel labelValue = new JLabel(VNString.currency(Double.parseDouble(string)));
                labelValue.setFont(new Font("Inter", Font.PLAIN, 13));
                labelValue.setPreferredSize(new Dimension(120, 50));
                labelValue.setHorizontalAlignment(JLabel.RIGHT);
                panel.add(labelValue);
            }

            JLabel labelTotal = new JLabel(VNString.currency(total));
            labelTotal.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotal.setHorizontalAlignment(JLabel.RIGHT);
            labelTotal.setPreferredSize(new Dimension(120, 50));
            panel.add(labelTotal);

            centerPanel.add(panel, "wrap");
            System.out.println(list);
            i++;
        }

        content.repaint();
        content.revalidate();
    }

    private void byQuarterChart() {
        List<List<String>> dataSale_Discount = MySQL.getSale_DiscountByQuarter();
        List<List<String>> dataCapitalPrice = MySQL.getCapitalPriceByQuarter();
        List<List<String>> dataSalary_Allowance_Bonus_Deduction_Fine = MySQL.getSalary_Allowance_Bonus_Deduction_FineByQuarter();

        List<Integer> quarterList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Lặp qua từ tháng 1 đến tháng hiện tại
        for (Month month : Month.values()) {
            LocalDate monthStart = LocalDate.of(today.getYear(), month, 1);
            int quarter = (monthStart.getMonthValue() - 1) / 3 + 1;

            // Nếu tháng hiện tại hoặc trước tháng hiện tại, thêm quý vào danh sách
            if (monthStart.isBefore(today) || monthStart.getMonth() == today.getMonth()) {
                if (!quarterList.contains(quarter)) {
                    quarterList.add(quarter);
                }
            }
        }

        List<List<String>> data = new ArrayList<>();

        for (Integer quarter : quarterList) {
            List<String> list = new ArrayList<>();
            list.add(quarter.toString());

            boolean check = false;
            for (List<String> strings : dataSale_Discount) {
                if (strings.get(0).equals(quarter.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
                list.add("0");
            }

            check = false;
            for (List<String> strings : dataCapitalPrice) {
                if (strings.get(0).equals(quarter.toString())) {
                    list.add(strings.get(1));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
            }

            check = false;
            for (List<String> strings : dataSalary_Allowance_Bonus_Deduction_Fine) {
                if (strings.get(0).equals(quarter.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    list.add(strings.get(3));
                    list.add(strings.get(4));
                    list.add(strings.get(5));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
                list.add("0");
                list.add("0");
                list.add("0");
                list.add("0");
            }

            data.add(list);
        }

        content.removeAll();

        JLabel jLabelTile1 = new JLabel("Lợi nhuận theo quý");
        jLabelTile1.setPreferredSize(new Dimension(890, 30));
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        jLabelTile1.setVerticalAlignment(JLabel.CENTER);
        jLabelTile1.setHorizontalAlignment(JLabel.CENTER);
        content.add(jLabelTile1, "wrap");

        CurveLineChart chart = new CurveLineChart();
//        chart.setTitle("Lợi nhuận theo tháng");
        chart.addLegend("Lợi nhuận thuần", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Lợi nhuận từ hoạt động kinh doanh (Lợi nhuận bán hàng - Chi phí nhân viên)", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Thu nhập khác", Color.decode("#0099F7"), Color.decode("#F11712"));
//        chart.addLegend("Thu nhập khác", Color.decode("#0099F5"), Color.decode("#F11710"));
        chart.setForeground(new Color(0x919191));
        chart.setFillColor(true);

        JPanel panelShadow = new JPanel();
        panelShadow.setPreferredSize(new Dimension(890, 650));
        panelShadow.setBackground(new Color(255, 255, 255));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow.setLayout(new BorderLayout());
        panelShadow.add(chart, BorderLayout.CENTER);
        content.add(panelShadow, "wrap");

        List<ModelData> lists = new ArrayList<>();
        for (List<String> list : data) {
            double sale = Double.parseDouble(list.get(1)) - Double.parseDouble(list.get(2)) - Double.parseDouble(list.get(3));
            double excess = Double.parseDouble(list.get(4)) + Double.parseDouble(list.get(5)) + Double.parseDouble(list.get(6));
            double other = Double.parseDouble(list.get(7)) + Double.parseDouble(list.get(8));
            double profit = sale - excess + other;
            lists.add(new ModelData("Quý " + list.get(0) + "/" + LocalDate.now().getYear(), profit, sale - excess, other));

        }

        for (ModelData d : lists) {
            chart.addData(new ModelLineChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
        }
        chart.start();

        content.repaint();
        content.revalidate();
    }

    private void byMonthReport() {
        List<List<String>> dataSale_Discount = MySQL.getSale_DiscountByMonth();
        List<List<String>> dataCapitalPrice = MySQL.getCapitalPriceByMonth();
        List<List<String>> dataSalary_Allowance_Bonus_Deduction_Fine = MySQL.getSalary_Allowance_Bonus_Deduction_FineByMonth();

        List<Integer> monthList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String[] columns = new String[]{" "};
        // Duyệt qua các tháng từ tháng 1 đến tháng hiện tại
        for (Month month : Month.values()) {
            // Kiểm tra xem tháng đó đã qua hay chưa
            if (month.compareTo(today.getMonth()) <= 0) {
                monthList.add(month.getValue());
                columns = Arrays.copyOf(columns, columns.length + 1);
                columns[columns.length - 1] = month.getValue() + "/" + LocalDate.now().getYear();
            }
        }
        columns = Arrays.copyOf(columns, columns.length + 1);
        columns[columns.length - 1] = "Tổng";

        List<List<String>> data = new ArrayList<>();

        for (Integer month : monthList) {
            List<String> list = new ArrayList<>();
            list.add(month.toString());

            boolean check = false;
            double value1 = 0;
            for (List<String> strings : dataSale_Discount) {
                if (strings.get(0).equals(month.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    value1 = Double.parseDouble(strings.get(1)) - Double.parseDouble(strings.get(2));
                    list.add(String.valueOf(value1));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
            }

            check = false;
            double value2 = 0;
            for (List<String> strings : dataCapitalPrice) {
                if (strings.get(0).equals(month.toString())) {
                    list.add(strings.get(1));
                    value2 = value1 - Double.parseDouble(strings.get(1));
                    list.add(String.valueOf(value2));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
            }

            check = false;
            double value3 = 0;
            double value4 = 0;
            double value5 = 0;
            double value6 = 0;
            for (List<String> strings : dataSalary_Allowance_Bonus_Deduction_Fine) {
                if (strings.get(1).equals(month.toString())) {
                    value3 = Double.parseDouble(strings.get(2)) + Double.parseDouble(strings.get(3)) + Double.parseDouble(strings.get(4));
                    list.add(String.valueOf(value3));
                    list.add(strings.get(2));
                    list.add(strings.get(3));
                    list.add(strings.get(4));
                    value4 = value2 - value3;
                    list.add(String.valueOf(value4));

                    value5 = Double.parseDouble(strings.get(5)) + Double.parseDouble(strings.get(6));
                    list.add(String.valueOf(value5));
                    list.add(strings.get(5));
                    list.add(strings.get(6));
                    value6 = value4 + value5;
                    list.add(String.valueOf(value6));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add(String.valueOf(value2));
                list.add("0.0");
                list.add("0.0");
                list.add("0.0");
                list.add(String.valueOf(value2));
            }

            data.add(list);
        }

        content.removeAll();

        initTopContent("Báo cáo kết quả hoạt động kinh doanh theo tháng");

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(885, 610));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);


        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        salesLabelPanel.setPreferredSize(new Dimension(Math.max(885, (120 * columns.length - 1) + 200), 40));
        addLabelsToPanel(salesLabelPanel, columns, new Dimension(120, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        List<List<String>> convertData = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            convertData.add(new ArrayList<>());
        }
        for (int i = 0; i < data.size(); i++) {  // Duyệt qua hàng
            for (int j = 1; j < 15; j++) {  // Duyệt qua cột
                convertData.get(j - 1).add(data.get(i).get(j));
            }
        }

        int i = 1;
        for (List<String> list : convertData) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(Math.max(885, (120 * columns.length - 1) + 200), 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel label = new JLabel();
            label.setFont(new Font("Inter", Font.BOLD, 13));
            label.setPreferredSize(new Dimension(200, 50));
            panel.add(label);
            if (i == 1) {
                label.setText("Doanh thu bán hàng (1)");
            }
            if (i == 2) {
                label.setText("Chiết khấu hoá đơn (2)");
            }
            if (i == 3) {
                label.setText("Doanh thu thuần (3 = 1-2)");
            }
            if (i == 4) {
                label.setText("Giá vốn sản phẩm (4)");
            }
            if (i == 5) {
                label.setText("Lợi nhuận gộp bán hàng (5 = 3-4)");
            }
            if (i == 6) {
                label.setText("Chi phí (6)");
            }
            if (i == 7) {
                label.setText("     Tiền lương nhân viên");
            }
            if (i == 8) {
                label.setText("     Tiền phụ cấp");
            }
            if (i == 9) {
                label.setText("     Tiền thưởng");
            }
            if (i == 10) {
                label.setText("Lợi nhuận kinh doanh (7 = 5-6)");
            }
            if (i == 11) {
                label.setText("Thu nhập khác (8)");
            }
            if (i == 12) {
                label.setText("     Tiền thu giảm trừ");
            }
            if (i == 13) {
                label.setText("     Tiền thu phạt");
            }
            if (i == 14) {
                label.setText("Lợi nhuận thuần (9 = 7+8)");
            }
            double total = 0;
            for (String string : list) {
                total += Double.parseDouble(string);

                JLabel labelValue = new JLabel(VNString.currency(Double.parseDouble(string)));
                labelValue.setFont(new Font("Inter", Font.PLAIN, 13));
                labelValue.setPreferredSize(new Dimension(120, 50));
                labelValue.setHorizontalAlignment(JLabel.RIGHT);
                panel.add(labelValue);
            }

            JLabel labelTotal = new JLabel(VNString.currency(total));
            labelTotal.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotal.setHorizontalAlignment(JLabel.RIGHT);
            labelTotal.setPreferredSize(new Dimension(120, 50));
            panel.add(labelTotal);

            centerPanel.add(panel, "wrap");
            System.out.println(list);
            i++;
        }

        content.repaint();
        content.revalidate();
    }

    private void byMonthChart() {
        List<List<String>> dataSale_Discount = MySQL.getSale_DiscountByMonth();
        List<List<String>> dataCapitalPrice = MySQL.getCapitalPriceByMonth();
        List<List<String>> dataSalary_Allowance_Bonus_Deduction_Fine = MySQL.getSalary_Allowance_Bonus_Deduction_FineByMonth();

        List<Integer> monthList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Duyệt qua các tháng từ tháng 1 đến tháng hiện tại
        for (Month month : Month.values()) {
            // Kiểm tra xem tháng đó đã qua hay chưa
            if (month.compareTo(today.getMonth()) <= 0) {
                monthList.add(month.getValue());
            }
        }

        List<List<String>> data = new ArrayList<>();

        for (Integer month : monthList) {
            List<String> list = new ArrayList<>();
            list.add(month.toString());

            boolean check = false;
            for (List<String> strings : dataSale_Discount) {
                if (strings.get(0).equals(month.toString())) {
                    list.add(strings.get(1));
                    list.add(strings.get(2));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
                list.add("0");
            }

            check = false;
            for (List<String> strings : dataCapitalPrice) {
                if (strings.get(0).equals(month.toString())) {
                    list.add(strings.get(1));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
            }

            check = false;
            for (List<String> strings : dataSalary_Allowance_Bonus_Deduction_Fine) {
                if (strings.get(1).equals(month.toString())) {
                    list.add(strings.get(2));
                    list.add(strings.get(3));
                    list.add(strings.get(4));
                    list.add(strings.get(5));
                    list.add(strings.get(6));
                    check = true;
                    break;
                }
            }
            if (!check) {
                list.add("0");
                list.add("0");
                list.add("0");
                list.add("0");
                list.add("0");
            }

            data.add(list);
        }

        content.removeAll();

        JLabel jLabelTile1 = new JLabel("Lợi nhuận theo tháng");
        jLabelTile1.setPreferredSize(new Dimension(890, 30));
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        jLabelTile1.setVerticalAlignment(JLabel.CENTER);
        jLabelTile1.setHorizontalAlignment(JLabel.CENTER);
        content.add(jLabelTile1, "wrap");

        CurveLineChart chart = new CurveLineChart();
//        chart.setTitle("Lợi nhuận theo tháng");
        chart.addLegend("Lợi nhuận thuần", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Lợi nhuận từ hoạt động kinh doanh (Lợi nhuận bán hàng - Chi phí nhân viên)", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Thu nhập khác", Color.decode("#0099F7"), Color.decode("#F11712"));
//        chart.addLegend("Thu nhập khác", Color.decode("#0099F5"), Color.decode("#F11710"));
        chart.setForeground(new Color(0x919191));
        chart.setFillColor(true);

        JPanel panelShadow = new JPanel();
        panelShadow.setPreferredSize(new Dimension(890, 650));
        panelShadow.setBackground(new Color(255, 255, 255));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow.setLayout(new BorderLayout());
        panelShadow.add(chart, BorderLayout.CENTER);
        content.add(panelShadow, "wrap");

        List<ModelData> lists = new ArrayList<>();
        for (List<String> list : data) {
            double sale = Double.parseDouble(list.get(1)) - Double.parseDouble(list.get(2)) - Double.parseDouble(list.get(3));
            double excess = Double.parseDouble(list.get(4)) + Double.parseDouble(list.get(5)) + Double.parseDouble(list.get(6));
            double other = Double.parseDouble(list.get(7)) + Double.parseDouble(list.get(8));
            double profit = sale - excess + other;
            lists.add(new ModelData(list.get(0) + "/" + LocalDate.now().getYear(), profit, sale - excess, other));

        }

        for (ModelData d : lists) {
            chart.addData(new ModelLineChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
        }
        chart.start();

        content.repaint();
        content.revalidate();
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Inter", Font.PLAIN, 14));
        return radioButton;
    }

    private void initTopContent(String tile) {
        JPanel titletTopPanel = new JPanel(new BorderLayout());
        titletTopPanel.setBackground(Color.WHITE);

        titletTopPanel.setPreferredSize(new Dimension(885, 70));
        content.add(titletTopPanel, "wrap");
        // Cập nhật ngày lập
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        JLabel dateLabel = new JLabel("Ngày lập: " + formattedDateTime);
        dateLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        // mặc đinh tiêu đề là báo cáo cuối ngày về bán hàng
        JLabel titleLabel = new JLabel(tile);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(dateLabel);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);

        titletTopPanel.add(datePanel, BorderLayout.NORTH);
        titletTopPanel.add(titlePanel, BorderLayout.CENTER);
    }

    private void addLabelsToPanel(JPanel panel, String[] labels, Dimension dimension) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Inter", Font.BOLD, 13));

            if (i != 0) {
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                jLabel.setPreferredSize(dimension);
            } else {
                jLabel.setPreferredSize(new Dimension(200, 30));
            }
            panel.add(jLabel);
            i++;
        }
    }

    private JPanel createLabelPanel(Color background, Color border) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]10", ""));
        panel.setPreferredSize(new Dimension(868, 40));
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, border));
        return panel;
    }
}
