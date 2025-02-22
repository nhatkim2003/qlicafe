package com.coffee.GUI.DialogGUI.FormAddGUI;


import com.coffee.BLL.MaterialBLL;
import com.coffee.BLL.ProductBLL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Product;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.HomeGUI;
import com.coffee.GUI.SaleGUI;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.swing.DataSearch;
import com.coffee.GUI.components.swing.EventClick;
import com.coffee.GUI.components.swing.PanelSearch;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.main.Cafe_Application;
import com.coffee.utils.Resource;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.awt.Cursor.*;

public class AddProductGUI1 extends DialogForm {
    private final MaterialBLL materialBLL = new MaterialBLL();
    private final ProductBLL productBLL = new ProductBLL();

    private JLabel lblImage;
    private JTextField txtPrice;
    private JTextField txtCapitalPrice;
    private JTextField txtCategory;
    ;
    private final JPopupMenu menu;
    private final PanelSearch search;
    private JTextField txtSearch;

    private String imageProduct = "productDefault";

    private final int productID = productBLL.getAutoID(productBLL.searchProducts());

    public AddProductGUI1() {
        super();
        super.setTitle("Thêm sản phẩm bán trực tiếp");
        super.setSize(new Dimension(1000, 350));
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
                Material material = materialBLL.findMaterialsBy(Map.of("name", data.getText())).get(0);
                txtCapitalPrice.setText(material.getUnit_price() + "");
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

        content.setLayout(new FlowLayout());

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


        lblImage = new JLabel();
        PanelImage.add(lblImage);


        JButton btnImage = new JButton("Thêm ảnh");
        btnImage.setPreferredSize(new Dimension(100, 30));
        btnImage.setCursor(new Cursor(HAND_CURSOR));
        btnImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                imageProduct = uploadImage();
            }
        });
        containerImage.add(PanelImage, "alignx center,wrap");
        containerImage.add(btnImage, "alignx center");

        content.add(containerAtributeProduct);
        content.add(containerImage);
        EmptyBorder emptyBorder = new EmptyBorder(0, 30, 0, 0);
        content.setBorder(emptyBorder);

        JLabel lblName = createLabel("Tên sản phẩm");
        JLabel lblCategory = createLabel("Thể loại");
        JLabel lblPrice = createLabel("Giá bán");
        JLabel lblCapitalPrice = createLabel("Giá Vốn");

        txtSearch = new MyTextFieldUnderLine();
        txtCategory = new MyTextFieldUnderLine();

        txtPrice = new MyTextFieldUnderLine();
        txtCapitalPrice = new MyTextFieldUnderLine();
        txtCapitalPrice.setFocusable(false);
        txtSearch = new MyTextFieldUnderLine();
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

        containerAtributeProduct.add(lblName);
        containerAtributeProduct.add(txtSearch, "wrap");

        containerAtributeProduct.add(lblCategory);
        containerAtributeProduct.add(txtCategory, "wrap");


        containerAtributeProduct.add(lblPrice);
        containerAtributeProduct.add(txtPrice, "wrap");

        containerAtributeProduct.add(lblCapitalPrice);
        containerAtributeProduct.add(txtCapitalPrice, "wrap");


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


        JButton buttonAdd = new JButton("Thêm");
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(new Color(255, 255, 255));
        buttonAdd.setPreferredSize(new Dimension(100, 35));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addProduct();

            }
        });
        containerButton.add(buttonAdd);
        containerButton.add(buttonCancel);
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
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(170, 30));
        label.setFont(new Font("Public Sans", Font.BOLD, 16));
        return label;
    }

    private void addProduct() {
        String error = "";
        String product_name = txtSearch.getText();
        String category = txtCategory.getText();
        String price = txtPrice.getText();
        String capital_price = txtCapitalPrice.getText();

        Pair<Boolean, String> result;
        result = productBLL.validateName(product_name);
        if (!result.getKey()) {
            error += result.getValue() + "\n";
        }
        result = productBLL.validateCategory(category);
        if (!result.getKey()) {
            error += result.getValue() + "\n";
        }
        result = productBLL.validatePrice(price, "Giá bán");
        if (!result.getKey()) {
            error += result.getValue() + "\n";
        }
        List<Product> proList = productBLL.searchProducts("name = '" + product_name + "'", "deleted = 0");
        if(!proList.isEmpty()){
            error += "Sản phẩm đã tồn tại trong hệ thống\n";
        }
        List<Material> materialList = materialBLL.searchMaterials("name = '" + product_name + "'","sell = 1", "deleted = 0");
        if(materialList.isEmpty()){
            error += "Nguyên liệu này không tồn tại trong kho\n";
        }

        if (error.isEmpty()) {
            double priceDoule = Double.parseDouble(price);
            double capital_priceDouble = Double.parseDouble(capital_price);
            Product product = new Product(productID, product_name, "Không", category, priceDoule, capital_priceDouble, imageProduct, false);
            result = productBLL.addProduct(product);
            if (!result.getKey())
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                loadSaleGUI();
            }
        } else {
            JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);

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

        List<Material> materials1 = materialBLL.findMaterialsBySell("name", text, true);
        List<Material> materials2 = new ArrayList<>();
        for (Material m : materials1) {
              List<Product> products = productBLL.searchProducts("name = '" + m.getName()+"'");
              if(products.isEmpty()){
                  materials2.add(m);
              }
        }
        for (Material m : materials2) {
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

