/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coffee.GUI.components.swing;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author RAVEN
 */
public class Search_Item extends JPanel {
    private JLabel lbIcon;
    private JLabel lblPrice;
    private JLabel lbText;
    public Search_Item(DataSearch data) {
        initComponents();
        setData(data);
    }
    public Search_Item(DataSearch data,String a) {
        initComponents1();
        setData1(data);
    }

    private void setData(DataSearch data) {
        addEventMouse(this);
        addEventMouse(lbText);
        lbText.setText(data.getText());
    }
    private void setData1(DataSearch data) {
        addEventMouse(this);
        addEventMouse(lbText);
        lbText.setText(data.getText() + " (" + data.getText1() + ") ");
        lblPrice.setText("Giá bán: " + data.getPrice());
    }

    private void addEventMouse(Component com) {
        com.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(new Color(215, 216, 216));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(Color.WHITE);
            }

        });
    }

    private ActionListener eventClick;

    public void addEvent(ActionListener eventClick) {
        this.eventClick = eventClick;
    }

    private void initComponents() {
        setLayout(new FlowLayout());
        lbIcon = new javax.swing.JLabel();
        lbText = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lbIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search_small.png")));
        lbText.setFont(new java.awt.Font("sansserif", 0, 14));
        lbText.setForeground(new java.awt.Color(38, 38, 38));

        lbText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbTextMouseClicked(evt);
            }
        });

        add(lbIcon);
        add(lbText);

    }
    private void initComponents1() {
        lbIcon = new JLabel();
        lbText = new JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lbIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search_small.png")));
        lbText.setFont(new java.awt.Font("sansserif", 0, 14));
        lbText.setForeground(new java.awt.Color(38, 38, 38));

        lbText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbTextMouseClicked(evt);
            }
        });
        lblPrice = new JLabel();
        lblPrice.setPreferredSize(new Dimension(100,20));
        lblPrice.setFont(new java.awt.Font("sansserif", 0, 14));
        lblPrice.setForeground(new java.awt.Color(38, 38, 38));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbText, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbText, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lblPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    private void lbTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbTextMouseClicked
        eventClick.actionPerformed(null);
    }

    public String getText() {
        return lbText.getText();
    }

    public void setSelected(boolean act) {
        if (act) {
            setBackground(new Color(215, 216, 216));
        } else {
            setBackground(Color.WHITE);
        }
    }
}
