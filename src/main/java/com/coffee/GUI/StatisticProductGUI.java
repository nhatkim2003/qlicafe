package com.coffee.GUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.DAL.MySQL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Product;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.barchart.BarChart;
import com.coffee.GUI.components.barchart.ModelBarChart;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class StatisticProductGUI extends JPanel {
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private MyTextField txtSearchProductName;
    private PanelSearch searchProductName;
    private JPopupMenu menuProductName;
    private MyTextField txtSearchMaterialName;
    private PanelSearch searchMaterialName;
    private JPopupMenu menuMaterialName;
    private Product selected_product = null;
    private String material_name = null;
    private JComboBox<String> jComboBoxCategory;
    private JComboBox<String> jComboBoxRemainMaterial;
    private JPanel content;
    private int concern = 0;
    private int displayType = 0;
    private JScrollPane scrollPane;
    private static final int PANEL_WIDTH = 885;
    private static final int PANEL_HEIGHT = 40;

    public StatisticProductGUI() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 700));
        setLayout(new BorderLayout());
        init();
        initTxtSearchName();
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
        initRightBar();

        content = new JPanel();
        content.setLayout(new MigLayout());
        content.setPreferredSize(new Dimension(730, 700));
        content.setBackground(new Color(238, 238, 238));
        add(content, BorderLayout.CENTER);

        saleChart();
    }

    private void initRightBar() {
        JPanel jPanel = new JPanel(new MigLayout("", "5[]5", "10[]10"));
        jPanel.setBackground(new Color(238, 238, 238));
        scrollPane.setViewportView(jPanel);

        displayType = 0;

        // chon hien thi
        RoundedPanel displayTypePanel = new RoundedPanel();
        displayTypePanel.setBackground(new Color(255, 255, 255));
        displayTypePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        displayTypePanel.setPreferredSize(new Dimension(247, 100));
        jPanel.add(displayTypePanel, "wrap");

        ButtonGroup btnGroupDisplayType = new ButtonGroup();
        JLabel labelDisplayType = new JLabel("Chọn hiển thị");
        labelDisplayType.setFont(new Font("Inter", Font.BOLD, 14));
        displayTypePanel.add(labelDisplayType, "wrap");

        JRadioButton reportRadioButton = createRadioButton("Báo cáo");
        reportRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayType = 1;
                loadData();
            }
        });

        if (concern == 0 || concern == 1 || concern == 2) {
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
        } else {
            reportRadioButton.setSelected(true);
        }
        btnGroupDisplayType.add(reportRadioButton);
        displayTypePanel.add(reportRadioButton, "wrap");


        // chon mau quan tam
        RoundedPanel concernsPanel = new RoundedPanel();
        concernsPanel.setBackground(new Color(255, 255, 255));
        concernsPanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        concernsPanel.setPreferredSize(new Dimension(247, 220));
        jPanel.add(concernsPanel, "wrap");

        JLabel labelConcerns = new JLabel("Mối quan tâm");
        labelConcerns.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(labelConcerns, "wrap");

        JRadioButton radio1 = createRadioButton("Bán hàng");
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

        JRadioButton radio2 = createRadioButton("Lợi nhuận");
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

        JRadioButton radio3 = createRadioButton("Xuất nhập tồn");
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

        JRadioButton radio4 = createRadioButton("Xuất huỷ");
        if (concern == 3)
            radio4.setSelected(true);
        radio4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                concern = 3;
                initRightBar();
                loadData();
            }
        });

        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(radio1);
        btnGroupConcerns.add(radio2);
        btnGroupConcerns.add(radio3);
        btnGroupConcerns.add(radio4);

        concernsPanel.add(radio1, "wrap");
        concernsPanel.add(radio2, "wrap");
        concernsPanel.add(radio3, "wrap");
        concernsPanel.add(radio4, "wrap");

        // thoi gian

        RoundedPanel timePanel = new RoundedPanel();
        timePanel.setBackground(new Color(255, 255, 255));
        timePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        timePanel.setPreferredSize(new Dimension(247, 50));
        jPanel.add(timePanel, "wrap");

        JLabel labelTime = new JLabel("Thời gian");
        labelTime.setFont(new Font("Inter", Font.BOLD, 14));

        timePanel.add(labelTime, "wrap");
        timePanel.add(editor);


        // ten san pham

        RoundedPanel searchNamePanel = new RoundedPanel();
        searchNamePanel.setBackground(new Color(255, 255, 255));
        searchNamePanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        searchNamePanel.setPreferredSize(new Dimension(247, 60));
        jPanel.add(searchNamePanel, "wrap");
        if (concern == 0 || concern == 1) {
            JLabel labelName = new JLabel("Tên sản phẩm");
            labelName.setFont(new Font("Inter", Font.BOLD, 14));
            searchNamePanel.add(labelName, "wrap");

            selected_product = null;

            txtSearchProductName = new MyTextField();
            txtSearchProductName.setPreferredSize(new Dimension(200, 40));
            txtSearchProductName.putClientProperty("JTextField.placeholderText", "Nhập tên sản phẩm tìm kiếm");
            txtSearchProductName.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    txtSearchProductNameMouseClicked(evt);
                }
            });
            txtSearchProductName.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    txtSearchProductNameKeyPressed(evt);
                }

                public void keyReleased(java.awt.event.KeyEvent evt) {
                    txtSearchProductNameKeyReleased(evt);
                }
            });
            searchNamePanel.add(txtSearchProductName, "wrap");
        } else {
            JLabel labelName = new JLabel("Tên nguyên liệu");
            labelName.setFont(new Font("Inter", Font.BOLD, 14));
            searchNamePanel.add(labelName, "wrap");

            material_name = null;

            txtSearchMaterialName = new MyTextField();
            txtSearchMaterialName.setPreferredSize(new Dimension(200, 40));
            txtSearchMaterialName.putClientProperty("JTextField.placeholderText", "Nhập tên nguyên liệu tìm kiếm");
            txtSearchMaterialName.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    txtSearchMaterialNameMouseClicked(evt);
                }
            });
            txtSearchMaterialName.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    txtSearchMaterialNameKeyPressed(evt);
                }

                public void keyReleased(java.awt.event.KeyEvent evt) {
                    txtSearchMaterialNameKeyReleased(evt);
                }
            });
            searchNamePanel.add(txtSearchMaterialName, "wrap");
        }

        // the loai hoac ton kho

        RoundedPanel searchPanel = new RoundedPanel();
        searchPanel.setBackground(new Color(255, 255, 255));
        searchPanel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        searchPanel.setPreferredSize(new Dimension(247, 50));
        if (concern == 0 || concern == 1) {
            JLabel labelCategory = new JLabel("Thể loại sản phẩm");
            labelCategory.setFont(new Font("Inter", Font.BOLD, 14));
            searchPanel.add(labelCategory, "wrap");

            jComboBoxCategory = new JComboBox<>();
            jComboBoxCategory.setPreferredSize(new Dimension(200, 30));
            jComboBoxCategory.setBackground(new Color(1, 120, 220));
            jComboBoxCategory.setForeground(Color.white);

            jComboBoxCategory.addItem("Tất cả");
            jComboBoxCategory.setSelectedIndex(0);
            for (String category : new ProductBLL().getCategories())
                jComboBoxCategory.addItem(category);
            jComboBoxCategory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadData();
                }
            });
            searchPanel.add(jComboBoxCategory, "wrap");

            jPanel.add(searchPanel, "wrap");
        }
    }

    private void loadData() {
        if (concern == 0) {
            if (displayType == 0)
                saleChart();
            if (displayType == 1)
                saleReport();
        }
        if (concern == 1) {
            if (displayType == 0)
                profitChart();
            if (displayType == 1)
                profitReport();
        }
        if (concern == 2) {
            if (displayType == 0)
                import_exportChart();
            if (displayType == 1)
                import_exportReport();
        }
        if (concern == 3) {
            destroy_exportReport();
        }
    }

    private void destroy_exportReport() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String materialName = material_name == null ? null : material_name;

        assert start != null;
        List<List<String>> dataDestroyExport = MySQL.getDestroyExports(materialName, start.toString(), end.toString());

        content.removeAll();

        initTopContent("Báo cáo xuất nhập tồn nguyên liệu", start, end);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 580));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);

        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        salesLabelPanel.setPreferredSize(new Dimension(880, 40));
        addLabelsToPanel(salesLabelPanel, new String[]{"Mã nguyên liệu", "Tên nguyên liệu", "SL xuất huỷ", "Giá trị xuất huỷ"}, 10, 10, new Dimension(400, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        double totalQuantityDestroyExport = 0;
        double totalDestroyExport = 0;

        for (List<String> list : dataDestroyExport) {
            totalQuantityDestroyExport += Double.parseDouble(list.get(2));
            totalDestroyExport += Double.parseDouble(list.get(3));
        }
        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new Color(202, 202, 202));
        salesDataPanel.setPreferredSize(new Dimension(880, 40));
        addLabelsToPanel(salesDataPanel, new String[]{"SL nguyên liệu: " + dataDestroyExport.size(), " ", String.valueOf(totalQuantityDestroyExport), VNString.currency(totalDestroyExport)}, 10, 10, new Dimension(400, 30));
        centerPanel.add(salesDataPanel, "wrap");

        for (List<String> list : dataDestroyExport) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(868, 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel labelID = new JLabel(list.get(0));
            labelID.setFont(new Font("Inter", Font.PLAIN, 13));
            labelID.setPreferredSize(new Dimension(410, 30));
            panel.add(labelID);

            JLabel labelName = new JLabel(list.get(1));
            labelName.setFont(new Font("Inter", Font.PLAIN, 13));
            labelName.setPreferredSize(new Dimension(410, 30));
            panel.add(labelName);

            JLabel labelQuantityDestroyExport = new JLabel(list.get(2));
            labelQuantityDestroyExport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantityDestroyExport.setPreferredSize(new Dimension(410, 30));
//            labelQuantityDestroyExport.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelQuantityDestroyExport);

            JLabel labelTotalExport = new JLabel(VNString.currency(Double.parseDouble(list.get(3))));
            labelTotalExport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotalExport.setPreferredSize(new Dimension(370, 30));
            panel.add(labelTotalExport);

            centerPanel.add(panel, "wrap");
        }

        content.repaint();
        content.revalidate();

//        System.out.println(Arrays.toString(dataDestroyExport.toArray()));
    }

    private void import_exportChart() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String materialName = material_name == null ? null : material_name;

        assert start != null;
        List<List<String>> dataMostImport = MySQL.getTop5MostImport(materialName, start.toString(), end.toString());
        List<List<String>> dataMostExport = MySQL.getTop5MostExports(materialName, start.toString(), end.toString());

        content.removeAll();

        JLabel jLabelTile1 = new JLabel("Top 5 nguyên liệu nhập nhiều nhất");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        content.add(jLabelTile1, "center, span, wrap");

        BarChart barChartMostImport = new BarChart();
        barChartMostImport.setPreferredSize(new Dimension(1000, 350));
