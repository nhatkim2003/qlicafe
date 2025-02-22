package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.RecipeBLL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Product;
import com.coffee.DTO.Recipe;
import com.coffee.GUI.DialogGUI.DialogFormDetail_1;
import com.coffee.GUI.components.DataTable;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.GUI.components.RoundedScrollPane;
import com.coffee.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class DetailProductGUI extends DialogFormDetail_1 {
    private final List<Product> Products ;
    private final int product_id;
    private final RecipeBLL recipeBLL = new RecipeBLL();
    private List<Recipe> recipes ;
    private final MaterialBLL materialBLL = new MaterialBLL();
    private final String imageProduct;

    private DataTable dataTable;

    private JTextField txtNameProduct;
    private JTextField txtPrice ;
    private JTextField txtCategory;
    private JTextField txtCapitalPrice;
    private JLabel selectedLabel;

    public DetailProductGUI(List<Product> Products) {
        super();
        super.setTitle("Chi tiết sản phẩm");
        this.Products = Products;
        this.product_id = Products.get(0).getId();
        this.imageProduct = Products.get(0).getImage();
        init(Products);
        setVisible(true);
    }

    public void init( List<Product> Products) {
        recipes=  recipeBLL.searchRecipes("product_id = " + product_id);

        content.setPreferredSize(new Dimension(1000,700));
        bottom.setPreferredSize(new Dimension(1000,500));
        containerButton.setPreferredSize(new Dimension(0,0));
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
        PanelImage.setBackground(new Color(255, 255, 255));

        JLabel lblImage = new JLabel();
        ImageIcon icon = new FlatSVGIcon("image/Product/" +imageProduct + ".svg");
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        lblImage.setIcon(icon);

        PanelImage.add(lblImage);

        containerImage.add(PanelImage, "alignx center,wrap");
        top.add(containerAtributeProduct);
        top.add(containerImage);
        EmptyBorder emptyBorder = new EmptyBorder(0, 30, 0, 0);
        top.setBorder(emptyBorder);

        String[] columnNames = new String[]{"ID", "Tên nguyên liệu", "Đơn vị", "Giá vốn", "SL", "T.Tiền"};
        dataTable = new DataTable(new Object[0][0], columnNames,
                null,
                false, false, false, 6);
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

        txtNameProduct.setFocusable(false);
        txtCategory.setFocusable(false);
        txtPrice.setFocusable(false);
        txtCapitalPrice.setFocusable(false);

        new JPanel();
        JPanel panelSize;
        panelSize = new JPanel(new FlowLayout(FlowLayout.LEFT,20,0));
        panelSize.setBackground(new Color(255, 255, 255));
        panelSize.setPreferredSize(new Dimension(350, 40));

        txtNameProduct.setText(Products.get(0).getName());
        txtCategory.setText(Products.get(0).getCategory());

        String defaultSize = "";
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

            if (defaultSize.isEmpty()) {
                defaultSize = size;
            }

            if (size.equals(defaultSize)) {
                selectedLabel = label;
                label.setBackground(new Color(59, 130, 198));
                label.setForeground(Color.WHITE);
                loadProductBySize(size);
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
                    loadProductBySize(size);
                }
            });

            panelSize.add(label);
            panelSize.revalidate();
            panelSize.repaint();
        }

        containerAtributeProduct.add(lblName);
        containerAtributeProduct.add(txtNameProduct,"wrap");

        containerAtributeProduct.add(lblCategory);
        containerAtributeProduct.add(txtCategory,"wrap");

        containerAtributeProduct.add(lblSize);
        containerAtributeProduct.add(panelSize,"wrap");

        containerAtributeProduct.add(lblPrice);
        containerAtributeProduct.add(txtPrice,"wrap");

        containerAtributeProduct.add(lblCapitalPrice);
        containerAtributeProduct.add(txtCapitalPrice,"wrap");

        JLabel lblListMaterial = new JLabel("Danh sách nguyên liệu");

        lblListMaterial.setFont(new Font("Public Sans", Font.BOLD, 18));

        center.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        center.add(lblListMaterial);

    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(170, 30));
        label.setFont(new Font("Public Sans", Font.PLAIN, 16));
        return label;
    }


    private void loadProductBySize(String size){
        for(Product product: Products){
            if(product.getSize().equals(size)){
                String name = product.getName();
                String category = product.getCategory();
                double price = product.getPrice();
                double capital_price = product.getCapital_price();
                txtNameProduct.setText(name);
                txtCategory.setText(category);
                txtPrice.setText(VNString.currency(price));
                txtCapitalPrice.setText(VNString.currency(capital_price));
                loadRecipeBySize(size);
            }
        }
    }
    private void loadRecipeBySize(String size) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        for (Recipe recipe : recipes) {
            if (recipe.getSize().equals(size)) {
                Material material = materialBLL.findMaterialsBy(Map.of("id", recipe.getMaterial_id())).get(0);
                String materialName = material.getName();
                String materialPrice = VNString.currency(material.getUnit_price()/1000);
                double materialPriceD = material.getUnit_price()/1000;
                double totalAmount = materialPriceD * recipe.getQuantity();
                Object[] rowData = {recipe.getMaterial_id(), materialName, recipe.getUnit(), materialPrice, recipe.getQuantity(), VNString.currency(totalAmount)};
                model.addRow(rowData);
            }
        }
    }
}
