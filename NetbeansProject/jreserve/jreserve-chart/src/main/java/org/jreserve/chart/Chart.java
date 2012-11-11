package org.jreserve.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

/**
 *
 * @author Peter Decsi
 */
public abstract class Chart {

    private final static java.awt.Color PLOT_BACKGROUND = java.awt.Color.WHITE;
    
    protected JFreeChart chart;
    protected ChartFormat format;
    
    protected Chart(ChartFormat format) {
        this.format = format;
        createChartPanel();
    }
    
    protected void createChartPanel() {
        chart = buildChart();
        formatPlot(chart.getPlot());
    }
    
    protected abstract JFreeChart buildChart();
    
    protected void formatPlot(Plot plot) {
        plot.setBackgroundPaint(PLOT_BACKGROUND);
    }
    
    public JFreeChart getJFreeChart() {
        return chart;
    }
}