//        barChartMostImport.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartMostImport, "wrap");

        JLabel jLabelTile2 = new JLabel("Top 5 nguyên liệu xuất nhiều nhất");
        jLabelTile2.setFont(new Font("Inter", Font.BOLD, 15));
        content.add(jLabelTile2, "center, span, wrap");

        BarChart barChartMostExport = new BarChart();
        barChartMostExport.setPreferredSize(new Dimension(1000, 350));
//        barChartMostExport.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartMostExport, "wrap");

        barChartMostImport.addLegend("Số lượng nhập ", new Color(189, 135, 245));
        for (List<String> list : dataMostImport) {
            barChartMostImport.addData(new ModelBarChart(list.get(1), new double[]{Double.parseDouble(list.get(2))}));
        }
        barChartMostImport.start();

        barChartMostExport.addLegend("Số lượng xuất", new Color(189, 135, 245));
        barChartMostExport.addLegend("Xuất huỷ", new Color(135, 189, 245));
        barChartMostExport.addLegend("Xuất bán", new Color(139, 229, 222));
        for (List<String> list : dataMostExport) {
            barChartMostExport.addData(new ModelBarChart(list.get(1), new double[]{Double.parseDouble(list.get(4)), Double.parseDouble(list.get(2)), Double.parseDouble(list.get(3))}));
        }
        barChartMostExport.start();

