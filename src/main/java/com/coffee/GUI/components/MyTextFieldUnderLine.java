package com.coffee.GUI.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MyTextFieldUnderLine extends JTextField {
    public MyTextFieldUnderLine() {
        init();
    }

    public void init() {
        setPreferredSize(new Dimension(300, 30));
        setFont(new Font("Public Sans", Font.PLAIN, 14));
        setBackground(new Color(255, 255, 255));
//        setHorizontalAlignment(SwingConstants.CENTER); -- tao tat

        setOpaque(false);

        Border bottomBorderBlack = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(163, 162, 149));
        Border bottomBorderGreen = BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(75, 172, 77));

        setBorder(bottomBorderBlack);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(bottomBorderGreen); // Khi focus vào, thay đổi màu border thành xanh lá cây
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(bottomBorderBlack); // Khi mất focus, thay đổi màu border về màu đen
            }
        });
    }
}
