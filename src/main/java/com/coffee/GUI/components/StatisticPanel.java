package com.coffee.GUI.components;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatisticPanel extends RoundedPanel {
    public RoundedPanel menu;
    public RoundedPanel content;
    public RoundedPanel[] tabs;
    public StatisticPanel() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(0xFFFFFF));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1140, 760));

        menu = new RoundedPanel();
        content = new RoundedPanel();
        tabs = new RoundedPanel[4];
        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = new RoundedPanel();
            tabs[i].setBackground(new Color(0xA8A8AF));
            tabs[i].setPreferredSize(new Dimension(150, 70));
        }

        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 1,0));
        menu.setPreferredSize(new Dimension(1000, 30));
        menu.setBackground(new Color(0xFFFFFF));
        menu.setBorder(new EmptyBorder(0,30,0,0));
        add(menu, BorderLayout.NORTH);

        content.setLayout(new BorderLayout());
        content.setPreferredSize(new Dimension(1140, 750));
        content.setBackground(new Color(0xA8A8AF));
        add(content, BorderLayout.CENTER);

        for (RoundedPanel tab : tabs) {
            menu.add(tab);
        }
    }
}