//        System.out.println(Arrays.toString(dataMostImport.toArray()));
//        System.out.println(Arrays.toString(dataMostExport.toArray()));

        content.repaint();
        content.revalidate();
    }

    private void import_exportReport() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String materialName = material_name == null ? null : material_name;

        assert start != null;
        List<List<String>> dataTotalImport = MySQL.getTotalImport(materialName, start.toString(), end.toString());
        List<List<String>> dataTotalExport = MySQL.getTotalExport(materialName, start.toString(), end.toString());
        List<List<String>> dataImportExport = new ArrayList<>();

        for (Material material : new MaterialBLL().searchMaterials("deleted = 0")) {
            List<String> data = new ArrayList<>();
            data.add(material.getId() + "");
            data.add(material.getName());
            boolean check = false;
            for (List<String> dataImport : dataTotalImport) {
                if (dataImport.get(0).equals(material.getId() + "")) {
                    data.add(dataImport.get(2));
                    data.add(String.valueOf(Double.parseDouble(dataImport.get(2)) * material.getUnit_price()));
                    check = true;
                    break;
                }
            }
            if (!check) {
                data.add("0");
                data.add("0");
            }

            check = false;
            for (List<String> dataExport : dataTotalExport) {
                if (dataExport.get(0).equals(material.getId() + "")) {
                    data.add(dataExport.get(2));
                    data.add(dataExport.get(3));
                    data.add(dataExport.get(4));
                    data.add(String.valueOf(Double.parseDouble(dataExport.get(4)) * material.getUnit_price()));
                    check = true;
                    break;
                }
            }
            if (!check) {
                data.add("0");
                data.add("0");
                data.add("0");
                data.add("0");
            }
            dataImportExport.add(data);
        }

        content.removeAll();

        initTopContent("Báo cáo xuất nhập tồn nguyên liệu", start, end);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 580));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);

        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        salesLabelPanel.setPreferredSize(new Dimension(1100, 40));
        addLabelsToPanel(salesLabelPanel, new String[]{"Mã NL", "Tên NL", "SL nhập", "Giá trị nhập", "SL xuất huỷ", "SL xuất bán", "Tổng SL xuất", "Giá trị xuất"}, 10, 10, new Dimension(150, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        double totalQuantityImport = 0;
        double totalImport = 0;
        double totalQuantityDestroyExport = 0;
        double totalQuantitySaleExport = 0;
        double totalQuantityExport = 0;
        double totalExport = 0;

        for (List<String> list : dataImportExport) {
            totalQuantityImport += Double.parseDouble(list.get(2));
            totalImport += Double.parseDouble(list.get(3));
            totalQuantityDestroyExport += Double.parseDouble(list.get(4));
            totalQuantitySaleExport += Double.parseDouble(list.get(5));
            totalQuantityExport += Double.parseDouble(list.get(6));
            totalExport += Double.parseDouble(list.get(7));
        }
        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new Color(202, 202, 202));
        salesDataPanel.setPreferredSize(new Dimension(1100, 40));
        addLabelsToPanel(salesDataPanel, new String[]{"SL nguyên liệu: " + dataImportExport.size(), " ", String.valueOf(totalQuantityImport), VNString.currency(totalImport), String.valueOf(totalQuantityDestroyExport), String.valueOf(totalQuantitySaleExport), String.valueOf(totalQuantityExport), VNString.currency(totalExport)}, 10, 10, new Dimension(150, 30));
        centerPanel.add(salesDataPanel, "wrap");
//
        for (List<String> list : dataImportExport) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(1100, 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel labelID = new JLabel(list.get(0));
            labelID.setFont(new Font("Inter", Font.PLAIN, 13));
            labelID.setPreferredSize(new Dimension(150, 30));
            panel.add(labelID);

            JLabel labelName = new JLabel(list.get(1));
            labelName.setFont(new Font("Inter", Font.PLAIN, 13));
            labelName.setPreferredSize(new Dimension(150, 30));
            panel.add(labelName);

            JLabel labelQuantityImport = new JLabel(list.get(2));
            labelQuantityImport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantityImport.setPreferredSize(new Dimension(150, 30));
//            labelQuantityImport.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelQuantityImport);

            JLabel labelTotalImport = new JLabel(VNString.currency(Double.parseDouble(list.get(3))));
            labelTotalImport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotalImport.setPreferredSize(new Dimension(150, 30));
            panel.add(labelTotalImport);

            JLabel labelQuantityDestroyExport = new JLabel(list.get(4));
            labelQuantityDestroyExport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantityDestroyExport.setPreferredSize(new Dimension(150, 30));
//            labelQuantityDestroyExport.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelQuantityDestroyExport);

            JLabel labelQuantitySaleExport = new JLabel(list.get(5));
            labelQuantitySaleExport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantitySaleExport.setPreferredSize(new Dimension(150, 30));
//            labelQuantitySaleExport.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelQuantitySaleExport);

            JLabel labelQuantityExport = new JLabel(list.get(6));
            labelQuantityExport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantityExport.setPreferredSize(new Dimension(150, 30));
//            labelQuantityExport.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelQuantityExport);

            JLabel labelTotalExport = new JLabel(VNString.currency(Double.parseDouble(list.get(7))));
            labelTotalExport.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotalExport.setPreferredSize(new Dimension(150, 30));
            panel.add(labelTotalExport);

            centerPanel.add(panel, "wrap");
        }

        content.repaint();
        content.revalidate();

//        System.out.println(Arrays.toString(dataImportExport.toArray()));
    }

    private void profitReport() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String product_Name = selected_product == null ? null : selected_product.getName();
        String size = selected_product == null ? null : selected_product.getSize();
        String product_Category = Objects.requireNonNull(jComboBoxCategory.getSelectedItem()).toString();

        assert start != null;
        List<List<String>> dataProfitProduct = MySQL.getProfitProduct(product_Name, size, product_Category, start.toString(), end.toString());

        content.removeAll();

        initTopContent("Báo cáo lợi nhuận theo sản phẩm", start, end);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 580));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);

        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        salesLabelPanel.setPreferredSize(new Dimension(900, 40));
        addLabelsToPanel(salesLabelPanel, new String[]{"Mã sản phẩm", "Tên sản phẩm", "SL Bán", "Doanh thu", "Tổng giá vốn", "Lợi nhuận", "Tỷ suất"}, 8, 2, new Dimension(150, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        double totalQuantity = 0;
        double totalSales = 0;
        double totalCapitalPrice = 0;
        double totalProfit = 0;
        double totalCapitalizationRate = 0;
        for (List<String> list : dataProfitProduct) {
            totalProfit += Double.parseDouble(list.get(3));
            totalSales += Double.parseDouble(list.get(5));
            totalQuantity += Double.parseDouble(list.get(4));
            totalCapitalPrice += Double.parseDouble(list.get(6));
        }
        totalCapitalizationRate = totalProfit / totalSales * 100;
        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new Color(202, 202, 202));
        salesDataPanel.setPreferredSize(new Dimension(900, 40));
        addLabelsToPanel(salesDataPanel, new String[]{"SL Sản phẩm: " + dataProfitProduct.size(), " ", String.valueOf((int) totalQuantity), VNString.currency(totalSales), VNString.currency(totalCapitalPrice), VNString.currency(totalProfit), String.format("%.2f", totalCapitalizationRate) + " %"}, 8, 2, new Dimension(150, 30));
        centerPanel.add(salesDataPanel, "wrap");

        for (List<String> list : dataProfitProduct) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(900, 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel labelID = new JLabel(list.get(0));
            labelID.setFont(new Font("Inter", Font.PLAIN, 13));
            labelID.setPreferredSize(new Dimension(150, 30));
            panel.add(labelID);

            JLabel labelName = new JLabel(list.get(1) + (Objects.equals(list.get(2), "Không") ? " " : " " + list.get(2)));
            labelName.setFont(new Font("Inter", Font.PLAIN, 13));
            labelName.setPreferredSize(new Dimension(150, 30));
            panel.add(labelName);

            JLabel labelQuantity = new JLabel(list.get(4));
            labelQuantity.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantity.setPreferredSize(new Dimension(150, 30));
            labelQuantity.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelQuantity);

            JLabel labelTotal = new JLabel(VNString.currency(Double.parseDouble(list.get(5))));
            labelTotal.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotal.setPreferredSize(new Dimension(150, 30));
            panel.add(labelTotal);

            JLabel labelCapitalPrice = new JLabel(VNString.currency(Double.parseDouble(list.get(6))));
            labelCapitalPrice.setFont(new Font("Inter", Font.PLAIN, 13));
            labelCapitalPrice.setPreferredSize(new Dimension(150, 30));
            panel.add(labelCapitalPrice);

            JLabel labelProfit = new JLabel(VNString.currency(Double.parseDouble(list.get(3))));
            labelProfit.setFont(new Font("Inter", Font.PLAIN, 13));
            labelProfit.setPreferredSize(new Dimension(150, 30));
            panel.add(labelProfit);

            JLabel labelRate = new JLabel(String.format("%.2f", Double.parseDouble(list.get(7))) + " %");
            labelRate.setFont(new Font("Inter", Font.PLAIN, 13));
            labelRate.setPreferredSize(new Dimension(150, 30));
            panel.add(labelRate);

            centerPanel.add(panel, "wrap");
        }

        content.repaint();
        content.revalidate();

