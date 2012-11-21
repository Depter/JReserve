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
    "LBL.DevelopmentPeriodsChartData.Title=Development periods",
    "LBL.DevelopmentPeriodsChartData.Scaled.Title=Scaled development periods",
    "LBL.DevelopmentPeriodsChartData.Axis=Accident period"
})
public class DevelopmentPeriodsChartData extends TriangleChartData<Integer, Date> {

    public static NavigablePanel createPanel(TriangleWidget widget) {
        DevelopmentPeriodsChartData data = new DevelopmentPeriodsChartData(widget);
        MultiSeriesLineChart<Integer, Date> chart = new MultiSeriesLineChart<Integer, Date>(data);
        String title = Bundle.LBL_DevelopmentPeriodsChartData_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }

    public static NavigablePanel createScaledPanel(TriangleWidget widget) {
        DevelopmentPeriodsChartData data = new DevelopmentPeriodsChartData(widget);
        MultiSeriesScaledLineChart<Integer, Date> chart = new MultiSeriesScaledLineChart<Integer, Date>(data);
        String title = Bundle.LBL_DevelopmentPeriodsChartData_Scaled_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }
    
    private DateRenderer renderer = new DateRenderer();

    private DevelopmentPeriodsChartData(TriangleWidget widget) {
        super(widget);
    }
    
    @Override
    public Map<Integer, Map<Date, Double>> getData() {
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
    public String getColumnLabel(Date columnKey) {
        return renderer.toString(columnKey);
    }

    @Override
    public String getDomainAxisTitle() {
        return Bundle.LBL_DevelopmentPeriodsChartData_Axis();
    }
}
