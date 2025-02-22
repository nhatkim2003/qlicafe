package com.coffee.GUI.components;

import javax.swing.*;
import java.awt.*;

public class Layout3 extends RoundedPanel {
    public JPanel top;
    public JPanel center;
    public RoundedPanel bottom;
    public RoundedPanel SearchPanel;
    public RoundedPanel FunctionPanel;
    public JPanel Category;
    public RoundedScrollPane scrollPane;

    public Layout3() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));

        top = new JPanel();
        center = new JPanel();
        bottom = new RoundedPanel();
        SearchPanel = new RoundedPanel();
        FunctionPanel = new RoundedPanel();
        Category = new JPanel();
        scrollPane = new RoundedScrollPane(Category, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(1165, 50));
        top.setBackground(new Color(191, 198, 208));
        add(top, BorderLayout.NORTH);

        center.setLayout(new BorderLayout());
        center.setBorder(BorderFactory.createMatteBorder(10, 0, 10, 0, new Color(191, 198, 208)));
        center.setPreferredSize(new Dimension(1165, 85));
        add(center, BorderLayout.CENTER);

        Category.setLayout(new FlowLayout(FlowLayout.LEFT));
        Category.setBackground(new Color(191, 198, 208));

        center.add(scrollPane, BorderLayout.CENTER);

        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(1165, 625));
        bottom.setBackground(Color.white);
        add(bottom, BorderLayout.SOUTH);

        SearchPanel.setLayout(new GridBagLayout());
        SearchPanel.setBackground(new Color(191, 198, 208));
        SearchPanel.setPreferredSize(new Dimension(510, 50));
        top.add(SearchPanel, BorderLayout.WEST);

        FunctionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        FunctionPanel.setBackground(new Color(191, 198, 208));
        FunctionPanel.setPreferredSize(new Dimension(620, 50));
        top.add(FunctionPanel, BorderLayout.EAST);

    }
}
