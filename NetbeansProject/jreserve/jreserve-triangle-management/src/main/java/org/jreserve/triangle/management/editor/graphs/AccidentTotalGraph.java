package org.jreserve.triangle.management.editor.graphs;

import java.util.*;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.AccidentTotalGraph.Title=Accident total"
})
public class AccidentTotalGraph extends TotalBarGraph {
    
    public AccidentTotalGraph(TriangleWidget widget) {
        super(Bundle.LBL_AccidentTotalGraph_Title(), widget);
    }
    
    @Override
    protected void addData(Map<Date, Double> data, TriangleCell cell) {
        Double value = cell.getDisplayValue();
        if(value == null || value.isNaN()) return;
        Date date = cell.getAccidentBegin();
        Double sum = data.get(date);
        data.put(date, sum(sum, value));
    }
    
    protected Double sum(Double sum, Double value) {
        if(sum == null || widget.isCummulated())
            return value;
        return sum + value;
    }
}
