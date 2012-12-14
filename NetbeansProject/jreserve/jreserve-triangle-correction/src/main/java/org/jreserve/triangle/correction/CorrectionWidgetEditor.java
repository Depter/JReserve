package org.jreserve.triangle.correction;

import org.jreserve.triangle.widget.WidgetEditor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CorrectionWidgetEditor implements WidgetEditor {

    private final static Double EPSILON = 0.0000000001;
    
    @Override
    public boolean setCellValue(int row, int column, double value) {
        if(Math.abs(value) < EPSILON)
            deleteCorrection(row, column);
        else
            addCorrection(row, column, value);
        return true;
    }
    
    private void deleteCorrection(int row, int column) {
    }
    
    private void addCorrection(int row, int column, double value) {
    }

}
