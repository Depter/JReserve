package org.jreserve.triangle.management.editor.graphs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
abstract class TriangleSeriesGraph<R extends Comparable<R>, C extends Comparable<C>> extends TriangleGraph {
    
    private final static java.awt.Image IMG = ImageUtilities.loadImage("resources/chart_xy.png");
    private final static java.awt.Color PLOT_GRID = new java.awt.Color(134, 134, 134);
    private final static Shape SHAPE = ShapeUtilities.createDownTriangle(3);
    
    private JSplitPane splitPane;
    private CheckBoxPanel checkPanel;
    
    protected DefaultCategoryDataset dataset;
    
    private List<SeriesCheckBox> seriesChecks;
    protected List<SeriesCheckBox> visibleChecks;
    
    private ColorGenerator colorGenerator;
    private Map<R, Color> colors;

    protected TriangleSeriesGraph(String displayName, TriangleWidget widget) {
        super(displayName, IMG, widget);
    }
    
    @Override
    protected void initComponents() {
        dataset = new DefaultCategoryDataset();
        seriesChecks = new ArrayList<SeriesCheckBox>();
        visibleChecks = new ArrayList<SeriesCheckBox>();
        colorGenerator = new ColorGenerator();
        colors = new HashMap<R, Color>();
        
        createCheckBoxPanel();
        createChartPanel();
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(checkPanel), chartPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.getComponent(2).setBackground(BACKGROUND);
        super.setContent(splitPane);
    }
    
    private void createCheckBoxPanel() {
        checkPanel = new CheckBoxPanel();
        checkPanel.setCheckBoxes(seriesChecks);
    } 

    @Override
    protected JFreeChart buildChart() {
        createDataset();
        return ChartFactory.createLineChart(null, getDomainAxisName(), null, dataset, PlotOrientation.VERTICAL, true, true, false);
    }
    
    protected abstract String getDomainAxisName();
    
    @Override
    protected void formatPlot(Plot plot) {
        super.formatPlot(plot);
        
        CategoryPlot cPlot = (CategoryPlot) plot;
        cPlot.setRangeGridlinePaint(PLOT_GRID);
        cPlot.setDomainGridlinePosition(CategoryAnchor.START);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        
        CategoryAxis axis = (CategoryAxis) cPlot.getDomainAxis();
        axis.setLowerMargin(0d);
        axis.setCategoryMargin(0d);
    }

    @Override
    protected void widgetChanged() {
        createDataset();
    }
    
    
    private void createDataset() {
        Map<R, Map<C, Double>> data = createData();
        setSeriesCheck(data.keySet());
        visibleChecks.clear();
        fillSeriesChecks(data);
        addSelectedValues();
    }
    
    protected abstract Map<R, Map<C, Double>> createData();
    
    private void setSeriesCheck(Set<R> rowKeys) {
        removeDeletedSeries(rowKeys);
        addNewSeries(rowKeys);
        Collections.sort(seriesChecks);
        checkPanel.setCheckBoxes(seriesChecks);
    }
    
    private void removeDeletedSeries(Set<R> rowKeys) {
        for(Iterator<SeriesCheckBox> it=seriesChecks.iterator(); it.hasNext();)
            if(!rowKeys.contains(it.next().rowKey))
                it.remove();
    }
    
    private void addNewSeries(Set<R> rowKeys) {
        for(R rowKey : rowKeys)
            if(getSeriesCheckBox(rowKey) == null)
                seriesChecks.add(new SeriesCheckBox(rowKey));
    }
    
    private SeriesCheckBox getSeriesCheckBox(R rowKey) {
        for(SeriesCheckBox check : seriesChecks)
            if(check.rowKey.equals(rowKey))
                return check;
        return null;
    }

    private void fillSeriesChecks(Map<R, Map<C, Double>> data) {
        for(SeriesCheckBox check : seriesChecks) {
            Map<C, Double> series = data.get(check.rowKey); 
            check.setData(series);
            if(check.isSelected())
                visibleChecks.add(check);
        }
    }
    
