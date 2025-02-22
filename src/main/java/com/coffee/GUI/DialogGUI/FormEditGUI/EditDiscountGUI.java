package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.DiscountBLL;
import com.coffee.BLL.Discount_DetailBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.DTO.Discount;
import com.coffee.DTO.Discount_Detail;
import com.coffee.GUI.DialogGUI.DialogFormDetail_1;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.SaleGUI;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class EditDiscountGUI extends DialogFormDetail_1 {
    private final DiscountBLL discountBLL = new DiscountBLL();
    List<Discount_Detail> list_discount_detail = new ArrayList<>();

    Discount_DetailBLL discount_detailBLL = new Discount_DetailBLL();

    private final ProductBLL productBLL = new ProductBLL();

    private JPanel containerForm;
    private final JPanel containerProductType = new JPanel();
    private final RoundedPanel containerProductTypeContent = new RoundedPanel();
    private JButton btnAddConditions;
    private JTextField txtSearch;
    private JRadioButton radio1;
    private JRadioButton radio2;
    private ButtonGroup btgroup;

    private final RoundedPanel containerBillType = new RoundedPanel();
    private final RoundedPanel containerBillTypeContent = new RoundedPanel();
    private JLabel lblForm;
    private JLabel lblFormValue;
    private JLabel lblBuy;
    private JScrollPane scrollPane;
    private JTextField txtDiscountCode;
    private JTextField txtProgramName;
    private JScrollPane scrollPaneBill;
    private final Discount discount;
    private final int discount_id;


    public EditDiscountGUI(Discount discount) {
        super();
        super.setTitle("Sửa chương trình giảm giá");
        super.getContentPane().setBackground(new Color(255, 255, 255));

        this.discount = discount;
        this.discount_id = discount.getId();

        init(discount);
        setVisible(true);
    }

    public void init(Discount discount) {
        list_discount_detail = discount_detailBLL.searchDiscount_Details("discount_id = " + discount_id);
        content.setBackground(new Color(255, 255, 255));
        top.setLayout(new MigLayout("", "30[]30[]60[]30[]", "[]10[]"));
        top.setPreferredSize(new Dimension(1000, 200));


        center.setBackground(new Color(255, 255, 255));
        center.setPreferredSize(new Dimension(1000, 70));
        center.setLayout(new MigLayout("", "30[]20[]30[]100[]", "[]"));
        bottom.setBackground(new Color(242, 242, 242));
        bottom.setPreferredSize(new Dimension(1000, 400));

        JLabel lblDiscountCode = createLabel("Mã giảm giá");
        JLabel lblProgramName = createLabel("Tên chương trình");
        JLabel lblWordEffect = createLabel("Hiệu lực từ");
        JLabel lblArrive = createLabel("Đến");
        JLabel lblStatus = createLabel("Trạng thái");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDateString = dateFormat.format(discount.getStart_date());
        String endDateString = dateFormat.format(discount.getEnd_date());

        JTextField txtStartDate = new MyTextFieldUnderLine();
        txtStartDate.setFocusable(false);
        txtStartDate.setText(startDateString);
        JTextField txtEndDate = new MyTextFieldUnderLine();
        txtEndDate.setText(endDateString);
        txtEndDate.setFocusable(false);

        txtDiscountCode = new MyTextFieldUnderLine();
        txtDiscountCode.setFocusable(false);
        txtDiscountCode.setText(String.valueOf(discount_id));

        txtProgramName = new MyTextFieldUnderLine();
        txtProgramName.setText(discount.getName());


        radio1 = new JRadioButton("Đang áp dụng");
        radio2 = new JRadioButton("Ngừng áp dụng");
        btgroup = new ButtonGroup();
        btgroup.add(radio1);
        btgroup.add(radio2);
        boolean status = discount.isStatus();
        if (!status)
            radio1.setSelected(true);
        else
            radio2.setSelected(true);


        JPanel panelTimeApplication = new JPanel();
        panelTimeApplication.setPreferredSize(new Dimension(500, 60));
        panelTimeApplication.setLayout(new MigLayout("", "[]20[]60[]20[]", "15[]"));
        panelTimeApplication.setBackground(new Color(255, 255, 255));

        panelTimeApplication.add(lblWordEffect);
        panelTimeApplication.add(txtStartDate);
        panelTimeApplication.add(lblArrive);
        panelTimeApplication.add(txtEndDate);

        JPanel panelStatus = new JPanel();
        panelStatus.setLayout(new MigLayout("", "[]30[]30[]", "15[]"));
        panelStatus.setBackground(new Color(255, 255, 255));
        panelStatus.setPreferredSize(new Dimension(500, 60));
        panelStatus.add(lblStatus);
        panelStatus.add(radio1);
        panelStatus.add(radio2);

        top.add(lblDiscountCode);
        top.add(txtDiscountCode);
        top.add(panelTimeApplication, "wrap");
        top.add(lblProgramName);
        top.add(txtProgramName);
        top.add(panelStatus, "wrap");

        JLabel lblDiscountType = createLabel("Giảm giá theo:");
        lblDiscountType.setPreferredSize(new Dimension(100, 30));

        JLabel lblTypeValue = createLabel("");
        lblTypeValue.setFont(new Font("Public Sans", Font.PLAIN, 14));

        containerForm = new JPanel();
        containerForm.setLayout(new MigLayout("", "[]20[]", "[]"));
        containerForm.setPreferredSize(new Dimension(300, 40));
        containerForm.setBackground(new Color(255, 255, 255));

        lblForm = createLabel("Hình thức: ");
        lblFormValue = createLabel("");
        lblFormValue.setFont((new Font("Public Sans", Font.PLAIN, 14)));

        containerForm.add(lblForm);
        lblFormValue.setText("");
        containerForm.add(lblFormValue);


        center.add(lblDiscountType);
        center.add(lblTypeValue);
        center.add(containerForm);
//        center.add(btnAddConditions);

        JLabel totalBill = createLabel("Tổng hóa đơn");
        JLabel lbDiscount = createLabel("Giảm giá");


        containerBillType.setLayout(new MigLayout("", "30[]50[]", "[]"));
        containerBillType.setBackground(new Color(242, 242, 242));
//        containerBillType.setBackground(new Color(255, 255, 255));

        containerBillType.setPreferredSize(new Dimension(1000, 50));
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225));
        containerBillType.setBorder(bottomBorder);
        containerBillType.add(totalBill);
        containerBillType.add(lbDiscount);

        containerBillTypeContent.setLayout(new MigLayout("", ""));

        containerBillTypeContent.setBackground(new Color(242, 242, 242));
        containerBillTypeContent.setPreferredSize(new Dimension(920, 350));
        scrollPaneBill = new JScrollPane(containerBillTypeContent);
        scrollPaneBill.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneBill.setBorder(null);
        scrollPaneBill.setBackground(new Color(255, 255, 255));
        scrollPaneBill.setWheelScrollingEnabled(true);
        scrollPaneBill.getVerticalScrollBar().setUnitIncrement(13);

        containerProductType.setLayout(new MigLayout("", "[]", ""));

        containerProductType.setBackground(new Color(255, 255, 255));
        scrollPane = new JScrollPane(containerProductType);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(255, 255, 255));
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(13);

        boolean type = discount.isType();
        if (!type) {
            lblTypeValue.setText("Sản phẩm");
            lblFormValue.setText("Giảm giá (theo SL mua)");
            loadDiscountProductPanels();
            bottom.add(scrollPane, BorderLayout.CENTER);
        } else {
            lblTypeValue.setText("Đơn hàng");
            lblFormValue.setText("Giảm giá đơn hàng");
            loadDiscountBillPanels();
            bottom.add(containerBillType, BorderLayout.NORTH);
            bottom.add(scrollPaneBill, BorderLayout.CENTER);
        }

        JButton buttonCancel = new JButton("Huỷ");
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
        JButton buttonAdd = new JButton("Cập nhật");
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(new Color(255, 255, 255));
        buttonAdd.setPreferredSize(new Dimension(100, 35));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Pair<Boolean, String> result = updateDiscount();
                if (result.getKey()) {
                    JOptionPane.showMessageDialog(null, "Sửa chương trình giảm giá thành công");
                    dispose();
                    loadSaleGUI();
                } else {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        containerButton.add(buttonAdd);

    }

    private Pair<Boolean, String> updateDiscount() {

        String name = txtProgramName.getText();
        boolean statusBoolean = !radio1.isSelected();

        Date endDate = discount.getEnd_date();
        if (!statusBoolean) {
            if (endDate.before(java.sql.Date.valueOf(LocalDate.now())))
                return new Pair<>(false, "Chương trình giảm giá đã hết hạn");
            else {
                updateStatusDicount();
                discount.setName(name);
                discount.setStatus(statusBoolean);
                discountBLL.updateDiscount(discount);
                return new Pair<>(true, "");
            }
        } else {
            discount.setName(name);
            discount.setStatus(statusBoolean);
            discountBLL.updateDiscount(discount);
            return new Pair<>(true, "");
        }
    }

    private void updateStatusDicount() {
        List<Discount> discountList = discountBLL.searchDiscounts("status =0");

        for (Discount discount : discountList) {
            discount.setStatus(true);
            discountBLL.updateStatusDiscount(discount);
        }
    }

    private void loadDiscountBillPanels() {
        containerBillTypeContent.removeAll();
        for (Discount_Detail discount_detail : list_discount_detail) {
            RoundedPanel panel = createPanel_Bill(containerBillTypeContent);
            JTextField txtTotalBill = (JTextField) panel.getComponent(1);
            txtTotalBill.setText(NumberFormat.getNumberInstance(Locale.US).format(discount_detail.getDiscountBill()));
            JTextField txtReduction = (JTextField) panel.getComponent(3);
            txtReduction.setText(NumberFormat.getNumberInstance(Locale.US).format(discount_detail.getPercent()));
        }
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
        txtQuantity.setFocusable(false);

        JLabel discount = createLabel("Giảm ");
        JTextField txtValue = new MyTextFieldUnderLine();
        txtValue.setFocusable(false);

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
//                contaierDiscountBill.remove(Panel_Bill);
//                contaierDiscountBill.revalidate();
//                contaierDiscountBill.repaint();
            }
        });

        Panel_Bill.add(btnRemoveRow);

        contaierDiscountBill.add(Panel_Bill, "wrap ");
        contaierDiscountBill.revalidate();
        contaierDiscountBill.repaint();
        return Panel_Bill;
    }

    private void loadDiscountProductPanels() {
        containerProductType.removeAll();
        Set<String> uniqueProducts = new HashSet<>();
        for (Discount_Detail discount_detail : list_discount_detail) {
            String idSize = discount_detail.getProduct_id() + ":" + discount_detail.getSize();
            uniqueProducts.add(idSize);
        }

        for (String productId_size : uniqueProducts) {
            RoundedPanel contaierDiscountProduct = new RoundedPanel();
            contaierDiscountProduct.setLayout(new MigLayout("", "[]"));
            contaierDiscountProduct.setPreferredSize(new Dimension(915, 100));
            RoundedPanel contaierNameProduct = createPanelProductName(contaierDiscountProduct);
            JTextField txtProductName = (JTextField) ((RoundedPanel) contaierNameProduct.getComponent(1)).getComponent(0);

            String[] parts = productId_size.split(":");
            String product_id = parts[0];
            int product_id_int = Integer.parseInt(product_id);
            String size = parts[1];
            String name = productBLL.searchProducts("id = " + product_id).get(0).getName();

            txtProductName.setText(name + " (" + size + ")");
            contaierDiscountProduct.add(contaierNameProduct, BorderLayout.NORTH);

            List<Discount_Detail> listCondition = getProductCondition(product_id_int, size);
            for (Discount_Detail d : listCondition) {
                RoundedPanel Panel_Discount_Detail_Product = createPanel_Discount_Detail_Product(contaierDiscountProduct);
                JTextField txtQuantity = (JTextField) Panel_Discount_Detail_Product.getComponent(1);
                JTextField txtValue = (JTextField) Panel_Discount_Detail_Product.getComponent(3);
                txtQuantity.setText(NumberFormat.getNumberInstance(Locale.US).format(d.getQuantity() ));
                txtValue.setText(NumberFormat.getNumberInstance(Locale.US).format(d.getPercent() ));
            }

            RoundedPanel containerbtnAddRow = createContaierBtnAddRow(contaierDiscountProduct);
            contaierDiscountProduct.add(containerbtnAddRow, BorderLayout.SOUTH);

            containerProductType.add(contaierDiscountProduct, "wrap");
        }

        containerProductType.revalidate();
        containerProductType.repaint();
    }

    public List<Discount_Detail> getProductCondition(int product_id, String size) {
        List<Discount_Detail> list = new ArrayList<>();
        for (Discount_Detail d : list_discount_detail) {
            if (d.getProduct_id() == product_id && d.getSize().equals(size)) {
                list.add(d);
            }
        }
        return list;
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
        txtS.setFocusable(false);


        contaierSearch.add(txtS);


        JButton btnRemove = createButton();
        btnRemove.setIcon(new FlatSVGIcon("icon/icons8-remove-26.svg"));


        contaierNameProduct.add(lblBuy);
        contaierNameProduct.add(contaierSearch);
        contaierNameProduct.add(btnRemove);

        return contaierNameProduct;
    }

    private RoundedPanel createPanel_Discount_Detail_Product(RoundedPanel contaierDiscountProduct) {
        RoundedPanel Panel_Discount_Detail_Product = new RoundedPanel();
        Panel_Discount_Detail_Product.setName("Panel_Discount_Detail_Product");
        Panel_Discount_Detail_Product.setLayout(new MigLayout("", "100[]20[]20[]20[]20[]390[]10"));
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225));
        Panel_Discount_Detail_Product.setBorder(bottomBorder);

        JLabel quantity = createLabel("Số lượng từ");
        JTextField txtQuantity = new MyTextFieldUnderLine();
        txtQuantity.setFocusable(false);
        JLabel discount = createLabel("Giảm giá");
        JTextField txtValue = new MyTextFieldUnderLine();
        txtValue.setFocusable(false);

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

        Panel_Discount_Detail_Product.add(btnRemoveRow);

        contaierDiscountProduct.add(Panel_Discount_Detail_Product, "wrap ");
        contaierDiscountProduct.revalidate();
        contaierDiscountProduct.repaint();
        return Panel_Discount_Detail_Product;
    }

    private RoundedPanel createContaierBtnAddRow(RoundedPanel contaierDiscountProduct) {
        RoundedPanel containerbtnAddRow = new RoundedPanel();
        JButton btnAddRow = createButton();
        btnAddRow.setText(" + Thêm dòng");
        btnAddRow.setForeground(new Color(99, 165, 210));
        btnAddRow.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        containerbtnAddRow.setLayout(new FlowLayout(FlowLayout.LEFT, 80, 0));
        containerbtnAddRow.add(btnAddRow);
        return containerbtnAddRow;
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