//        System.out.println(dataProfitProduct);
    }

    private void profitChart() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String product_Name = selected_product == null ? null : selected_product.getName();
        String size = selected_product == null ? null : selected_product.getSize();
        String product_Category = Objects.requireNonNull(jComboBoxCategory.getSelectedItem()).toString();

        assert start != null;
        List<List<String>> dataProfitProduct = MySQL.getTop5ProfitProduct(product_Name, size, product_Category, start.toString(), end.toString());
        List<List<String>> dataCapitalizationRate = MySQL.getTop5CapitalizationRate(product_Name, size, product_Category, start.toString(), end.toString());

        content.removeAll();

        JLabel jLabelTile1 = new JLabel("Top 5 sản phẩm lợi nhuận cao nhất");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        content.add(jLabelTile1, "center, span, wrap");

        BarChart barChartProfitProduct = new BarChart();
        barChartProfitProduct.setPreferredSize(new Dimension(1000, 350));
//        barChartProfitProduct.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartProfitProduct, "wrap");

        JLabel jLabelTile2 = new JLabel("Top 5 sản phẩm theo tỷ suất");
        jLabelTile2.setFont(new Font("Inter", Font.BOLD, 15));
        content.add(jLabelTile2, "center, span, wrap");

        BarChart barChartCapitalizationRate = new BarChart();
        barChartCapitalizationRate.setPreferredSize(new Dimension(1000, 350));