    protected void formatSeries() {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) plot.getRenderer();
        for(int i=0,size=visibleChecks.size(); i<size; i++)
            formatSeries(lineRenderer, i);
    }

    private void formatSeries(LineAndShapeRenderer lineRenderer, int index) {
        lineRenderer.setSeriesShapesFilled(index, true);
        lineRenderer.setSeriesPaint(index, visibleChecks.get(index).color);
        lineRenderer.setSeriesStroke(index, new BasicStroke(2));
        lineRenderer.setSeriesShapesFilled(index, true);
        lineRenderer.setSeriesShapesVisible(index, true);
        lineRenderer.setSeriesShape(index, SHAPE);
    }

    protected abstract String getRowName(R rowKey);
    
    protected abstract String getColumnName(C rowKey);
    
    protected void addSelectedValues() {
        dataset.clear();
        
        for(SeriesCheckBox check : visibleChecks) {
            String rowName = getRowName(check.rowKey);
            for(C key : check.values.keySet())
                dataset.addValue(check.values.get(key), rowName, getColumnName(key));
        }
        
        if(chart != null)
            formatSeries();        
    }
    
    protected class SeriesCheckBox extends JCheckBox implements Comparable<SeriesCheckBox>, ActionListener {
        
        protected R rowKey;
        protected Map<C, Double> values;
        private Color color;
        
        private SeriesCheckBox(R rowKey) {
            super(getRowName(rowKey));
            super.setSelected(true);
            super.addActionListener(this);
            this.rowKey = rowKey;
            this.values = new TreeMap<C, Double>();
            initColor();
        }
        
        private void initColor() {
            color = colors.get(rowKey);
            if(color == null) {
                color = colorGenerator.nextColor();
                colors.put(rowKey, color);
            }
        }
        
        void setData(Map<C, Double> data) {
            values.clear();
            values.putAll(data);
            
            if(super.isSelected())
                addValues();
        }
        
        private void addValues() {
            String rowName = getRowName(rowKey);
            for(C column : values.keySet())
                dataset.addValue(values.get(column), rowName, getColumnName(column));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(super.isSelected()) {
                int index = Collections.binarySearch(visibleChecks, this);
                visibleChecks.add(-(index+1), this);
                readdSeries();
            } else {
                visibleChecks.remove(this);
                dataset.removeRow(getRowName(rowKey));
            }
            formatSeries();
        }
        
        private void readdSeries() {
            dataset.clear();
            for(SeriesCheckBox check : visibleChecks)
                if(check.isSelected())
                    check.addValues();
        }
        
        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(this.getClass().equals(o.getClass()))
                return rowKey.compareTo(((SeriesCheckBox)o).rowKey) == 0;
            return false;
        }
        
        @Override
        public int hashCode() {
            return rowKey.hashCode();
        }

        @Override
        public int compareTo(SeriesCheckBox o) {
            if(o == null)
                return -1;
            return rowKey.compareTo(o.rowKey);
        }
    }
    
    private class CheckBoxPanel extends JPanel {
        
        private GridBagConstraints gc;
        
        private CheckBoxPanel() {
            super(new GridBagLayout());
            super.setBorder(new EmptyBorder(0, 5, 5, 5));
            gc = new GridBagConstraints();
            gc.gridy=0;gc.gridx=0;
            gc.weightx=1d; gc.weighty=0d;
            gc.anchor=GridBagConstraints.WEST;
            gc.fill = GridBagConstraints.VERTICAL;
            gc.insets = new java.awt.Insets(5, 0, 0, 0);
        }
        
        void setCheckBoxes(List<SeriesCheckBox> checks) {
            super.removeAll();
            for(int y=0, size=checks.size(); y<size; y++) {
                gc.gridy=y;
                super.add(checks.get(y), gc);
            }
            addGlue(checks.size());
            super.revalidate();
        }
        
        private void addGlue(int y) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridy=y;c.gridx=0;
            c.weightx=1d; c.weighty=1d;
            c.anchor=GridBagConstraints.NORTH;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new java.awt.Insets(0, 0, 0, 0);
            super.add(Box.createGlue(), c);
        }
    } 
}
