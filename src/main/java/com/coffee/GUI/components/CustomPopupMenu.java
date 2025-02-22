package com.coffee.GUI.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class CustomPopupMenu extends JPopupMenu {
    public CustomPopupMenu() {
        // Tạo LayoutManager cho JPopupMenu
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        // Căn giữa các phần tử theo cả hai chiều X và Y
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);
    }
    public void addMenuItem(String text) {
                    JMenuItem menuItem = new JMenuItem(text);
                    menuItem.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều ngang
                    add(menuItem);
                }
}
