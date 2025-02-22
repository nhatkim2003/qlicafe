package com.coffee.GUI.DialogGUI.FormDetailGUI;

import com.coffee.DTO.Product;
import com.coffee.GUI.DialogGUI.DialogForm;
import com.coffee.GUI.components.MyTextFieldUnderLine;
import com.coffee.GUI.components.RoundedPanel;

import com.coffee.main.Cafe_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class DetailProductGUI1 extends DialogForm {

    public DetailProductGUI1(Product product) {
        super();
        super.setTitle("Sửa sản phẩm bán trực tiếp");
        super.setSize(new Dimension(1000, 350));
        super.setLocationRelativeTo(Cafe_Application.homeGUI);
        init(product);
        setVisible(true);

    }

    public void init(Product product) {

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
        PanelImage.setLayout(new MigLayout("", "[]", "[]"));
        PanelImage.setBackground(new Color(255, 255, 255));

        JLabel lblImage = new JLabel();
        ImageIcon iconProduct = new FlatSVGIcon("image/Product/" + product.getImage() + ".svg");
        Image imgProduct = iconProduct.getImage();
        Image newImgProduct = imgProduct.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
        iconProduct = new ImageIcon(newImgProduct);
        lblImage.setIcon(iconProduct);
        PanelImage.add(lblImage);

        containerImage.add(PanelImage, "alignx center,wrap");
        content.add(containerAtributeProduct);
        content.add(containerImage);
        EmptyBorder emptyBorder = new EmptyBorder(0, 30, 0, 0);
        content.setBorder(emptyBorder);

        JLabel lblName = createLabel("Tên sản phẩm");
        JLabel lblCategory = createLabel("Thể loại");
        JLabel lblPrice = createLabel("Giá bán");
        JLabel lblCapitalPrice = createLabel("Giá Vốn");

        JTextField txtSearch = new MyTextFieldUnderLine();
        txtSearch.setText(product.getName());
        JTextField txtCategory = new MyTextFieldUnderLine();
        txtCategory.setText(product.getCategory());
        JTextField txtPrice = new MyTextFieldUnderLine();
        txtPrice.setText(product.getPrice() + "");
        JTextField txtCapitalPrice = new MyTextFieldUnderLine();
        txtCapitalPrice.setText(product.getCapital_price() + "");
        txtCapitalPrice.setFocusable(false);


        containerAtributeProduct.add(lblName);
        containerAtributeProduct.add(txtSearch, "wrap");

        containerAtributeProduct.add(lblCategory);
        containerAtributeProduct.add(txtCategory, "wrap");


        containerAtributeProduct.add(lblPrice);
        containerAtributeProduct.add(txtPrice, "wrap");

        containerAtributeProduct.add(lblCapitalPrice);
        containerAtributeProduct.add(txtCapitalPrice, "wrap");

    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(170, 30));
        label.setFont(new Font("Public Sans", Font.BOLD, 16));
        return label;
    }


}

