package org.jreserve.triangle.widget.charts;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.chart.ChartUtil;
import org.jreserve.chart.ListColorGenerator;
import org.jreserve.chart.MultiSeriesBarChart;
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
    "LBL.CalendarYearTotalChartData.Title=Calendar years",
    "LBL.CalendarYearTotalChartData.Row=Calendar year"
})
public class CalendarYearTotalChartData extends TriangleChartData<String, Date> {
    private final static java.awt.Color BAR_COLOR = new java.awt.Color(79, 129, 189);

    public static NavigablePanel createPanel(TriangleWidget widget) {
        AccidentTotalChartData data = new AccidentTotalChartData(widget);
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
        Map<Date, Double> data = new TreeMap<Date, Double>();
        result.put(Bundle.LBL_CalendarYearTotalChartData_Row(), data);
        
        for(TriangleCell cell : widget.getCells())
            addData(data, cell);
        
        return result;
    }
    
    private void addData(Map<Date, Double> data, TriangleCell cell) {
        Double value = cell.getDisplayValue();
        if(value == null || value.isNaN()) return;
        Date date = cell.getDevelopmentBegin();
        Double sum = data.get(date);
        data.put(date, sum==null? value : sum+value);
    }
}