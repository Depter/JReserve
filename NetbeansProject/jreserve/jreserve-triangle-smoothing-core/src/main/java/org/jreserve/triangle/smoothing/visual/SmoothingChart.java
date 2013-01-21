package org.jreserve.triangle.smoothing.visual;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.chart.ChartData;
import org.jreserve.chart.ChartUtil;
import org.jreserve.chart.MultiSeriesLineChart;
import org.jreserve.triangle.smoothing.visual.SmoothingTableModel.SmoothDummy;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.SmoothingChart.Original=Original",
    "LBL.SmoothingChart.Smoothed=Smoothed",
    "LBL.SmoothingChart.Title=Smoothing"
})
class SmoothingChart extends ChartData<String, Integer> {

    static JPanel createChart(SmoothingTableModel model) {
        SmoothingChart data = new SmoothingChart(model);
        MultiSeriesLineChart<String, Integer> chart = new MultiSeriesLineChart<String, Integer>(data);
        return ChartUtil.createPanel(chart, true);
    }
    
    private SmoothingTableModel model;
    
    private SmoothingChart(SmoothingTableModel model) {
        this.model = model;
        this.model.addChangeListener(new TableListener());
    }
    
    @Override
    public Map<String, Map<Integer, Double>> getData() {
        Map<String, Map<Integer, Double>> result = new LinkedHashMap<String, Map<Integer, Double>>();
        Map<Integer, Double> original = new LinkedHashMap<Integer, Double>();
        Map<Integer, Double> smoothed = new LinkedHashMap<Integer, Double>();
                
        for(int r=0, rows=model.getRowCount(); r<rows; r++) {
            SmoothDummy dummy = model.getDummyAt(r);
            original.put(r+1, dummy.getInput());
            if(dummy.isApplied())
                smoothed.put(r+1, dummy.getSmoothed());
        }
        
        result.put(Bundle.LBL_SmoothingTableModel_Original(), original);
        result.put(Bundle.LBL_SmoothingTableModel_Smoothed(), smoothed);
        return result;
    }

    private class TableListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChangeEvent();
        }
    }
}
