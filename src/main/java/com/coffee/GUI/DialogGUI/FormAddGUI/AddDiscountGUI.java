package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.DiscountBLL;
import com.coffee.BLL.Discount_DetailBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.DTO.Discount;
import com.coffee.DTO.Discount_Detail;
import com.coffee.DTO.Product;
import com.coffee.GUI.DialogGUI.DialogFormDetail_1;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.SaleGUI;
import com.coffee.GUI.components.DatePicker;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.main.Cafe_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class AddDiscountGUI extends DialogFormDetail_1 {
    private DiscountBLL discountBLL = new DiscountBLL();
    List<Discount_Detail> discountInfoList = new ArrayList<>();
    int id = discountBLL.getAutoID(discountBLL.searchDiscounts());
    Discount_DetailBLL discount_detailBLL = new Discount_DetailBLL();
    private DatePicker[] datePicker;
    private JFormattedTextField[] editor;

    private ProductBLL productBLL = new ProductBLL();


    private JPanel containerForm;
    private JPanel containerProductType = new JPanel();
    private RoundedPanel containerProductTypeContent = new RoundedPanel();
    private JButton btnAddConditions;
    private JTextField txtSearch;
    private JPopupMenu menu;
    private PanelSearch search;
    private RoundedPanel containerBillType = new RoundedPanel();
    private RoundedPanel containerBillTypeContent = new RoundedPanel();
    private JLabel lblForm;
    private JLabel lblFormValue;
    private JLabel lblBuy;
    private JComboBox<String> cbDiscountType;
    private JScrollPane scrollPaneProduct;
    private JScrollPane scrollPaneBill;
    private JTextField txtDiscountCode;
    private JTextField txtProgramName;
    private JRadioButton radio1;
    private JRadioButton radio2;
    private ButtonGroup btgroup;

    public AddDiscountGUI() {
        super();
        super.setTitle("Thêm chương trình giảm giá");
        super.getContentPane().setBackground(new Color(255, 255, 255));
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
                txtSearch.setText(data.getText() + " (" + data.getText1() + ")");
                System.out.println("Click Item : " + data.getText());
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
                System.out.println("Remove Item : " + data.getText());
            }
        });
        setVisible(true);
    }

    public void init() {
        content.setBackground(new Color(255, 255, 255));
        top.setLayout(new MigLayout("", "30[]30[]60[]30[]", "[][]"));
        top.setPreferredSize(new Dimension(1000, 100));
        center.setBackground(new Color(255, 255, 255));
        center.setPreferredSize(new Dimension(1000, 70));
        center.setLayout(new MigLayout("", "30[]30[]30[]100[]", "[]"));
//        bottom.setBackground(new Color(255, 255, 255));
        bottom.setBackground(new Color(242, 242, 242));

        bottom.setPreferredSize(new Dimension(1000, 400));

        JLabel lblDiscountCode = createLabel("Mã giảm giá");
        JLabel lblProgramName = createLabel("Tên chương trình");
        JLabel lblWordEffect = createLabel("Hiệu lực từ");
        JLabel lblArrive = createLabel("Đến");
        JLabel lblStatus = createLabel("Trạng thái");

        txtDiscountCode = new MyTextFieldUnderLine();
        txtDiscountCode.setFocusable(false);
        txtDiscountCode.setText(id + "");
        txtProgramName = new MyTextFieldUnderLine();

        radio1 = new JRadioButton("Đang áp dụng");
        radio1.setActionCommand("Đang áp dụng");

        radio2 = new JRadioButton("Ngừng áp dụng");
        radio2.setActionCommand("Ngừng áp dụng");
        btgroup = new ButtonGroup();
        btgroup.add(radio1);
        btgroup.add(radio2);

        JPanel panelTimeApplication = new JPanel();
        panelTimeApplication.setPreferredSize(new Dimension(500, 40));
        panelTimeApplication.setLayout(new MigLayout("", "[][]30[][]", "15[]"));
        panelTimeApplication.setBackground(new Color(255, 255, 255));

        JPanel panelStatus = new JPanel();
        panelStatus.setLayout(new MigLayout("", "[]50[]50[]", "15[]"));
        panelStatus.setBackground(new Color(255, 255, 255));
        panelStatus.setPreferredSize(new Dimension(500, 40));
        panelStatus.add(lblStatus);
        panelStatus.add(radio1);
        panelStatus.add(radio2);

        datePicker = new DatePicker[2];
        editor = new JFormattedTextField[2];

        for (int i = 0; i < 2; i++) {
            datePicker[i] = new DatePicker();
            editor[i] = new JFormattedTextField();

            datePicker[i].setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
            datePicker[i].setEditor(editor[i]);
            datePicker[i].setCloseAfterSelected(true);

            editor[i].setPreferredSize(new Dimension(150, 30));
            editor[i].setFont(new Font("Inter", Font.BOLD, 15));

            if (i == 0) {
                panelTimeApplication.add(lblWordEffect);
            } else {
                lblArrive.setPreferredSize(new Dimension(30, 30));
                panelTimeApplication.add(lblArrive);
            }
            panelTimeApplication.add(editor[i]);
        }

        top.add(lblDiscountCode);
        top.add(txtDiscountCode);
        top.add(panelTimeApplication, "wrap");
        top.add(lblProgramName);
        top.add(txtProgramName);
        top.add(panelStatus, "wrap");

        JLabel lblDiscountType = createLabel("Giảm giá theo");
        lblDiscountType.setPreferredSize(new Dimension(100, 30));

        String[] items = {"Sản phẩm", "Đơn hàng"};
        cbDiscountType = new JComboBox<>(items);
        cbDiscountType.setBackground(new Color(1, 120, 220));
        cbDiscountType.setForeground(Color.white);
        cbDiscountType.setPreferredSize(new Dimension(100, 30));
        cbDiscountType.addActionListener(e -> {
            SelectDiscountType();
        });

        containerForm = new JPanel();
        containerForm.setLayout(new MigLayout("", "[]20[]", "[]"));
        containerForm.setPreferredSize(new Dimension(300, 40));
        containerForm.setBackground(new Color(255, 255, 255));

        lblForm = createLabel("Hình thức");
        lblFormValue = createLabel("");
        lblFormValue.setFont((new Font("Public Sans", Font.PLAIN, 14)));

        containerForm.add(lblForm);
        lblFormValue.setText("Giảm giá (theo SL mua)");
        containerForm.add(lblFormValue);

        btnAddConditions = new JButton("Thêm điều kiện");
        btnAddConditions.setBackground(new Color(1, 120, 220));
        btnAddConditions.setForeground(new Color(255, 255, 255));
        btnAddConditions.setIcon(new FlatSVGIcon("icon/Add1.svg"));
        btnAddConditions.setPreferredSize(new Dimension(100, 40));
        btnAddConditions.setFont(new Font("Public Sans", Font.PLAIN, 15));
        btnAddConditions.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddConditions.setFocusPainted(false);
        btnAddConditions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) cbDiscountType.getSelectedItem();
                if (selectedValue.equals("Đơn hàng")) {
                    createPanel_Bill(containerBillTypeContent);
                } else {
                    createPanelDiscountProduct();
                }
            }
        });

        center.add(lblDiscountType);
        center.add(cbDiscountType);
        center.add(containerForm);
        center.add(btnAddConditions);

        JLabel totalBill = createLabel("Tổng hóa đơn");
        JLabel discount = createLabel("Giảm giá");

        containerBillType.setLayout(new MigLayout("", "30[]50[]", "[]"));
        containerBillType.setBackground(new Color(242, 242, 242));
