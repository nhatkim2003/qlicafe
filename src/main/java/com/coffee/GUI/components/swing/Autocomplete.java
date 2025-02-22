///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.coffee.GUI.components.swing;
//
//import com.coffee.BLL.MaterialBLL;
//import com.coffee.DTO.Material;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.*;
//
//
//
//
///**
// *
// * @author RAVEN
// */
//public class Autocomplete  {
//
//    private JPopupMenu menu;
//    private PanelSearch search;
//    private MyTextField txtSearch;
//    MaterialBLL materialBLL = new MaterialBLL();
//    public Autocomplete() {
//        initComponents();
//        menu = new JPopupMenu();
//        search = new PanelSearch();
//        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
//        menu.add(search);
//        menu.setFocusable(false);
//        search.addEventClick(new EventClick() {
//            @Override
//            public void itemClick(DataSearch data) {
//                menu.setVisible(false);
//                txtSearch.setText(data.getText());
//                System.out.println("Click Item : " + data.getText());
//            }
//
//            @Override
//            public void itemRemove(Component com, DataSearch data) {
//                search.remove(com);
//                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
//                if (search.getItemSize() == 0) {
//                    menu.setVisible(false);
//                }
//                System.out.println("Remove Item : " + data.getText());
//            }
//        });
//    }
//
//    private void initComponents() {
//
//        txtSearch = new MyTextField();
//
//        txtSearch.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search.png"))); // NOI18N
//        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                txtSearchMouseClicked(evt);
//            }
//        });
//        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//                txtSearchKeyPressed(evt);
//            }
//            public void keyReleased(java.awt.event.KeyEvent evt) {
//                txtSearchKeyReleased(evt);
//            }
//        });
//
////        GroupLayout layout = new GroupLayout();
////        layout.setHorizontalGroup(
////            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
////            .addGroup(layout.createSequentialGroup()
////                .addContainerGap()
////                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
////                .addContainerGap(388, Short.MAX_VALUE))
////        );
////        layout.setVerticalGroup(
////            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
////            .addGroup(layout.createSequentialGroup()
////                .addContainerGap()
////                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
////                .addContainerGap(439, Short.MAX_VALUE))
////        );
//
//
//    }// </editor-fold>//GEN-END:initComponents
//
//    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
//        if (search.getItemSize() > 0) {
//            menu.show(txtSearch, 0, txtSearch.getHeight());
//            search.clearSelected();
//        }
//    }
//
//    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
//        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
//            String text = txtSearch.getText().trim().toLowerCase();
//            search.setData(search(text));
//            if (search.getItemSize() > 0) {
//                menu.show(txtSearch, 0, txtSearch.getHeight());
//                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
//            } else {
//                menu.setVisible(false);
//            }
//        }
//    }//GEN-LAST:event_txtSearchKeyReleased
//
//    private List<DataSearch> search(String text) {
//        List<DataSearch> list = new ArrayList<>();
//        List<Material>  materials = materialBLL.findMaterials("name", text);
//        for(Material m : materials)
//            list.add(new DataSearch(m.getName()));
//        return list;
//    }
//
//    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_UP) {
//            search.keyUp();
//        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
//            search.keyDown();
//        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            String text = search.getSelectedText();
//            txtSearch.setText(text);
//            menu.setVisible(false);
//        }
//    }//GEN-LAST:event_txtSearchKeyPressed
//
//}
