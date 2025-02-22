package com.coffee.GUI;

import com.coffee.BLL.ProductBLL;
import com.coffee.DTO.Function;
import com.coffee.DTO.Product;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddProductGUI;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddProductGUI1;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailProductGUI;
import com.coffee.GUI.DialogGUI.FormDetailGUI.DetailProductGUI1;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditProductGUI;
import com.coffee.GUI.DialogGUI.FormEditGUI.EditProductGUI1;
import com.coffee.GUI.components.*;
import com.coffee.ImportExcel.AddProductFromExcel;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.coffee.utils.Resource.chooseExcelFile;

public class ProductGUI extends Layout3 {
    private final ProductBLL productBLL = new ProductBLL();
    private List<Function> functions;
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private boolean detail = false;
    private boolean edit = false;
    private boolean remove = false;
    private DataTable dataTable;
    private List<String> categoriesName;
    private String categoryName = "TẤT CẢ";
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private int indexColumnEdit = -1;
    private int indexColumnRemove = -1;

    private String[] columnNames;

    public ProductGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("edit")))
            edit = true;
        if (functions.stream().anyMatch(f -> f.getName().equals("remove")))
            remove = true;
        initComponents(functions);

    }

    public void initComponents(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        categoriesName = new ArrayList<>();

        columnNames = new String[]{"Ảnh", "Tên sản phẩm", "Size", "Giá bán"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        if (edit) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnEdit = columnNames.length - 1;
            columnNames[indexColumnEdit] = "Sửa";
        }

        if (remove) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnRemove = columnNames.length - 1;
            columnNames[indexColumnRemove] = "Xoá";
        }

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(),
                detail, edit, remove, 4);
        int[] columnWidths = {150, 200, 20, 100};

        for (int i = 0; i < columnWidths.length; i++) {
            dataTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        dataTable.setRowHeight(150);

        dataTable.getColumnModel().getColumn(0).setCellRenderer(new CustomPanelRenderer());
        dataTable.getColumnModel().getColumn(2).setCellRenderer(new CustomPanelRenderer());
        dataTable.getColumnModel().getColumn(3).setCellRenderer(new CustomPanelRenderer());
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));

        bottom.add(scrollPane, BorderLayout.CENTER);

        containerSearch.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(380, 40));

        SearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        SearchPanel.add(containerSearch);
        SearchPanel.add(jButtonSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.setPreferredSize(new Dimension(250, 30));

        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonSearch.addActionListener(e -> searchProducts());
        SearchPanel.add(jButtonSearch);

        loadDataTable(productBLL.getData(productBLL.searchProducts("deleted = 0")));
        RoundedPanel refreshPanel = new RoundedPanel();
        refreshPanel.setLayout(new GridBagLayout());
        refreshPanel.setPreferredSize(new Dimension(130, 40));
        refreshPanel.setBackground(new Color(1, 120, 220));
        refreshPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }
        });
        FunctionPanel.add(refreshPanel);
        JLabel refreshLabel = new JLabel("Làm mới");
        refreshLabel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        refreshLabel.setForeground(Color.white);
        refreshLabel.setIcon(new FlatSVGIcon("icon/refresh.svg"));
        refreshPanel.add(refreshLabel);

        if (functions.stream().anyMatch(f -> f.getName().equals("add"))) {
            RoundedPanel roundedPanel = getRoundedPanel();
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int option = JOptionPane.showOptionDialog(null,
                            "Chọn loại sản phẩm bạn muốn thêm:",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String[]{"Thêm sản phẩm", "Thêm sản phẩm chế biến"},
                            "Thêm sản phẩm");

                    if (option == JOptionPane.YES_OPTION) {
                        new AddProductGUI1();
                        refresh();
                    } else if (option == JOptionPane.NO_OPTION) {
                        new AddProductGUI();
                        refresh();
                    }

                }
            });
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Thêm mới");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/add.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("excel"))) {
            RoundedPanel roundedPanel = getRoundedPanel();
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    File file = chooseExcelFile(null);
                    if (file != null) {
                        Pair<Boolean, String> result;
                        try {
                            result = new AddProductFromExcel().AddProductFromExcell(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!result.getKey()) {
                            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công",
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            refresh();
                            loadSaleGUI();
                        }
                    }
                }
            });
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Nhập Excel");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/import.svg"));
            roundedPanel.add(panel);

        }
        if (functions.stream().anyMatch(f -> f.getName().equals("pdf"))) {
            RoundedPanel roundedPanel = getRoundedPanel();
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Xuất PDF");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/export.svg"));
            roundedPanel.add(panel);
        }

        loadCategory();

    }

    private RoundedPanel getRoundedPanel() {
        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(130, 40));
        roundedPanel.setBackground(new Color(1, 120, 220));
        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return roundedPanel;
    }

    public void refresh() {
        jTextFieldSearch.setText("");
        loadCategory();
        loadDataTable(productBLL.getData(productBLL.searchProducts("deleted = 0")));

    }

    public void loadDataTable(Object[][] objects) {

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int columnCount = model.getColumnCount();
        model.setRowCount(0);
        int columnWidth = dataTable.getColumnModel().getColumn(0).getPreferredWidth();
        int rowHeight = dataTable.getRowHeight(0);
        ArrayList<Object[]> allProducts = ConvertProductUnique(objects);

        for (Object[] productArray : allProducts) {
            String productName = (String) productArray[1];
            ArrayList<String> sizes = (ArrayList<String>) productArray[4];
            ArrayList<Double> prices = (ArrayList<Double>) productArray[3];
            ImageIcon icon = new FlatSVGIcon("image/Product/" + productArray[5] + ".svg");
            Image image = icon.getImage();
            Image newImg = image.getScaledInstance(columnWidth, rowHeight, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            JLabel productImage = new JLabel(icon);
            productImage.scrollRectToVisible(new Rectangle());
            CustomPopupMenu popupMenuSize = new CustomPopupMenu();
            CustomPopupMenu popupMenuPrice = new CustomPopupMenu();
            for (String size : sizes) {
                popupMenuSize.addMenuItem(size);
            }
            for (Double price : prices) {
                popupMenuPrice.addMenuItem(VNString.currency(price));
            }
            JLabel iconDetail = null;
            JLabel iconEdit = null;
            JLabel iconRemove = null;
            if (detail) {
                iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
            }
            if (edit) {
                iconEdit = new JLabel(new FlatSVGIcon("icon/edit.svg"));
            }
            if (remove) {
                iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));

            }

            Object[] rowData;
            if (columnCount == 7) {
                rowData = new Object[]{productImage, productName, popupMenuSize, popupMenuPrice, iconDetail, iconEdit, iconRemove};
            } else if (columnCount == 6) {
                rowData = new Object[]{productImage, productName, popupMenuSize, popupMenuPrice, iconDetail, iconEdit};
            } else if (columnCount == 5) {
                rowData = new Object[]{productImage, productName, popupMenuSize, popupMenuPrice, iconDetail};
            } else {
                rowData = new Object[]{productImage, productName, popupMenuSize, popupMenuPrice};
            }

            model.addRow(rowData);

        }

    }

    public ArrayList<Object[]> ConvertProductUnique(Object[][] objects) {
        ArrayList<Object[]> allProducts = new ArrayList<>();
        for (Object[] product : objects) {
            String productID = (String) product[0];
            String productName = (String) product[1];
            String category = (String) product[2];
            double price = Double.parseDouble((String) product[3]);
            String size = (String) product[4];
            String productImage = (String) product[5];

            boolean productExists = false;
            for (Object[] productArray : allProducts) {
                String existingProductName = (String) productArray[1];
                if (existingProductName.equals(productName)) {
                    productExists = true;

                    ((ArrayList<String>) productArray[4]).add(size);
                    ((ArrayList<Double>) productArray[3]).add(price);
                    break;
                }
            }

            if (!productExists) {
                ArrayList<String> sizes = new ArrayList<>();
                sizes.add(size);

                ArrayList<Double> prices = new ArrayList<>();
                prices.add(price);

                Object[] productArray = {productID, productName, category, prices, sizes, productImage};
                allProducts.add(productArray);
            }
        }
        return allProducts;
    }

    public void loadCategory() {
        Category.removeAll();
        categoriesName.removeAll(categoriesName);
        categoriesName.add("TẤT CẢ");
        categoriesName.addAll(productBLL.getCategories());

        Color defaultColor = new Color(236, 185, 188); // Màu mặc định
        Color clickedColor = new Color(253, 143, 143); // Màu khi nhấp vào
        for (String category : categoriesName) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new FlowLayout());
            roundedPanel.setBackground(defaultColor);
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    RoundedPanel roundedPanel = (RoundedPanel) e.getComponent();
                    JLabel jLabel = (JLabel) roundedPanel.getComponent(0);
                    if (!categoryName.equals(jLabel.getText())) {
                        categoryName = jLabel.getText();
                        searchCategory();
                    }
                    // Đổi màu khi nhấp vào
                    roundedPanel.setBackground(clickedColor);
                    // Đổi màu các panel khác trở lại màu mặc định
                    for (Component comp : Category.getComponents()) {
                        if (comp != roundedPanel && comp instanceof RoundedPanel) {
                            ((RoundedPanel) comp).setBackground(defaultColor);
                        }
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
        }
        Category.repaint();
        Category.revalidate();
    }

    private void searchCategory() {
        if (categoryName.equals("TẤT CẢ"))
            loadDataTable(productBLL.getData(productBLL.searchProducts("deleted = 0")));
        else
            loadDataTable(productBLL.getData(productBLL.findProductsBy(Map.of("category", categoryName))));
        jTextFieldSearch.setText("");
    }

    private void searchProducts() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(productBLL.getData(productBLL.searchProducts("deleted = 0")));
        } else {
            loadDataTable(productBLL.getData(productBLL.findProducts("name", jTextFieldSearch.getText())));
        }
        categoryName = "";
    }

    private void selectFunction() {
        int indexColumn = dataTable.getSelectedColumn();
        int indexRow = dataTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        Object selectedValue = model.getValueAt(indexRow, 1);
        List<Product> products = productBLL.findProductsBy(Map.of("name", selectedValue.toString()));
        Product product = products.get(0);
        String size = product.getSize();

        if (indexColumn == indexColumnDetail) {
            if (size.equals("Không")) {
                new DetailProductGUI1(product);
                refresh();
            } else {
                new DetailProductGUI(products);
                refresh();
            }
        }

        if (indexColumn == indexColumnEdit) {
            if (size.equals("Không")) {
                new EditProductGUI1(product);
                refresh();
            } else {
                new EditProductGUI(products);
                refresh();
            }
        }

        if (indexColumn == indexColumnRemove) {
            deleteProduct(productBLL.findProductsBy(Map.of("name", selectedValue.toString())));
        }
    }

    private void deleteProduct(List<Product> products) {
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá sản phẩm?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            boolean check = true;
            for (Product product : products) {
                Pair<Boolean, String> result = productBLL.deleteProduct(product);
                if(!result.getKey()){
                    check = false;
                    break;
                }
            }
            if (check) {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thành công",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refresh();
                loadSaleGUI();
            }
            else{
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thất bại",
                        "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    private void loadSaleGUI() {
        if (Cafe_Application.homeGUI.indexSaleGUI != -1) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SaleGUI saleGUI = (SaleGUI) Cafe_Application.homeGUI.allPanelModules[Cafe_Application.homeGUI.indexSaleGUI];
                    saleGUI.loadCategory();
                    saleGUI.loadProductRoundPanel();
                    saleGUI.loadProduct(saleGUI.resultSearch);
                }
            });
            thread.start();
        }
    }


}
