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
    "LBL.DevelopmentPeriodsChartData.Title=Development periods",
    "LBL.DevelopmentPeriodsChartData.Scaled.Title=Scaled development periods",
    "LBL.DevelopmentPeriodsChartData.Axis=Accident period"
})
public class DevelopmentPeriodsChartData extends TriangleChartData<Integer, Date> {

    public static NavigablePanel createPanel(TriangleWidget widget) {
        return createPanel(widget.getLookup());
    }

    public static NavigablePanel createPanel(Lookup lookup) {
        return createPanel(new DevelopmentPeriodsChartData(lookup));
    }

    public static NavigablePanel createPanel(TriangularData data) {
        return createPanel(new DevelopmentPeriodsChartData(data));
    }

    private static NavigablePanel createPanel(DevelopmentPeriodsChartData data) {
        MultiSeriesLineChart<Integer, Date> chart = new MultiSeriesLineChart<Integer, Date>(data);
        String title = Bundle.LBL_DevelopmentPeriodsChartData_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }

    public static NavigablePanel createScaledPanel(TriangleWidget widget) {
        return createScaledPanel(widget.getLookup());
    }

    public static NavigablePanel createScaledPanel(Lookup lookup) {
        return createScaledPanel(new DevelopmentPeriodsChartData(lookup));
    }

    public static NavigablePanel createScaledPanel(TriangularData data) {
        return createScaledPanel(new DevelopmentPeriodsChartData(data));
    }

    private static NavigablePanel createScaledPanel(DevelopmentPeriodsChartData data) {
        MultiSeriesScaledLineChart<Integer, Date> chart = new MultiSeriesScaledLineChart<Integer, Date>(data);
        String title = Bundle.LBL_DevelopmentPeriodsChartData_Scaled_Title();
        return ChartUtil.createMultiSeriesNavigablePanel(title, ChartUtil.CHART_XY, chart);
    }
    
    private DateRenderer renderer = new DateRenderer();

    private DevelopmentPeriodsChartData(Lookup lookup) {
        super(lookup);
    }

    private DevelopmentPeriodsChartData(TriangularData data) {
        super(data);
    }
    
    @Override
    public Map<Integer, Map<Date, Double>> getData() {
        double[][] values = getValues();
        Map<Integer, Map<Date, Double>> result = new TreeMap<Integer, Map<Date, Double>>();
        
        for(int d=0, dCount=data.getDevelopmentCount(); d<dCount; d++) {
            Map<Date, Double> dev = new TreeMap<Date, Double>();
            int a = 0;
            
            while(d < data.getDevelopmentCount(a)) {
                dev.put(data.getAccidentName(a), values[a][d]);
                a++;
            }
            
            if(!dev.isEmpty())
                result.put(d, dev);
        }
        
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