//        barChartCapitalizationRate.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartCapitalizationRate, "wrap");

        barChartProfitProduct.addLegend("Lợi nhuận", new Color(245, 189, 135));
        for (List<String> list : dataProfitProduct) {
            barChartProfitProduct.addData(new ModelBarChart(list.get(1) + (list.get(2).equals("Không") ? "" : "(" + list.get(2) + ")"), new double[]{Double.parseDouble(list.get(3))}));
        }
        barChartProfitProduct.start();

        barChartCapitalizationRate.addLegend("Tỷ suất (%)", new Color(245, 189, 135));
        for (List<String> list : dataCapitalizationRate) {
            barChartCapitalizationRate.addData(new ModelBarChart(list.get(1) + (list.get(2).equals("Không") ? "" : "(" + list.get(2) + ")"), new double[]{Double.parseDouble(list.get(3))}));
        }
        barChartCapitalizationRate.start();

//        System.out.println(Arrays.toString(dataProfitProduct.toArray()));
//        System.out.println(Arrays.toString(dataCapitalizationRate.toArray()));

        content.repaint();
        content.revalidate();
    }

    private void saleChart() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String product_Name = selected_product == null ? null : selected_product.getName();
        String size = selected_product == null ? null : selected_product.getSize();
        String product_Category = Objects.requireNonNull(jComboBoxCategory.getSelectedItem()).toString();

        assert start != null;
        List<List<String>> dataSaleProduct = MySQL.getTop5SaleProduct(product_Name, size, product_Category, start.toString(), end.toString());
        List<List<String>> dataBestSeller = MySQL.getTop5BestSellers(product_Name, size, product_Category, start.toString(), end.toString());

        content.removeAll();

        JLabel jLabelTile1 = new JLabel("Top 5 sản phẩm doanh số cao nhất");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        content.add(jLabelTile1, "center, span, wrap");

        BarChart barChartSaleProduct = new BarChart();
        barChartSaleProduct.setPreferredSize(new Dimension(1000, 350));
