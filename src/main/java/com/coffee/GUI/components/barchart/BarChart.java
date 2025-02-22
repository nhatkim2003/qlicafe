package com.coffee.GUI.components.barchart;

import com.coffee.GUI.components.barchart.blankchart.BlankPlotBarChart;
import com.coffee.GUI.components.barchart.blankchart.BlankPlotBarChatRender;
import com.coffee.GUI.components.barchart.blankchart.BarChartSeriesSize;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BarChart extends javax.swing.JPanel {

    private List<ModelBarChartLegend> legends = new ArrayList<>();
    private List<ModelBarChart> model = new ArrayList<>();
    private final int seriesSize = 12;
    private final int seriesSpace = 6;
    private final Animator animator;
    private float animate;

    public BarChart() {
        initComponents();
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                repaint();
            }
        };
        animator = new Animator(800, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        blankPlotBarChart.setBlankPlotChatRender(new BlankPlotBarChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotBarChart chart, Graphics2D g2, BarChartSeriesSize size, int index) {
                double totalSeriesWidth = (seriesSize * legends.size()) + (seriesSpace * (legends.size() - 1));
                double x = (size.getWidth() - totalSeriesWidth) / 2;
                for (int i = 0; i < legends.size(); i++) {
                    ModelBarChartLegend legend = legends.get(i);
                    g2.setColor(legend.getColor());
                    double seriesValues = chart.getSeriesValuesOf(model.get(index).getValues()[i], size.getHeight()) * animate;
                    g2.fillRect((int) (size.getX() + x), (int) (size.getY() + size.getHeight() - seriesValues), seriesSize, (int) seriesValues);
                    x += seriesSpace + seriesSize;
                }
            }
        });
    }

    public void addLegend(String name, Color color) {
        ModelBarChartLegend data = new ModelBarChartLegend(name, color);
        legends.add(data);
        panelLegend.add(new LegendBarChartItem(data));
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(ModelBarChart data) {
        model.add(data);
        blankPlotBarChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotBarChart.getMaxValues()) {
            blankPlotBarChart.setMaxValues(max);
        }
    }

    public void clear() {
        animate = 0;
        blankPlotBarChart.setLabelCount(0);
        model.clear();
        repaint();
    }

    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blankPlotBarChart = new BlankPlotBarChart();
        panelLegend = new javax.swing.JPanel();

        setBackground(new Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                                        .addComponent(blankPlotBarChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(blankPlotBarChart, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(panelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private BlankPlotBarChart blankPlotBarChart;
    private javax.swing.JPanel panelLegend;
    // End of variables declaration//GEN-END:variables
}
