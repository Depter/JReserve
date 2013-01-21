package org.jreserve.chart;

import java.awt.Color;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Peter Decsi
 */
public abstract class MultiSeriesCategoryChart<R extends Comparable<R>, C extends Comparable<C>> extends Chart implements ChangeListener {
    
    private final static java.awt.Color PLOT_GRID = new java.awt.Color(134, 134, 134);
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    protected ChartData<R, C> chartData;
    private DefaultCategoryDataset dataset;
    protected Set<SelectableSeries> series = new TreeSet<SelectableSeries>();
    
    private ColorGenerator colorGenerator;
    private Map<R, Color> colors = new HashMap<R, Color>();
    
    protected MultiSeriesCategoryChart(ChartData<R, C> chartData) {
        this(chartData, new DefaultColorGenerator());
    }
    
    protected MultiSeriesCategoryChart(ChartData<R, C> chartData, ColorGenerator colorGenerator) {
        super(chartData);
        this.chartData = chartData;
        this.chartData.addChangeListener(this);
        this.colorGenerator = colorGenerator==null? new DefaultColorGenerator() : colorGenerator;
        stateChanged(null);
    }
    
    @Override
    protected void formatPlot(Plot plot) {
        super.formatPlot(plot);
        
        CategoryPlot cPlot = (CategoryPlot) plot;
        cPlot.setRangeGridlinePaint(PLOT_GRID);
        allignLegend();
        
        format.formatPlot(plot);
    }
    
    private void allignLegend() {
        LegendTitle legend = chart.getLegend();
        if(legend != null)
            legend.setPosition(RectangleEdge.RIGHT);
    }
    
    @Override
    public void stateChanged(ChangeEvent evt) {
        Map<R, Map<C, Double>> data = chartData.getData();
        setSeries(data.keySet());
        setSeriesData(data);
        updateDataset();
    }
    
    protected DefaultCategoryDataset getDataSet() {
        if(dataset == null)
            dataset = new DefaultCategoryDataset();
        return dataset;
    }
    
    protected void clearDataSet() {
        getDataSet().clear();
    }
    
    private void setSeries(Set<R> rowKeys) {
        removeDeletedSeries(rowKeys);
        addNewSeries(rowKeys);
    }
    
    private void removeDeletedSeries(Set<R> rowKeys) {
        for(Iterator<SelectableSeries> it=series.iterator(); it.hasNext();)
            if(!rowKeys.contains(it.next().rowKey))
                it.remove();
    }
    
    private void addNewSeries(Set<R> rowKeys) {
        for(R rowKey : rowKeys)
            if(getSeries(rowKey) == null)
                series.add(new SelectableSeries(rowKey));
    }
    
    private SelectableSeries getSeries(R rowKey) {
        for(SelectableSeries serie : series)
            if(serie.rowKey.equals(rowKey))
                return serie;
        return null;
    }
    
    private void setSeriesData(Map<R, Map<C, Double>> data) {
        for(R rowKey : data.keySet()) {
            SelectableSeries serie = getSeries(rowKey);
            serie.setValues(data.get(rowKey));
        }
    }
    
    protected void updateDataset() {
        clearDataSet();
        for(SelectableSeries serie : series)
            updateDataset(serie);
        fireChangeEvent();
    }
    
    private void updateDataset(SelectableSeries serie) {
        if(serie.selected && !serie.values.isEmpty()) {
            addValues(serie);
            formatSeries(serie.rowKey);
        }
    }
    
    private void addValues(SelectableSeries series) {
        String rowName = chartData.getRowLabel(series.rowKey);
        for(C columnKey : series.values.keySet()) {
            String columnName = chartData.getColumnLabel(columnKey);
            dataset.addValue(series.values.get(columnKey), rowName, columnName);
        }
    }
    
    protected abstract void formatSeries(R rowKey);
    
    public boolean isSeriesShown(R rowKey) {
        SelectableSeries serie = getSeries(rowKey);
        return serie != null && serie.selected;
    }
    
    public void setSeriesShown(R rowKey, boolean show) {
        SelectableSeries serie = getSeries(rowKey);
        if(serie != null && serie.selected!=show) {
            serie.selected = show;
            updateDataset();
        }
            
    }
    
    protected Color getColor(R rowKey) {
        Color color = colors.get(rowKey);
        if(color == null) {
            color = colorGenerator.nextColor();
            colors.put(rowKey, color);
        }
        return color;
    }
    
    protected void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    protected void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    protected class SelectableSeries implements Comparable<SelectableSeries> {
        
        private R rowKey;
        private boolean selected = true;
        private Map<C, Double> values = new TreeMap<C, Double>();

        SelectableSeries(R rowKey) {
            this.rowKey = rowKey;
            if(!colors.containsKey(rowKey))
                colors.put(rowKey, colorGenerator.nextColor());
        }
        
        void setValues(Map<C, Double> values) {
            this.values.clear();
            if(values != null)
                this.values.putAll(values);
        }

        public R getRowKey() {
            return rowKey;
        }
        
        public String getName() {
            return chartData.getRowLabel(rowKey);
        }
        
        public boolean isSelected() {
            return selected;
        }

        public Map<C, Double> getValues() {
            return values;
        }
        
        @Override
        public int compareTo(SelectableSeries o) {
            if(o == null) return -1;
            if(this == o) return 0;
            if(getClass().equals(o.getClass()))
                return rowKey.compareTo(((SelectableSeries)o).rowKey);
            return -1;
        }
        
        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(this == o) return true;
            if(getClass().equals(o.getClass()))
                return compareTo((SelectableSeries)o) == 0;
            return false;
        }
        
        @Override
        public int hashCode() {
            return rowKey.hashCode();
        }
    }
}
