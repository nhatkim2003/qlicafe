package com.coffee.GUI;

import com.coffee.BLL.ReceiptBLL;
import com.coffee.DAL.MySQL;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailReceiptGUI;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.barchart.BarChart;
import com.coffee.GUI.components.barchart.ModelBarChart;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class StatisticSaleGUI extends JPanel {
    private JScrollPane scrollPane;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private JPanel content;
    private RoundedPanel displayTypePanel;

    private JLabel dateLabel;
    private JLabel titleLabel = new JLabel();
    JLabel dateReportLabel;
    private JScrollPane scrollPaneDetail;
    private JPanel centerPanel;
    JLabel titleChart;
    private static final int PANEL_WIDTH = 885;
    private static final int PANEL_HEIGHT = 40;
    private static final Dimension LABEL_SIZE = new Dimension(150, 30);
    Map<JPanel, Boolean> expandedStateMap = new HashMap<>(); // Biến để theo dõi trạng thái của nút btnDetail

    JRadioButton timeRadioButton;
    JRadioButton profitRadioButton;
    JRadioButton discountRadioButton;
    JRadioButton categoryRadioButton;

    JRadioButton chartRadioButton = createRadioButton("Biểu đồ");

    JRadioButton reportRadioButton = createRadioButton("Báo cáo");
    ButtonGroup btnGroupDisplayType = new ButtonGroup();

    public StatisticSaleGUI() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 700));
        setLayout(new BorderLayout());
        init();
        setVisible(true);
    }

    private void init() {
        datePicker = new DatePicker();
        editor = new JFormattedTextField();
        datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setEditor(editor);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.setSelectedDateRange(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now())); // bao loi o day
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                loadData();
            }
        });

        editor.setPreferredSize(new Dimension(240, 30));
        editor.setFont(new Font("Inter", Font.BOLD, 13));
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 700));
        add(scrollPane, BorderLayout.EAST);
        initRightBartEndOfDayPanel();

        content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(255, 255, 255));
        add(content, BorderLayout.CENTER);
        initTopContent();
        initCenterContent();
        timeReport();
        chartRadioButton.addActionListener(e -> {
            loadData();
        });
        reportRadioButton.addActionListener(e -> {
            loadData();
        });
    }

    private void timeChart() {
        content.removeAll();
        content.setLayout(new MigLayout("", "[]", "10[]10"));
        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        assert start != null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedStartDate = dateFormat.format(start);
        String formattedEndDate = dateFormat.format(end);
        titleChart = new JLabel("Doanh thu thuần  ");
        titleChart.setFont(new Font("Inter", Font.BOLD, 18));

        JLabel dateChart = new JLabel();
        if (start.equals(end)) {
            dateChart.setText("Ngày " + formattedStartDate);
        } else {
            dateChart.setText("Từ ngày " + formattedStartDate + " đến ngày " + formattedEndDate);
        }
        content.add(titleChart, "center, span, wrap");
        content.add(dateChart, "center, span, wrap");

        BarChart barChartTime = new BarChart();
        barChartTime.setPreferredSize(new Dimension(1000, 800));
        barChartTime.addLegend("Doanh thu", new Color(135, 189, 245));
        List<List<String>> saleTimes = MySQL.getSaleTime(start.toString(), end.toString());
        for (List<String> list : saleTimes) {
            barChartTime.addData(new ModelBarChart(list.get(0) , new double[]{Double.parseDouble(list.get(4))}));
        }
        barChartTime.start();
//        barChartProfitProduct.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));

        content.add(barChartTime, "wrap");
        content.repaint();
        content.revalidate();
    }

    private void profitChart() {
        content.removeAll();
        content.setLayout(new MigLayout("", "[]", "10[]10"));
        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        assert start != null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedStartDate = dateFormat.format(start);
        String formattedEndDate = dateFormat.format(end);
        titleChart = new JLabel("Lợi nhuận  ");
        titleChart.setFont(new Font("Inter", Font.BOLD, 18));
        JLabel dateChart = new JLabel();
        if (start.equals(end)) {
            dateChart.setText("Ngày " + formattedStartDate);
        } else {
            dateChart.setText("Từ ngày " + formattedStartDate + " đến ngày " + formattedEndDate);
        }
        content.add(titleChart, "center, span, wrap");
        content.add(dateChart, "center, span, wrap");

        BarChart barChartProfit = new BarChart();
        barChartProfit.setPreferredSize(new Dimension(1000, 800));
        barChartProfit.addLegend("Lợi nhuận", new Color(129, 197, 130));
        barChartProfit.addLegend("Doanh thu", new Color(135, 189, 245));
        barChartProfit.addLegend("Giá vốn", new Color(255, 200, 51));
        List<List<String>> saleProfit = MySQL.getProfitInvoice(start.toString(), end.toString());
        for (List<String> list : saleProfit) {
            barChartProfit.addData(new ModelBarChart(list.get(0) , new double[]{Double.parseDouble(list.get(5)),Double.parseDouble(list.get(3)),Double.parseDouble(list.get(4))}));
        }
        barChartProfit.start();
//        barChartProfitProduct.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartProfit, "wrap");
        content.repaint();
        content.revalidate();
    }

    private void loadData() {
        if (timeRadioButton.isSelected()) {
            if (chartRadioButton.isSelected()) {
                timeChart();
            } else if (reportRadioButton.isSelected()) {
                timeReport();
            }
//
        } else if (profitRadioButton.isSelected()) {
            if (chartRadioButton.isSelected()) {
                profitChart();
            } else if (reportRadioButton.isSelected()) {
                profitReport();
            }
        } else if (discountRadioButton.isSelected()) {
            discountReport();
        } else if (categoryRadioButton.isSelected()) {
            categoryReport();
        }
    }

    private void initTopContent() {
        JPanel titletTopPanel = new JPanel(new BorderLayout());
        titletTopPanel.setBackground(Color.WHITE);

        titletTopPanel.setPreferredSize(new Dimension(885, 100));
        content.add(titletTopPanel);
        // Cập nhật ngày lập
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        dateLabel = new JLabel("Ngày lập: " + formattedDateTime);
        dateLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        // Cập nhật ngày bán
        dateReportLabel = new JLabel();
        dateReportLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        assert start != null;
        updateTitle((Date) start, (Date) end);

        // mặc đinh tiêu đề là báo cáo cuối ngày về bán hàng
        titleLabel .setText("Báo cáo bán hàng theo thời gian");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(dateLabel);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel dateReportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dateReportPanel.setBackground(Color.WHITE);
        dateReportPanel.add(dateReportLabel);

        titletTopPanel.add(datePanel, BorderLayout.NORTH);
        titletTopPanel.add(titlePanel, BorderLayout.CENTER);
        titletTopPanel.add(dateReportPanel, BorderLayout.SOUTH);

    }

    private void initCenterContent() {
        centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(new Color(255, 255, 255));
        scrollPaneDetail = new JScrollPane();
        scrollPaneDetail.setPreferredSize(new Dimension(PANEL_WIDTH, 580));
        scrollPaneDetail.setViewportView(centerPanel);
        scrollPaneDetail.setBorder(null);

        content.add(scrollPaneDetail);
    }

    private JPanel createLabelPanel(Color background, LayoutManager layoutManager) {
        JPanel panel = new JPanel();
        panel.setLayout(layoutManager);
        panel.setPreferredSize(new Dimension(868, PANEL_HEIGHT));
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));
        return panel;
    }

    private void addLabelsToPanel(JPanel panel, String[] labels, int customRight, Font font) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(font);
            jLabel.setPreferredSize(LABEL_SIZE);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            if (i == 0) jLabel.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == customRight) jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(jLabel);
            i++;
        }
    }

    private void addLabelsToPanelDiscount(JPanel panel, String[] labels, Font font) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(font);
            jLabel.setPreferredSize(LABEL_SIZE);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            if (i == 0) jLabel.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == 2 || i == 3) jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(jLabel);
            i++;
        }
    }

    private void addLabelsToPanelProfit(JPanel panel, String[] labels, Font font) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(font);
            jLabel.setPreferredSize(LABEL_SIZE);
            if (i == 0)
                jLabel.setHorizontalAlignment(SwingConstants.LEFT);
            else {
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            panel.add(jLabel);
            i++;
        }
    }

    private void timeReport() {
        content.removeAll();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(255, 255, 255));

        initTopContent();
        initCenterContent();

        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }

        assert start != null;
        titleLabel.setText("Báo cáo bán hàng theo thời gian");
        updateTitle((Date) start, (Date) end);
        centerPanel.removeAll();
        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        addLabelsToPanel(salesLabelPanel, new String[]{"Thời gian", "SL đơn bán", "Tổng tiền hàng", "Giảm giá HĐ", "Doanh thu"}, 4, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesLabelPanel, "wrap");

        List<Pair<List<String>, List<List<String>>>> saleTimes = MySQL.getSalesTime(start.toString(), end.toString());

        double billQuantity = 0;
        BigDecimal totalListedPrice = BigDecimal.ZERO;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalDifference = BigDecimal.ZERO;
        for (Pair<List<String>, List<List<String>>> list : saleTimes) {
            List<String> keyList = list.getKey();
            List<List<String>> invoices = list.getValue();
            if (!keyList.isEmpty() && !invoices.isEmpty()) {
                billQuantity += Double.parseDouble(keyList.get(1));
                totalListedPrice = totalListedPrice.add(new BigDecimal(keyList.get(2)));
                totalDifference = totalDifference.add(new BigDecimal(keyList.get(3)));
                totalRevenue = totalRevenue.add(new BigDecimal(keyList.get(4)));
            }
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String formattedQuantity = numberFormat.format(billQuantity);
        String formattedListedPrice = numberFormat.format(totalListedPrice);
        String formattedRevenue = numberFormat.format(totalRevenue);
        String formattedDifference = numberFormat.format(totalDifference);
        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new MigLayout("", "10[]20[]20[]20[]20[]20[]10", ""));
        addLabelsToPanel(salesDataPanel, new String[]{"", formattedQuantity, formattedListedPrice, formattedDifference, formattedRevenue}, 4, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesDataPanel, "wrap");

        for (Pair<List<String>, List<List<String>>> list : saleTimes) {
            List<String> keyList = list.getKey();
            List<List<String>> invoices = list.getValue();
            if (!keyList.isEmpty() && !invoices.isEmpty()) {
                JPanel summaryPanel = createPanelProduct(keyList, invoices);
                expandedStateMap.put(summaryPanel, false); // Ban đầu, tất cả các panel đều không mở rộng
                centerPanel.add(summaryPanel, "wrap");
            }
        }

        centerPanel.repaint();
        centerPanel.revalidate();
        content.repaint();
        content.revalidate();
    }

    private void profitReport() {
        content.removeAll();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(255, 255, 255));

        initTopContent();
        initCenterContent();

        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }

        assert start != null;
        titleLabel.setText("Báo cáo bán hàng theo lợi nhuân");
        updateTitle((Date) start, (Date) end);
        centerPanel.removeAll();
        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        addLabelsToPanelProfit(salesLabelPanel, new String[]{"Thời gian", "Tổng tiền hàng", "Giảm giá HĐ", "Doanh thu", "Tổng giá vốn", "Lợi nhuận"}, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesLabelPanel, "wrap");

        List<Pair<List<String>, List<List<String>>>> saleProfits = MySQL.getSalesProfit(start.toString(), end.toString());

        BigDecimal totalListedPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;

        for (Pair<List<String>, List<List<String>>> list : saleProfits) {
            List<String> keyList = list.getKey();
            List<List<String>> invoices = list.getValue();
            if (!keyList.isEmpty() && !invoices.isEmpty()) {
                totalListedPrice = totalListedPrice.add(new BigDecimal(keyList.get(1)));
                totalDiscount = totalDiscount.add(new BigDecimal(keyList.get(2)));
                totalRevenue = totalRevenue.add(new BigDecimal(keyList.get(3)));
                totalCostPrice = totalCostPrice.add(new BigDecimal(keyList.get(4)));
                totalProfit = totalProfit.add(new BigDecimal(keyList.get(5)));
            }
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String formattedListedPrice = numberFormat.format(totalListedPrice);
        String formattedDiscount = numberFormat.format(totalDiscount);
        String formattedRevenue = numberFormat.format(totalRevenue);
        String formattedCostPrice = numberFormat.format(totalCostPrice);
        String formattedProfit = numberFormat.format(totalProfit);

        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new MigLayout("", "10[]20[]20[]20[]20[]20[]10", ""));
        addLabelsToPanelProfit(salesDataPanel, new String[]{"", formattedListedPrice, formattedDiscount, formattedRevenue, formattedCostPrice, formattedProfit}, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesDataPanel, "wrap");

        for (Pair<List<String>, List<List<String>>> list : saleProfits) {
            List<String> keyList = list.getKey();
            List<List<String>> invoices = list.getValue();
            if (!keyList.isEmpty() && !invoices.isEmpty()) {
                JPanel summaryPanel = createPanelProfit(keyList, invoices);
                expandedStateMap.put(summaryPanel, false); // Ban đầu, tất cả các panel đều không mở rộng
                centerPanel.add(summaryPanel, "wrap");
            }
        }

        centerPanel.repaint();
        centerPanel.revalidate();
        content.repaint();
        content.revalidate();
    }

    private void discountReport() {
        content.removeAll();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(255, 255, 255));

        initTopContent();
        initCenterContent();

        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        updateTitle((Date) start, (Date) end);
        titleLabel.setText("Báo cáo bán hàng theo giảm giá HĐ");
        assert start != null;
        centerPanel.removeAll();
        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        addLabelsToPanelDiscount(salesLabelPanel, new String[]{"Mã giao dịch", "Thời gian", "Giá trị HĐ", "Giảm giá HĐ", "Hình thức"}, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesLabelPanel, "wrap");

        List<List<String>> listBill = MySQL.getInvoiceDiscount(start.toString(), end.toString());

        double billQuantity = 0;
        BigDecimal totalListedPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (List<String> list : listBill) {
            billQuantity++;
            totalListedPrice = totalListedPrice.add(new BigDecimal(list.get(2)));
            totalDiscount = totalDiscount.add(new BigDecimal(list.get(3)));
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String formattedQuantity = numberFormat.format(billQuantity);
        String formattedListedPrice = numberFormat.format(totalListedPrice);
        String formattedDiscountPrice = numberFormat.format(totalDiscount);

        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        addLabelsToPanelDiscount(salesDataPanel, new String[]{"Số lượng HĐ" + formattedQuantity, "", formattedListedPrice, formattedDiscountPrice, ""}, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesDataPanel, "wrap");
        for (List<String> list : listBill) {
            JPanel dataLabelPanel = createLabelPanel(new Color(255, 255, 255), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
            addLabelsToPanelDetailDiscount(dataLabelPanel, list.toArray(new String[0]));
            centerPanel.add(dataLabelPanel, "wrap");
        }

        centerPanel.repaint();
        centerPanel.revalidate();
        content.repaint();
        content.revalidate();
    }

    private void categoryReport() {
        content.removeAll();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(255, 255, 255));

        initTopContent();
        initCenterContent();

        java.util.Date start = null;
        java.util.Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }

        assert start != null;
        updateTitle((Date) start, (Date) end);
        titleLabel .setText("Báo cáo bán hàng theo nhóm hàng hóa");
        centerPanel.removeAll();
        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        addLabelsToPanel(salesLabelPanel, new String[]{"Nhóm hàng", "Số lượng bán", "", "", "Doanh thu"}, 4, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesLabelPanel, "wrap");


        List<List<String>> saleCategories = MySQL.getSaleCategory(start.toString(), end.toString());


        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new MigLayout("", "10[]20[]20[]20[]20[]20[]10", ""));

        long quantity_Category = saleCategories.size();
        double quantity_Product = 0;
        BigDecimal revenue = BigDecimal.ZERO;

        for (List<String> saleCategory : saleCategories) {
            quantity_Product += Double.parseDouble(saleCategory.get(1));
            revenue = revenue.add(new BigDecimal(saleCategory.get(2)));
        }

        String formattedNumberQuantity = NumberFormat.getNumberInstance(Locale.US).format(quantity_Product);
        String formattedNumberRevenue = NumberFormat.getNumberInstance(Locale.US).format(revenue);
        addLabelsToPanel(salesDataPanel, new String[]{"Sl nhóm hàng: " + quantity_Category, formattedNumberQuantity, "", "", formattedNumberRevenue,}, 4, new Font("Inter", Font.BOLD, 13));
        centerPanel.add(salesDataPanel, "wrap");

        for (List<String> saleCategory : saleCategories) {
            JPanel salesDataPanel1 = createLabelPanel(new Color(255, 255, 255), new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
            double saleProductNumber = Double.parseDouble(saleCategory.get(1));
            String formattedNumber = NumberFormat.getNumberInstance(Locale.US).format(saleProductNumber);
            double revenueDetail = Double.parseDouble(saleCategory.get(2));
            String formattedNumber1 = NumberFormat.getNumberInstance(Locale.US).format(revenueDetail);
            addLabelsToPanel(salesDataPanel1, new String[]{saleCategory.get(0), formattedNumber, "", "", formattedNumber1}, 4, new Font("Inter", Font.PLAIN, 13));
            centerPanel.add(salesDataPanel1, "wrap");
        }
        centerPanel.repaint();
        centerPanel.revalidate();
        content.repaint();
        content.revalidate();
    }

    private JPanel createPanelProduct(List<String> keyList, List<List<String>> invoices) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        panel.setPreferredSize(new Dimension(868, 50));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

        JPanel containerBtn = new JPanel();
        containerBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        containerBtn.setPreferredSize(new Dimension(150, 30));
        containerBtn.setBackground(Color.WHITE);

        JButton btnDetail = new JButton();
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setIcon(new FlatSVGIcon("icon/add-square-svgrepo-com.svg")); // Đặt biểu tượng ban đầu
        btnDetail.setBorderPainted(false);
        btnDetail.setFocusPainted(false);
        btnDetail.setContentAreaFilled(false);
        btnDetail.addActionListener(e -> {
            JPanel selectedPanel = (JPanel) btnDetail.getParent().getParent(); // Lấy panel cha của nút
            togglePanel(btnDetail, selectedPanel, invoices);// Kích hoạt chức năng mở rộng/ thu gọn khi nhấn nút
        });

        JLabel lblDate = new JLabel(keyList.get(0));
        lblDate.setFont(new Font("Inter", Font.BOLD, 13));
        lblDate.setForeground(new Color(33, 113, 188));


        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        BigDecimal quntity = new BigDecimal(keyList.get(1));
        String formattedQuantity = numberFormat.format(quntity);
        JLabel lblQuantity = new JLabel(formattedQuantity);
        lblQuantity.setFont(new Font("Inter", Font.PLAIN, 13));
        lblQuantity.setHorizontalAlignment(SwingConstants.CENTER);
        lblQuantity.setPreferredSize(new Dimension(150, 50));

        containerBtn.add(btnDetail);
        containerBtn.add(lblDate);

        BigDecimal ListedPrice = new BigDecimal(keyList.get(2));
        String formattedListedPrice = numberFormat.format(ListedPrice);
        JLabel lblListedPrice = new JLabel(formattedListedPrice);
        lblListedPrice.setFont(new Font("Inter", Font.PLAIN, 13));
        lblListedPrice.setHorizontalAlignment(SwingConstants.CENTER);
        lblListedPrice.setPreferredSize(new Dimension(150, 50));

        BigDecimal PriceDiscount = new BigDecimal(keyList.get(3));
        String formattedPriceDiscount = numberFormat.format(PriceDiscount);
        JLabel lblPriceDiscount = new JLabel(formattedPriceDiscount);
        lblPriceDiscount.setFont(new Font("Inter", Font.PLAIN, 13));
        lblPriceDiscount.setPreferredSize(new Dimension(150, 50));
        lblPriceDiscount.setHorizontalAlignment(SwingConstants.CENTER);

        BigDecimal revenue = new BigDecimal(keyList.get(4));
        String formattedRevenue = numberFormat.format(revenue);
        JLabel lblRevenue = new JLabel(formattedRevenue);
        lblRevenue.setFont(new Font("Inter", Font.PLAIN, 13));
        lblRevenue.setPreferredSize(new Dimension(150, 50));
        lblRevenue.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(containerBtn);
        panel.add(lblQuantity);
        panel.add(lblListedPrice);
        panel.add(lblPriceDiscount);
        panel.add(lblRevenue);


        return panel;
    }

    private JPanel createPanelProfit(List<String> keyList, List<List<String>> invoices) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        panel.setPreferredSize(new Dimension(868, 50));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

        JPanel containerBtn = new JPanel();
        containerBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        containerBtn.setPreferredSize(new Dimension(150, 30));
        containerBtn.setBackground(Color.WHITE);

        JButton btnDetail = new JButton();
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setIcon(new FlatSVGIcon("icon/add-square-svgrepo-com.svg")); // Đặt biểu tượng ban đầu
        btnDetail.setBorderPainted(false);
        btnDetail.setFocusPainted(false);
        btnDetail.setContentAreaFilled(false);
        btnDetail.addActionListener(e -> {
            JPanel selectedPanel = (JPanel) btnDetail.getParent().getParent(); // Lấy panel cha của nút
            togglePanelProfit(btnDetail, selectedPanel, invoices);// Kích hoạt chức năng mở rộng/ thu gọn khi nhấn nút
        });

        JLabel lblDate = new JLabel(keyList.get(0));
        lblDate.setFont(new Font("Inter", Font.BOLD, 13));
        lblDate.setForeground(new Color(33, 113, 188));


        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        containerBtn.add(btnDetail);
        containerBtn.add(lblDate);

        BigDecimal ListedPrice = new BigDecimal(keyList.get(1));
        String formattedListedPrice = numberFormat.format(ListedPrice);
        JLabel lblListedPrice = new JLabel(formattedListedPrice);
        lblListedPrice.setFont(new Font("Inter", Font.PLAIN, 13));
        lblListedPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        lblListedPrice.setPreferredSize(new Dimension(150, 50));

        BigDecimal PriceDiscount = new BigDecimal(keyList.get(2));
        String formattedPriceDiscount = numberFormat.format(PriceDiscount);
        JLabel lblPriceDiscount = new JLabel(formattedPriceDiscount);
        lblPriceDiscount.setFont(new Font("Inter", Font.PLAIN, 13));
        lblPriceDiscount.setPreferredSize(new Dimension(150, 50));
        lblPriceDiscount.setHorizontalAlignment(SwingConstants.RIGHT);

        BigDecimal revenue = new BigDecimal(keyList.get(3));
        String formattedRevenue = numberFormat.format(revenue);
        JLabel lblRevenue = new JLabel(formattedRevenue);
        lblRevenue.setFont(new Font("Inter", Font.PLAIN, 13));
        lblRevenue.setPreferredSize(new Dimension(150, 50));
        lblRevenue.setHorizontalAlignment(SwingConstants.RIGHT);

        BigDecimal totalCostPrice = new BigDecimal(keyList.get(4));
        String formattedTotalCostPrice = numberFormat.format(totalCostPrice);
        JLabel lblTotalCostPrice = new JLabel(formattedTotalCostPrice);
        lblTotalCostPrice.setFont(new Font("Inter", Font.PLAIN, 13));
        lblTotalCostPrice.setPreferredSize(new Dimension(150, 50));
        lblTotalCostPrice.setHorizontalAlignment(SwingConstants.RIGHT);


        BigDecimal totalProfit = new BigDecimal(keyList.get(5));
        String formattedTotalProfit = numberFormat.format(totalProfit);
        JLabel lblTotalProfit = new JLabel(formattedTotalProfit);
        lblTotalProfit.setFont(new Font("Inter", Font.PLAIN, 13));
        lblTotalProfit.setPreferredSize(new Dimension(150, 50));
        lblTotalProfit.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(containerBtn);

        panel.add(lblListedPrice);
        panel.add(lblPriceDiscount);
        panel.add(lblRevenue);
        panel.add(lblTotalCostPrice);
        panel.add(lblTotalProfit);

        return panel;
    }

    private void togglePanel(JButton btnDetail, JPanel selectedPanel, List<List<String>> invoices) {
        boolean isExpanded = expandedStateMap.get(selectedPanel); // Lấy trạng thái mở rộng của panel được chọn
        if (isExpanded) {
            removeDetailPanels(selectedPanel);
            btnDetail.setIcon(new FlatSVGIcon("icon/add-square-svgrepo-com.svg"));
        } else {
            addDetailPanelProduct(selectedPanel, invoices); // Thêm danh sách hóa đơn khi mở rộng
            btnDetail.setIcon(new FlatSVGIcon("icon/minus-square-svgrepo-com.svg"));
        }
        expandedStateMap.put(selectedPanel, !isExpanded); // Cập nhật trạng thái mở rộng
    }

    private void togglePanelProfit(JButton btnDetail, JPanel selectedPanel, List<List<String>> invoices) {
        boolean isExpanded = expandedStateMap.get(selectedPanel); // Lấy trạng thái mở rộng của panel được chọn
        if (isExpanded) {
            removeDetailPanels(selectedPanel);
            btnDetail.setIcon(new FlatSVGIcon("icon/add-square-svgrepo-com.svg"));
        } else {
            addDetailPanelProfit(selectedPanel, invoices); // Thêm danh sách hóa đơn khi mở rộng
            btnDetail.setIcon(new FlatSVGIcon("icon/minus-square-svgrepo-com.svg"));
        }
        expandedStateMap.put(selectedPanel, !isExpanded); // Cập nhật trạng thái mở rộng
    }

    private void removeDetailPanels(JPanel selectedPanel) {
        int selectedIndex = centerPanel.getComponentZOrder(selectedPanel);

        for (int i = selectedIndex + 1; i < centerPanel.getComponentCount(); i++) {
            Component component = centerPanel.getComponent(i);

            Boolean isDetailPanel = (Boolean) ((JPanel) component).getClientProperty("isDetailPanel");
            if (isDetailPanel != null && isDetailPanel) {
                centerPanel.remove(component);
                i--;
            } else break;
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }


    private void addDetailPanelProduct(JPanel selectedPanel, List<List<String>> invoices) {
        for (List<String> invoiceDetail : invoices) {
            int selectedIndex1 = centerPanel.getComponentZOrder(selectedPanel);
            JPanel panel = createPanelDetail(invoiceDetail.toArray(new String[0]));
//            int selectedIndex = centerPanel.getComponentZOrder(selectedPanel);
            centerPanel.add(panel, "growx, wrap", selectedIndex1 + 1);
        }
        int selectedIndex = centerPanel.getComponentZOrder(selectedPanel);
        JPanel salesDataPanel = createLabelPanel(new Color(215, 239, 207), new MigLayout("", "10[]20[]20[]20[]20[]20[]10", ""));
        addLabelsToPanel(salesDataPanel, new String[]{"Mã giao dịch", "Thời gian", "Tổng tiền hàng", "Giảm giá HĐ", "Doanh thu"}, 4, new Font("Inter", Font.BOLD, 13));
        salesDataPanel.putClientProperty("isDetailPanel", true);
        centerPanel.add(salesDataPanel, "growx, wrap", selectedIndex + 1);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void addDetailPanelProfit(JPanel selectedPanel, List<List<String>> invoices) {
        for (List<String> invoiceDetail : invoices) {
            int selectedIndex1 = centerPanel.getComponentZOrder(selectedPanel);
            JPanel panel = createPanelDetailProfit(invoiceDetail.toArray(new String[0]));
//            int selectedIndex = centerPanel.getComponentZOrder(selectedPanel);
            centerPanel.add(panel, "growx, wrap", selectedIndex1 + 1);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel createPanelDetail(String[] labels) {
        JPanel panel = createLabelPanel(Color.WHITE, new MigLayout("", "10[]20[]20[]20[]20[]10", ""));
        panel.putClientProperty("isDetailPanel", true); // Đặt thuộc tính cho panel chi tiết
        addLabelsToPanelDetailTime(panel, labels);
        return panel;
    }

    private JPanel createPanelDetailProfit(String[] labels) {
        JPanel panel = createLabelPanel(Color.WHITE, new MigLayout("", "10[][]20[]20[]20[]10", ""));
        panel.putClientProperty("isDetailPanel", true); // Đặt thuộc tính cho panel chi tiết
        addLabelsToPanelDetailProfit(panel, labels);
        return panel;
    }

    private void addLabelsToPanelDetailTime(JPanel panel, String[] labels) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Inter", Font.PLAIN, 13));
            jLabel.setPreferredSize(LABEL_SIZE);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);

            if (i == 0) {
                if (!label.startsWith("HD")) {
                    int invoiceNumber = Integer.parseInt(label);
                    String formattedInvoiceNumber = String.format("HD%06d", invoiceNumber); // Định dạng số thành chuỗi "HD000001"
                    jLabel.setText(formattedInvoiceNumber);
                }
                jLabel.setHorizontalAlignment(SwingConstants.LEFT);
                jLabel.setForeground(new Color(33, 113, 188));
                jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                jLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String labelText = jLabel.getText();
                        if (labelText.startsWith("HD")) {
                            String numberText = labelText.substring(2); // Cắt bỏ "HD" từ chuỗi
                            try {
                                int invoiceNumber = Integer.parseInt(numberText);
                                new DetailReceiptGUI(new ReceiptBLL().searchReceipts("id = " + invoiceNumber).get(0));
                            } catch (NumberFormatException ex) {
                                System.out.println("Lỗi: không thể chuyển đổi thành số nguyên");
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        jLabel.setForeground(new Color(100, 157, 204));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        jLabel.setForeground(new Color(33, 113, 188));
                    }
                });
            }

            if (i == 2 || i == 3) {
                String formattedRevenue = NumberFormat.getNumberInstance(Locale.US).format(new BigDecimal(label));
                jLabel.setText(formattedRevenue);
                jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
            if (i == 4) {
                String formattedRevenue = NumberFormat.getNumberInstance(Locale.US).format(new BigDecimal(label));
                jLabel.setText(formattedRevenue);
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            }

            panel.add(jLabel);
            i++;
        }
    }

    private void addLabelsToPanelDetailProfit(JPanel panel, String[] labels) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Inter", Font.PLAIN, 13));
            jLabel.setPreferredSize(LABEL_SIZE);
            jLabel.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == 0) {
                jLabel.setPreferredSize(new Dimension(110, 30));
                if (!label.startsWith("HD")) {
                    int invoiceNumber = Integer.parseInt(label);
                    String formattedInvoiceNumber = String.format("HD%06d", invoiceNumber); // Định dạng số thành chuỗi "HD000001"
                    jLabel.setText(formattedInvoiceNumber);
                }
                jLabel.setForeground(new Color(33, 113, 188));
                jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                jLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String labelText = jLabel.getText();
                        if (labelText.startsWith("HD")) {
                            String numberText = labelText.substring(2); // Cắt bỏ "HD" từ chuỗi
                            try {
                                int invoiceNumber = Integer.parseInt(numberText);
                                new DetailReceiptGUI(new ReceiptBLL().searchReceipts("id = " + invoiceNumber).get(0));
                            } catch (NumberFormatException ex) {
                                System.out.println("Lỗi: không thể chuyển đổi thành số nguyên");
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        jLabel.setForeground(new Color(100, 157, 204));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        jLabel.setForeground(new Color(33, 113, 188));
                    }
                });
            } else if (i == 1) {
                jLabel.setPreferredSize(new Dimension(30, 30));
            } else {
//                long temp = Long.parseLong(label);
                String formatted = NumberFormat.getNumberInstance(Locale.US).format(new BigDecimal(label));
                jLabel.setText(formatted);
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            }


            panel.add(jLabel);
            i++;
        }
    }

    private void addLabelsToPanelDetailDiscount(JPanel panel, String[] labels) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Inter", Font.PLAIN, 13));
            jLabel.setPreferredSize(LABEL_SIZE);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            if (i == 0) {
                jLabel.setHorizontalAlignment(SwingConstants.LEFT);
                if (!label.startsWith("HD")) {
                    int invoiceNumber = Integer.parseInt(label);
                    String formattedInvoiceNumber = String.format("HD%06d", invoiceNumber); // Định dạng số thành chuỗi "HD000001"
                    jLabel.setText(formattedInvoiceNumber);
                }
                jLabel.setForeground(new Color(33, 113, 188));
                jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                jLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String labelText = jLabel.getText();
                        if (labelText.startsWith("HD")) {
                            String numberText = labelText.substring(2); // Cắt bỏ "HD" từ chuỗi
                            try {
                                int invoiceNumber = Integer.parseInt(numberText);
                                new DetailReceiptGUI(new ReceiptBLL().searchReceipts("id = " + invoiceNumber).get(0));
                            } catch (NumberFormatException ex) {
                                System.out.println("Lỗi: không thể chuyển đổi thành số nguyên");
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        jLabel.setForeground(new Color(100, 157, 204));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        jLabel.setForeground(new Color(33, 113, 188));
                    }
                });
            } else if (i == 2 || i == 3) {
                String formattedRevenue = NumberFormat.getNumberInstance(Locale.US).format(new BigDecimal(label));
                jLabel.setText(formattedRevenue);
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            }


            panel.add(jLabel);
            i++;
        }
    }

    private void initRightBartEndOfDayPanel() {
        JPanel jPanel = new JPanel(new MigLayout("", "5[]5", "10[]10"));
        jPanel.setBorder(null);
        jPanel.setBackground(new Color(238, 238, 238));
        scrollPane.setViewportView(jPanel);

        displayTypePanel = createRoundedPanel(new MigLayout("", "10[]10", "10[]10"), 300, 70);
        displayTypePanel.setBackground(new Color(255, 255, 255));
        displayTypePanel.setPreferredSize(new Dimension(247, 120));
        jPanel.add(displayTypePanel, "wrap");

        RoundedPanel concernsPanel = createRoundedPanel(new MigLayout("", "10[]10", "10[]10"), 515, 70);
        concernsPanel.setBackground(new Color(255, 255, 255));
        concernsPanel.setPreferredSize(new Dimension(247, 220));
        jPanel.add(concernsPanel, "wrap");

        JLabel label = new JLabel("Mối quan tâm");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(label, "wrap");

        timeRadioButton = createRadioButton("Thời gian");
        profitRadioButton = createRadioButton("Lợi nhuận");
        discountRadioButton = createRadioButton("Giảm giá HĐ");
        categoryRadioButton = createRadioButton("Thể loại");

        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(timeRadioButton);
        btnGroupConcerns.add(profitRadioButton);
        btnGroupConcerns.add(discountRadioButton);
        btnGroupConcerns.add(categoryRadioButton);

        concernsPanel.add(timeRadioButton, "wrap");
        concernsPanel.add(profitRadioButton, "wrap");
        concernsPanel.add(discountRadioButton, "wrap");
        concernsPanel.add(categoryRadioButton, "wrap");


        concernsPanel.repaint();
        concernsPanel.revalidate();

        // mặc định khi chọn vào tab bán hàng sẽ chọn tiêu chí thời gian đầu tiên là mặc định
        timeRadioButton.setSelected(true);
        updateDisplayType("Báo cáo và biểu đồ");

        timeRadioButton.addActionListener(e -> {
            // Kiểm tra nếu thời gian được chọn
            if (timeRadioButton.isSelected()) {
                titleLabel.setText("Báo cáo bán hàng theo thời gian");
                updateDisplayType("Báo cáo và biểu đồ");
                loadData();
            }
        });

        profitRadioButton.addActionListener(e -> {
            // Kiểm tra nếu lọi nhuận được chọn
            if (profitRadioButton.isSelected()) {
                titleLabel.setText("Báo cáo lợi nhuận theo hóa đơn");
                updateDisplayType("Báo cáo và biểu đồ");
                loadData();
            }
        });

        discountRadioButton.addActionListener(e -> {
            // Kiểm tra nếu giảm HD được chọn
            if (discountRadioButton.isSelected()) {
                titleLabel.setText("Báo cáo giảm giá theo hóa đơn");
                updateDisplayType("Báo cáo");
                loadData();
            }
        });

        categoryRadioButton.addActionListener(e -> {
            // Kiểm tra nếu thể loại được chọn
            if (categoryRadioButton.isSelected()) {
                titleLabel.setText("Báo cáo bán hàng theo nhóm hàng hóa");
                updateDisplayType("Báo cáo");
                loadData();
            }
        });

        RoundedPanel timePanel = new RoundedPanel();
        timePanel.setBackground(new Color(255, 255, 255));
        timePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        timePanel.setPreferredSize(new Dimension(247, 50));
        jPanel.add(timePanel, "wrap");
        timePanel.setBackground(new Color(255, 255, 255));
        timePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        timePanel.setPreferredSize(new Dimension(247, 50));
        jPanel.add(timePanel, "wrap");

        JLabel labelTime = new JLabel("Thời gian");
        labelTime.setFont(new Font("Inter", Font.BOLD, 14));


        timePanel.add(labelTime, "wrap");
        timePanel.add(editor);

    }

    private void updateTitle(Date start, Date end) {

        if (start != null && end != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedStartDate = dateFormat.format(start);
            String formattedEndDate = dateFormat.format(end);

            if (start.equals(end)) {
                dateReportLabel.setText("Ngày bán: " + formattedStartDate);
            } else {
                dateReportLabel.setText("Từ ngày " + formattedStartDate + " đến ngày " + formattedEndDate);
            }
        }
    }

    private void setupDisplayTypeReportAndChart() {
        JLabel label = new JLabel("Kiểu hiển thị");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        displayTypePanel.add(label, "wrap");


        btnGroupDisplayType.add(chartRadioButton);
        btnGroupDisplayType.add(reportRadioButton);

        reportRadioButton.setSelected(true);
        displayTypePanel.add(chartRadioButton, "wrap");
        displayTypePanel.add(reportRadioButton, "wrap");
    }

    private void setupDisplayTypeReport() {
        JLabel label = new JLabel("Kiểu hiển thị");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        displayTypePanel.add(label, "wrap");

        reportRadioButton.setSelected(true);
        displayTypePanel.add(reportRadioButton);
    }

    private void updateDisplayType(String displayType) {
        displayTypePanel.removeAll();
        if (displayType.equals("Báo cáo và biểu đồ"))
            setupDisplayTypeReportAndChart();
        else
            setupDisplayTypeReport();

        displayTypePanel.repaint();
        displayTypePanel.revalidate();

    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Inter", Font.PLAIN, 14));
        return radioButton;
    }

    private RoundedPanel createRoundedPanel(LayoutManager layoutManager, int width, int height) {
        RoundedPanel panel = new RoundedPanel();
        panel.setLayout(layoutManager);
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }
}
