package org.jreserve.triangle.visual.widget.chart;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.chart.ChartUtil;
import org.jreserve.chart.MultiSeriesLineChart;
import org.jreserve.chart.MultiSeriesScaledLineChart;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.visual.widget.TriangleWidget;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.AccidentPeriodsChartData.Title=Accident periods",
    "LBL.AccidentPeriodsChartData.Scaled.Title=Scaled accident periods",
    "LBL.AccidentPeriodsChartData.Axis=Development period"
})
public class AccidentPeriodsChartData extends TriangleChartData<Date, Integer> {

    public static NavigablePanel createPanel(TriangleWidget widget) {
        return createPanel(widget.getLookup());
    }

    public static NavigablePanel createPanel(Lookup lookup) {
        return createPanel(new AccidentPeriodsChartData(lookup));
    }

    public static NavigablePanel createPanel(TriangularData data) {
        return createPanel(new AccidentPeriodsChartData(data));
    }

    private static NavigablePanel createPanel(AccidentPeriodsChartData chartData) {
        MultiSeriesLineChart<Date, Integer> chart = new MultiSeriesLineChart<Date, Integer>(chartData);
        String title = Bundle.LBL_AccidentPeriodsChartData_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }

    public static NavigablePanel createScaledPanel(TriangleWidget widget) {
        return createScaledPanel(widget.getLookup());
    }

    public static NavigablePanel createScaledPanel(Lookup lookup) {
        return createScaledPanel(new AccidentPeriodsChartData(lookup));
    }

    public static NavigablePanel createScaledPanel(TriangularData data) {
        return createScaledPanel(new AccidentPeriodsChartData(data));
    }

    private static NavigablePanel createScaledPanel(AccidentPeriodsChartData chartData) {
        MultiSeriesScaledLineChart<Date, Integer> chart = new MultiSeriesScaledLineChart<Date, Integer>(chartData);
        String title = Bundle.LBL_AccidentPeriodsChartData_Scaled_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }

    private DateRenderer renderer = new DateRenderer();
    
    public AccidentPeriodsChartData(Lookup lookup) {
        super(lookup);
    }
    
    public AccidentPeriodsChartData(TriangularData data) {
        super(data);
    } 
    
    @Override
    public Map<Date, Map<Integer, Double>> getData() {
        double[][] values = getValues();
        Map<Date, Map<Integer, Double>> result = new TreeMap<Date, Map<Integer, Double>>();
        
        for(int i=0, size=values.length; i<size; i++) 
            result.put(data.getAccidentName(i), getRowData(values[i]));
        return result;
    }
    
    private Map<Integer, Double> getRowData(double[] row) {
        Map<Integer, Double> rowData = new TreeMap<Integer, Double>();
        for(int i=0, size=row.length; i<size; i++)
            rowData.put(i+1, row[i]);            
        return rowData;
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
