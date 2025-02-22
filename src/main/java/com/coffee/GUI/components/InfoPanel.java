package com.coffee.GUI.components;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends RoundedPanel {
    public JPanel TitleInfoAccount;
    public RoundedPanel InfoAccountPanel;
    public JPanel TitleInfoStaff;
    public RoundedPanel InfoStaffPanel;

    public InfoPanel() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(255, 255, 255));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setPreferredSize(new Dimension(1165, 733));

        TitleInfoAccount = new JPanel();
        InfoAccountPanel = new RoundedPanel();
        TitleInfoStaff = new JPanel();
        InfoStaffPanel = new RoundedPanel();

        TitleInfoAccount.setLayout(new BorderLayout());
        TitleInfoAccount.setPreferredSize(new Dimension(1160, 50));
        TitleInfoAccount.setBackground(new Color(255, 255, 255));
        add(TitleInfoAccount);

        InfoAccountPanel.setLayout(new MigLayout("", "150[]90[]10[]150"));
        InfoAccountPanel.setPreferredSize(new Dimension(1160, 120));
        InfoAccountPanel.setBackground(new Color(255, 255, 255));
        add(InfoAccountPanel);

        TitleInfoStaff.setLayout(new BorderLayout());
        TitleInfoStaff.setPreferredSize(new Dimension(1160, 50));
        TitleInfoStaff.setBackground(new Color(255, 255, 255));
        add(TitleInfoStaff);

        InfoStaffPanel.setLayout(new MigLayout("", "150[]50[]50[]50[]150", "20[]20"));
        InfoStaffPanel.setPreferredSize(new Dimension(1160, 493));
        InfoStaffPanel.setBackground(new Color(255, 255, 255));
        add(InfoStaffPanel);
    }
}
