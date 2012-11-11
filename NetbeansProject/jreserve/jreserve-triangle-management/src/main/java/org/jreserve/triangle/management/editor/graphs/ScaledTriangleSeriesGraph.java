package org.jreserve.triangle.management.editor.graphs;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 */
abstract class ScaledTriangleSeriesGraph<R extends Comparable<R>, C extends Comparable<C>> extends TriangleSeriesGraph<R, C> {

    protected ScaledTriangleSeriesGraph(String displayName, TriangleWidget widget) {
        super(displayName, widget);
    }
    
    
    @Override
    protected void addSelectedValues() {
        dataset.clear();
        C scaleColumn = getScaleColumn();
        if(scaleColumn == null) return;
        
        for(SeriesCheckBox check : visibleChecks) {
            String rowName = getRowName(check.rowKey);
            double scale = check.values.get(scaleColumn);
            for(C key : check.values.keySet()) {
                dataset.addValue(check.values.get(key)/scale, rowName, getColumnName(key));
            }
        }
        
        if(chart != null)
            formatSeries();        
    }
    
    private C getScaleColumn() {
        if(visibleChecks.isEmpty())
            return null;
        Set<C> columns = new TreeSet<C>(visibleChecks.get(0).values.keySet());
        for(int i=1, size=visibleChecks.size(); i<size; i++) {
            Set<C> keys = visibleChecks.get(i).values.keySet();
            for(Iterator<C> it=columns.iterator(); it.hasNext();)
                if(!keys.contains(it.next()))
                    it.remove();
        }
        
        if(columns.isEmpty())
            return null;
        return (C) columns.toArray()[0];
    }   
}