//        containerBillType.setBackground(new Color(255, 255, 255));

        containerBillType.setPreferredSize(new Dimension(1000, 50));
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225));
        containerBillType.setBorder(bottomBorder);
        containerBillType.add(totalBill);
        containerBillType.add(discount);

        containerBillTypeContent.setLayout(new MigLayout("", ""));

        containerBillTypeContent.setBackground(new Color(242, 242, 242));
        scrollPaneBill = new JScrollPane(containerBillTypeContent);
        scrollPaneBill.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneBill.setBorder(null);
        scrollPaneBill.setBackground(new Color(255, 255, 255));
        scrollPaneBill.setWheelScrollingEnabled(true);
        scrollPaneBill.getVerticalScrollBar().setUnitIncrement(13);
        createPanel_Bill(containerBillTypeContent);

        containerProductType.setLayout(new MigLayout("", "[]", ""));

        containerProductType.setBackground(new Color(255, 255, 255));
        scrollPaneProduct = new JScrollPane(containerProductType);
        scrollPaneProduct.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPaneProduct, BorderLayout.CENTER);
        scrollPaneProduct.setBorder(null);
        scrollPaneProduct.setBackground(new Color(255, 255, 255));
        scrollPaneProduct.setWheelScrollingEnabled(true);
        scrollPaneProduct.getVerticalScrollBar().setUnitIncrement(13);

        bottom.add(scrollPaneProduct, BorderLayout.CENTER);
        createPanelDiscountProduct();

        JButton buttonCancel = new JButton("Huỷ");