//        barChartSaleProduct.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartSaleProduct, "wrap");

        JLabel jLabelTile2 = new JLabel("Top 5 sản phẩm bán chạy theo số lượng");
        jLabelTile2.setFont(new Font("Inter", Font.BOLD, 15));
        content.add(jLabelTile2, "center, span, wrap");

        BarChart barChartBestSeller = new BarChart();
        barChartBestSeller.setPreferredSize(new Dimension(1000, 350));
//        barChartBestSeller.setFont(new java.awt.Font("sansserif", Font.BOLD, 8));
        content.add(barChartBestSeller, "wrap");

        barChartSaleProduct.addLegend("Doanh thu", new Color(135, 189, 245));
        for (List<String> list : dataSaleProduct) {
            barChartSaleProduct.addData(new ModelBarChart(list.get(1) + (list.get(2).equals("Không") ? "" : "(" + list.get(2) + ")"), new double[]{Double.parseDouble(list.get(3))}));
        }
        barChartSaleProduct.start();

        barChartBestSeller.addLegend("Số lượng bán", new Color(135, 189, 245));
        for (List<String> list : dataBestSeller) {
            barChartBestSeller.addData(new ModelBarChart(list.get(1) + (list.get(2).equals("Không") ? "" : "(" + list.get(2) + ")"), new double[]{Double.parseDouble(list.get(3))}));
        }
        barChartBestSeller.start();

