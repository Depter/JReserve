package org.jreserve.chart;

import java.awt.BasicStroke;
import java.awt.Shape;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author Peter Decsi
 */
public class MultiSeriesLineChart<R extends Comparable<R>, C extends Comparable<C>> extends MultiSeriesCategoryChart<R, C> {
    private final static Shape SHAPE = ShapeUtilities.createDownTriangle(3);
    
    private LineAndShapeRenderer lineRenderer;
    
    public MultiSeriesLineChart(ChartData<R, C> chartData) {
        super(chartData);
    }
    
    public MultiSeriesLineChart(ChartData<R, C> chartData, ColorGenerator colorGenerator) {
        super(chartData, colorGenerator);
    }

    @Override
    protected JFreeChart buildChart() {
        return ChartFactory.createLineChart(
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
        
        CategoryPlot cPlot = (CategoryPlot) plot;
        NumberAxis axis = (NumberAxis) cPlot.getRangeAxis();
        axis.setAutoRange(true);
        axis.setAutoRangeIncludesZero(false);
    }
    
    @Override
    protected void formatSeries(R rowKey) {
        String rowName = chartData.getRowLabel(rowKey);
        int index = getDataSet().getRowIndex(rowName);
        if(index < 0) return;
        
        LineAndShapeRenderer renderer = getLineRenderer();
        if(renderer == null) return;
        
        renderer.setSeriesShapesFilled(index, true);
        renderer.setSeriesPaint(index, super.getColor(rowKey));
        renderer.setSeriesStroke(index, new BasicStroke(2));
        renderer.setSeriesShapesFilled(index, true);
        renderer.setSeriesShapesVisible(index, true);
        renderer.setSeriesShape(index, SHAPE);
    }

    private LineAndShapeRenderer getLineRenderer() {
        if(lineRenderer == null) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            lineRenderer = (LineAndShapeRenderer) plot.getRenderer();
        }
        return lineRenderer;
    }

}
