package org.jreserve.smoothing;

import java.util.*;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleCellUtil;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.WidgetData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Smoother {
    
    private TriangleCell[][] cells;
    private int layer;
    
    public Smoother(TriangleWidget widget, int layer) {
        this(widget.getCellArray(), layer);
    }
    
    public Smoother(TriangleCell[][] cells, int layer) {
        initCells(cells);
        initLayer(layer);
    }
    
    private void initCells(TriangleCell[][] cells) {
        if(cells == null)
            throw new NullPointerException("TriangleCells was null!");
        this.cells = cells;
    }
    
    private void initLayer(int layer) {
        if(layer < 0)
            throw new IllegalArgumentException("Layer was less tehn 0! "+layer);
        this.layer = layer;
    }
    
    public void smooth(Smoothing smoothing) {
        List<SmoothingCell> cells = getCells(smoothing);
        double[] values = getInput(cells);
        values = smoothing.smooth(values);
        setValues(cells, values);
    }
    
    private List<SmoothingCell> getCells(Smoothing smoothing) {
        List<SmoothingCell> cells = smoothing.getCells();
        Collections.sort(cells);
        return cells;
    }
    
    private double[] getInput(List<SmoothingCell> cells) {
        double[] input = new double[cells.size()];
        for(int i=0, size=input.length; i<size; i++)
            input[i] = getValue(cells.get(i));
        return input;
    }
    
    private double getValue(SmoothingCell cell) {
        TriangleCell tc = getCellAt(cell);
        return getCellValue(tc);
    }
    
    private TriangleCell getCellAt(SmoothingCell cell) {
        Date accident = cell.getAccident();
        Date development = cell.getDevelopment();
        for(TriangleCell[] row : cells)
            for(TriangleCell tc : row)
                if(tc.acceptsDates(accident, development))
                    return tc;
        return null;
    }
    
    private double getCellValue(TriangleCell cell) {
        if(cell == null)
            return Double.NaN;
        Double value = cell.getValueUnder(layer);
        return value==null? Double.NaN : value.doubleValue();
    }
    
    private void setValues(List<SmoothingCell> cells, double[] values) {
        Set<WidgetData<Double>> datas = createValues(cells, values);
        datas.addAll(TriangleCellUtil.extractValues(this.cells, layer));
        TriangleCellUtil.setCellValues(this.cells, new ArrayList<WidgetData<Double>>(datas), layer);
    }
    
    private Set<WidgetData<Double>> createValues(List<SmoothingCell> cells, double[] smoothed) {
        Set<WidgetData<Double>> values = new HashSet<WidgetData<Double>>();
        for(int i=0, size=smoothed.length; i<size; i++)
            if(cells.get(i).isApplied())
                values.add(createData(smoothed[i], cells.get(i)));
        return values;
    }
    
    private WidgetData<Double> createData(double value, SmoothingCell cell) {
        TriangleCell tc = getCellAt(cell);
        Date accident = tc.getAccidentBegin();
        Date development = tc.getDevelopmentBegin();
        return new WidgetData<Double>(accident, development, value);
    }
}
