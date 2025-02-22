package com.coffee.GUI.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private static final Color GRID_COLOR = Color.GRAY;
    private static final int GRID_THICKNESS = 1;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ((JLabel) cellComponent).setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa nội dung

        // Thêm border cho các hàng, trừ hàng cuối cùng
        Border border;
        if (row < table.getRowCount() - 1) {
            border = BorderFactory.createMatteBorder(0, 0, GRID_THICKNESS, 0, GRID_COLOR);
        } else {
            border = BorderFactory.createEmptyBorder();
        }
        ((JComponent) cellComponent).setBorder(border);


        return cellComponent;
    }
}