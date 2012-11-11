package org.jreserve.triangle.management.editor.graphs;

import java.util.*;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ScaledAccidentPeriodsGraph.Title=Scaled Accident periods",
    "LBL.ScaledAccidentPeriodsGraph.Axis=Development period"
})
public class ScaledAccidentPeriodsGraph extends ScaledTriangleSeriesGraph<Date, Integer> {
    
    private DateRenderer dateRenderer;
    
    public ScaledAccidentPeriodsGraph(TriangleWidget widget) {
        super(Bundle.LBL_ScaledAccidentPeriodsGraph_Title(), widget);
    }
    
    @Override
    protected String getDomainAxisName() {
        return Bundle.LBL_ScaledAccidentPeriodsGraph_Axis();
    }
    
    @Override
    protected Map<Date, Map<Integer, Double>> createData() {
        List<Date> dates = getAccidentDates();
        double[][] data = widget.flatten();
        Map<Date, Map<Integer, Double>> result = new TreeMap<Date, Map<Integer, Double>>();
        
        for(int i=0, size=data.length; i<size; i++) {
            result.put(dates.get(i), getRowData(data[i]));
        }
        return result;
    }
    
    private List<Date> getAccidentDates() {
        Set<Date> dates = new TreeSet<Date>();
        for(TriangleCell cell : widget.getCells())
            if(cell != null)
                dates.add(cell.getAccidentBegin());
        return new ArrayList<Date>(dates);
    }

    private Map<Integer, Double> getRowData(double[] row) {
        Map<Integer, Double> data = new TreeMap<Integer, Double>();
        for(int i=0, size=row.length; i<size; i++)
            data.put(i+1, row[i]);            
        return data;
    }
    
    @Override
    protected String getRowName(Date rowKey) {
        return getDateRenderer().toString(rowKey);
    }
    
    private DateRenderer getDateRenderer() {
        if(dateRenderer == null)
            dateRenderer = new DateRenderer();
        return dateRenderer;
    }

    @Override
    protected String getColumnName(Integer rowKey) {
        return ""+rowKey.intValue();
    }


}
