package org.jreserve.chart;

import java.util.*;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Peter Decsi
 */
public class MultiSeriesScaledLineChart<R extends Comparable<R>, C extends Comparable<C>> extends MultiSeriesLineChart<R, C> {
    
    public MultiSeriesScaledLineChart(ChartData<R, C> chartData) {
        super(chartData);
    }
    
    public MultiSeriesScaledLineChart(ChartData<R, C> chartData, ColorGenerator colorGenerator) {
        super(chartData, colorGenerator);
    }

    @Override
    protected void updateDataset() {
        clearDataSet();
        
        List<SelectableSeries> visibles = getVisibleSeries();
        C scaleColumn = getScaleColumn(visibles);
        if(scaleColumn != null)
            setScaledValues(visibles, scaleColumn);
        
        fireChangeEvent();
    }
    
    private List<SelectableSeries> getVisibleSeries() {
        List<SelectableSeries> result = new ArrayList<SelectableSeries>();
        for(SelectableSeries serie : series)
            if(serie.isSelected())
                result.add(serie);
        return result;
    }
    
    private C getScaleColumn(List<SelectableSeries> visibles) {
        if(series.isEmpty()) return null;
        
        Set<C> columns = new TreeSet<C>(visibles.get(0).getValues().keySet());
        for(int i=1, size=visibles.size(); i<size; i++) {
            Set<C> keys = visibles.get(i).getValues().keySet();
            for(Iterator<C> it=columns.iterator(); it.hasNext();)
                if(!keys.contains(it.next()))
                    it.remove();
        }
        
        if(columns.isEmpty())
            return null;
        return (C) columns.toArray()[0];
    }   
    
    private void setScaledValues(List<SelectableSeries> visibles, C scaleColumn) {
        DefaultCategoryDataset ds = getDataSet();
        for(SelectableSeries serie : visibles) {
            setScaledValues(ds, serie, scaleColumn);
            formatSeries(serie.getRowKey());
        }
    }
    
    private void setScaledValues(DefaultCategoryDataset ds, SelectableSeries serie, C scaleColumn) {
        String rowName = chartData.getRowLabel(serie.getRowKey());
        Map<C, Double> values = serie.getValues();
        double scale = values.get(scaleColumn);

        for (C key : values.keySet())
            ds.addValue(values.get(key) / scale, rowName, chartData.getColumnLabel(key));
    }
}
