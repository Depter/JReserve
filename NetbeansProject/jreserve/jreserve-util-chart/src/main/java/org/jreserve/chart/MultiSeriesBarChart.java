package org.jreserve.chart;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

/**
 *
 * @author Peter Decsi
 */
public class MultiSeriesBarChart<R extends Comparable<R>, C extends Comparable<C>> extends MultiSeriesCategoryChart<R, C> {

    private BarRenderer barRenderer;
    
    public MultiSeriesBarChart(ChartData<R, C> chartData) {
        super(chartData);
    }
    
    public MultiSeriesBarChart(ChartData<R, C> chartData, ColorGenerator colorGenerator) {
        super(chartData, colorGenerator);
    }

    @Override
    protected JFreeChart buildChart() {
        return ChartFactory.createBarChart(
                format.getChartTitle(), 
                format.getDomainAxisTitle(), 
                format.getRangeAxisTitle(), 
                super.getDataSet(), 
                format.getPlotOrientation(), 
                format.showLegend(), 
                format.showToolTip(), 
                format.showURLs());
    }

    @Override
    protected void formatPlot(Plot plot) {
        super.formatPlot(plot);
        CategoryPlot categoryPlot = (CategoryPlot) plot;
        
        barRenderer = (BarRenderer) categoryPlot.getRenderer();
        barRenderer.setBarPainter(new StandardBarPainter());
        
        CategoryAxis axis = categoryPlot.getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);       
    }
    
    @Override
    protected void formatSeries(R rowKey) {
        String key = chartData.getRowLabel(rowKey);
        int index = super.getDataSet().getRowIndex(key);
        Color color = super.getColor(rowKey);
        getBarRenderer().setSeriesPaint(index, color);
    }
    
    private BarRenderer getBarRenderer() {
        if(barRenderer == null) {
            CategoryPlot plot = (CategoryPlot) super.getJFreeChart().getPlot();
            barRenderer = (BarRenderer) plot.getRenderer();
        }
        return barRenderer;
    }

}