//        buttonCancel.setBackground(new Color(213, 50, 77));
//        buttonCancel.setForeground(new Color(255, 255, 255));
        buttonCancel.setPreferredSize(new Dimension(100, 35));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
//                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);
        JButton buttonAdd = new JButton("Thêm");
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(new Color(255, 255, 255));
        buttonAdd.setPreferredSize(new Dimension(100, 35));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String selectedValue = (String) cbDiscountType.getSelectedItem();
                assert selectedValue != null;
                if (selectedValue.equals("Đơn hàng")) {
                    if (checkDiscount() && get_discount_information_from_bill_panels()) {
                        // trước khi thêm đợt giảm giá mới, cập nhật các mã giãm giá cũ về ngưng áp dụng
                        updateStatusDicount();
                        if (addDiscount() && addDiscount_Detail()) {
                            JOptionPane.showMessageDialog(null, "Thêm đợt giảm giá thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            loadSaleGUI();
                            dispose();
                        }
                    }
                } else {
                    if (checkDiscount() && get_discount_information_from_product_panels()) {
                        updateStatusDicount();
                        if (addDiscount() && addDiscount_Detail()) {
                            JOptionPane.showMessageDialog(null, "Thêm đợt giảm giá thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            loadSaleGUI();
                            dispose();
                        }
                    }
                }

            }
        });
        containerButton.add(buttonAdd);
    }

    private boolean checkDiscount() {
        Pair<Boolean, String> result;
        String name;
        Date startDate, endDate;

        name = txtProgramName.getText();
        result = discountBLL.validateName(name);
        if (!result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        startDate = datePicker[0].getDateSQL_Single();
        endDate = datePicker[1].getDateSQL_Single();

        result = discountBLL.validateDate(startDate, endDate);
        if (!result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean addDiscount() {
        Pair<Boolean, String> result;
        String name;
        Date startDate, endDate;
        boolean type = true;
        boolean status = true;

        name = txtProgramName.getText();

        startDate = datePicker[0].getDateSQL_Single() != null ? java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(datePicker[0].getDateSQL_Single())) : null;
        endDate = datePicker[1].getDateSQL_Single() != null ? java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(datePicker[1].getDateSQL_Single())) : null;

        Object selectedItem = cbDiscountType.getSelectedItem();
        if (selectedItem != null) {
            String typeText = selectedItem.toString();
            type = !typeText.equals("Sản phẩm");
        }

        ButtonModel selectedButtonModel = btgroup.getSelection();
        if (selectedButtonModel != null) {
            String selectedValue = selectedButtonModel.getActionCommand();
            status = !selectedValue.equals("Đang áp dụng");
        }

        Discount discount = new Discount(id, name, startDate, endDate, type, status);
        result = discountBLL.addDiscount(discount);
        return result.getKey();
    }

    private void updateStatusDicount() {
        List<Discount> discountList = discountBLL.searchDiscounts("status =0");

        for (Discount discount : discountList) {
            discount.setStatus(true);
            Pair<Boolean, String> result = discountBLL.updateStatusDiscount(discount);
            if (!result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean addDiscount_Detail() {
        for (Discount_Detail d : discountInfoList) {
            Pair<Boolean, String> result;
            result = discount_detailBLL.addDiscount_Detail(d);
            if (!result.getKey()) {
                return false;
            }
        }
        return true;
    }

    private void SelectDiscountType() {
        containerForm.removeAll();
        bottom.removeAll();
        String selectedValue = (String) cbDiscountType.getSelectedItem();
        if (selectedValue.equals("Đơn hàng")) {
            containerForm.add(lblForm);
            lblFormValue.setText("Giảm giá đơn hàng");
            containerForm.add(lblFormValue);
            bottom.add(containerBillType, BorderLayout.NORTH);
            bottom.add(scrollPaneBill, BorderLayout.CENTER);
        } else {
            containerForm.add(lblForm);
            lblFormValue.setText("Giảm giá (theo SL mua)");
            containerForm.add(lblFormValue);
            bottom.add(scrollPaneProduct, BorderLayout.CENTER);
        }
        containerForm.revalidate();
        containerForm.repaint();
        bottom.revalidate();
        bottom.repaint();
    }

    private boolean get_discount_information_from_bill_panels() {
        boolean hasError = false;
        Set<Double> seenTotalBills = new HashSet<>();

        for (Component component : containerBillTypeContent.getComponents()) {
            if (component instanceof RoundedPanel && component.getName() != null && component.getName().equals("Panel_Bill")) {
                RoundedPanel panel = (RoundedPanel) component;
                JTextField txtTotalBill = (JTextField) panel.getComponent(1);
                JTextField txtReduction = (JTextField) panel.getComponent(3);

                if (txtTotalBill.getText().trim().isEmpty() || txtReduction.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    hasError = true;
                    break;
                }

                double totalBill = Double.parseDouble(txtTotalBill.getText().trim());
                double discountPercentage = Double.parseDouble(txtReduction.getText().trim());

                if (!seenTotalBills.add(totalBill)) {
                    JOptionPane.showMessageDialog(this, "Điều kiện giảm giá đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtTotalBill.requestFocus();
                    hasError = true;
                    break;
                }

                if (discountPercentage > 100) {
                    JOptionPane.showMessageDialog(this, "Phần trăm giảm không được lớn hơn 100", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtReduction.requestFocus();
                    hasError = true;
                    break;
                }

                Discount_Detail discount_detail = new Discount_Detail(id, 0, "0", 0, discountPercentage, totalBill);
                discountInfoList.add(discount_detail);
            }
        }

        if (hasError) {
            discountInfoList.clear();
            return false;
        }
        return true;
    }

    private RoundedPanel createPanel_Bill(RoundedPanel contaierDiscountBill) {
        RoundedPanel Panel_Bill = new RoundedPanel();
        Panel_Bill.setName("Panel_Bill");
//        Panel_Bill.setBackground(new Color(255,255,255));
        Panel_Bill.setBackground(new Color(242, 242, 242));
        Panel_Bill.setPreferredSize(new Dimension(920, 30));

        Panel_Bill.setLayout(new MigLayout("", "25[]20[]25[]20[]20[]520[]"));
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225));
        Panel_Bill.setBorder(bottomBorder);

        JLabel quantity = createLabel("Từ");
        JTextField txtQuantity = new MyTextFieldUnderLine();
        txtQuantity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        JLabel discount = createLabel("Giảm ");
        JTextField txtValue = new MyTextFieldUnderLine();
        txtValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        JButton percent = createButton();
        percent.setContentAreaFilled(true);
        percent.setText(" % ");
        percent.setBackground(new Color(52, 147, 54));
        percent.setForeground(Color.WHITE);
        percent.setFont(new Font("Times New Roman", Font.BOLD, 14));
        percent.setPreferredSize(new Dimension(30, 30));
        percent.setMargin(new Insets(0, 5, 0, 5));

        Panel_Bill.add(quantity);
        Panel_Bill.add(txtQuantity);
        Panel_Bill.add(discount);
        Panel_Bill.add(txtValue);
        Panel_Bill.add(percent);

        JButton btnRemoveRow = createButton();
        btnRemoveRow.setIcon(new FlatSVGIcon("icon/icons8-remove-26.svg"));

        btnRemoveRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contaierDiscountBill.remove(Panel_Bill);
                contaierDiscountBill.revalidate();
                contaierDiscountBill.repaint();
            }
        });

        Panel_Bill.add(btnRemoveRow);

        contaierDiscountBill.add(Panel_Bill, "wrap ");
        contaierDiscountBill.revalidate();
        contaierDiscountBill.repaint();
        return Panel_Bill;
    }

    private void createPanelDiscountProduct() {
        RoundedPanel contaierDiscountProduct = new RoundedPanel();
        contaierDiscountProduct.setLayout(new MigLayout("", "[]"));
        contaierDiscountProduct.setPreferredSize(new Dimension(915, 100));

        RoundedPanel contaierNameProduct = createPanelProductName(contaierDiscountProduct);
        contaierDiscountProduct.add(contaierNameProduct, BorderLayout.NORTH);

        createPanel_Discount_Detail_Product(contaierDiscountProduct);

        RoundedPanel containerbtnAddRow = createContaierBtnAddRow(contaierDiscountProduct);
        contaierDiscountProduct.add(containerbtnAddRow, BorderLayout.SOUTH);

        containerProductType.add(contaierDiscountProduct, "wrap");
        containerProductType.revalidate();
        containerProductType.repaint();

    }

    private RoundedPanel createPanelProductName(RoundedPanel contaierDiscountProduct) {
        RoundedPanel contaierNameProduct = new RoundedPanel();
        contaierNameProduct.setLayout(new MigLayout("", "10[]30[]240[]"));
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)); // Viền dưới
        contaierNameProduct.setBorder(bottomBorder);

        JLabel lblBuy = createLabel("Khi mua");
        RoundedPanel contaierSearch = new RoundedPanel();
        contaierSearch.setLayout(new MigLayout("", "[][]"));
        JTextField txtS = new MyTextFieldUnderLine();

        txtS.setPreferredSize(new Dimension(500, 30));
        txtS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearch = txtS;
                txtSearchMouseClicked(evt);
            }
        });
        txtS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearch = txtS;
                txtSearchKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearch = txtS;
                txtSearchKeyReleased(evt);
            }
        });

        JButton category = createButton();
        category.setIcon(new FlatSVGIcon("icon/icons8-category-20.svg"));

        JComboBox<String> comboBox = createCategoryByButton(category);
        comboBox.addActionListener(e -> {
            String value = comboBox.getSelectedItem().toString();
            txtSearch = txtS;
            txtSearch.setText("Thể loại: " + value);
        });

        contaierSearch.add(txtS);
        contaierSearch.add(category);
        contaierSearch.add(comboBox);

        JButton btnRemove = createButton();
        btnRemove.setIcon(new FlatSVGIcon("icon/icons8-remove-26.svg"));
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                containerProductType.remove(contaierDiscountProduct);
                containerProductType.revalidate();
                containerProductType.repaint();
            }
        });

        contaierNameProduct.add(lblBuy);
        contaierNameProduct.add(contaierSearch);
        contaierNameProduct.add(btnRemove);

        return contaierNameProduct;
    }

    private void createPanel_Discount_Detail_Product(RoundedPanel contaierDiscountProduct) {
        RoundedPanel Panel_Discount_Detail_Product = new RoundedPanel();
        Panel_Discount_Detail_Product.setName("Panel_Discount_Detail_Product");
        Panel_Discount_Detail_Product.setLayout(new MigLayout("", "100[]20[]20[]20[]20[]390[]10"));
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225));
        Panel_Discount_Detail_Product.setBorder(bottomBorder);

        JLabel quantity = createLabel("Số lượng từ");
        JTextField txtQuantity = new MyTextFieldUnderLine();
        txtQuantity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        JLabel discount = createLabel("Giảm giá");
        JTextField txtValue = new MyTextFieldUnderLine();
        txtValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        JButton percent = createButton();
        percent.setContentAreaFilled(true);
        percent.setText(" % ");
        percent.setBackground(new Color(52, 147, 54));
        percent.setForeground(Color.WHITE);
        percent.setFont(new Font("Times New Roman", Font.BOLD, 14));
        percent.setPreferredSize(new Dimension(30, 40));
        percent.setMargin(new Insets(0, 5, 0, 5));

        Panel_Discount_Detail_Product.add(quantity);
        Panel_Discount_Detail_Product.add(txtQuantity);
        Panel_Discount_Detail_Product.add(discount);
        Panel_Discount_Detail_Product.add(txtValue);
        Panel_Discount_Detail_Product.add(percent);

        JButton btnRemoveRow = createButton();
        btnRemoveRow.setIcon(new FlatSVGIcon("icon/icons8-minus-26.svg"));

        btnRemoveRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contaierDiscountProduct.remove(Panel_Discount_Detail_Product);
                contaierDiscountProduct.revalidate();
                contaierDiscountProduct.repaint();
            }
        });

        Panel_Discount_Detail_Product.add(btnRemoveRow);

        contaierDiscountProduct.add(Panel_Discount_Detail_Product, "wrap ");
        contaierDiscountProduct.revalidate();
        contaierDiscountProduct.repaint();

    }

    private RoundedPanel createContaierBtnAddRow(RoundedPanel contaierDiscountProduct) {
        RoundedPanel containerbtnAddRow = new RoundedPanel();
        JButton btnAddRow = createButton();
        btnAddRow.setText(" + Thêm dòng");
        btnAddRow.setForeground(new Color(99, 165, 210));
        btnAddRow.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        btnAddRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPanel_Discount_Detail_Product(contaierDiscountProduct);
            }
        });

        containerbtnAddRow.setLayout(new FlowLayout(FlowLayout.LEFT, 80, 0));
        containerbtnAddRow.add(btnAddRow);
        return containerbtnAddRow;
    }

    private boolean get_discount_information_from_product_panels() {
        Set<String> productNames = new HashSet<>();
        boolean hasError = false;
        for (Component component : containerProductType.getComponents()) {
            if (component instanceof RoundedPanel contaierDiscountProduct) {
                RoundedPanel contaierNameProduct = (RoundedPanel) contaierDiscountProduct.getComponent(0);
                JTextField txtProductName = (JTextField) ((RoundedPanel) contaierNameProduct.getComponent(1)).getComponent(0);
                String productName = txtProductName.getText().trim();

                if (productName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Chưa nhập tên sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtProductName.requestFocus();
                    hasError = true;
                    break;
                }

                if (productNames.contains(productName)) {
                    JOptionPane.showMessageDialog(this, "Tên sản phẩm '" + productName + "' đã được nhập trước đó", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    contaierNameProduct.requestFocusInWindow();
                    hasError = true;
                    break;
                } else {
                    productNames.add(productName);
                }
                Set<Integer> seenQuantity = new HashSet<>();
                for (Component subComponent : contaierDiscountProduct.getComponents()) {
                    if (subComponent instanceof RoundedPanel Panel_Discount_Detail_Product && subComponent.getName() != null && subComponent.getName().equals("Panel_Discount_Detail_Product")) {
                        JTextField txtQuantity = (JTextField) Panel_Discount_Detail_Product.getComponent(1);
                        JTextField txtValue = (JTextField) Panel_Discount_Detail_Product.getComponent(3);

                        if (txtQuantity.getText().trim().isEmpty() || txtValue.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            hasError = true;
                            break;
                        }

                        int quantity = Integer.parseInt(txtQuantity.getText().trim());
                        double discountPercentage = Double.parseDouble(txtValue.getText().trim());

                        if (!seenQuantity.add(quantity)) {
                            JOptionPane.showMessageDialog(this, "Điều kiện giảm giá đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            txtQuantity.requestFocus();
                            hasError = true;
                            break;
                        }

                        if (discountPercentage > 100) {
                            JOptionPane.showMessageDialog(this, "Phần trăm giảm không được lớn hơn 100", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            txtValue.requestFocus();
                            hasError = true;
                            break;
                        }
                        if (productName.contains("Thể loại: ")) {
                            String resultList = removeTheloai(txtProductName.getText().trim());
                            if (resultList.equals("Tất cả")) {
                                List<Product> products = productBLL.searchProducts("deleted = 0");
                                for (Product p : products) {
                                    int product_id = p.getId();
                                    String size = p.getSize();
                                    Discount_Detail discount_detail = new Discount_Detail(id, product_id, size, quantity, discountPercentage, 0);
                                    discountInfoList.add(discount_detail);
                                }
                            } else {
                                List<Product> products = productBLL.searchProducts("category = '" + resultList + "'");
                                for (Product p : products) {
                                    int product_id = p.getId();
                                    String size = p.getSize();
                                    Discount_Detail discount_detail = new Discount_Detail(id, product_id, size, quantity, discountPercentage, 0);
                                    discountInfoList.add(discount_detail);
                                }
                            }
                        } else {
                            List<String> resultList = convertTxtSearchToArray(productName);
                            if (resultList.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                txtProductName.requestFocus();
                                hasError = true;
                                break;
                            }
                            String name = resultList.get(0);
                            String size = resultList.get(1);
                            List<Product> products = productBLL.searchProducts("name = '" + name + "'", "size = '" + size + "'");
                            if (products.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                txtProductName.requestFocus();
                                hasError = true;
                                break;
                            } else {
                                int product_id = products.get(0).getId();
                                Discount_Detail discount_detail = new Discount_Detail(id, product_id, size, quantity, discountPercentage, 0);
                                discountInfoList.add(discount_detail);
                            }
                        }
                    }
                }
            }
        }
        if (hasError) {
            discountInfoList.clear();
            return false;
        }
        return true;
    }

    private JComboBox<String> createCategoryByButton(JButton category) {
        String[] categories = productBLL.getCategories().toArray(new String[0]);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"-- Chọn thể loại --", "Tất cả"});
        comboBox.setVisible(false);
        comboBox.setBorder(null);
        comboBox.setBackground(new Color(242, 242, 242));

        for (String c : categories) {
            comboBox.addItem(c);
        }
        category.addActionListener(new ActionListener() {
            boolean isComboBoxVisible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                isComboBoxVisible = !isComboBoxVisible;

                if (isComboBoxVisible) {
                    Point location = category.getLocationOnScreen();
                    comboBox.setLocation(location.x, location.y + category.getHeight());
                    comboBox.setVisible(true);
                    comboBox.requestFocus();
                } else {
                    comboBox.setVisible(false);
                }
            }
        });
        return comboBox;
    }

    private List<String> convertTxtSearchToArray(String txtSearchValue) {
        List<String> resultList = new ArrayList<>();
        String[] values = txtSearchValue.split("\\(");

        if (values.length == 2) {
            String name = values[0].trim();
            String size = values[1].replaceAll("\\)", "").trim();
            resultList.add(name);
            resultList.add(size);
        }

        return resultList;
    }

    private String removeTheloai(String txtSearchValue) {
        String result = txtSearchValue;
        if (txtSearchValue.startsWith("Thể loại: ")) {
            result = txtSearchValue.substring("Thể loại: ".length()).trim();
        }
        return result;
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData1(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(450, (search.getItemSize() * 35) + 2);

            } else {
                menu.setVisible(false);
            }
        }

    }

    private java.util.List<DataSearch> search(String text) {
        java.util.List<DataSearch> list = new ArrayList<>();
        List<Product> products = productBLL.findProducts("name", text);

        for (Product p : products) {
            if (list.size() == 7) {
                break;
            }
            list.add(new DataSearch(p.getName(), p.getSize(), p.getPrice().toString()));
        }
        return list;
    }

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {
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

    private JLabel createLabel(String title) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(80, 30));
        label.setText(title);
        label.setFont((new Font("Public Sans", Font.BOLD, 14)));
        return label;
    }

    private JButton createButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadSaleGUI() {
        if (Cafe_Application.homeGUI.indexSaleGUI != -1) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexSaleGUI] = new SaleGUI(HomeGUI.account);
                }
            });
            thread.start();
        }
    }
}
