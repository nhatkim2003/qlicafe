package com.coffee.GUI.DialogGUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.coffee.GUI.components.RoundedPanel;
import com.coffee.main.Cafe_Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogForm extends JDialog {
    public RoundedPanel title;
    public RoundedPanel content;
    public RoundedPanel containerButton;

    public DialogForm() {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(242, 245, 250));

        setLayout(new MigLayout("", "50[]50", "10[]10"));
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
        title = new RoundedPanel();
        content = new RoundedPanel();
        containerButton = new RoundedPanel();

        title.setLayout(new BorderLayout());
        title.setBackground(new Color(242, 245, 250));
        title.setPreferredSize(new Dimension(1000, 70));
        add(title, "wrap, span, center");

        content.setLayout(new MigLayout("", "20[]20[]20", "20[]20[]20"));
        content.setBackground(new Color(255, 255, 255));
        content.setPreferredSize(new Dimension(1000, 530));
        add(content, "wrap");

        containerButton.setLayout(new FlowLayout());
        containerButton.setBackground(new Color(242, 245, 250));
        containerButton.setPreferredSize(new Dimension(1000, 100));
        add(containerButton, "wrap");

    }

    public void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        ;
        if (choice == 1)
            dispose();
    }


}
