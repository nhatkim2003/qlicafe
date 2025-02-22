package com.coffee.GUI.DialogGUI.FormAddGUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.BLL.RecipeBLL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Product;
import com.coffee.DTO.Recipe;
import com.coffee.GUI.DialogGUI.DialogFormDetail_1;
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

import static java.awt.Cursor.*;

public class AddProductGUI extends DialogFormDetail_1 {
    private final MaterialBLL materialBLL = new MaterialBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final RecipeBLL recipeBLL = new RecipeBLL();
    private final List<Recipe> recipeList = new ArrayList<>();

    private final List<Product> productList = new ArrayList<>();
    private JLabel lblImage;
    private DataTable dataTable;

    private JTextField txtNameProduct;
    private JComboBox<String> cbSize;
    private JPanel panelSize;
    private JTextField txtPrice;
    private JTextField txtCapitalPrice;
    private JTextField txtCategory;
    private JTextField txtQuantity;
    private JTextField txtUnit;
    private final JPopupMenu menu;
    private final PanelSearch search;
    private JTextField txtSearch;
    private JLabel selectedLabel;
    private final int productID = productBLL.getAutoID(productBLL.searchProducts());

    private int materialID;
    private String imageProduct = "productDefault";
    private final JLabel iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));


    public AddProductGUI() {
        super();
        super.setTitle("Thêm sản phẩm");
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
                Material material = materialBLL.findMaterialsBy(Map.of("name", data.getText())).get(0);
                materialID = material.getId();
                String unit = material.getUnit();
                if (unit.equals("kg")) {
                    txtUnit.setText("g");
                } else if (unit.equals("lít")) {
                    txtUnit.setText("ml");
                } else {
                    txtUnit.setText(unit);
                }

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

        top.setLayout(new FlowLayout());

        RoundedPanel containerAtributeProduct = new RoundedPanel();
        containerAtributeProduct.setLayout(new MigLayout("", "[]40[][][]100", "10[]10[]10[]10"));

        containerAtributeProduct.setBackground(new Color(255, 255, 255));
        containerAtributeProduct.setPreferredSize(new Dimension(600, 200));

        RoundedPanel containerImage = new RoundedPanel();
        containerImage.setLayout(new MigLayout("", "[]", "[][]"));
//        containerImage.setBackground(new Color(217, 217, 217));
        containerImage.setBackground(new Color(255, 255, 255));
        containerImage.setPreferredSize(new Dimension(200, 200));

        JPanel PanelImage = new JPanel();
        PanelImage.setPreferredSize(new Dimension(160, 160));

        PanelImage.setBackground(new Color(228, 231, 235));

        lblImage = new JLabel();
        PanelImage.add(lblImage);


        JButton btnImage = new JButton("Thêm ảnh");
        btnImage.setPreferredSize(new Dimension(100, 30));
        btnImage.setCursor(new Cursor(HAND_CURSOR));
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

        JLabel lblName = createLabel("Tên sản phẩm");
        JLabel lblCategory = createLabel("Thể loại");
        JLabel lblSize = createLabel("Size");
        JLabel lblPrice = createLabel("Giá bán");
        JLabel lblCapitalPrice = createLabel("Giá Vốn");

        txtNameProduct = new MyTextFieldUnderLine();
        txtCategory = new MyTextFieldUnderLine();

        panelSize = new JPanel();
        panelSize = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelSize.setBackground(new Color(255, 255, 255));
        panelSize.setPreferredSize(new Dimension(350, 30));

        cbSize = createComboBox();

        String[] sizeOptions = {"Chọn size", "S", "M", "L"};
        for (String size : sizeOptions)
            cbSize.addItem(size);

        cbSize.addActionListener(e -> {
            String selectedSize = (String) cbSize.getSelectedItem();
            assert selectedSize != null;

            if (!selectedSize.equals("Chọn size")) {
                if (selectedLabel != null) {
                    selectedLabel.setBackground(Color.WHITE);
                    selectedLabel.setForeground(Color.BLACK);
                }

                productList.add(new Product(productID, "", selectedSize, "", 0, 0, "", false));
                JLabel label = createSize(selectedSize);
                selectedLabel = label;
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
                        ;
                        loadCapitalPriceBySize(selectedSize);
                        loadRecipeBySize(selectedSize);

                    }
                });
                panelSize.add(label);
                cbSize.removeItem(selectedSize);
                panelSize.revalidate();
                panelSize.repaint();
                resetTxt();
            }
        });


        JButton deleteSize = getjButton();

        txtPrice = new MyTextFieldUnderLine();
        txtPrice.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Pair<Boolean, String> result = updatePriceProductBySize();
                if (!result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        txtCapitalPrice = new MyTextFieldUnderLine();
        txtCapitalPrice.setFocusable(false);

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
        btnAddMaterial.setCursor(new Cursor(HAND_CURSOR));
        btnAddMaterial.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new AddMaterialGUI();
            }
        });
        containerInforMaterial.add(btnAddMaterial);
        bottom.add(containerInforMaterial, BorderLayout.NORTH);

        JLabel lblNameMaterial = createLabelMaterial("Tên nguyên liệu");


        txtSearch = new MyTextFieldUnderLine();
        txtSearch.setPreferredSize(new Dimension(310, 30));

        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked();
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
        btnThem.setCursor(new Cursor(HAND_CURSOR));

        btnThem.addActionListener(e -> addMaterialTemp());

        containerInforMaterial.add(lblNameMaterial);
        containerInforMaterial.add(txtSearch);
        containerInforMaterial.add(lblQuantity);
        containerInforMaterial.add(txtQuantity);
        containerInforMaterial.add(lblUnit);
        containerInforMaterial.add(txtUnit);
        containerInforMaterial.add(btnThem);

        String[] columnNames = new String[]{"ID", "Tên nguyên liệu", "Đơn vị", "Giá vốn", "SL", "T.Tiền"};
        columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
        columnNames[columnNames.length - 1] = "Xóa";
        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(), e -> {
        }, false, true, true, 6, true, 4);
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
        JButton buttonCancel = new JButton("Huỷ");

        buttonCancel.setPreferredSize(new Dimension(100, 35));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(HAND_CURSOR));

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


        JButton buttonAdd = getButton();
        containerButton.add(buttonAdd);
        containerButton.add(buttonCancel);
    }

    private JButton getButton() {
        JButton buttonAdd = new JButton("Thêm");
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(new Color(255, 255, 255));
        buttonAdd.setPreferredSize(new Dimension(100, 35));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Pair<Boolean, String> result = checkSizeRecipeEmpty();
                if (!result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                result = addAllProducts();
                if (!result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                addAllRecipe();
            }
        });
        return buttonAdd;
    }

    private JButton getjButton() {
        JButton deleteSize = new JButton();
        deleteSize.setPreferredSize(new Dimension(30, 30));
        deleteSize.setBackground(new Color(255, 255, 255));
        deleteSize.setBorder(null);
        deleteSize.setCursor(new Cursor(HAND_CURSOR));
        ImageIcon iconRemove = new FlatSVGIcon("icon/remove.svg");
        Image imageRemove = iconRemove.getImage();
        Image newImgRemove = imageRemove.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        iconRemove = new ImageIcon(newImgRemove);
        deleteSize.setIcon(iconRemove);
        deleteSize.addActionListener(e -> {
            if (selectedLabel != null) {
                String labelText = selectedLabel.getText();
                removeSizeProduct(labelText);
                panelSize.remove(selectedLabel);
                cbSize.addItem(labelText);
                panelSize.revalidate();
                panelSize.repaint();
                resetTxt();
                selectedLabel = null;
            } else {
                JOptionPane.showMessageDialog(null, "Chọn size muốn xóa");
            }
        });
        return deleteSize;
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
            return new Pair<>(false, "Vui lòng chọn size");
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
                    return new Pair<>(true, "");
                }
            }
        }
        return new Pair<>(true, "");
    }


    private String uploadImage() {
        File selectedFile = Resource.chooseProductImageFile(this);
        if (selectedFile != null) {

            String imageFile = "SP" + productID + ".svg";
            String imageName = "SP" + productID;
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
        Recipe recipe = new Recipe(productID, materialID, Double.parseDouble(quantity), size, unit);
        if (!checkRecipeExist(recipe)) {
            recipeList.add(recipe);
            addDataToTable(materialID, quantity, unit);
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

    private void addDataToTable(int materialID, String quantity, String unit) {
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
            txtCapitalPrice.setText(VNString.currency(totalAmount));

        } else {
            double capitalPrice = VNString.parseCurrency(txtCapitalPrice.getText());
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
                double totalAmount = VNString.parseCurrency(model.getValueAt(indexRow, 5).toString());
                double capitalPrice = VNString.parseCurrency(txtCapitalPrice.getText());
                txtCapitalPrice.setText(VNString.currency(capitalPrice - totalAmount));
                while (iterator.hasNext()) {
                    Recipe recipe = iterator.next();
                    if (recipe.getProduct_id() == productID && recipe.getMaterial_id() == id && recipe.getSize().equals(labelText)) {
                        iterator.remove();
                    }
                }
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
        txtPrice.setText("");
        txtCapitalPrice.setText("");
        txtSearch.setText("");
        txtQuantity.setText("");
        txtUnit.setText("");
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
    }

    private void loadRecipeBySize(String size) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        double capitalPrice = 0.0;
        for (Recipe recipe : recipeList) {
            if (recipe.getSize().equals(size)) {
                Material material = materialBLL.findMaterialsBy(Map.of("id", recipe.getMaterial_id())).get(0);
                String materialPrice = VNString.currency(material.getUnit_price() / 1000);
                double materialPriceD = material.getUnit_price() / 1000;
                double totalAmount = materialPriceD * recipe.getQuantity();
                capitalPrice += totalAmount;
            }
        }
        txtSearch.setText("");
        txtQuantity.setText("");
        txtUnit.setText("");
        txtCapitalPrice.setText(VNString.currency(capitalPrice));
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

    private void loadPriceBySize(String size) {
        if (!productList.isEmpty()) {
            for (Product product : productList) {
                if (product.getSize().equals(size)) {
                    String price = product.getPrice().toString();
                    if (price.equals("0.0")) {
                        txtPrice.setText("");
                    } else
                        txtPrice.setText(VNString.currency(product.getPrice()));
                    return;
                }
            }
        }

    }

    private void loadCapitalPriceBySize(String size) {
        if (!productList.isEmpty()) {
            for (Product product : productList) {
                if (product.getSize().equals(size)) {
                    String capital_price = String.valueOf(product.getCapital_price());

                    if (capital_price.equals("0.0"))
                        txtCapitalPrice.setText("");
                    else
                        txtCapitalPrice.setText(VNString.currency(product.getCapital_price()));
                    return;
                }
            }
        }

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


    private JLabel createSize(String size) {
        JLabel label = new JLabel(size);
        label.setPreferredSize(new Dimension(30, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFocusable(true);
        label.setCursor(new Cursor(HAND_CURSOR));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.setBackground(new Color(59, 130, 198));
        return label;
    }

    private Pair<Boolean, String> addAllProducts() {
        boolean allProductsAdded = true;
        String name = txtNameProduct.getText();
        String category = txtCategory.getText();
        Pair<Boolean, String> result = productBLL.validateName(name);
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }
        result = productBLL.validateCategory(category);
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        for (Product product : productList) {
            product.setName(name);
            product.setCategory(category);
            product.setImage(imageProduct);
            double capitalPrice = SetCapitalBySize(product.getSize());
            product.setCapital_price(capitalPrice);
            result = productBLL.addProduct(product);

            if (!result.getKey()) {
                allProductsAdded = false;
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        if (allProductsAdded) {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            loadSaleGUI();
        }
        return new Pair<>(true, "");
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

    private void addAllRecipe() {
        for (Recipe recipe : recipeList) {
            Pair<Boolean, String> result = recipeBLL.addRecipe(recipe);
            if (!result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    private void txtSearchMouseClicked() {
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

