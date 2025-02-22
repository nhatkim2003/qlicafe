package com.coffee.GUI.components;

import net.miginfocom.swing.MigLayout;


import javax.swing.*;
import java.awt.*;

public class LayoutStatistic extends RoundedPanel {
    public RoundedPanel top;
    public RoundedPanel center;
    public RoundedPanel displayTypePanel;
    public RoundedPanel concernsPanel;
    public RoundedPanel timePanel;



    public LayoutStatistic() {
        initializeComponents();
    }

    private void initializeComponents() {
        setBackground(new Color(191, 198, 208));
        setPreferredSize(new Dimension(1165, 733));
        setLayout(new BorderLayout());

        top = createRoundedPanel(new Color(191, 198, 208), new FlowLayout(FlowLayout.LEFT), 1165, 100);
        center = createRoundedPanel(Color.CYAN, new BorderLayout(), 1165, 680);
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        displayTypePanel = createRoundedPanel(new MigLayout("", "[]", "[][][]"), 300, 70);
        concernsPanel = createRoundedPanel(new MigLayout("", "[]", "[][][]"), 515, 70);
        timePanel = createRoundedPanel(new MigLayout("", "[]", "[][][]"), 300, 70);

        top.add(displayTypePanel);
        top.add(concernsPanel);
        top.add(timePanel);


        JTabbedPane jTabbedPane = new JTabbedPane();
        RoundedPanel endOfDayPanel = new RoundedPanel();
        RoundedPanel salesPanel = new RoundedPanel();
        RoundedPanel productPanel = new RoundedPanel();
        RoundedPanel supplierPanel = new RoundedPanel();
        RoundedPanel employeePanel = new RoundedPanel();
        RoundedPanel financePanel = new RoundedPanel();


        endOfDayPanel.setBackground(Color.WHITE);
        salesPanel.setBackground(Color.WHITE);
        productPanel.setBackground(Color.WHITE);
        supplierPanel.setBackground(Color.WHITE);
        employeePanel.setBackground(Color.WHITE);
        financePanel.setBackground(Color.WHITE);


        jTabbedPane.addTab("Cuối Ngày", endOfDayPanel);
        jTabbedPane.addTab("Bán Hàng", salesPanel);
        jTabbedPane.addTab("Sản Phẩm", productPanel);
        jTabbedPane.addTab("Nhà cung cấp", supplierPanel);
        jTabbedPane.addTab("Nhân Viên", employeePanel);
        jTabbedPane.addTab("Tài Chính", financePanel);
        center.add(jTabbedPane, BorderLayout.CENTER);


    }

    private RoundedPanel createRoundedPanel(Color color, LayoutManager layoutManager, int width, int height) {
        RoundedPanel panel = new RoundedPanel();
        panel.setBackground(color);
        panel.setLayout(layoutManager);
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }

    private RoundedPanel createRoundedPanel(LayoutManager layoutManager, int width, int height) {
        RoundedPanel panel = new RoundedPanel();
        panel.setLayout(layoutManager);
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }
}
