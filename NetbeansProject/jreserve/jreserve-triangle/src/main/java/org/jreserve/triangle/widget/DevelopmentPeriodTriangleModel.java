package org.jreserve.triangle.widget;

import org.jreserve.data.model.TriangleCell;
import org.jreserve.data.model.TriangleRow;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DevelopmentPeriodTriangleModel extends AbstractTriangleModel {
    
    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ImportTableModel_Periods();
        return ""+ column;
    }

    @Override
    protected TriangleCell getCellAt(TriangleRow row, int column) {
        return row.getCell(column - 1);
    }
}
