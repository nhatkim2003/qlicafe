package com.coffee.GUI.DialogGUI.FormEditGUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.BLL.RecipeBLL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Product;
import com.coffee.DTO.Recipe;
import com.coffee.GUI.DialogGUI.DialogFormDetail_1;
import com.coffee.GUI.DialogGUI.FormAddGUI.AddMaterialGUI;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.SaleGUI;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.Resource;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class EditProductGUI extends DialogFormDetail_1 {
    private final MaterialBLL materialBLL = new MaterialBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final RecipeBLL recipeBLL = new RecipeBLL();
    private List<Product> productList;
    private List<Product> productListDefault;
    private List<Recipe> recipeList;
    private List<Recipe> recipeListDefault;

    private final JLabel iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
    private JLabel lblImage;
    private DataTable dataTable;
    private final int product_id;
    private JTextField txtNameProduct;
    private JComboBox<String> cbSize;
    private JPanel panelSize;
    private JTextField txtPrice;
    private JTextField txtCategory;
    private JTextField txtQuantity;
    private JTextField txtUnit;
    private JTextField txtCapitalPrice;
    private final JPopupMenu menu;
    private final PanelSearch search;
    private JTextField txtSearch;
    private JLabel selectedLabel;
    private int materialID;
    private String imageProduct;

    public EditProductGUI(List<Product> Products) {
        super();
        super.setTitle("Sửa sản phẩm");
        this.productList = Products;
        productListDefault = new ArrayList<>(Products);
        this.product_id = Products.get(0).getId();
        this.imageProduct = Products.get(0).getImage();
        init(productList);
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText("");
                txtSearch.setText(data.getText());
                Material material = materialBLL.findMaterialsBy(Map.of("name", data.getText())).get(0);
                materialID = material.getId();
                if (material.getUnit().equals("kg"))
                    txtUnit.setText("g");
                else if (material.getUnit().equals("lít"))
                    txtUnit.setText("ml");
                else
                    txtUnit.setText(material.getUnit());
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

    public void init(List<Product> Products) {
        recipeList = recipeBLL.searchRecipes("product_id = " + product_id);
        recipeListDefault = new ArrayList<>(recipeList);
        txtUnit = new MyTextFieldUnderLine();
        txtSearch = new MyTextFieldUnderLine();
        txtQuantity = new MyTextFieldUnderLine();
        top.setLayout(new FlowLayout());

        RoundedPanel containerAtributeProduct = new RoundedPanel();
        containerAtributeProduct.setLayout(new MigLayout("", "[]40[][][]100", "10[]10[]10[]10"));

        containerAtributeProduct.setBackground(new Color(255, 255, 255));

        containerAtributeProduct.setPreferredSize(new Dimension(600, 200));

        RoundedPanel containerImage = new RoundedPanel();
        containerImage.setLayout(new MigLayout("", "[]", "[][]"));
        containerImage.setBackground(new Color(255, 255, 255));

        containerImage.setPreferredSize(new Dimension(200, 200));

        JPanel PanelImage = new JPanel();
        PanelImage.setPreferredSize(new Dimension(150, 150));
        PanelImage.setLayout(new MigLayout("", "[]", "[]"));
        PanelImage.setBackground(new Color(255, 255, 255));

        lblImage = new JLabel();
        ImageIcon iconProduct = new FlatSVGIcon("image/Product/" + imageProduct + ".svg");
        Image imgProduct = iconProduct.getImage();
        Image newImgProduct = imgProduct.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
        iconProduct = new ImageIcon(newImgProduct);
        lblImage.setIcon(iconProduct);
        PanelImage.add(lblImage);


        JButton btnImage = new JButton("Thêm ảnh");
        btnImage.setPreferredSize(new Dimension(100, 30));
        btnImage.setBackground(new Color(1, 120, 220));
        btnImage.setForeground(Color.white);
        btnImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                imageProduct = uploadImage();
            }
        });
        containerImage.add(PanelImage, "alignx center,wrap");
        containerImage.add(btnImage, "alignx center");

        top.add(containerAtributeProduct);
        top.add(containerImage);
        EmptyBorder emptyBorder = new EmptyBorder(0, 30, 0, 0);
        top.setBorder(emptyBorder);

        String[] columnNames = new String[]{"ID", "Tên nguyên liệu", "Đơn vị", "Giá vốn", "SL", "T.Tiền"};
        columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
        columnNames[columnNames.length - 1] = "Xóa";
        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(), e -> updateQuantity(),
                false, true, true, 6, true, 4);
        int[] columnWidths = {50, 300, 50, 50, 50, 50};

        for (int i = 0; i < columnWidths.length; i++) {
            dataTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }


        RoundedScrollPane scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        RoundedPanel containerDataTable = new RoundedPanel();
        containerDataTable.setLayout(new BorderLayout());
        containerDataTable.setBackground(new Color(255, 255, 255));
        EmptyBorder emptyBorderTop = new EmptyBorder(20, 0, 0, 0);
        containerDataTable.setBorder(emptyBorderTop);
        containerDataTable.add(scrollPane, BorderLayout.CENTER);
        bottom.add(containerDataTable, BorderLayout.CENTER);

        JTableHeader jTableHeader = dataTable.getTableHeader();
        jTableHeader.setBackground(new Color(120, 123, 125));


        JLabel lblName = createLabel("Tên sản phẩm");
        JLabel lblCategory = createLabel("Thể loại");
        JLabel lblSize = createLabel("Size");
        JLabel lblPrice = createLabel("Giá bán");
        JLabel lblCapitalPrice = createLabel("Giá Vốn");

        txtNameProduct = new MyTextFieldUnderLine();
        txtCategory = new MyTextFieldUnderLine();
        txtPrice = new MyTextFieldUnderLine();
        txtCapitalPrice = new MyTextFieldUnderLine();
        txtCapitalPrice.setFocusable(false);
        txtPrice.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Pair<Boolean, String> result = updatePriceProductBySize();
                if (!result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        txtNameProduct.setText(Products.get(0).getName());
        txtCategory.setText(Products.get(0).getCategory());

        panelSize = new JPanel();
        panelSize = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelSize.setBackground(new Color(255, 255, 255));
        panelSize.setPreferredSize(new Dimension(350, 30));
        String defaultSize = "";
        Set<String> addedLabels = new HashSet<>();
        for (Product product : Products) {
            String size = product.getSize();
            JLabel label = new JLabel(size);
            label.setPreferredSize(new Dimension(30, 30));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFocusable(true);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
            addedLabels.add(label.getText());
            if (defaultSize.isEmpty()) {
                defaultSize = size;
            }

            if (size.equals(defaultSize)) {
                selectedLabel = label;
                label.setBackground(new Color(59, 130, 198));
                label.setForeground(Color.WHITE);
                loadPriceBySize(size);
                loadCapitalPriceBySize(size);
                loadRecipeBySize(size);
            }

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedLabel != null) {
                        selectedLabel.setBackground(Color.WHITE);
                        selectedLabel.setForeground(Color.BLACK);
                    }
                    label.setBackground(new Color(59, 130, 198));
                    label.setForeground(Color.WHITE);
                    selectedLabel = label;
                    loadPriceBySize(size);
                    loadCapitalPriceBySize(size);
                    loadRecipeBySize(size);
                }
            });

            panelSize.add(label);
            panelSize.revalidate();
            panelSize.repaint();
        }
        cbSize = createComboBox();
        Set<String> sizes = productList.stream().map(Product::getSize).collect(Collectors.toSet());
        Set<String> availableSizes = new HashSet<>(Arrays.asList("Chọn size", "S", "M", "L"));

        for (String size : availableSizes) {
            if (!sizes.contains(size)) {
                cbSize.addItem(size);
            }
        }

        cbSize.addActionListener(e -> {
            String selectedSize = (String) cbSize.getSelectedItem();
            if (selectedSize.equals("Chọn size")) {
                return;
            }
            if (selectedLabel != null) {
                selectedLabel.setBackground(Color.WHITE);
                selectedLabel.setForeground(Color.BLACK);
            }

            productList.add(new Product(product_id, "", selectedSize, "", 0, 0, "", false));
            JLabel label = createSize(selectedSize);
            selectedLabel = label;
            addedLabels.add(selectedSize);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedLabel != null) {
                        selectedLabel.setBackground(Color.WHITE);
                        selectedLabel.setForeground(Color.BLACK);
                    }
                    label.setBackground(new Color(59, 130, 198));
                    label.setForeground(Color.WHITE);
                    selectedLabel = label;
                    loadPriceBySize(selectedSize);
                    loadCapitalPriceBySize(selectedSize);
                    loadRecipeBySize(selectedSize);
                }
            });
            panelSize.add(label);
            cbSize.removeItem(selectedSize);
            panelSize.revalidate();
            panelSize.repaint();
            resetTxt();
            txtPrice.setText("0");
        });


        JButton deleteSize = new JButton();
        deleteSize.setPreferredSize(new Dimension(30, 30));
        deleteSize.setBackground(new Color(255, 255, 255));
        deleteSize.setBorder(null);
        deleteSize.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ImageIcon iconRemove = new FlatSVGIcon("icon/remove.svg");
        Image imageRemove = iconRemove.getImage();
        Image newImgRemove = imageRemove.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        iconRemove = new ImageIcon(newImgRemove);
        deleteSize.setIcon(iconRemove);
        deleteSize.addActionListener(e -> {
            if (selectedLabel != null) {
                panelSize.remove(selectedLabel);
                String labelText = selectedLabel.getText();
                addedLabels.remove(labelText);
                panelSize.revalidate();
                panelSize.repaint();
                resetTxt();
                removeSizeProduct(labelText);
                cbSize.addItem(labelText);
                selectedLabel = null;
            } else {
                JOptionPane.showMessageDialog(null, "Chọn size muốn xóa");
            }
        });


        containerAtributeProduct.add(lblName);
        containerAtributeProduct.add(txtNameProduct, "wrap");

        containerAtributeProduct.add(lblCategory);
        containerAtributeProduct.add(txtCategory, "wrap");

        containerAtributeProduct.add(lblSize);
        containerAtributeProduct.add(panelSize);
        containerAtributeProduct.add(cbSize);
        containerAtributeProduct.add(deleteSize, "wrap");

        containerAtributeProduct.add(lblPrice);
        containerAtributeProduct.add(txtPrice, "wrap");

        containerAtributeProduct.add(lblCapitalPrice);
        containerAtributeProduct.add(txtCapitalPrice, "wrap");

        JLabel lblListMaterial = new JLabel("Danh sách nguyên liệu");
        lblListMaterial.setFont(new Font("Public Sans", Font.BOLD, 18));

        center.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        center.add(lblListMaterial);

        RoundedPanel containerInforMaterial = new RoundedPanel();
        containerInforMaterial.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        containerInforMaterial.setBackground(new Color(255, 255, 255));


        JButton btnAddMaterial = new JButton();
        ImageIcon icon = new FlatSVGIcon("icon/add.svg");
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        btnAddMaterial.setIcon(icon);
        btnAddMaterial.setPreferredSize(new Dimension(30, 30));
        btnAddMaterial.setBackground(new Color(0, 182, 62));
        btnAddMaterial.setFont(new Font("Public Sans", Font.BOLD, 16));
        btnAddMaterial.setForeground(Color.WHITE);
        btnAddMaterial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddMaterial.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new AddMaterialGUI();
            }
        });
        containerInforMaterial.add(btnAddMaterial);
        bottom.add(containerInforMaterial, BorderLayout.NORTH);

        JLabel lblNameMaterial = createLabelMaterial("Tên nguyên liệu");


        txtSearch.setPreferredSize(new Dimension(310, 30));

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

        JLabel lblQuantity = createLabelMaterial("Số lượng");
        txtQuantity = new MyTextFieldUnderLine();
        txtQuantity.setPreferredSize(new Dimension(70, 30));

        JLabel lblUnit = createLabelMaterial("Đơn vị");
        txtUnit = new MyTextFieldUnderLine();
        txtUnit.setPreferredSize(new Dimension(70, 30));
        txtUnit.setEnabled(false);

        JButton btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(80, 30));
        btnThem.setBackground(new Color(1, 120, 220));
        btnThem.setFont(new Font("Public Sans", Font.BOLD, 16));
        btnThem.setForeground(Color.WHITE);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnThem.addActionListener(e -> addMaterialTemp());

        containerInforMaterial.add(lblNameMaterial);
        containerInforMaterial.add(txtSearch);
        containerInforMaterial.add(lblQuantity);
        containerInforMaterial.add(txtQuantity);
        containerInforMaterial.add(lblUnit);
        containerInforMaterial.add(txtUnit);
        containerInforMaterial.add(btnThem);

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

        JButton buttonUpdate = new JButton("Cập nhật");
        buttonUpdate.setBackground(new Color(1, 120, 220));
        buttonUpdate.setForeground(new Color(255, 255, 255));
        buttonUpdate.setPreferredSize(new Dimension(100, 35));
        buttonUpdate.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Pair<Boolean, String> result = checkSizeRecipeEmpty();
                if (!result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (updateAllProducts() && updateAllRecipe()) {
                    JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    loadSaleGUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        containerButton.add(buttonUpdate);

    }

    private JLabel createSize(String size) {
        JLabel label = new JLabel(size);
        label.setPreferredSize(new Dimension(30, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFocusable(true);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.setBackground(new Color(59, 130, 198));
        label.setForeground(Color.WHITE);
        return label;
    }

    private Pair<Boolean, String> validateMaterial(String name, String quantity) {
        Pair<Boolean, String> result;

        if (name.isBlank()) {
            return new Pair<>(false, "Vui lòng chọn nguyên liệu");
        }
        result = materialBLL.validateQuantity(quantity, "Số lượng");
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }
        return new Pair<>(true, "hợp lệ");
    }

    private Pair<Boolean, String> updatePriceProductBySize() {
        if (selectedLabel == null) {
            return new Pair<>(false, "Chưa chọn size");
        }
        String size = selectedLabel.getText();
        Pair<Boolean, String> result;
        String price = txtPrice.getText();
        result = productBLL.validatePrice(price, "Giá bán");

        if (!result.getKey()) {
            txtPrice.requestFocus();
            return new Pair<>(false, result.getValue());
        }

        if (!productList.isEmpty()) {
            for (Product product : productList) {
                if (product.getSize().equals(size)) {
                    product.setPrice(Double.parseDouble(price));
                    return new Pair<>(true, result.getValue());
                }
            }
        }

        return new Pair<>(true, result.getValue());
    }

    private double SetCapitalBySize(String size) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        double capitalPrice = 0.0;
        for (Recipe recipe : recipeList) {
            if (recipe.getSize().equals(size)) {
                Material material = materialBLL.findMaterialsBy(Map.of("id", recipe.getMaterial_id())).get(0);
                double materialPriceD = material.getUnit_price() / 1000;
                double totalAmount = materialPriceD * recipe.getQuantity();
                capitalPrice += totalAmount;
            }
        }
        return capitalPrice;
    }

    private String uploadImage() {
        File selectedFile = Resource.chooseProductImageFile(this);
        if (selectedFile != null) {
            String imageFile = "SP" + product_id + ".svg";
            String imageName = "SP" + product_id;
            java.nio.file.Path destinationPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "image", "Product", imageFile);
            java.nio.file.Path destinationPathTarget = Paths.get(System.getProperty("user.dir"), "target", "classes", "image", "Product", imageFile);
            try {
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(selectedFile.toPath(), destinationPathTarget, StandardCopyOption.REPLACE_EXISTING);
                InputStream inputStream = Files.newInputStream(destinationPath);
                ImageIcon icon = new FlatSVGIcon(inputStream);
                Image image = icon.getImage();
                Image newImg = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                lblImage.setIcon(icon);
                return imageName;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private void addMaterialTemp() {
        String name = txtSearch.getText();
        String quantity = txtQuantity.getText();
        String unit = txtUnit.getText();
        String size = selectedLabel.getText();
        Pair<Boolean, String> result = validateMaterial(name, quantity);
        if (!result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (unit.isBlank()) {
            JOptionPane.showMessageDialog(null, "Nguyên liệu không tồn tại",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Recipe recipe = new Recipe(product_id, materialID, Double.parseDouble(quantity), size, unit);
        if (!checkRecipeExist(recipe)) {
            recipeList.add(recipe);
            addDataToTable(materialID, quantity,unit);
        } else {
            JOptionPane.showMessageDialog(null, "Nguyên liệu đã có trong công thức",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean checkRecipeExist(Recipe recipe) {
        for (Recipe r : recipeList) {
            if (r.getProduct_id() == recipe.getProduct_id()
                    && r.getMaterial_id() == recipe.getMaterial_id()
                    && r.getSize().equals(recipe.getSize())) {
                return true;
            }
        }
        return false;
    }

    private void addDataToTable(int materialID, String quantity,String unit) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        Material material = materialBLL.findMaterialsBy(Map.of("id", materialID)).get(0);
        String materialName = material.getName();

        String materialPrice = VNString.currency(material.getUnit_price() / 1000);
        double materialPriceD = material.getUnit_price() / 1000;
        double totalAmount = materialPriceD * Double.parseDouble(quantity);
        Object[] rowData = {materialID, materialName, unit, materialPrice, Double.parseDouble(quantity), VNString.currency(totalAmount), iconRemove};
        model.addRow(rowData);

        txtSearch.setText("");
        txtQuantity.setText("");
        txtUnit.setText("");
        if (txtCapitalPrice.getText().isEmpty()) {
            txtCapitalPrice.setText(VNString.currency(Double.parseDouble(materialPrice)));
        } else {

            double capitalPrice = VNString.parseCurrency(txtCapitalPrice.getText());
            System.out.println("Giá vốn" + capitalPrice);
            System.out.println("Tổng tiền" + totalAmount);
            txtCapitalPrice.setText(VNString.currency(capitalPrice + totalAmount));
        }
    }

    private void selectFunction() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (indexColumn == 6) {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá nguyên liệu?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
            if (choice == 1) {
                Iterator<Recipe> iterator = recipeList.iterator();
                String labelText = selectedLabel.getText();
                int id = (int) model.getValueAt(indexRow, 0);
                while (iterator.hasNext()) {
                    Recipe recipe = iterator.next();
                    if (recipe.getProduct_id() == product_id &&
                            recipe.getMaterial_id() == id &&
                            recipe.getSize().equals(labelText)) {
                        iterator.remove();
                    }
                }
                double totalAmount = VNString.parseCurrency((String) model.getValueAt(indexRow, 5));
                double capitalPrice = VNString.parseCurrency(txtCapitalPrice.getText());
                txtCapitalPrice.setText(VNString.currency(capitalPrice - totalAmount));
                model.removeRow(indexRow);

            }
        }
    }


    private void removeSizeProduct(String size) {
        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getSize().equals(size)) {
                iterator.remove();
                removeRecipeByProduct(product);
            }
        }
    }

    private void removeRecipeByProduct(Product product) {
        recipeList.removeIf(recipe -> recipe.getProduct_id() == product.getId() && recipe.getSize().equals(product.getSize()));
    }


    private void resetTxt() {
        txtCapitalPrice.setText("");
        txtSearch.setText("");
        txtQuantity.setText("");
        txtUnit.setText("");
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
    }

    private void loadPriceBySize(String size) {
        if (!productList.isEmpty()) {
            for (Product product : productList) {
                if (product.getSize().equals(size)) {
                    Double price = product.getPrice();
                    txtPrice.setText(VNString.currency(price));
                    return;
                }
            }
        }
    }

    private void loadCapitalPriceBySize(String size) {
        if (!productList.isEmpty()) {
            for (Product product : productList) {
                if (product.getSize().equals(size)) {
                    txtCapitalPrice.setText(VNString.currency(product.getCapital_price()));
                    return;
                }
            }
        }
    }

    private void loadRecipeBySize(String size) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        double capitalPrice = 0.0;
        for (Recipe recipe : recipeList) {
            if (recipe.getSize().equals(size)) {
                Material material = materialBLL.findMaterialsBy(Map.of("id", recipe.getMaterial_id())).get(0);
                String materialName = material.getName();
                String materialPrice = VNString.currency(material.getUnit_price() / 1000);
                double materialPriceD = material.getUnit_price() / 1000;
                double totalAmount = materialPriceD * recipe.getQuantity();
                Object[] rowData = {recipe.getMaterial_id(), materialName, recipe.getUnit(), materialPrice, recipe.getQuantity(), VNString.currency(totalAmount), iconRemove};
                model.addRow(rowData);
                capitalPrice += totalAmount;
            }
        }
        txtSearch.setText("");
        txtQuantity.setText("");
        txtUnit.setText("");
        txtCapitalPrice.setText(VNString.currency(capitalPrice));

    }

    private boolean updateAllProducts() {
        String name = txtNameProduct.getText();
        String category = txtCategory.getText();
        String image = imageProduct;

        // Xử lý xóa sản phẩm không tồn tại trong productList
        for (Product product_default : productListDefault) {
            boolean isExist = false;
            for (Product product : productList) {
                if (product_default.getSize().equals(product.getSize())) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                Pair<Boolean, String> result = productBLL.deleteProduct(product_default);
                if (!result.getKey())
                    return false;
            }
        }

        // Xử lý cập nhật và thêm sản phẩm

        for (Product product : productList) {
            product.setName(name);
            product.setCategory(category);
            product.setImage(image);
            double capitalPrice = SetCapitalBySize(product.getSize());
            product.setCapital_price(capitalPrice);
            boolean check1 = false;

            for (Product product_default : productListDefault) {
                if (product.getSize().equals(product_default.getSize())) {
                    Pair<Boolean, String> result = productBLL.updateProduct(product);
                    if (!result.getKey())
                        return false;
                    check1 = true;
                    break;
                }
            }

            if (!check1) { // Nếu size không tồn tại trong productListDefault thì thực hiện thêm sản phẩm
                // Kiểm tra nếu sản phẩm đã được xóa trước đó
                List<Product> productsDeleted = productBLL.searchProducts("id = '" + product_id + "'", "deleted = 1");
                boolean check2 = false;
                for (Product productDeleted : productsDeleted) {
                    if (productDeleted.getSize().equals(product.getSize())) {
                        Pair<Boolean, String> result = productBLL.updateProduct(product);
                        check2 = true;
                        if (!result.getKey())
                            return false;
                        break;
                    }
                }
                // Nếu sản phẩm không tồn tại trong danh sách sản phẩm đã bị xóa, thực hiện thêm mới
                if (!check2) {
                    Pair<Boolean, String> result = productBLL.addProduct(product);
                    if (!result.getKey())
                        return false;
                }

            }
        }

        return true;
    }


    private boolean updateAllRecipe() {
        for (Recipe recipe_default : recipeListDefault) {
            boolean check = false;
            for (Recipe recipe : recipeList) {
                if (recipe_default.getMaterial_id() == recipe.getMaterial_id() && recipe_default.getSize().equals(recipe.getSize())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                Pair<Boolean, String> result = recipeBLL.deleteRecipe(recipe_default);
                if (!result.getKey())
                    return false;
            }
        }

        for (Recipe recipe : recipeList) {
            boolean check2 = false;
            for (Recipe recipe_default : recipeListDefault) {
                if (recipe.getMaterial_id() == recipe_default.getMaterial_id() && recipe.getSize().equals(recipe_default.getSize())) {
                    check2 = true;
                    Pair<Boolean, String> result = recipeBLL.updateRecipe(recipe);
                    if (!result.getKey())
                        return false;
                    break;
                }
            }
            if (!check2) {
                Pair<Boolean, String> result = recipeBLL.addRecipe(recipe);
                if (!result.getKey())
                    return false;
            }
        }
        return true;
    }

    private Pair<Boolean, String> checkSizeRecipeEmpty() {
        StringBuilder error = new StringBuilder();
        for (Product product : productList) {
            boolean check = false;
            for (Recipe r : recipeList) {
                if (r.getSize().equals(product.getSize())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                if (error.isEmpty())
                    error.append("Vui lòng nhập nguyên liệu cho size ").append("\n").append(product.getSize()).append("\n");
                else
                    error.append(product.getSize()).append("\n");
            }
        }
        if (!error.isEmpty()) {
            return new Pair<>(false, error.toString());
        }
        return new Pair<>(true, "");
    }

    private void updateQuantity() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        String quantity = model.getValueAt(indexRow, indexColumn).toString();
        double capitalPrice = VNString.parseCurrency( model.getValueAt(indexRow, indexColumn - 1).toString());
        if (quantity != null) {
            try {
                double quantityDouble = Double.parseDouble(quantity);
                double newValue = quantityDouble * capitalPrice;
                Double CapitalPrice = VNString.parseCurrency(txtCapitalPrice.getText());
                Double oldValue = VNString.parseCurrency(model.getValueAt(indexRow, indexColumn + 1).toString());
                txtCapitalPrice.setText(VNString.currency(CapitalPrice - oldValue + newValue ));
                model.setValueAt(VNString.currency(newValue), indexRow, indexColumn + 1);

                int material_id = Integer.parseInt(model.getValueAt(indexRow, 0).toString());
                String size = selectedLabel.getText();
                for (Recipe recipe : recipeList) {
                    if (recipe.getMaterial_id() == material_id && recipe.getSize().equals(size)) {
                        recipe.setQuantity(quantityDouble);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Số lượng không phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(() -> {
                    dataTable.editCellAt(indexRow, indexColumn);
                    dataTable.getEditorComponent().requestFocusInWindow();
                });
            }
        }
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
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
            } else {
                menu.setVisible(false);
            }
        }
    }

    private List<DataSearch> search(String text) {
        List<DataSearch> list = new ArrayList<>();
        List<Material> materials = materialBLL.findMaterials("name", text);
        for (Material m : materials) {
            if (list.size() == 7) {
                break;
            }
            list.add(new DataSearch(m.getName()));
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

    private JLabel createLabelMaterial(String text) {
        JLabel label = new JLabel(text);
        label.setFont((new Font("Public Sans", Font.BOLD, 14)));
        label.setPreferredSize(null);
        return label;
    }


    private JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(30, 30));
        comboBox.setFont(new Font("Public Sans", Font.PLAIN, 14));
        comboBox.setBackground(new Color(245, 246, 250));
        return comboBox;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(170, 30));
        label.setFont(new Font("Public Sans", Font.BOLD, 16));
        return label;
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

