package org.jreserve.triangle.management.editor.graphs;

import java.util.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ScaledDevelopmentPeriodsGraph.Title=Scaled development periods",
    "LBL.ScaledDevelopmentPeriodsGraph.Axis=Accident periods"
})
public class ScaledDevelopmentPeriodsGraph extends ScaledTriangleSeriesGraph<Integer, Date> {
    
    private DateRenderer dateRenderer;
    
    public ScaledDevelopmentPeriodsGraph(TriangleWidget widget) {
        super(Bundle.LBL_ScaledDevelopmentPeriodsGraph_Title(), widget);
    }
    
    @Override
    protected String getDomainAxisName() {
        return Bundle.LBL_ScaledDevelopmentPeriodsGraph_Axis();
    }
    
    @Override
    protected void formatPlot(Plot plot) {
        super.formatPlot(plot);
        CategoryAxis axis = ((CategoryPlot) plot).getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);       
    }
    
    @Override
    protected Map<Integer, Map<Date, Double>> createData() {
        List<Date> accidents = getAccidentDates();
        double[][] data = widget.flatten();
        int columnCount = getColumnCount(data);
        return createData(columnCount, data, accidents);
    }
    
    private List<Date> getAccidentDates() {
        Set<Date> dates = new TreeSet<Date>();
        for(TriangleCell cell : widget.getCells())
            if(cell != null)
                dates.add(cell.getAccidentBegin());
        return new ArrayList<Date>(dates);
    }

    private int getColumnCount(double[][] data) {
        int count = -1;
        for(double[] row : data) {
            if(count < row.length)
                count = row.length;
        }
        return count;   
    }
    
    private Map<Integer, Map<Date, Double>> createData(int columnCount, double[][] data, List<Date> accidents) {
        Map<Integer, Map<Date, Double>> result = new TreeMap<Integer, Map<Date, Double>>();
        for(int c=0; c<columnCount; c++)
            result.put((c+1), createColumn(c, data, accidents));
        return result;
    }
    
    private Map<Date, Double> createColumn(int column, double[][] data, List<Date> accidents) {
        Map<Date, Double> result = new TreeMap<Date, Double>();
        for(int r=0, size=data.length; r<size; r++)
            if(data[r].length > column)
                result.put(accidents.get(r), data[r][column]);
        return result;
    }
    
    @Override
    protected String getRowName(Integer rowKey) {
        return ""+rowKey.intValue();
    }

    @Override
    protected String getColumnName(Date columnKey) {
        return getDateRenderer().toString(columnKey);
    }
    
    private DateRenderer getDateRenderer() {
        if(dateRenderer == null)
            dateRenderer = new DateRenderer();
        return dateRenderer;
    }
}
