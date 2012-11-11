package org.jreserve.triangle.management.editor.graphs;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
abstract class TotalBarGraph extends TriangleGraph {
    
    private final static java.awt.Image IMG = ImageUtilities.loadImage("resources/chart_bar.png");

    private final static java.awt.Color BAR_COLOR = new java.awt.Color(79, 129, 189);
    private final static java.awt.Color PLOT_GRID = new java.awt.Color(134, 134, 134);
    
    private DefaultCategoryDataset dataset;
    private DateRenderer renderer;
    
    protected TotalBarGraph(String displayName, TriangleWidget widget) {
        super(displayName, IMG, widget);
    }

    @Override
    protected void formatPlot(Plot plot) {
        super.formatPlot(plot);
        CategoryPlot categoryPlot = (CategoryPlot) plot;
        categoryPlot.setRangeGridlinePaint(PLOT_GRID);
        
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setSeriesPaint(0, BAR_COLOR);
        
        CategoryAxis axis = categoryPlot.getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);       
    }
    
    @Override
    protected JFreeChart buildChart() {
        createDataset();
        return ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
    }
    
    private void createDataset() {
        Map<Date, Double> data = createData();
        dataset = getDataSet();
        renderer = getDateRenderer();
        
        dataset.clear();
        for(Date date : data.keySet())
            dataset.addValue(data.get(date), "NAME", renderer.toString(date));
    }
    
    protected Map<Date, Double> createData() {
        Map<Date, Double> data = new TreeMap<Date, Double>();
        for(TriangleCell cell : widget.getCells())
            addData(data, cell);
        return data;
    }
    
    protected abstract void addData(Map<Date, Double> data, TriangleCell cell);
    
    protected DefaultCategoryDataset getDataSet() {
        if(dataset == null)
            dataset = new DefaultCategoryDataset();
        return dataset;
    }
    
    protected DateRenderer getDateRenderer() {
        if(renderer == null)
            renderer = new DateRenderer();
        return renderer;
    }
    
    @Override
    protected void widgetChanged() {
        createDataset();
    }
}
