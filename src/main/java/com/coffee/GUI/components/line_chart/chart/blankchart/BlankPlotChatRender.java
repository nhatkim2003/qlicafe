package com.coffee.GUI.components.line_chart.chart.blankchart;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public abstract class BlankPlotChatRender {

    public abstract String getLabelText(int index);

    public abstract void renderGraphics(BlankPlotLineChart chart, Graphics2D g2, Rectangle2D rectangle);

    public abstract void mouseMove(Rectangle2D rectangle, MouseEvent mouse);
}
