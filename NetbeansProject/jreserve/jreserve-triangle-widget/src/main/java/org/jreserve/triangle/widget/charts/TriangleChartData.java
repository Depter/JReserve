package org.jreserve.triangle.widget.charts;

import org.jreserve.chart.ChartData;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidgetListener;

/**
 *
 * @author Peter Decsi
 */
abstract class TriangleChartData<R extends Comparable<R>, C extends Comparable<C>> extends ChartData<R, C> implements TriangleWidgetListener {

    protected TriangleWidget widget;
    
    protected TriangleChartData(TriangleWidget widget) {
        this.widget = widget;
        widget.addTriangleWidgetListener(this);
    }

    @Override
    public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue) {
        fireChangeEvent();
    }

    @Override
    public void valuesChanged() {
        fireChangeEvent();
    }

    @Override
    public void structureChanged() {
        fireChangeEvent();
    }

    @Override
    public void commentsChanged() {
    }

}
