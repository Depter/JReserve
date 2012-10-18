package org.jreserve.triangle.mvc;

import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleRow;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTableUtil {

    
    public static void setValues(TriangleTable<Double> table, List<Data<Double>> values) {
        for(int r=0, rCount=table.getRowCount(); r<rCount; r++)
            setValues(table.getRow(r), values);
    }
    
    private static void setValues(TriangleRow<Double> row, List<Data<Double>> values) {
        values = row.getRelevantData(values);
        for(int c=0, cCount=row.getCellCount(); c<cCount; c++)
            setValues(row.getCell(c), values);
    }
    
    private static void setValues(TriangleCell<Double> cell, List<Data<Double>> values) {
        values = cell.getRelevantData(values);
        Double value = values.isEmpty()? null : sumValues(values);
        cell.setValue(value);
    }
    
    private static double sumValues(List<Data<Double>> values) {
        double sum = 0d;
        for(Data<Double> value : values) {
            double d = value.getValue();
            if(Double.isNaN(d))
                return Double.NaN;
            sum += d;
        }
        return sum;
    }
}
