package org.jreserve.triangle.widget.charts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.chart.ChartData;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class TriangleChartData<R extends Comparable<R>, C extends Comparable<C>> extends ChartData<R, C> implements ChangeListener {

    protected TriangleWidget widget;
    
    protected TriangleChartData(TriangleWidget widget) {
        this.widget = widget;
        this.widget.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireChangeEvent();
    }
    
    protected List<Date> getAccidentDates() {
        TriangularData data = widget.getData();
        int aCount = data.getAccidentCount();
        List<Date> result = new ArrayList<Date>(aCount);
        for(int a=0; a<aCount; a++)
            result.add(data.getAccidentName(a));
        return result;
    }
    
    protected double[][] getValues() {
        double[][] values = widget.getData().getData();
        if(widget.isCummulated())
            cummulate(values);
        return values;
    }
    
    private void cummulate(double[][] values) {
        for(double[] row : values)
            cummulate(row);
    }

    private void cummulate(double[] row) {
        double sum = 0d;
        for(int c=0, size=row.length; c<size; c++) {
            double v = row[c];
            if(!Double.isNaN(v)) {
                sum += v;
                row[c] = sum;
            }
        }
    }
}
