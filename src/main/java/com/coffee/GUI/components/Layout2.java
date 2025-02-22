package com.coffee.GUI.components;

import javax.swing.*;
import java.awt.*;

public class Layout2 extends RoundedPanel {
    public JPanel top;
    public RoundedPanel bottom;
    public RoundedPanel containerSearchPanel;
    public RoundedPanel FunctionPanel;
    public RoundedPanel FilterDatePanel;
    public RoundedPanel SearchPanel;

    public Layout2() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));

        top = new JPanel();
        bottom = new RoundedPanel();
        containerSearchPanel = new RoundedPanel();
        SearchPanel = new RoundedPanel();
        FilterDatePanel = new RoundedPanel();
        FunctionPanel = new RoundedPanel();

        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(1165, 100));
        top.setBackground(new Color(191, 198, 208));
        add(top, BorderLayout.NORTH);

        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(1165, 630));
        bottom.setBackground(Color.white);
        add(bottom, BorderLayout.SOUTH);

        containerSearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        containerSearchPanel.setBackground(new Color(191, 198, 208));
        containerSearchPanel.setPreferredSize(new Dimension(1165, 100));
        top.add(containerSearchPanel, BorderLayout.CENTER);

        FilterDatePanel.setLayout(new BorderLayout());
        FilterDatePanel.setBackground(new Color(191, 198, 208));
        FilterDatePanel.setPreferredSize(new Dimension(1165, 40));
        containerSearchPanel.add(FilterDatePanel);

        SearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        SearchPanel.setBackground(new Color(191, 198, 208));
        SearchPanel.setPreferredSize(new Dimension(600, 50));
        containerSearchPanel.add(SearchPanel);

        FunctionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        FunctionPanel.setBackground(new Color(191, 198, 208));
        FunctionPanel.setPreferredSize(new Dimension(650, 40));
        FilterDatePanel.add(FunctionPanel, BorderLayout.EAST);
    }
}