//        System.out.println(Arrays.toString(dataSaleProduct.toArray()));
//        System.out.println(Arrays.toString(dataBestSeller.toArray()));

        content.repaint();
        content.revalidate();
    }

    private void saleReport() {
        Date start = null;
        Date end = null;
        if (datePicker.getDateSQL_Between() != null) {
            start = datePicker.getDateSQL_Between()[0];
            end = datePicker.getDateSQL_Between()[1];
        }
        String product_Name = selected_product == null ? null : selected_product.getName();
        String size = selected_product == null ? null : selected_product.getSize();
        String product_Category = Objects.requireNonNull(jComboBoxCategory.getSelectedItem()).toString();

        assert start != null;
        List<List<String>> dataSaleProduct = MySQL.getSaleProduct(product_Name, size, product_Category, start.toString(), end.toString());

        content.removeAll();

        initTopContent("Báo cáo bán hàng theo sản phẩm", start, end);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 580));
        content.add(jScrollPane, "wrap");

        JPanel centerPanel = new JPanel(new MigLayout("", "0[]0", "0[]0"));
        centerPanel.setBackground(Color.WHITE);
        jScrollPane.setViewportView(centerPanel);

        JPanel salesLabelPanel = createLabelPanel(new Color(178, 232, 255), new Color(202, 202, 202));
        addLabelsToPanel(salesLabelPanel, new String[]{"Mã sản phẩm", "Tên sản phẩm", "Size", "SL Bán", "Doanh thu"}, 4, 8, new Dimension(200, 30));
        centerPanel.add(salesLabelPanel, "wrap");

        double totalQuantity = 0;
        double totalSales = 0;

        for (List<String> list : dataSaleProduct) {
            totalQuantity += Double.parseDouble(list.get(4));
            totalSales += Double.parseDouble(list.get(3));
        }

        JPanel salesDataPanel = createLabelPanel(new Color(242, 238, 214), new Color(202, 202, 202));
        addLabelsToPanel(salesDataPanel, new String[]{"SL Sản phẩm: " + dataSaleProduct.size(), "", "", String.valueOf((int) totalQuantity), VNString.currency(totalSales)}, 4, 8, new Dimension(200, 30));
        centerPanel.add(salesDataPanel, "wrap");

        for (List<String> list : dataSaleProduct) {
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("", "10[]10", ""));
            panel.setPreferredSize(new Dimension(868, 30));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(202, 202, 202)));

            JLabel labelID = new JLabel(list.get(0));
            labelID.setFont(new Font("Inter", Font.PLAIN, 13));
            labelID.setPreferredSize(new Dimension(200, 30));
            panel.add(labelID);

            JLabel labelName = new JLabel(list.get(1));
            labelName.setFont(new Font("Inter", Font.PLAIN, 13));
            labelName.setPreferredSize(new Dimension(200, 30));
            panel.add(labelName);

            JLabel labelQuantity = new JLabel(Objects.equals(list.get(2), "Không") ? " " : list.get(2));
            labelQuantity.setFont(new Font("Inter", Font.PLAIN, 13));
            labelQuantity.setPreferredSize(new Dimension(200, 30));
            panel.add(labelQuantity);

            JLabel labelPrice = new JLabel(list.get(4));
            labelPrice.setFont(new Font("Inter", Font.PLAIN, 13));
            labelPrice.setPreferredSize(new Dimension(200, 30));
            panel.add(labelPrice);

            JLabel labelTotal = new JLabel(VNString.currency(Double.parseDouble(list.get(3))));
            labelTotal.setFont(new Font("Inter", Font.PLAIN, 13));
            labelTotal.setPreferredSize(new Dimension(200, 30));
            labelTotal.setHorizontalAlignment(JLabel.RIGHT);
            panel.add(labelTotal);

            centerPanel.add(panel, "wrap");
        }

        content.repaint();
        content.revalidate();
