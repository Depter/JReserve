package org.jreserve.triangle.management.editor.graphs;

import java.util.Date;
import java.util.Map;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.CalendarTotalGraph.Title=Calendar year total"
})
public class CalendarTotalGraph extends TotalBarGraph {

    public CalendarTotalGraph(TriangleWidget widget) {
        super(Bundle.LBL_CalendarTotalGraph_Title(), widget);
    }
    
    @Override
    protected void addData(Map<Date, Double> data, TriangleCell cell) {
        Double value = cell.getDisplayValue();
        if(value == null || value.isNaN()) return;
        Date date = cell.getDevelopmentBegin();
        Double sum = data.get(date);
        data.put(date, sum==null? value : sum+value);
    }
}
