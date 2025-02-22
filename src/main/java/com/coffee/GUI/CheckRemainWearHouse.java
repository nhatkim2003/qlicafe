package com.coffee.GUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.Role_DetailBLL;
import com.coffee.BLL.ShipmentBLL;
import com.coffee.BLL.StaffBLL;
import com.coffee.DTO.*;
import com.coffee.DTO.Shipment;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.MyTextField;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckRemainWearHouse extends DialogForm {
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private Shipment selectedShipment = null;
    private List<Shipment> shipmentList = new ArrayList<>();
    private JScrollPane scrollPane;

    public CheckRemainWearHouse() {
        super();
        super.setTitle("Kiểm kho");
        super.setSize(new Dimension(1000, 600));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init();
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText(data.getText());
                selectedShipment = new ShipmentBLL().searchShipments("id = " + data.getText().split(" - ")[0]).get(0);
                for (Shipment shipment : shipmentList) {
                    if (shipment.getId() == selectedShipment.getId()) {
                        JOptionPane.showMessageDialog(null, "Lô hàng đã được thêm!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                shipmentList.add(selectedShipment);
                loadCheckRemainShipment();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(250, (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
            }
        });
        setVisible(true);
    }

    private void init() {
        JLabel titleName = new JLabel();
        titleName.setText("Kiểm kho");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        RoundedPanel containerSearch = new RoundedPanel();
        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(280, 40));

        content.add(containerSearch, "wrap");

        JLabel iconSearch = new JLabel();
        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        txtSearch = new MyTextField();
        txtSearch.setBackground(new Color(245, 246, 250));
        txtSearch.setBorder(BorderFactory.createEmptyBorder());
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập mã lô hàng cần tìm kiếm");
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        txtSearch.setPreferredSize(new Dimension(250, 30));
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        containerSearch.add(txtSearch);

        scrollPane = new JScrollPane();
        scrollPane.setBackground(Color.cyan);
        scrollPane.setPreferredSize(new Dimension(850, 400));
        content.add(scrollPane, "wrap, span");

        JButton buttonCancel = new JButton("Huỷ");
        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                shipmentList.clear();
                loadCheckRemainShipment();
            }
        });
        containerButton.add(buttonCancel);

        JButton buttonSave = new JButton("Hoàn thành");
        buttonSave.setPreferredSize(new Dimension(150, 30));
        buttonSave.setBackground(new Color(1, 120, 220));
        buttonSave.setForeground(Color.white);
        buttonSave.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editShipemnt();
            }
        });
        containerButton.add(buttonSave);

        loadCheckRemainShipment();
    }

    private void editShipemnt() {
        if (shipmentList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn lô hàng cần kiểm!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (Shipment shipment : shipmentList) {
            Shipment shipmentOld = new ShipmentBLL().searchShipments("id = " + shipment.getId()).get(0);
            Material material = new MaterialBLL().searchMaterials("id  = " + shipment.getMaterial_id()).get(0);
            material.setRemain_wearhouse(material.getRemain_wearhouse() - shipmentOld.getRemain() + shipment.getRemain());
            new ShipmentBLL().updateShipment(shipment);
            new MaterialBLL().updateMaterial(material);
        }
        JOptionPane.showMessageDialog(null, "Kiểm kho hành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void loadCheckRemainShipment() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "10[]10", "10[]10"));
        panel.setBackground(new Color(255, 255, 255));
        panel.setPreferredSize(new Dimension(850, 30));

        JLabel title1 = new JLabel();
        JLabel title2 = new JLabel();
        JLabel title3 = new JLabel();
        JLabel title4 = new JLabel();
        JLabel title5 = new JLabel();
        JLabel title6 = new JLabel();
        JLabel title7 = new JLabel();

        title1.setText("STT");
        title2.setText("Mã lô");
        title3.setText("Tên nguyên liệu");
        title4.setText("Tồn kho");
        title5.setText("Thực tế");
        title6.setText("SL lệch");
        title7.setText("Giá trị lệch");

        title1.setPreferredSize(new Dimension(50, 30));
        title1.setFont((new Font("Inter", Font.BOLD, 13)));

        title2.setPreferredSize(new Dimension(100, 30));
        title2.setFont((new Font("Inter", Font.BOLD, 13)));

        title3.setPreferredSize(new Dimension(200, 30));
        title3.setFont((new Font("Inter", Font.BOLD, 13)));

        title4.setPreferredSize(new Dimension(100, 30));
        title4.setFont((new Font("Inter", Font.BOLD, 13)));

        title5.setPreferredSize(new Dimension(100, 30));
        title5.setFont((new Font("Inter", Font.BOLD, 13)));

        title6.setPreferredSize(new Dimension(100, 30));
        title6.setFont((new Font("Inter", Font.BOLD, 13)));

        title6.setPreferredSize(new Dimension(100, 30));
        title6.setFont((new Font("Inter", Font.BOLD, 13)));

        title7.setPreferredSize(new Dimension(100, 30));
        title7.setFont((new Font("Inter", Font.BOLD, 13)));

        JPanel jpanel1 = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]10[]10[]0[]0", "0[]0"));
        jpanel1.setBackground(new Color(227, 242, 250));
        jpanel1.setPreferredSize(new Dimension(850, 30));

        jpanel1.add(title1);
        jpanel1.add(title2);
        jpanel1.add(title3);
        jpanel1.add(title4);
        jpanel1.add(title5);
        jpanel1.add(title6);
        jpanel1.add(title7);
        jpanel1.add(new JLabel(), "width 50,wrap");
        panel.add(jpanel1, "wrap");

        int i = 1;
        for (Shipment shipemnt : shipmentList) {
            JPanel jPanelBonus = new JPanel(new MigLayout("", "0[]10[]10[]10[]10[]10[]10[]0[]0", "0[]0"));
            jPanelBonus.setBackground(new Color(255, 255, 255));
            jPanelBonus.setPreferredSize(new Dimension(850, 40));

            JLabel jLabelNo = new JLabel(i + "");
            jLabelNo.setPreferredSize(new Dimension(50, 30));
            jLabelNo.setFont((new Font("Inter", Font.PLAIN, 13)));

            JLabel jLabelShipmentID = new JLabel(shipemnt.getId() + "");
            jLabelShipmentID.setPreferredSize(new Dimension(100, 30));
            jLabelShipmentID.setFont((new Font("Inter", Font.PLAIN, 13)));

            Material material = new MaterialBLL().searchMaterials("id  = " + shipemnt.getMaterial_id()).get(0);

            JLabel jLabelName = new JLabel(material.getName());
            jLabelName.setPreferredSize(new Dimension(200, 30));
            jLabelName.setFont((new Font("Inter", Font.PLAIN, 13)));

            JLabel jLabelRemain = new JLabel(shipemnt.getRemain() + "");
            jLabelRemain.setPreferredSize(new Dimension(100, 30));
            jLabelRemain.setFont((new Font("Inter", Font.PLAIN, 13)));

            JTextField jLabel = new MyTextFieldUnderLine();
            jLabel.setPreferredSize(new Dimension(100, 30));
            jLabel.setFont((new Font("Inter", Font.PLAIN, 13)));

            JLabel jLabelRemainCheck = new JLabel();
            jLabelRemainCheck.setPreferredSize(new Dimension(100, 30));
            jLabelRemainCheck.setFont((new Font("Inter", Font.PLAIN, 13)));

            JLabel jLabelCostRemainCheck = new JLabel();
            jLabelCostRemainCheck.setPreferredSize(new Dimension(100, 30));
            jLabelCostRemainCheck.setFont((new Font("Inter", Font.PLAIN, 13)));

            jPanelBonus.add(jLabelNo);
            jPanelBonus.add(jLabelShipmentID);
            jPanelBonus.add(jLabelName);
            jPanelBonus.add(jLabelRemain);
            jPanelBonus.add(jLabel);
            jPanelBonus.add(jLabelRemainCheck);
            jPanelBonus.add(jLabelCostRemainCheck);

            JLabel jLabelIconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
            jLabelIconRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jLabelIconRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    shipmentList.remove(shipemnt);
                    loadCheckRemainShipment();
                }
            });

            jPanelBonus.add(jLabelIconRemove, "width 50,wrap");
            panel.add(jPanelBonus, "wrap");

            jLabel.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        try {
                            Double.parseDouble(jLabel.getText());
                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "Số lượng thực tế không hợp lệ!",
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        double total = Double.parseDouble(jLabel.getText());
                        Material material = new MaterialBLL().searchMaterials("id  = " + shipemnt.getMaterial_id()).get(0);
                        double remainCheck = total - shipemnt.getRemain();
                        jLabelRemainCheck.setText(String.valueOf(remainCheck));
                        double costRemainCheck = remainCheck * material.getUnit_price();
                        jLabelCostRemainCheck.setText(VNString.currency(costRemainCheck));
                        shipemnt.setRemain(total);
                        panel.repaint();
                        panel.revalidate();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

        }
        scrollPane.setViewportView(panel);
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private java.util.List<DataSearch> search(String text) {
        selectedShipment = null;
        java.util.List<DataSearch> list = new ArrayList<>();
        List<Shipment> shipmentList = new ShipmentBLL().findShipments("id", text);
        for (Shipment m : shipmentList) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getId() + " - " + new MaterialBLL().searchMaterials("id = " + m.getMaterial_id()).get(0).getName()));
        }
        return list;
    }

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(250, (search.getItemSize() * 35) + 2);
            } else {
                menu.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            txtSearch.setText(text);

        }
        menu.setVisible(false);

    }
}
