package com.coffee.GUI.components;//package com.coffee.GUI.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseListener;

public class CustomPanelRenderer extends DefaultTableCellRenderer {
    private boolean flag = true;
    private static final Color GRID_COLOR = Color.GRAY;
    private static final int GRID_THICKNESS = 1;

    public CustomPanelRenderer() {
    }

    public CustomPanelRenderer(boolean flag) {
        this.flag = flag;
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Border border;
        if (row < table.getRowCount() - 1) {
            border = BorderFactory.createMatteBorder(0, 0, GRID_THICKNESS, 0, GRID_COLOR);
        } else {
            border = BorderFactory.createEmptyBorder();
        }
        if (value instanceof String) {
            JLabel label = new JLabel((String) value);
//            label.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa nội dung
            if (flag)
                label.setBorder(border);
            return label;
        }
        if (value instanceof CustomPopupMenu) {

            CustomPopupMenu popupMenu = (CustomPopupMenu) value;
            if (flag)
                popupMenu.setBorder(border);
            return popupMenu;
        }
        if (value instanceof JLabel) {
            JLabel label = (JLabel) value;
            if (flag)
                label.setBorder(border);
            return label;
        }
        return (Component) value;
    }
}

