package org.jreserve.triangle.widget.charts;

import java.util.*;
import org.jreserve.chart.ChartUtil;
import org.jreserve.chart.MultiSeriesLineChart;
import org.jreserve.chart.MultiSeriesScaledLineChart;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.AccidentPeriodsChartData.Title=Accident periods",
    "LBL.AccidentPeriodsChartData.Scaled.Title=Scaled accident periods",
    "LBL.AccidentPeriodsChartData.Axis=Development period"
})
public class AccidentPeriodsChartData extends TriangleChartData<Date, Integer> {

    public static NavigablePanel createPanel(TriangleWidget widget) {
        AccidentPeriodsChartData data = new AccidentPeriodsChartData(widget);
        MultiSeriesLineChart<Date, Integer> chart = new MultiSeriesLineChart<Date, Integer>(data);
        String title = Bundle.LBL_AccidentPeriodsChartData_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }

    public static NavigablePanel createScaledPanel(TriangleWidget widget) {
        AccidentPeriodsChartData data = new AccidentPeriodsChartData(widget);
        MultiSeriesScaledLineChart<Date, Integer> chart = new MultiSeriesScaledLineChart<Date, Integer>(data);
        String title = Bundle.LBL_AccidentPeriodsChartData_Scaled_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }

    private DateRenderer renderer = new DateRenderer();
    
    private AccidentPeriodsChartData(TriangleWidget widget) {
        super(widget);
    }
    
    @Override
    public Map<Date, Map<Integer, Double>> getData() {
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
    public String getRowLabel(Date rowKey) {
        return renderer.toString(rowKey);
    }

    @Override
    public String getDomainAxisTitle() {
        return Bundle.LBL_AccidentPeriodsChartData_Axis();
    }
}
