package com.coffee.GUI;

import com.coffee.BLL.*;
import com.coffee.DTO.*;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailReceiptGUI;
import com.coffee.GUI.components.Circle_ProgressBar;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.SalePanel;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class SaleGUI extends SalePanel {
    private final ProductBLL productBLL = new ProductBLL();
    private final StaffBLL staffBLL = new StaffBLL();
    private final ReceiptBLL receiptBLL = new ReceiptBLL();
    private final RecipeBLL recipeBLL = new RecipeBLL();
    private final MaterialBLL materialBLL = new MaterialBLL();
    private final Receipt_DetailBLL receipt_detailBLL = new Receipt_DetailBLL();
    private final DiscountBLL discountBLL = new DiscountBLL();
    private final Discount_DetailBLL discountDetailBLL = new Discount_DetailBLL();
    private final Account account;
    private List<Discount_Detail> discountDetails;
    private RoundedPanel containerSearch;
    private List<JPanel> productIncartPanelList;
    private List<JPanel> productIncartNotelList;
    private JLabel iconSearch;
    private JLabel staffName;
    private JLabel date;
    private List<JLabel> jLabelBill;
    private List<JLabel> nameReceiptDetail;
    private List<JLabel> deleteReceiptDetail;
    private List<JLabel> priceReceiptDetail;
    private List<JComboBox<String>> sizeReceiptDetail;
    private List<JTextField> quantityReceiptDetail;
    private JTextField jTextFieldSearch;
    private JTextField jTextFieldCash;
    private JButton jButtonSearch;
    private JButton jButtonPay;
    private JButton jButtonCancel;
    private ButtonGroup bg;
    private List<ButtonGroup> buttonGroupsSugar;
    private List<ButtonGroup> buttonGroupsIce;
    private List<String> categoriesName;
    private List<Integer> productIDList;
    private List<String> productNameList;
    private List<List<Object>> receiptDetailList;
    private List<Material> materials = new MaterialBLL().searchMaterials("deleted = 0");
    private String categoryName = "TẤT CẢ";
    private int indexShowOption = -1;
    private boolean note = false;
    private List<Pair<Integer, JLabel>> remainList = new ArrayList<>();
    private List<Pair<Integer, RoundedPanel>> productRoundPanelList = new ArrayList<>();
    private RoundedPanel selectedCategory = null;
    public List<Product> resultSearch = new ArrayList<>();
    public List<List<String>> bestSeller = productBLL.getBestSellers();
    private int discountType = -1;
    private Discount discountActive;
    private double totalPrice;
    private double totalDiscount;
    private double total = -1;
    private double cash = -1;
    private double excess = -1;

    public SaleGUI(Account account) {
        super();
        this.account = account;
        initComponents();
    }

    public void initComponents() {
        containerSearch = new RoundedPanel();
        productIncartPanelList = new ArrayList<>();
        productIncartNotelList = new ArrayList<>();
        productNameList = new ArrayList<>();
        iconSearch = new JLabel();
        staffName = new JLabel("Nhân viên: " + HomeGUI.staff.getName());
        date = new JLabel("Ngày: " + LocalDate.now());
        jTextFieldSearch = new JTextField();
        jTextFieldCash = new MyTextFieldUnderLine();
        jLabelBill = new ArrayList<>();
        nameReceiptDetail = new ArrayList<>();
        deleteReceiptDetail = new ArrayList<>();
        priceReceiptDetail = new ArrayList<>();
        quantityReceiptDetail = new ArrayList<>();
        sizeReceiptDetail = new ArrayList<>();
        jButtonSearch = new JButton("Tìm kiếm");
        jButtonPay = new JButton("Thanh toán");
        jButtonCancel = new JButton("Huỷ");
        categoriesName = new ArrayList<>();
        productIDList = new ArrayList<>();
        receiptDetailList = new ArrayList<>();
        buttonGroupsSugar = new ArrayList<>();
        buttonGroupsIce = new ArrayList<>();

        checkDiscount();

        List<Discount> list = discountBLL.searchDiscounts("status = 0");

        if (!list.isEmpty()) {
            discountActive = list.get(0);
            if (discountActive.isType())
                discountType = 1;
            else
                discountType = 0;

            discountDetails = discountDetailBLL.findDiscount_DetailsBy(Map.of("discount_id", discountActive.getId()));
        }

        containerSearch.setLayout(new MigLayout("", "10[]20[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(350, 40));
        SearchPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập tên sản phẩm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(350, 30));
        jTextFieldSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchProducts());
        SearchPanel.add(jButtonSearch);

        JRadioButton radio1 = new JRadioButton("Tất cả");
        radio1.setSelected(true);
        radio1.addActionListener(e -> searchProducts());
        SearchPanel.add(radio1);
        JRadioButton radio2 = new JRadioButton("Khuyến mãi");
        radio2.addActionListener(e -> searchProducts());
        SearchPanel.add(radio2);
        JRadioButton radio3 = new JRadioButton("Bán chạy");
        radio3.addActionListener(e -> searchProducts());
        SearchPanel.add(radio3);
        JRadioButton radio4 = new JRadioButton("Hết hàng");
        radio4.addActionListener(e -> searchProducts());
        SearchPanel.add(radio4);

        bg = new ButtonGroup();
        bg.add(radio1);
        bg.add(radio2);
        bg.add(radio3);
        bg.add(radio4);

        loadCategory();

        Thread threadRender = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProductRoundPanel();
                loadProduct(productBLL.searchProducts("deleted = 0"));
                resultSearch = productBLL.searchProducts("deleted = 0");
            }
        });
        threadRender.start();

        staffName.setFont((new Font("Palatino", Font.BOLD, 15)));
        StaffPanel.add(staffName);

        date.setFont(new Font("Palatino", Font.PLAIN, 13));
        StaffPanel.add(date);

        List<String> titles = new ArrayList<>(List.of(new String[]{"Sản phẩm", "Size", "SL", "Tổng cộng", "Xoá"}));

        for (String tittle : titles) {
            JLabel jLabel = getjLabel(tittle);
            Title.add(jLabel);
        }

        for (String string : new String[]{"Tổng cộng:", "Khuyến mãi:", "Thành Tiền:", "Tiền nhận:", "Tiền thừa:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 20));
            label.setText(string);
            label.setFont((new Font("Palatino", Font.BOLD, 13)));
            ContainerButtons.add(label);

            if (string.equals("Tổng cộng:") || string.equals("Khuyến mãi:") || string.equals("Thành Tiền:") || string.equals("Tiền thừa:")) {
                JLabel jLabel = new JLabel();
                jLabel.setPreferredSize(new Dimension(230, 10));
                jLabel.setFont((new Font("Palatino", Font.PLAIN, 13)));
                jLabel.setText(VNString.currency(0));
                jLabelBill.add(jLabel);
                ContainerButtons.add(jLabel, "wrap");
            } else {
                jTextFieldCash.setPreferredSize(new Dimension(230, 20));
                jTextFieldCash.setFont((new Font("Palatino", Font.PLAIN, 13)));
                jTextFieldCash.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            calculateExcess();
                        }
                    }
                });
                ContainerButtons.add(jTextFieldCash, "wrap");
            }
        }

        jButtonCancel.setPreferredSize(new Dimension(40, 40));
        jButtonCancel.setFont(new Font("Palatino", Font.BOLD, 15));
        jButtonCancel.setBackground(new Color(0xFFFFFF));
        jButtonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButtonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButtonCancel.setBackground(new Color(0xFFFFFF));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (receiptDetailList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Bạn muốn huỷ hoá đơn?",
                        "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
                if (choice == 1)
                    cancelBill();
            }
        });
        ContainerButtons.add(jButtonCancel);

        jButtonPay.setPreferredSize(new Dimension(40, 40));
        jButtonPay.setFont(new Font("Palatino", Font.BOLD, 15));
        jButtonPay.setBackground(new Color(1, 120, 220));
        jButtonPay.setForeground(Color.white);
        jButtonPay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonPay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Xác nhận xuất hoá đơn?",
                        "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
                if (choice == 1)
                    payBill();
            }
        });
        ContainerButtons.add(jButtonPay, "wrap");
    }

    private void payBill() {
        for (List<Object> receiptDetail : receiptDetailList) {
            if (receiptDetail.get(2).toString().equals("0")) {
                JOptionPane.showMessageDialog(this, "Số lượng sản phẩm phải lớn hơn 0.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        if (receiptDetailList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (cash == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền nhận.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (excess == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền nhận và nhấn enter để tính tiền thừa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = receiptBLL.getAutoID(receiptBLL.searchReceipts());
        int discount_id;
        if (discountType == -1)
            discount_id = 0;
        else
            discount_id = discountActive.getId();

        Receipt receipt = new Receipt(id, HomeGUI.staff.getId(), LocalDateTime.now(), totalPrice, totalDiscount, total, cash, excess, discount_id);

        Pair<Boolean, String> result = receiptBLL.addReceipt(receipt);
        if (result.getKey()) {
            List<Receipt_Detail> receipt_details = new ArrayList<>();

            for (List<Object> receiptDetail : receiptDetailList) {
                if (receiptDetail.size() == 6) {
                    boolean checkExist = false;
                    Product product = productBLL.findProductsBy(Map.of("name", receiptDetail.get(0),
                            "size", receiptDetail.get(1).toString())).get(0);
                    String notice = receiptDetail.get(4).toString() + " đường, " + receiptDetail.get(5).toString() + " đá";

                    for (Receipt_Detail detail : receipt_details) {
                        if (product.getId() == detail.getProduct_id() && product.getSize().equals(detail.getSize()) && notice.equals(detail.getNotice())) {
                            checkExist = true;
                            detail.setQuantity(detail.getQuantity() + Double.parseDouble(receiptDetail.get(2).toString()));
                            detail.setPrice(detail.getPrice() + Double.parseDouble(receiptDetail.get(3).toString()));
                            break;
                        }
                    }

                    if (!checkExist) {
                        Receipt_Detail newReceipt_detail = new Receipt_Detail(receipt.getId(), product.getId(), product.getSize(), Double.parseDouble(receiptDetail.get(2).toString()), Double.parseDouble(receiptDetail.get(3).toString()), notice);
                        receipt_details.add(newReceipt_detail);
                    }
                } else {
                    boolean checkExist = false;
                    Product product = productBLL.findProductsBy(Map.of("name", receiptDetail.get(0),
                            "size", receiptDetail.get(1).toString())).get(0);
//                    String notice = receiptDetail.get(4).toString() + " đường, " + receiptDetail.get(5).toString() + " đá";

                    for (Receipt_Detail detail : receipt_details) {
                        if (product.getId() == detail.getProduct_id() && product.getSize().equals(detail.getSize())) {
                            checkExist = true;
                            detail.setQuantity(detail.getQuantity() + Double.parseDouble(receiptDetail.get(2).toString()));
                            detail.setPrice(detail.getPrice() + Double.parseDouble(receiptDetail.get(3).toString()));
                            break;
                        }
                    }

                    if (!checkExist) {
                        Receipt_Detail newReceipt_detail = new Receipt_Detail(receipt.getId(), product.getId(), product.getSize(), Double.parseDouble(receiptDetail.get(2).toString()), Double.parseDouble(receiptDetail.get(3).toString()), " ");
                        receipt_details.add(newReceipt_detail);
                    }
                }

            }

            Receipt_DetailBLL receiptDetailBLL = new Receipt_DetailBLL();
            for (Receipt_Detail receipt_detail : receipt_details) {
                receipt_detail.setPrice_discount(receipt_detail.getPrice());
            }
            if (discountType == 0) {
                for (Receipt_Detail receipt_detail : receipt_details) {
                    List<Pair<Double, Double>> list = checkPercentDiscountType0(receipt_detail.getProduct_id(), receipt_detail.getSize());
                    if (!list.isEmpty()) {
                        double quantity = 0, percent = 0;
                        for (Pair<Double, Double> pair : list) {
                            if (pair.getKey() <= receipt_detail.getQuantity()) {
                                if (pair.getKey() > quantity) {
                                    quantity = pair.getKey();
                                    percent = pair.getValue();
                                }

                            }
                        }
                        receipt_detail.setPrice_discount(receipt_detail.getPrice() * (100 - percent) / 100);
                    }
                }
            }

            for (Receipt_Detail receipt_detail : receipt_details) {

                receiptDetailBLL.addReceipt_Detail(receipt_detail);
            }

            if (Cafe_Application.homeGUI.indexModuleReceiptGUI != -1) {
                ReceiptGUI receiptGUI = (ReceiptGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexModuleReceiptGUI];
                receiptGUI.refresh();
            }
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            refresh();
            new DetailReceiptGUI(receipt);
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refresh() {
        receiptDetailList.removeAll(receiptDetailList);
        nameReceiptDetail.removeAll(nameReceiptDetail);
        sizeReceiptDetail.removeAll(sizeReceiptDetail);
        quantityReceiptDetail.removeAll(quantityReceiptDetail);
        deleteReceiptDetail.removeAll(deleteReceiptDetail);

        productIncartNotelList.removeAll(productIncartNotelList);
        buttonGroupsSugar.removeAll(buttonGroupsSugar);
        buttonGroupsIce.removeAll(buttonGroupsIce);

        productIncartPanelList.removeAll(productIncartPanelList);

        Bill_detailPanel.removeAll();
        Bill_detailPanel.setPreferredSize(new Dimension(450, 400));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();

        jLabelBill.get(0).setText(VNString.currency(0));
        jLabelBill.get(1).setText(VNString.currency(0));
        jLabelBill.get(2).setText(VNString.currency(0));
        jLabelBill.get(3).setText(VNString.currency(0));
        total = -1;
        cash = -1;
        excess = -1;

        jTextFieldCash.setText("");

        loadCategory();

        Thread threadRender = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProductRoundPanel();
                loadProduct(productBLL.searchProducts("deleted = 0"));
                resultSearch = productBLL.searchProducts("deleted = 0");
            }
        });
        threadRender.start();
    }

    private void searchProducts() {
        List<Product> productList = productBLL.searchProducts("deleted = 0");
        String status = "";
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                status = button.getText();
                break;
            }
        }
        if (categoryName.equals("TẤT CẢ") && status.equals("Tất cả") && jTextFieldSearch.getText().isEmpty()) {
            resultSearch = productList;
            loadProduct(productList);
        } else {
            if (!jTextFieldSearch.getText().isEmpty()) {
                productList = productBLL.findProducts("name", jTextFieldSearch.getText());
            }
            if (!categoryName.equals("TẤT CẢ")) {
                productList.removeIf(product -> !product.getCategory().equals(categoryName));
            }
            if (!status.equals("Tất cả")) {
                if (status.equals("Khuyến mãi")) {
                    productList.removeIf(product -> !checkDiscountType0(product.getId()));
                } else if (status.equals("Bán chạy")) {
                    productList.removeIf(product -> !checkBestSellers(product.getId()));
                } else if (status.equals("Hết hàng")) {
                    productList.removeIf(product -> !checkOutOfRemain(product.getId()));
                }
            }
            resultSearch = productList;
            loadProduct(productList);
        }
    }

    private static JLabel getjLabel(String tittle) {
        JLabel jLabel = new JLabel(tittle);
        jLabel.setFont(new Font("Palatino", Font.BOLD, 13));
        if (Objects.equals(tittle, "Sản phẩm")) {
            jLabel.setPreferredSize(new Dimension(200, 40));
        } else if (Objects.equals(tittle, "Size") || Objects.equals(tittle, "SL") || Objects.equals(tittle, "Xoá")) {
            jLabel.setPreferredSize(new Dimension(40, 40));
        } else {
            jLabel.setPreferredSize(new Dimension(90, 40));
        }
        return jLabel;
    }

    public void loadCategory() {
        Category.removeAll();
        categoriesName.removeAll(categoriesName);
        categoriesName.add("TẤT CẢ");
        categoriesName.addAll(productBLL.getCategories());
        for (String category : categoriesName) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new FlowLayout());
            roundedPanel.setBackground(new Color(255, 180, 180, 181));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    RoundedPanel roundedPanel = (RoundedPanel) e.getComponent();
                    selectedCategory.setBackground(new Color(255, 180, 180, 181));
                    roundedPanel.setBackground(new Color(253, 143, 143));
                    selectedCategory = roundedPanel;

                    JLabel jLabel = (JLabel) roundedPanel.getComponent(0);
                    if (!categoryName.equals(jLabel.getText())) {
                        categoryName = jLabel.getText();
                        searchProducts();
//                        System.out.println(jLabel.getText());
                    }
                }
            });
            Category.add(roundedPanel);

            JLabel jLabel = new JLabel(category);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            jLabel.setVerticalAlignment(SwingConstants.CENTER);
            jLabel.setFont((new Font("Inter", Font.BOLD, 13)));
            roundedPanel.add(jLabel);

            roundedPanel.setPreferredSize(new Dimension(Math.max(jLabel.getPreferredSize().width + 10, 100), 31));

            if (category.equals("TẤT CẢ")) {
                roundedPanel.setBackground(new Color(253, 143, 143));
                selectedCategory = roundedPanel;
            }
        }
        Category.repaint();
        Category.revalidate();
    }

    public void loadProductRoundPanel() {
        productNameList.clear();
        productRoundPanelList.clear();
        remainList.clear();

        for (Product product : productBLL.searchProducts("deleted = 0")) {
            if (productNameList.contains(product.getName())) {
                continue;
            }
            RoundedPanel panel = new RoundedPanel();
            panel.setLayout(new MigLayout("", "5[]5"));
            panel.setBackground(new Color(228, 231, 235));
            panel.setPreferredSize(new Dimension(155, 260));
            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            productRoundPanelList.add(new Pair<>(product.getId(), panel));

            ImageIcon icon = new FlatSVGIcon("image/Product/" + product.getImage() + ".svg");
            Image image = icon.getImage();
            Image newImg = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);

            JLabel productImage = new JLabel(icon);
            productImage.scrollRectToVisible(new Rectangle());
            panel.add(productImage, "wrap");

            JLabel productName = new JLabel();
            productName.setPreferredSize(new Dimension(150, 30));
            productName.setVerticalAlignment(JLabel.CENTER);
            productName.setHorizontalAlignment(JLabel.CENTER);
            productName.setFont((new Font("Inter", Font.BOLD, 13)));
            productName.setText(product.getName());

            panel.add(productName, "wrap");

            boolean checkBestSeller = checkBestSellers(product.getId());
            if (checkBestSeller)
                productName.setIcon(new FlatSVGIcon("icon/fire-svgrepo-com.svg"));

            JLabel productPrice = new JLabel();
            productPrice.setVerticalAlignment(JLabel.CENTER);
            productPrice.setHorizontalAlignment(JLabel.CENTER);
            productPrice.setFont((new Font("Inter", Font.BOLD, 10)));
            productPrice.setPreferredSize(new Dimension(150, 30));

            boolean check = checkDiscountType0(product.getId());
            if (!check) {
                productPrice.setText(VNString.currency(product.getPrice()));
                panel.add(productPrice, "wrap");
            } else {
                productPrice.setText("<html>" + VNString.currency(product.getPrice()) + "\t <span style='color: red'>Khuyến mãi</span></html>");
                panel.add(productPrice, "wrap");
            }

            JLabel productRemain = new JLabel();
            productRemain.setPreferredSize(new Dimension(150, 30));
            productRemain.setVerticalAlignment(JLabel.CENTER);
            productRemain.setHorizontalAlignment(JLabel.CENTER);
            productRemain.setFont((new Font("Inter", Font.BOLD, 10)));
            int remain = checkRemainProduct(product);
            productRemain.setText("Còn Lại: " + remain);
            panel.add(productRemain, "wrap");
            remainList.add(new Pair<>(product.getId(), productRemain));
            productNameList.add(product.getName());
        }

        for (int i = 0; i < productRoundPanelList.size(); i++) {
            productRoundPanelList.get(i).getValue().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    for (int j = 0; j < productRoundPanelList.size(); j++) {
                        if (productRoundPanelList.get(j).getValue() == e.getComponent()) {
                            addProductToCart(productBLL.findProductsBy(Map.of("id", productRoundPanelList.get(j).getKey())).get(0));
                            break;
                        }
                    }
                }
            });
        }
        ContainerProduct.repaint();
        ContainerProduct.revalidate();
    }

    public void loadProduct(List<Product> products) {
        ContainerProduct.removeAll();
        productIDList.clear();

        for (Product product : products) {
            if (productIDList.contains(product.getId())) {
                continue;
            }
            for (Pair<Integer, RoundedPanel> roundedPanelPair : productRoundPanelList) {
                if (roundedPanelPair.getKey() == product.getId()) {
                    ContainerProduct.add(roundedPanelPair.getValue());
                    break;
                }
            }
            productIDList.add(product.getId());
        }

        ContainerProduct.setPreferredSize(new Dimension(690, productIDList.size() % 4 == 0 ?
                270 * productIDList.size() / 4 :
                270 * (productIDList.size() + (4 - productIDList.size() % 4)) / 4));
        ContainerProduct.repaint();
        ContainerProduct.revalidate();
    }

    private void addProductToCart(Product product) {
        List<Object> receiptDetail = new ArrayList<>();
        receiptDetail.add(product.getName());
        receiptDetail.add(product.getSize());
        receiptDetail.add(0);
//        double percent = checkPercentDiscount(product.getId());
//        if (percent == 0) {
        receiptDetail.add(0);
//        } else {
//            double newPrice = product.getPrice() - product.getPrice() * percent / 100;
//            receiptDetail.add(newPrice);
//        }
        receiptDetailList.add(receiptDetail);

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanel.setBackground(new Color(228, 231, 235));
        jPanel.setPreferredSize(new Dimension(450, 50));
        Bill_detailPanel.add(jPanel);

        productIncartPanelList.add(new JPanel());
        nameReceiptDetail.add(new JLabel());
        sizeReceiptDetail.add(new JComboBox<>());
        quantityReceiptDetail.add(new MyTextFieldUnderLine());
        priceReceiptDetail.add(new JLabel());
        deleteReceiptDetail.add(new JLabel());

        int index = receiptDetailList.size() - 1;

        nameReceiptDetail.get(index).setFont(new Font("Inter", Font.BOLD, 13));
        nameReceiptDetail.get(index).setCursor(new Cursor(Cursor.HAND_CURSOR));
        nameReceiptDetail.get(index).setText("<html>" + receiptDetailList.get(index).get(0) + "</html>");
        nameReceiptDetail.get(index).setPreferredSize(new Dimension(200, 40));
        nameReceiptDetail.get(index).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int j = 0; j < nameReceiptDetail.size(); j++) {
                    if (nameReceiptDetail.get(j) == e.getComponent()) {
                        if (j != indexShowOption && !receiptDetailList.get(j).get(1).equals("Không")) {
                            showOption(j);
                            indexShowOption = j;
                            note = true;
                        } else {
                            if (j == indexShowOption) {
                                loadProductInCart();
                                indexShowOption = -1;
                                note = false;
                            }
                        }

                    }
                }
            }
        });
        jPanel.add(nameReceiptDetail.get(index));

        if (!product.getSize().equals("Không")) {
            for (Product product1 : productBLL.findProductsBy(Map.of("name", receiptDetailList.get(index).get(0)))) {
                sizeReceiptDetail.get(index).addItem(product1.getSize());
            }
            sizeReceiptDetail.get(index).setPreferredSize(new Dimension(40, 40));
            sizeReceiptDetail.get(index).setFont(new Font("Inter", Font.PLAIN, 10));
            sizeReceiptDetail.get(index).setSelectedItem(receiptDetailList.get(index).get(1));
            sizeReceiptDetail.get(index).setBorder(BorderFactory.createEmptyBorder());
            sizeReceiptDetail.get(index).setBackground(new Color(228, 231, 235));
            sizeReceiptDetail.get(index).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int j = 0; j < sizeReceiptDetail.size(); j++) {
                        if (sizeReceiptDetail.get(j) == e.getSource()) {
                            saveSizeChanged(j);
                            return;
                        }
                    }
                }
            });
            jPanel.add(sizeReceiptDetail.get(index));

            receiptDetail.add("100%");
            receiptDetail.add("100%");
        } else {
            sizeReceiptDetail.get(index).addItem("Không");
            JLabel panel = new JLabel();
            panel.setFont(new Font("Inter", Font.PLAIN, 8));
            panel.setPreferredSize(new Dimension(40, 40));
            jPanel.add(panel);
        }

        quantityReceiptDetail.get(index).setFont(new Font("Inter", Font.PLAIN, 10));
        quantityReceiptDetail.get(index).setPreferredSize(new Dimension(40, 40));
        quantityReceiptDetail.get(index).setText(receiptDetailList.get(index).get(2).toString());

        quantityReceiptDetail.get(index).addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    for (int i = 0; i < quantityReceiptDetail.size(); i++) {
                        if (quantityReceiptDetail.get(i) == e.getComponent()) {
                            saveQuantityChanged(i);
                            return;
                        }
                    }
                }
            }
        });
        jPanel.add(quantityReceiptDetail.get(index));

        priceReceiptDetail.get(index).setText(VNString.currency(Double.parseDouble(receiptDetailList.get(index).get(3).toString())));
        priceReceiptDetail.get(index).setFont(new Font("Inter", Font.BOLD, 12));
        priceReceiptDetail.get(index).setPreferredSize(new Dimension(90, 40));
        jPanel.add(priceReceiptDetail.get(index));

        deleteReceiptDetail.get(index).setFont(new Font("Inter", Font.PLAIN, 12));
        deleteReceiptDetail.get(index).setPreferredSize(new Dimension(40, 40));
        deleteReceiptDetail.get(index).setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteReceiptDetail.get(index).setIcon(new FlatSVGIcon("icon/delete.svg"));
        deleteReceiptDetail.get(index).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int j = 0; j < deleteReceiptDetail.size(); j++) {
                    if (deleteReceiptDetail.get(j) == e.getComponent()) {
                        deleteProductInCart(j);
                        return;
                    }
                }

            }
        });
        jPanel.add(deleteReceiptDetail.get(index));

        JPanel productInCartNote = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productInCartNote.setPreferredSize(new Dimension(450, 150));
        productInCartNote.setBackground(new Color(228, 231, 235));

        JLabel choosingSugar = new JLabel("Chọn mức đường: ");
        choosingSugar.setFont(new Font("Inter", Font.PLAIN, 13));
        choosingSugar.setPreferredSize(new Dimension(150, 50));
        productInCartNote.add(choosingSugar);

        JRadioButton radioSugar1 = new JRadioButton("100%");
        radioSugar1.setSelected(true);
        radioSugar1.addActionListener(e -> saveNote(index));
        JRadioButton radioSugar2 = new JRadioButton("70%");
        radioSugar2.addActionListener(e -> saveNote(index));
        JRadioButton radioSugar3 = new JRadioButton("50%");
        radioSugar3.addActionListener(e -> saveNote(index));
        JRadioButton radioSugar4 = new JRadioButton("30%");
        radioSugar4.addActionListener(e -> saveNote(index));
        JRadioButton radioSugar5 = new JRadioButton("0%");
        radioSugar5.addActionListener(e -> saveNote(index));

        ButtonGroup bgSugar = new ButtonGroup();
        bgSugar.add(radioSugar1);
        bgSugar.add(radioSugar2);
        bgSugar.add(radioSugar3);
        bgSugar.add(radioSugar4);
        bgSugar.add(radioSugar5);
        buttonGroupsSugar.add(bgSugar);

        productInCartNote.add(radioSugar1);
        productInCartNote.add(radioSugar2);
        productInCartNote.add(radioSugar3);
        productInCartNote.add(radioSugar4);
        productInCartNote.add(radioSugar5);

        JLabel choosingIce = new JLabel("Chọn mức đá: ");
        choosingIce.setFont(new Font("Inter", Font.PLAIN, 13));
        choosingIce.setPreferredSize(new Dimension(150, 50));
        productInCartNote.add(choosingIce);

        JRadioButton radioIce1 = new JRadioButton("100%");
        radioIce1.setSelected(true);
        radioIce1.addActionListener(e -> saveNote(index));
        JRadioButton radioIce2 = new JRadioButton("70%");
        radioIce2.addActionListener(e -> saveNote(index));
        JRadioButton radioIce3 = new JRadioButton("50%");
        radioIce3.addActionListener(e -> saveNote(index));
        JRadioButton radioIce4 = new JRadioButton("30%");
        radioIce4.addActionListener(e -> saveNote(index));
        JRadioButton radioIce5 = new JRadioButton("0%");
        radioIce5.addActionListener(e -> saveNote(index));

        ButtonGroup bgIce = new ButtonGroup();
        bgIce.add(radioIce1);
        bgIce.add(radioIce2);
        bgIce.add(radioIce3);
        bgIce.add(radioIce4);
        bgIce.add(radioIce5);
        buttonGroupsIce.add(bgIce);

        productInCartNote.add(radioIce1);
        productInCartNote.add(radioIce2);
        productInCartNote.add(radioIce3);
        productInCartNote.add(radioIce4);
        productInCartNote.add(radioIce5);

        productIncartNotelList.add(productInCartNote);

        Bill_detailPanel.setPreferredSize(new Dimension(450, note ? (Math.max(400, receiptDetailList.size() * 55 + 150)) : (Math.max(400, receiptDetailList.size() * 55))));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();
        calculateTotal();
    }

    private void saveNote(int index) {
        for (Enumeration<AbstractButton> buttons = buttonGroupsSugar.get(index).getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                receiptDetailList.get(index).set(4, button.getText());
            }
        }

        for (Enumeration<AbstractButton> buttons = buttonGroupsIce.get(index).getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                receiptDetailList.get(index).set(5, button.getText());
            }
        }
    }

    private void showOption(int index) {
        Bill_detailPanel.removeAll();
        productIncartPanelList = new ArrayList<>();

        for (int i = 0; i < receiptDetailList.size(); i++) {
            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            jPanel.setBackground(new Color(228, 231, 235));
            jPanel.setPreferredSize(new Dimension(450, 50));

            jPanel.add(nameReceiptDetail.get(i));
            if (receiptDetailList.get(i).get(1).equals("Không")) {
                JLabel panel = new JLabel();
                panel.setFont(new Font("Inter", Font.PLAIN, 8));
                panel.setPreferredSize(new Dimension(40, 40));
                jPanel.add(panel);
            } else {
                jPanel.add(sizeReceiptDetail.get(i));
            }
            jPanel.add(quantityReceiptDetail.get(i));
            jPanel.add(priceReceiptDetail.get(i));
            jPanel.add(deleteReceiptDetail.get(i));

            if (i == index) {
                jPanel.add(productIncartNotelList.get(index));
                jPanel.setPreferredSize(new Dimension(450, 200));
            }

            Bill_detailPanel.add(jPanel);
            productIncartPanelList.add(jPanel);
        }
        Bill_detailPanel.setPreferredSize(new Dimension(450, Math.max(400, 55 * receiptDetailList.size() + 150)));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();
    }

    private void saveSizeChanged(int index) {
        List<Object> receipt = receiptDetailList.get(index);

        int quantity = Integer.parseInt(receipt.get(2).toString());
        Product product = productBLL.findProductsBy(Map.of("name", receipt.get(0),
                "size", Objects.requireNonNull(sizeReceiptDetail.get(index).getSelectedItem()))).get(0);
        int remain = checkRemainProduct(product);
        if (quantity > remain) {
            sizeReceiptDetail.get(index).setSelectedItem(receipt.get(1).toString());
            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm còn lại: " + remain + ".\nVui lòng thay đổi số lượng mua.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            materialBLL.getMaterialDAL().executeProcedureAddMaterial(product.getId(), receipt.get(1).toString(), Integer.parseInt(receipt.get(2).toString()));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        receipt.set(1, sizeReceiptDetail.get(index).getSelectedItem());
        try {
            materialBLL.getMaterialDAL().executeProcedureSubMaterial(product.getId(), receipt.get(1).toString(), Integer.parseInt(receipt.get(2).toString()));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        double price;
        price = product.getPrice();
        receipt.set(3, quantity * price);

        receiptDetailList.set(index, receipt);
//        sizeReceiptDetail.get(index).setSelectedItem(receipt.get(1));
        priceReceiptDetail.get(index).setText(VNString.currency(Double.parseDouble(receipt.get(3).toString())));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();
        calculateTotal();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProductRoundPanel();
                loadProduct(resultSearch);
            }
        });
        thread.start();
    }

    private void saveQuantityChanged(int index) {
        List<Object> receipt = receiptDetailList.get(index);

        if (quantityReceiptDetail.get(index).getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int quantity = Integer.parseInt(quantityReceiptDetail.get(index).getText());
        Product product = productBLL.findProductsBy(Map.of("name", receipt.get(0),
                "size", receipt.get(1).toString())).get(0);
        int remain = checkRemainProduct(product);
        if (quantity > remain) {
            quantityReceiptDetail.get(index).setText(receipt.get(2).toString());
            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm còn lại: " + remain + ".\nVui lòng thay đổi số lượng mua.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!receipt.get(1).toString().equals("Không")) {
            try {
                materialBLL.getMaterialDAL().executeProcedureAddMaterial(product.getId(), receipt.get(1).toString(), Integer.parseInt(receipt.get(2).toString()));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            receipt.set(2, Integer.parseInt(quantityReceiptDetail.get(index).getText()));
            try {
                materialBLL.getMaterialDAL().executeProcedureSubMaterial(product.getId(), receipt.get(1).toString(), Integer.parseInt(receipt.get(2).toString()));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            List<Material> materialList = materialBLL.findMaterialsBy(Map.of("name", product.getName()));
            Material newMaterial = materialList.get(0);
            Material oldMaterial = materialList.get(0);
            newMaterial.setRemain(newMaterial.getRemain() + Double.parseDouble(receipt.get(2).toString()));
            receipt.set(2, Integer.parseInt(quantityReceiptDetail.get(index).getText()));
            newMaterial.setRemain(newMaterial.getRemain() - Double.parseDouble(receipt.get(2).toString()));
            materialBLL.updateMaterial(newMaterial, oldMaterial);
        }

        double price;
        price = product.getPrice();
        receipt.set(3, quantity * price);

        receiptDetailList.set(index, receipt);
        priceReceiptDetail.get(index).setText(VNString.currency(Double.parseDouble(receipt.get(3).toString())));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();
        calculateTotal();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProductRoundPanel();
                loadProduct(resultSearch);
            }
        });
        thread.start();
    }

    private void deleteProductInCart(int index) {
        List<Object> receipt = receiptDetailList.get(index);
        Product product = productBLL.findProductsBy(Map.of("name", receipt.get(0),
                "size", receipt.get(1).toString())).get(0);
        if (!receipt.get(1).toString().equals("Không")) {
            try {
                materialBLL.getMaterialDAL().executeProcedureAddMaterial(product.getId(), receipt.get(1).toString(), Integer.parseInt(receipt.get(2).toString()));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            List<Material> materialList = materialBLL.findMaterialsBy(Map.of("name", product.getName()));
            Material newMaterial = materialList.get(0);
            Material oldMaterial = materialList.get(0);
            newMaterial.setRemain(newMaterial.getRemain() + Double.parseDouble(receipt.get(2).toString()));
            materialBLL.updateMaterial(newMaterial, oldMaterial);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProductRoundPanel();
                loadProduct(resultSearch);
            }
        });
        thread.start();

        receiptDetailList.remove(index);
        nameReceiptDetail.remove(index);
        sizeReceiptDetail.remove(index);
        quantityReceiptDetail.remove(index);
        priceReceiptDetail.remove(index);
        deleteReceiptDetail.remove(index);

        productIncartNotelList.remove(index);

        buttonGroupsSugar.remove(index);
        buttonGroupsIce.remove(index);
        loadProductInCart();
        calculateTotal();

        Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
        circleProgressBar.getRootPane().setOpaque(false);
        circleProgressBar.getContentPane().setBackground(new Color(0, 0, 0, 0));
        circleProgressBar.setBackground(new Color(0, 0, 0, 0));
        circleProgressBar.progress();
        circleProgressBar.setVisible(true);
    }

    private void loadProductInCart() {
        Bill_detailPanel.removeAll();
        productIncartPanelList = new ArrayList<>();

        for (int i = 0; i < receiptDetailList.size(); i++) {
            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            jPanel.setBackground(new Color(228, 231, 235));
            jPanel.setPreferredSize(new Dimension(450, 50));

            jPanel.add(nameReceiptDetail.get(i));
            if (receiptDetailList.get(i).get(1).equals("Không")) {
                JLabel panel = new JLabel();
                panel.setFont(new Font("Inter", Font.PLAIN, 8));
                panel.setPreferredSize(new Dimension(40, 40));
                jPanel.add(panel);
            } else {
                jPanel.add(sizeReceiptDetail.get(i));
            }
            jPanel.add(quantityReceiptDetail.get(i));
            jPanel.add(priceReceiptDetail.get(i));
            jPanel.add(deleteReceiptDetail.get(i));

            Bill_detailPanel.add(jPanel);
            productIncartPanelList.add(jPanel);
        }
        Bill_detailPanel.setPreferredSize(new Dimension(450, Math.max(400, 55 * receiptDetailList.size())));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();
    }

    private void cancelBill() {
        for (List<Object> receipt : receiptDetailList) {
            Product product = productBLL.findProductsBy(Map.of("name", receipt.get(0),
                    "size", receipt.get(1).toString())).get(0);
            if (!receipt.get(1).toString().equals("Không")) {
                try {
                    materialBLL.getMaterialDAL().executeProcedureAddMaterial(product.getId(), receipt.get(1).toString(), Integer.parseInt(receipt.get(2).toString()));
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                List<Material> materialList = materialBLL.findMaterialsBy(Map.of("name", product.getName()));
                Material newMaterial = materialList.get(0);
                Material oldMaterial = materialList.get(0);
                newMaterial.setRemain(newMaterial.getRemain() + Double.parseDouble(receipt.get(2).toString()));
                materialBLL.updateMaterial(newMaterial, oldMaterial);
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProductRoundPanel();
                loadProduct(resultSearch);
            }
        });
        thread.start();

        receiptDetailList.removeAll(receiptDetailList);
        nameReceiptDetail.removeAll(nameReceiptDetail);
        sizeReceiptDetail.removeAll(sizeReceiptDetail);
        quantityReceiptDetail.removeAll(quantityReceiptDetail);
        deleteReceiptDetail.removeAll(deleteReceiptDetail);

        productIncartNotelList.removeAll(productIncartNotelList);
        buttonGroupsSugar.removeAll(buttonGroupsSugar);
        buttonGroupsIce.removeAll(buttonGroupsIce);

        productIncartPanelList.removeAll(productIncartPanelList);

        Bill_detailPanel.removeAll();
        Bill_detailPanel.setPreferredSize(new Dimension(450, 400));
        Bill_detailPanel.repaint();
        Bill_detailPanel.revalidate();

        jLabelBill.get(0).setText(VNString.currency(0));
        jLabelBill.get(1).setText(VNString.currency(0));
        jLabelBill.get(2).setText(VNString.currency(0));
        jLabelBill.get(3).setText(VNString.currency(0));
        total = -1;
        cash = -1;
        excess = -1;

        jTextFieldCash.setText("");
        Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
        circleProgressBar.getRootPane().setOpaque(false);
        circleProgressBar.getContentPane().setBackground(new Color(0, 0, 0, 0));
        circleProgressBar.setBackground(new Color(0, 0, 0, 0));
        circleProgressBar.progress();
        circleProgressBar.setVisible(true);
    }

    private void calculateTotal() {
        double totalPrice = 0;
        double totalDiscount = 0;
        for (List<Object> receiptDetail : receiptDetailList) {
            totalPrice += Double.parseDouble(receiptDetail.get(3).toString());

            if (discountType == 0) {
                Product product = productBLL.findProductsBy(Map.of("name", receiptDetail.get(0),
                        "size", receiptDetail.get(1).toString())).get(0);
                List<Pair<Double, Double>> list = checkPercentDiscountType0(product.getId(), product.getSize());
                if (!list.isEmpty()) {
                    double quantity = 0, percent = 0;
                    for (Pair<Double, Double> pair : list) {
                        if (pair.getKey() <= Double.parseDouble(receiptDetail.get(2).toString())) {
                            if (pair.getKey() > quantity) {
                                quantity = pair.getKey();
                                percent = pair.getValue();
                            }

                        }
                    }
                    totalDiscount += Double.parseDouble(receiptDetail.get(3).toString()) * percent / 100;
                }
            }
        }
        if (discountType == 1) {
            double discountBill = 0, percent = 0;
            for (Discount_Detail discountDetail : discountDetails) {
                if (discountDetail.getDiscountBill() <= totalPrice) {
                    if (discountDetail.getDiscountBill() > discountBill) {
                        discountBill = discountDetail.getDiscountBill();
                        percent = discountDetail.getPercent();
                    }
                }
            }
            totalDiscount = totalPrice * percent / 100;
        }
        jLabelBill.get(0).setText(VNString.currency(totalPrice));
        if (totalDiscount == 0)
            jLabelBill.get(1).setText(VNString.currency(totalDiscount));
        else
            jLabelBill.get(1).setText("-" + VNString.currency(totalDiscount) + " (Mã giảm giá: " + discountActive.getId() + ")");
        jLabelBill.get(2).setText(VNString.currency(totalPrice - totalDiscount));
        jLabelBill.get(3).setText(VNString.currency(0));
        jTextFieldCash.setText("");
        cash = -1;
        excess = -1;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.total = totalPrice - totalDiscount;
        System.out.println(Arrays.toString(receiptDetailList.toArray()));
    }

    private void calculateExcess() {
        if (receiptDetailList.isEmpty()) {
            jTextFieldCash.setText("");
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (jTextFieldCash.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền nhận.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if ((Double.parseDouble(jTextFieldCash.getText()) - total) < 0) {
            jTextFieldCash.setText("");
            JOptionPane.showMessageDialog(this, "Không đủ tiền thanh toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            cash = Double.parseDouble(jTextFieldCash.getText());
            excess = cash - total;
            jLabelBill.get(3).setText(VNString.currency(excess));
        }

    }

    private List<Pair<Double, Double>> checkPercentDiscountType0(int product_id, String size) {
        List<Pair<Double, Double>> list = new ArrayList<>();
        for (Discount_Detail discountDetail : discountDetails) {
            if (product_id == discountDetail.getProduct_id() && size.equals(discountDetail.getSize())) {
                list.add(new Pair<>(discountDetail.getQuantity(), discountDetail.getPercent()));
            }
        }
        return list;
    }

    private boolean checkDiscountType0(int product_id) {
        if (discountType == 0) {
            for (Discount_Detail discountDetail : discountDetails) {
                if (product_id == discountDetail.getProduct_id()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBestSellers(int product_id) {
        for (List<String> list : bestSeller) {
            if (product_id == Integer.parseInt(list.get(0))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOutOfRemain(int product_id) {
        for (Pair<Integer, JLabel> jLabelPair : remainList) {
            if (jLabelPair.getKey() == product_id) {
                int remain = Integer.parseInt(jLabelPair.getValue().getText().split(": ")[1]);
                if (remain == 0)
                    return true;
            }
        }
        return false;
    }

    private int checkRemainProduct(Product product) {
        if (Objects.equals(product.getSize(), "Không")) {
            List<Material> materialList = materialBLL.findMaterialsBy(Map.of("name", product.getName()));
            if (materialList.isEmpty())
                return 0;
            else {
                Material material = materialList.get(0);
                return (int) material.getRemain();
            }
        } else {
            return Integer.parseInt(productBLL.getRemain(product.getId(), product.getSize()).get(0).get(0));
        }
    }

    private void checkDiscount() {
        for (Discount discount : discountBLL.searchDiscounts("status = 0")) {
            if (discount.getEnd_date().before(Date.valueOf(LocalDate.now()))) {
                discount.setStatus(true);
                discountBLL.updateStatusDiscount(discount);
            }
        }
    }
}
