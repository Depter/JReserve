package org.jreserve.triangle.widget.charts;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.chart.ChartUtil;
import org.jreserve.chart.ListColorGenerator;
import org.jreserve.chart.MultiSeriesBarChart;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CalendarYearTotalChartData.Title=Calendar years",
    "LBL.CalendarYearTotalChartData.Row=Calendar year"
})
public class CalendarYearTotalChartData extends TriangleChartData<String, Date> {
    private final static java.awt.Color BAR_COLOR = new java.awt.Color(79, 129, 189);

    public static NavigablePanel createPanel(TriangleWidget widget) {
        CalendarYearTotalChartData data = new CalendarYearTotalChartData(widget);
        MultiSeriesBarChart<String, Date> chart = new MultiSeriesBarChart<String, Date>(data, new ListColorGenerator(BAR_COLOR));
        String title = Bundle.LBL_CalendarYearTotalChartData_Title();
        return ChartUtil.createNavigablePanel(title, ChartUtil.CHART_BAR, chart, true);
    }
    
    
    private DateRenderer renderer = new DateRenderer();

    CalendarYearTotalChartData(TriangleWidget widget) {
        super(widget);
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
        Map<Date, Double> chartData = new TreeMap<Date, Double>();
        result.put(Bundle.LBL_CalendarYearTotalChartData_Row(), chartData);
        
        TriangularData data = widget.getData();
        
        for(int a=0, aCount=data.getAccidentCount(); a<aCount; a++)
            for(int d=0, dCount=data.getDevelopmentCount(a); d<dCount; d++)
                addData(chartData, data.getDevelopmentName(a, d), data.getValue(a, d));
        
        return result;
    }
    
    private void addData(Map<Date, Double> data, Date dev, double value) {
        if(Double.isNaN(value)) return;
        Double sum = data.get(dev);
        data.put(dev, sum==null? value : sum+value);
    }
}