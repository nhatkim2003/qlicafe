package com.coffee.GUI.DialogGUI;

import com.coffee.GUI.components.RoundedPanel;
import com.coffee.main.Cafe_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogFormDetail_1 extends JDialog {
    public RoundedPanel title;
    public RoundedPanel content;
    public RoundedPanel containerButton;
    protected RoundedPanel top;
    protected RoundedPanel center;
    protected RoundedPanel bottom;


    public DialogFormDetail_1() {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(255, 255, 255));
        setLayout(new MigLayout("", "[]", "[][]"));
        setIconImage(new FlatSVGIcon("image/coffee_logo.svg").getImage());
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Cafe_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        initComponents();
    }

    private void initComponents() {
//        title = new RoundedPanel();
        content = new RoundedPanel();
        containerButton = new RoundedPanel();
        top = new RoundedPanel();
        center = new RoundedPanel();
        bottom = new RoundedPanel();


//        title.setLayout(new BorderLayout());
//        title.setBackground(new Color(232, 206, 180));
//        title.setPreferredSize(new Dimension(1000, 70));
//        add(title, "wrap, span, center");

        content.setLayout(new MigLayout("", "[][]", "[][]"));
//        content.setBackground(new Color(217, 217, 217));
        content.setBackground(new Color(255, 255, 255));
        content.setPreferredSize(new Dimension(1000, 630));

        add(content, "wrap");

        containerButton.setLayout(new FlowLayout());
//        containerButton.setBackground(new Color(217, 217, 217));
        containerButton.setBackground(new Color(255, 255, 255));

        containerButton.setPreferredSize(new Dimension(1000, 70));
        add(containerButton);


        top.setLayout(new BorderLayout());
//        top.setBackground(new Color(217, 217, 217));
        top.setBackground(new Color(255, 255, 255));

        top.setPreferredSize(new Dimension(1000, 200));
        content.add(top, "wrap");

        center.setLayout(new BorderLayout());
        center.setBackground(new Color(255, 255, 255));

        center.setPreferredSize(new Dimension(1000, 70));
        content.add(center, "wrap");

        bottom.setLayout(new BorderLayout());
//        bottom.setBackground(new Color(217, 217, 217));
//        bottom.setBackground(new Color( 211, 227, 253));
        bottom.setBackground(new Color(255, 255, 255));


        bottom.setPreferredSize(new Dimension(1000, 330));
        content.add(bottom, "wrap");

    }

    public void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1)
            dispose();
    }

}

