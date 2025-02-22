package com.coffee.GUI;



import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.LayoutStatistic;
import com.coffee.GUI.components.RoundedPanel;

import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class StatisticSalesOldGUI extends LayoutStatistic {
    private DatePicker datePicker;
    private JFormattedTextField editor;


    public StatisticSalesOldGUI() {
        super();
        initUIComponents();

        setVisible(true);
    }

    private void initUIComponents() {
        initTopBartEndOfDayPanel();
        setupTime();


    }






    private void initTopBartEndOfDayPanel() {
        concernsPanel.removeAll();
        JLabel label = new JLabel("Mối quan tâm");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(label, "wrap");

        JRadioButton sellRadioButton = createRadioButton("Bán hàng");
        JRadioButton revenueAndExpenditureRadioButton = createRadioButton("Thu chi");
        JRadioButton productRadioButton = createRadioButton("Hàng hóa");

        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(sellRadioButton);
        btnGroupConcerns.add(revenueAndExpenditureRadioButton);
        btnGroupConcerns.add(productRadioButton);


        concernsPanel.add(sellRadioButton);
        concernsPanel.add(revenueAndExpenditureRadioButton);
        concernsPanel.add(productRadioButton);

        concernsPanel.repaint();
        concernsPanel.revalidate();

        // mặc định khi chọn vào tab bán hàng sẽ chọn tiêu chí bán hàng đầu tiên là mặc định
        sellRadioButton.setSelected(true);
        updateDisplayType("Báo cáo");

        sellRadioButton.addActionListener(e -> {
            // Kiểm tra nếu bán hàng  được chọn
            if (sellRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });
        revenueAndExpenditureRadioButton.addActionListener(e -> {
            // Kiểm tra nếu thu chi được chọn
            if (revenueAndExpenditureRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });
        sellRadioButton.addActionListener(e -> {
            // Kiểm tra nếu hàng hóa được chọn
            if (sellRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });
    }


    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Inter", Font.PLAIN, 14));
        return radioButton;
    }

    private void initTopBartSalesPanel() {
        concernsPanel.removeAll();

        JLabel label = new JLabel("Mối quan tâm");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(label, "wrap");

        JRadioButton timeRadioButton = createRadioButton("Thời gian");
        JRadioButton profitRadioButton = createRadioButton("Lợi nhuận");
        JRadioButton discountRadioButton = createRadioButton("Giảm giá HĐ");
        JRadioButton categoryRadioButton = createRadioButton("Thể loại");

        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(timeRadioButton);
        btnGroupConcerns.add(profitRadioButton);
        btnGroupConcerns.add(discountRadioButton);
        btnGroupConcerns.add(categoryRadioButton);

        concernsPanel.add(timeRadioButton);
        concernsPanel.add(profitRadioButton);
        concernsPanel.add(discountRadioButton);
        concernsPanel.add(categoryRadioButton);


        concernsPanel.repaint();
        concernsPanel.revalidate();

        // mặc định khi chọn vào tab bán hàng sẽ chọn tiêu chí thời gian đầu tiên là mặc định
        timeRadioButton.setSelected(true);
        updateDisplayType("Báo cáo và biểu đồ");

        timeRadioButton.addActionListener(e -> {
            // Kiểm tra nếu thời gian được chọn
            if (timeRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        profitRadioButton.addActionListener(e -> {
            // Kiểm tra nếu lọi nhuận được chọn
            if (profitRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        discountRadioButton.addActionListener(e -> {
            // Kiểm tra nếu giảm HD được chọn
            if (discountRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });

        categoryRadioButton.addActionListener(e -> {
            // Kiểm tra nếu thể loại được chọn
            if (categoryRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });

    }

    private void initTopBartProductPanel() {
        concernsPanel.removeAll();
        JLabel label = new JLabel("Mối quan tâm");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(label, "wrap");

        JRadioButton sellRadioButton = createRadioButton("Bán hàng");
        JRadioButton profitRadioButton = createRadioButton("Lợi nhuận");
        JRadioButton import_N_RadioButton = createRadioButton("Xuất nhập tồn");
        JRadioButton detailedImportAndExportInventoryRadioButton = createRadioButton("Xuất nhập tồn chi tiết");
        JRadioButton cancelExportRadioButton = createRadioButton("Xuất hủy");

        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(sellRadioButton);
        btnGroupConcerns.add(profitRadioButton);
        btnGroupConcerns.add(import_N_RadioButton);
        btnGroupConcerns.add(detailedImportAndExportInventoryRadioButton);
        btnGroupConcerns.add(cancelExportRadioButton);

        concernsPanel.add(sellRadioButton);
        concernsPanel.add(profitRadioButton);
        concernsPanel.add(import_N_RadioButton);
        concernsPanel.add(detailedImportAndExportInventoryRadioButton);
        concernsPanel.add(cancelExportRadioButton);

        concernsPanel.repaint();
        concernsPanel.revalidate();

        // mặc định khi chọn vào tab bán hàng sẽ chọn tiêu chí bán hàng đầu tiên là mặc định
        sellRadioButton.setSelected(true);
        updateDisplayType("Báo cáo và biểu đồ");

        sellRadioButton.addActionListener(e -> {
            // Kiểm tra nếu hàng được chọn
            if (sellRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        profitRadioButton.addActionListener(e -> {
            // Kiểm tra nếu lọi nhuận được chọn
            if (profitRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        import_N_RadioButton.addActionListener(e -> {
            // Kiểm tra nếu xuất nhập tồn được chọn
            if (import_N_RadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });

        detailedImportAndExportInventoryRadioButton.addActionListener(e -> {
            // Kiểm tra nếu xuất nhập tồn chi tiết được chọn
            if (detailedImportAndExportInventoryRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        cancelExportRadioButton.addActionListener(e -> {
            // Kiểm tra nếu xuất hủy được chọn
            if (cancelExportRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });

    }

    private void initTopBartSupplierPanel() {
        concernsPanel.removeAll();
        JLabel label = new JLabel("Mối quan tâm");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(label, "wrap");

        JRadioButton importRadioButton = createRadioButton("Nhập hàng");
        JRadioButton sellRadioButton = createRadioButton("Hàng bán theoNCC");


        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(importRadioButton);
        btnGroupConcerns.add(sellRadioButton);


        concernsPanel.add(importRadioButton);
        concernsPanel.add(sellRadioButton);


        concernsPanel.repaint();
        concernsPanel.revalidate();

        // mặc định khi chọn vào tab bán hàng sẽ chọn tiêu chí nhập hàng đầu tiên là mặc định
        importRadioButton.setSelected(true);
        updateDisplayType("Báo cáo và biểu đồ");

        importRadioButton.addActionListener(e -> {
            // Kiểm tra nếu hàng được chọn
            if (importRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        sellRadioButton.addActionListener(e -> {
            // Kiểm tra nếu Hàng bán theo NCC được chọn
            if (sellRadioButton.isSelected()) {
                updateDisplayType("Báo cáo");
            }
        });


    }

    private void initTopBartFinancePanel() {
        super.timePanel.removeAll();
        concernsPanel.removeAll();
        JLabel label = new JLabel("Thời gian");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        concernsPanel.add(label, "wrap");

        JRadioButton monthRadioButton = createRadioButton("Theo tháng");
        JRadioButton preciousRadioButton = createRadioButton("Theo quý");
        JRadioButton yearRadioButton = createRadioButton("Theo năm");


        ButtonGroup btnGroupConcerns = new ButtonGroup();
        btnGroupConcerns.add(monthRadioButton);
        btnGroupConcerns.add(preciousRadioButton);
        btnGroupConcerns.add(yearRadioButton);

        concernsPanel.add(monthRadioButton);
        concernsPanel.add(preciousRadioButton);
        concernsPanel.add(yearRadioButton);

        concernsPanel.repaint();
        concernsPanel.revalidate();

        // mặc định khi chọn vào tab tài chính sẽ chọn tiêu chí theo quý đầu tiên là mặc định
        preciousRadioButton.setSelected(true);
        updateDisplayType("Báo cáo và biểu đồ");

        preciousRadioButton.addActionListener(e -> {
            // Kiểm tra nếu theo quý được chọn
            if (preciousRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });

        monthRadioButton.addActionListener(e -> {
            // Kiểm tra nếu theo tháng được chọn
            if (monthRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });
        yearRadioButton.addActionListener(e -> {
            // Kiểm tra nếu theo năm được chọn
            if (yearRadioButton.isSelected()) {
                updateDisplayType("Báo cáo và biểu đồ");
            }
        });


    }

    private void setupDisplayTypeReportAndChart() {
        JLabel label = new JLabel("Kiểu hiển thị");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        displayTypePanel.add(label, "wrap");

        JRadioButton chartRadioButton = createRadioButton("Biểu đồ");
        JRadioButton reportRadioButton = createRadioButton("Báo cáo");

        ButtonGroup btnGroupDisplayType = new ButtonGroup();
        btnGroupDisplayType.add(chartRadioButton);
        btnGroupDisplayType.add(reportRadioButton);

        displayTypePanel.add(chartRadioButton);
        displayTypePanel.add(reportRadioButton);
    }

    private void setupDisplayTypeReport() {
        JLabel label = new JLabel("Kiểu hiển thị");
        label.setFont(new Font("Inter", Font.BOLD, 14));
        displayTypePanel.add(label, "wrap");

        JRadioButton reportRadioButton = createRadioButton("Báo cáo");
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

    private void setupTime() {
        JLabel label = new JLabel("Thời gian");
        label.setFont(new Font("Inter", Font.BOLD, 14));

        datePicker = new DatePicker();
        editor = new JFormattedTextField();

        datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setEditor(editor);
        datePicker.setCloseAfterSelected(true);
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {

            }
        });

        editor.setPreferredSize(new Dimension(280, 40));
        editor.setFont(new Font("Inter", Font.BOLD, 15));

        timePanel.add(label, "wrap");
        timePanel.add(editor);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(1165, 733);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.add(new StatisticSalesOldGUI());
            frame.setVisible(true);
        });
    }
}
