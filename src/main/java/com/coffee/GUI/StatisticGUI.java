package com.coffee.GUI;

import com.coffee.GUI.components.RoundedPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class StatisticGUI extends RoundedPanel {
    private final StatisticEndOfTheDayGUI statisticEndOfTheDayGUI = new StatisticEndOfTheDayGUI();
    private final StatisticSaleGUI statisticSaleGUI = new StatisticSaleGUI();
    private final StatisticStaffGUI statisticStaffGUI = new StatisticStaffGUI();
    private final StatisticProductGUI statisticProductGUI = new StatisticProductGUI();
    private final StatisticFinanceGUI statisticFinanceGUI = new StatisticFinanceGUI();

    public StatisticGUI() {
        setBackground(new Color(191, 198, 208));
        setPreferredSize(new Dimension(1165, 733));
        setLayout(new BorderLayout());
        init();
        setVisible(true);
    }

    private void init() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setBackground(Color.white);
        jTabbedPane.setPreferredSize(new Dimension(1165, 733));
        jTabbedPane.addTab("Cuối Ngày", new FlatSVGIcon("icon/pie-chart-svgrepo-com.svg"), statisticEndOfTheDayGUI);
        jTabbedPane.addTab("Bán Hàng", new FlatSVGIcon("icon/receipt-svgrepo-com.svg"), statisticSaleGUI);
        jTabbedPane.addTab("Nhân Viên", new FlatSVGIcon("icon/icon_module1.svg"), statisticStaffGUI);
        jTabbedPane.addTab("Sản Phẩm", new FlatSVGIcon("icon/box-delivery-logistic-package-present-product-svgrepo-com.svg"), statisticProductGUI);
        jTabbedPane.addTab("Tài Chính", new FlatSVGIcon("icon/statistic-graph-chart-growth-finance-svgrepo-com.svg"), statisticFinanceGUI);
        add(jTabbedPane, BorderLayout.CENTER);

        jTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jTabbedPane.getSelectedIndex() == 2) {
                    statisticStaffGUI.loadBarChart();
                    statisticStaffGUI.loadPieChart();
                    statisticStaffGUI.loadLineChart();
                }
            }
        });
    }


}
