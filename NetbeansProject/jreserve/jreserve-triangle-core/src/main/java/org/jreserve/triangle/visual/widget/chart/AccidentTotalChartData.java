package org.jreserve.triangle.visual.widget.chart;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.chart.ChartUtil;
import org.jreserve.chart.ListColorGenerator;
import org.jreserve.chart.MultiSeriesBarChart;
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
    "LBL.AccidentTotalChartData.Title=Accident totaal",
    "LBL.AccidentTotalChartData.Row=Accident"
})
public class AccidentTotalChartData extends TriangleChartData<String, Date> {
    private final static java.awt.Color BAR_COLOR = new java.awt.Color(79, 129, 189);

    public static NavigablePanel createPanel(TriangleWidget widget) {
        return createPanel(widget.getLookup());
    }

    public static NavigablePanel createPanel(Lookup lookup) {
        return createPanel(new AccidentTotalChartData(lookup));
    }

    private static NavigablePanel createPanel(AccidentTotalChartData chartData) {
        MultiSeriesBarChart<String, Date> chart = new MultiSeriesBarChart<String, Date>(chartData, new ListColorGenerator(BAR_COLOR));
        String title = Bundle.LBL_AccidentTotalChartData_Title();
        return ChartUtil.createNavigablePanel(title, ChartUtil.CHART_BAR, chart, true);
    }

    public static NavigablePanel createPanel(TriangularData data) {
        return createPanel(new AccidentTotalChartData(data));
    }
    
    private DateRenderer renderer = new DateRenderer();
    
    public AccidentTotalChartData(Lookup lookup) {
        super(lookup);
    }
    
    public AccidentTotalChartData(TriangularData data) {
        super(data);
    }

    @Override
    public String getColumnLabel(Date columnKey) {
        return renderer.toString(columnKey);
    }

    @Override
    public boolean showLegend() {
        return false;
    }
    
    @Override
    public Map<String, Map<Date, Double>> getData() {
        Map<String, Map<Date, Double>> result = new TreeMap<String, Map<Date, Double>>();
        Map<Date, Double> totals = getTotals(data);
        result.put(Bundle.LBL_AccidentTotalChartData_Row(), totals);
        return result;
    }
    
    private Map<Date, Double> getTotals(TriangularData data) {
        Map<Date, Double> totals = new TreeMap<Date, Double>();
        for(int a=0, aCount=data.getAccidentCount(); a<aCount; a++)
            totals.put(data.getAccidentName(a), getTotal(data, a));
        return totals;
    }
    
    private double getTotal(TriangularData data, int accident) {
        double sum = 0d;
        for(int d=0, dCount=data.getDevelopmentCount(accident); d<dCount; d++) {
            double v = data.getValue(accident, d);
            if(!Double.isNaN(v))
                sum += v;
        }
        return sum;
    }
}
