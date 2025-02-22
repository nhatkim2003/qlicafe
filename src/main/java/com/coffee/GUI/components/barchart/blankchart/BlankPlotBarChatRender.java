package com.coffee.GUI.components.barchart.blankchart;

import java.awt.*;

public abstract class BlankPlotBarChatRender {

    public abstract String getLabelText(int index);

    public abstract void renderSeries(BlankPlotBarChart chart, Graphics2D g2, BarChartSeriesSize size, int index);
}
