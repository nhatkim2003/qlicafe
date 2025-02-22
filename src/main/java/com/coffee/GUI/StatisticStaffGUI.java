package com.coffee.GUI;

import com.coffee.BLL.PayrollBLL;
import com.coffee.BLL.Payroll_DetailBLL;
import com.coffee.BLL.Role_DetailBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DAL.MySQL;
import com.coffee.DTO.Payroll;
import com.coffee.DTO.Payroll_Detail;
import com.coffee.DTO.Role_Detail;
import com.coffee.DTO.Staff;
import com.coffee.GUI.components.barchart.BarChart;
import com.coffee.GUI.components.barchart.ModelBarChart;
import com.coffee.GUI.components.line_chart.ModelData;
import com.coffee.GUI.components.line_chart.chart.CurveLineChart;
import com.coffee.GUI.components.line_chart.chart.ModelLineChart;
import com.coffee.GUI.components.pie_chart.ModelPieChart;
import com.coffee.GUI.components.pie_chart.PieChart;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticStaffGUI extends JPanel {
    private JPanel left;
    private JPanel right;
    private JPanel bottom;

    public StatisticStaffGUI() {
        setBackground(new Color(238, 238, 238));
        setPreferredSize(new Dimension(1000, 700));
        setLayout(new BorderLayout());
        init();
        setVisible(true);
    }

    private void init() {
        JPanel top = new JPanel(new BorderLayout());
        top.setPreferredSize(new Dimension(1000, 350));
        top.setBackground(new Color(238, 238, 238));
        add(top, BorderLayout.NORTH);

        bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(1000, 350));
        bottom.setBackground(Color.white);
        add(bottom, BorderLayout.SOUTH);

        left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(580, 350));
        left.setBackground(Color.white);
        top.add(left, BorderLayout.WEST);

        right = new JPanel(new BorderLayout());
        right.setPreferredSize(new Dimension(580, 350));
        right.setBackground(Color.white);
        top.add(right, BorderLayout.EAST);

        loadBarChart();
        loadPieChart();
        loadLineChart();
    }

    public void loadBarChart() {
        List<List<String>> data = MySQL.getTop3BestStaff();
        left.removeAll();

        JLabel jLabelTile1 = new JLabel("Top 3 nhân viên có giờ làm nhiều nhất trong tháng");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 17));
        jLabelTile1.setHorizontalAlignment(JLabel.CENTER);
        left.add(jLabelTile1, BorderLayout.NORTH);

        BarChart barChart = new BarChart();

        barChart.addLegend("Số giờ làm", new Color(1, 120, 220));
        for (List<String> list : data) {
            Staff staff = new StaffBLL().searchStaffs("id = " + list.get(0)).get(0);
            barChart.addData(new ModelBarChart(staff.getName() + " - " + staff.getId(), new double[]{Double.parseDouble(list.get(1))}));
        }
        barChart.start();

        left.add(barChart, BorderLayout.CENTER);

        left.repaint();
        left.revalidate();
    }

    public void loadLineChart() {
        List<List<String>> data = new ArrayList<>();
        List<Role_Detail> managers = new Role_DetailBLL().searchRole_detailsByRole(2, Date.valueOf(LocalDate.now()).toString());
        List<Role_Detail> staffWearHouses = new Role_DetailBLL().searchRole_detailsByRole(3, Date.valueOf(LocalDate.now()).toString());
        List<Role_Detail> staffSales = new Role_DetailBLL().searchRole_detailsByRole(4, Date.valueOf(LocalDate.now()).toString());

        for (Payroll payroll : new PayrollBLL().searchPayrolls("year = YEAR(NOW())")) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(payroll.getMonth()));

            double salary_manager = 0;
            for (Role_Detail role_detail : managers) {
                Payroll_Detail payrollDetail = new Payroll_DetailBLL().searchPayroll_Details("payroll_id = " + payroll.getId(), "staff_id = " + role_detail.getStaff_id()).get(0);
                salary_manager += payrollDetail.getSalary_amount();
            }
            list.add(String.valueOf(salary_manager));

            double salary_staffWearHouse = 0;
            for (Role_Detail role_detail : staffWearHouses) {
                Payroll_Detail payrollDetail = new Payroll_DetailBLL().searchPayroll_Details("payroll_id = " + payroll.getId(), "staff_id = " + role_detail.getStaff_id()).get(0);
                salary_staffWearHouse += payrollDetail.getSalary_amount();
            }
            list.add(String.valueOf(salary_staffWearHouse));

            double salary_staffSale = 0;
            for (Role_Detail role_detail : staffSales) {
                Payroll_Detail payrollDetail = new Payroll_DetailBLL().searchPayroll_Details("payroll_id = " + payroll.getId(), "staff_id = " + role_detail.getStaff_id()).get(0);
                salary_staffSale += payrollDetail.getSalary_amount();
            }
            list.add(String.valueOf(salary_staffSale));

            data.add(list);
        }

        bottom.removeAll();

        JLabel jLabelTile1 = new JLabel("Lương trung bình theo chức vụ trong năm");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 17));
        jLabelTile1.setHorizontalAlignment(JLabel.CENTER);
        bottom.add(jLabelTile1, BorderLayout.NORTH);

        CurveLineChart chart = new CurveLineChart();
        chart.addLegend("Quản lý", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Nhân viên kho", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Nhân viên bán hàng", Color.decode("#0099F7"), Color.decode("#F11712"));
        chart.setForeground(new Color(0x919191));
        chart.setFillColor(true);

        JPanel panelShadow = new JPanel();
        panelShadow.setPreferredSize(new Dimension(1150, 300));
        panelShadow.setBackground(new Color(255, 255, 255));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow.setLayout(new BorderLayout());
        panelShadow.add(chart, BorderLayout.CENTER);
        bottom.add(panelShadow, BorderLayout.CENTER);

        List<ModelData> lists = new ArrayList<>();
        for (List<String> list : data) {
            lists.add(new ModelData(list.get(0) + "/" + LocalDate.now().getYear(), Double.parseDouble(list.get(1)), Double.parseDouble(list.get(2)), Double.parseDouble(list.get(3))));
        }

        for (ModelData d : lists) {
            chart.addData(new ModelLineChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
        }
        chart.start();

        bottom.repaint();
        bottom.revalidate();
    }

    public void loadPieChart() {
        List<Role_Detail> managers = new Role_DetailBLL().searchRole_detailsByRole(2, Date.valueOf(LocalDate.now()).toString());
        List<Role_Detail> staffWearHouses = new Role_DetailBLL().searchRole_detailsByRole(3, Date.valueOf(LocalDate.now()).toString());
        List<Role_Detail> staffSales = new Role_DetailBLL().searchRole_detailsByRole(4, Date.valueOf(LocalDate.now()).toString());

        right.removeAll();
        JPanel jPanel = new JPanel(new MigLayout("", "0[100%]0", ""));
        jPanel.setBackground(new Color(255, 255, 255));

        JLabel jLabelTile1 = new JLabel("Thống kê chức vụ");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 17));
        jLabelTile1.setHorizontalAlignment(JLabel.CENTER);
        jPanel.add(jLabelTile1, "center, wrap, span");
        right.add(jPanel, BorderLayout.NORTH);

        JPanel jPanelNotice = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanelNotice.setBackground(new Color(255, 255, 255));

        JLabel jLabelAvailable = new JLabel("Quản lý");
        JPanel jPanelAvailable = new JPanel();
        jPanelAvailable.setPreferredSize(new Dimension(10, 10));
        jPanelAvailable.setBackground(new Color(189, 135, 245));

        jPanelNotice.add(jPanelAvailable);
        jPanelNotice.add(jLabelAvailable);

        JLabel jLabelDone = new JLabel("Nhân viên kho");
        JPanel jPanelDone = new JPanel();
        jPanelDone.setPreferredSize(new Dimension(10, 10));
        jPanelDone.setBackground(new Color(135, 189, 245));

        jPanelNotice.add(jPanelDone);
        jPanelNotice.add(jLabelDone);

        JLabel jLabelAbsent = new JLabel("Nhân viên bán hàng");
        JPanel jPanelAbsent = new JPanel();
        jPanelAbsent.setPreferredSize(new Dimension(10, 10));
        jPanelAbsent.setBackground(new Color(139, 229, 184));

        jPanelNotice.add(jPanelAbsent);
        jPanelNotice.add(jLabelAbsent);

        jPanel.add(jPanelNotice, "center, wrap, span");

        PieChart pieChart = new PieChart();
        pieChart.setFont(new java.awt.Font("Inter", Font.BOLD, 14));
        pieChart.setChartType(PieChart.PeiChartType.DEFAULT);
        pieChart.addData(new ModelPieChart("Quản lý", managers.size(), new Color(189, 135, 245)));
        pieChart.addData(new ModelPieChart("Nhân viên kho", staffWearHouses.size(), new Color(135, 189, 245)));
        pieChart.addData(new ModelPieChart("Nhân viên bán hàng", staffSales.size(), new Color(139, 229, 184)));

        right.add(pieChart, BorderLayout.CENTER);

        right.repaint();
        right.revalidate();
    }


}
