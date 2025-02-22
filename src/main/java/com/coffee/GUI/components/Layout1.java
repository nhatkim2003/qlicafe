package com.coffee.GUI.components;

import javax.swing.*;
import java.awt.*;

public class Layout1 extends RoundedPanel {
    public JPanel top;
    public RoundedPanel bottom;
    public RoundedPanel SearchPanel;
    public RoundedPanel FunctionPanel;

    public Layout1() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));

        top = new JPanel();
        bottom = new RoundedPanel();
        SearchPanel = new RoundedPanel();
        FunctionPanel = new RoundedPanel();

        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(1165, 50));
        top.setBackground(new Color(191, 198, 208));
        add(top, BorderLayout.NORTH);

        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(1165, 680));
        bottom.setBackground(Color.white);
        add(bottom, BorderLayout.SOUTH);

        SearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        SearchPanel.setBackground(new Color(191, 198, 208));
        SearchPanel.setPreferredSize(new Dimension(560, 50));
        top.add(SearchPanel, BorderLayout.WEST);

        FunctionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        FunctionPanel.setBackground(new Color(191, 198, 208));
        FunctionPanel.setPreferredSize(new Dimension(590, 50));
        top.add(FunctionPanel, BorderLayout.EAST);

    }
}
