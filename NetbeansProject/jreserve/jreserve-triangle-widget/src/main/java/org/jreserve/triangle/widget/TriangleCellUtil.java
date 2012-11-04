package org.jreserve.triangle.widget;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCellUtil {
    
    public static void setCellValues(TriangleCell[][] cells, List<List<WidgetData<Double>>> values) {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                setCellValues(cell, values);
    }
    
    public static void setCellValues(TriangleCell cell, List<List<WidgetData<Double>>> values) {
        if(cell != null) {
            List<Double> sum = new ArrayList<Double>(values.size());
            for(List<WidgetData<Double>> dataList : values)
                sum.add(sum(cell, dataList));
            cell.setValues(sum);
        }
    }
    
    private static Double sum(TriangleCell cell, List<WidgetData<Double>> datas) {
        Double sum = null;
        for(WidgetData<Double> data : datas)
            if(cell.acceptsData(data))
                sum = add(sum, data.getValue());
        return sum;
    }

    public static void cummulate(TriangleCell[][] cells) {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                if(cell != null)
                    cell.setCummulated(true);
//        for(TriangleCell[] row : cells) {
//            TriangleCell prev = null;
//            for(TriangleCell cell : row) {
//                cummulate(prev, cell);
//                prev = cell;
//            }
//        }
    }
    
    public static void cummulate(TriangleCell previous, TriangleCell current) {
        if(previous != null && current != null) {
            List<Double> cummulated = cummulatValues(previous, current);
            current.setValues(cummulated);
        }
    }
    
    public static List<Double> cummulatValues(TriangleCell previous, TriangleCell current) {
        if(current == null)
            return null;
        if(previous == null)
            return current.getValues();
        checkLayerSize(previous, current);
        return calculateCummulatedValues(previous, current);
    }
    
    private static void checkLayerSize(TriangleCell previous, TriangleCell current) {
        int pSize = previous.getLayerCount();
        int cSize = current.getLayerCount();
        if(cSize != pSize)
            throwDifferentLayerSizeException(cSize, pSize);
    }
    
    private static void throwDifferentLayerSizeException(int cSize, int pSize) {
        String msg = "Different value sizes! this=%d, previous=%d";
        msg = String.format(msg, cSize, pSize);
        throw new IllegalArgumentException(msg);
    }
    
    private static List<Double> calculateCummulatedValues(TriangleCell previous, TriangleCell current) {
        List<Double> cummulated = current.getValues();
        for(int i=0, length=cummulated.size(); i<length; i++) {
            Double v = current.getValueAt(i);
            Double p = previous.getValueAt(i);
            cummulated.set(i, add(v, p));
        }
        return cummulated;
    }
    
    public static Double add(Double sum, Double v) {
        if(v == null) { 
            return sum;
        } else if(sum == null || Double.isNaN(sum) || Double.isNaN(v)) {
            return v;
        } else {
            return sum + v;
        }
    }
    
    public static void deCummulate(TriangleCell[][] cells) {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                if(cell != null)
                    cell.setCummulated(false);
//        for(TriangleCell[] row : cells)
//            for(int i=row.length-1; i>0; i--)
//                TriangleCellUtil.deCummulatValues(row[i-1], row[i]);
    }
    
    public static void deCummulate(TriangleCell previous, TriangleCell current) {
        if(previous != null && current != null) {
            List<Double> deCummulated = deCummulatValues(previous, current);
            current.setValues(deCummulated);
        }
    }
    
    public static List<Double> deCummulatValues(TriangleCell previous, TriangleCell current) {
        if(current == null)
            return null;
        if(previous == null)
            return current.getValues();
        checkLayerSize(previous, current);
        return calculateDeCummulatedValues(previous, current);
    }
    
    private static List<Double> calculateDeCummulatedValues(TriangleCell previous, TriangleCell current) {
        List<Double> cummulated = current.getValues();
        for(int i=0, length=cummulated.size(); i<length; i++) {
            Double v = cummulated.get(i);
            Double p = previous.getValueAt(i);
            cummulated.set(i, subtract(v, p));
        }
        return cummulated;
    }
    
    private static Double subtract(Double a, Double b) {
        if(b == null) 
            return a;
        if(a == null)
            return b==null? null : -b;
        if(Double.isNaN(a) || Double.isNaN(b))
            return Double.NaN;
        return a - b;
    }
    
    public static List<WidgetData<Double>> extractValues(TriangleCell[][] cells, int layer) {
        List<WidgetData<Double>> result = new ArrayList<WidgetData<Double>>();
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                extractValue(cell, layer, result);
        return result;
    }
    
    private static void extractValue(TriangleCell cell, int layer, List<WidgetData<Double>> result) {
        if(cell == null) return;
        Double value = cell.getValueAt(layer);
        if(value == null) return;
        result.add(new WidgetData<Double>(cell.getAccidentBegin(), cell.getAccidentEnd(), value));
    }
    
    public static void setValue(TriangleCell cell, int layer, Double value) {
        cell.setValueAt(layer, value);
    }
    
    private TriangleCellUtil() {}
}
