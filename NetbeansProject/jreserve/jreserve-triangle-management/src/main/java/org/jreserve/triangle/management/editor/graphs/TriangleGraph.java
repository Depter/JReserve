package org.jreserve.triangle.management.editor.graphs;

import java.awt.Image;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidgetListener;

/**
 *
 * @author Peter Decsi
 */
abstract class TriangleGraph extends NavigablePanel implements TriangleWidgetListener {

    protected final static java.awt.Color BACKGROUND = new java.awt.Color(255, 125, 48);
    private final static java.awt.Color PLOT_BACKGROUND = java.awt.Color.WHITE;
    
    protected TriangleWidget widget;
    protected JFreeChart chart;
    protected ChartPanel chartPanel;
    
    protected TriangleGraph(String displayName, Image img, TriangleWidget widget) {
        super(displayName, img);
        super.setBackground(BACKGROUND);
        setWidget(widget);
        initComponents();
    }
    
    private void setWidget(TriangleWidget widget) {
        this.widget = widget;
        this.widget.addTriangleWidgetListener(this);
    }
    
    protected void initComponents() {
        createChartPanel();
        super.setContent(chartPanel);
    }
    
    protected void createChartPanel() {
        chart = buildChart();
        formatPlot(chart.getPlot());
        chartPanel = new ChartPanel(chart);
        chartPanel.setPopupMenu(null);
    }
    
    protected void formatPlot(Plot plot) {
        plot.setBackgroundPaint(PLOT_BACKGROUND);
    }
    
    protected abstract JFreeChart buildChart();
    
    protected abstract void widgetChanged();
    
    @Override
    public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
        widgetChanged();
    }

    @Override
    public void valuesChanged() {
        widgetChanged();
    }

    @Override
    public void structureChanged() {
        widgetChanged();
    }

    @Override public void commentsChanged() {}
}