//        System.out.println(dataSaleProduct);
    }

    private void addLabelsToPanel(JPanel panel, String[] labels, int columnRight, int columnCenter, Dimension dimension) {
        int i = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Inter", Font.BOLD, 13));
            jLabel.setPreferredSize(dimension);
            if (i == columnRight)
                jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            if (i == columnCenter)
                jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(jLabel);
            i++;
        }
    }

    private JPanel createLabelPanel(Color background, Color border) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]10", ""));
        panel.setPreferredSize(new Dimension(868, PANEL_HEIGHT));
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, border));
        return panel;
    }

    private void initTxtSearchName() {
        menuProductName = new JPopupMenu();
        searchProductName = new PanelSearch();
        menuProductName.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menuProductName.add(searchProductName);
        menuProductName.setFocusable(false);
        searchProductName.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menuProductName.setVisible(false);
                txtSearchProductName.setText(data.getText());
                selected_product = new ProductBLL().findProductsBy(Map.of("name", data.getText())).get(0);
                loadData();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                searchProductName.remove(com);
                menuProductName.setPopupSize(200, (searchProductName.getItemSize() * 35) + 2);
                if (searchProductName.getItemSize() == 0) {
                    menuProductName.setVisible(false);
                }
            }
        });

        menuMaterialName = new JPopupMenu();
        searchMaterialName = new PanelSearch();
        menuMaterialName.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menuMaterialName.add(searchMaterialName);
        menuMaterialName.setFocusable(false);
        searchMaterialName.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menuMaterialName.setVisible(false);
                txtSearchMaterialName.setText(data.getText());
                material_name = data.getText();
                loadData();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                searchMaterialName.remove(com);
                menuMaterialName.setPopupSize(200, (searchMaterialName.getItemSize() * 35) + 2);
                if (searchMaterialName.getItemSize() == 0) {
                    menuMaterialName.setVisible(false);
                }
            }
        });
    }

    private void initTopContent(String tile, Date start, Date end) {
        JPanel titletTopPanel = new JPanel(new BorderLayout());
        titletTopPanel.setBackground(Color.WHITE);

        titletTopPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 100));
        content.add(titletTopPanel, "wrap");
        // Cập nhật ngày lập
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        JLabel dateLabel = new JLabel("Ngày lập: " + formattedDateTime);
        dateLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        // Cập nhật ngày bán
        JLabel dateReportLabel = new JLabel("Từ ngày: " + new SimpleDateFormat("dd-MM-yyyy").format(start) + " đến ngày " + new SimpleDateFormat("dd-MM-yyyy").format(end));
        dateReportLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        // mặc đinh tiêu đề là báo cáo cuối ngày về bán hàng
        JLabel titleLabel = new JLabel(tile);
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

    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Inter", Font.PLAIN, 14));
        return radioButton;
    }

    private void txtSearchProductNameMouseClicked(java.awt.event.MouseEvent evt) {
        if (searchProductName.getItemSize() > 0 && !txtSearchProductName.getText().isEmpty()) {
            menuProductName.show(txtSearchProductName, 0, txtSearchProductName.getHeight());
            searchProductName.clearSelected();
        }
    }


    private void txtSearchProductNameKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearchProductName.getText().trim().toLowerCase();
            searchProductName.setData(searchProductName(text));
            if (searchProductName.getItemSize() > 0 && !txtSearchProductName.getText().isEmpty()) {
                menuProductName.show(txtSearchProductName, 0, txtSearchProductName.getHeight());
                menuProductName.setPopupSize(200, (searchProductName.getItemSize() * 35) + 2);
            } else {
                menuProductName.setVisible(false);
            }
        }
    }

    private java.util.List<DataSearch> searchProductName(String text) {
        selected_product = null;
        java.util.List<DataSearch> list = new ArrayList<>();
        java.util.List<String> allName = new ProductBLL().getAllName();
        allName.removeIf(s -> !s.toLowerCase().contains(text.toLowerCase()));
        for (String m : allName) {
            if (list.size() == 7) {
                break;
            }
            list.add(new DataSearch(m));
        }
        return list;
    }

    private void txtSearchProductNameKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchProductName.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchProductName.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchProductName.getSelectedText();
            txtSearchProductName.setText(text);

        }
        menuProductName.setVisible(false);
    }

    private void txtSearchMaterialNameMouseClicked(java.awt.event.MouseEvent evt) {
        if (searchMaterialName.getItemSize() > 0 && !txtSearchMaterialName.getText().isEmpty()) {
            menuMaterialName.show(txtSearchMaterialName, 0, txtSearchMaterialName.getHeight());
            searchMaterialName.clearSelected();
        }
    }


    private void txtSearchMaterialNameKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearchMaterialName.getText().trim().toLowerCase();
            searchMaterialName.setData(searchMaterialName(text));
            if (searchMaterialName.getItemSize() > 0 && !txtSearchMaterialName.getText().isEmpty()) {
                menuMaterialName.show(txtSearchMaterialName, 0, txtSearchMaterialName.getHeight());
                menuMaterialName.setPopupSize(200, (searchMaterialName.getItemSize() * 35) + 2);
            } else {
                menuMaterialName.setVisible(false);
            }
        }
    }

    private java.util.List<DataSearch> searchMaterialName(String text) {
        material_name = null;
        java.util.List<DataSearch> list = new ArrayList<>();
        List<Material> materials = new MaterialBLL().findMaterials("name", text);
        for (Material m : materials) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
        }
        return list;
    }

    private void txtSearchMaterialNameKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchMaterialName.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchMaterialName.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchMaterialName.getSelectedText();
            txtSearchMaterialName.setText(text);

        }
        menuMaterialName.setVisible(false);
    }
}
