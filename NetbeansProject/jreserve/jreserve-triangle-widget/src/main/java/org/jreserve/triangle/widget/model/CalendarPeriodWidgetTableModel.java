package org.jreserve.triangle.widget.model;

import java.util.Date;
import org.jreserve.triangle.TriangularData;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CalendarPeriodWidgetTableModel.Name=Calendar periods"
})
public class CalendarPeriodWidgetTableModel extends AbstractWidgetTableModel {
    
    public CalendarPeriodWidgetTableModel() {
    }
    
    public CalendarPeriodWidgetTableModel(TriangularData data) {
        super(data);
    }

    @Override
    protected String getRowName(int row) {
        return parseDate(data.getAccidentName(row));
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "";
        return parseDate(data.getDevelopmentName(0, column-1));
    }
    
    private String parseDate(Date date) {
        return date.toString();
    }
    
    @Override
    protected int getAccident(int row, int column) {
        return row;
    }
    
    @Override
    protected int getDevelopment(int row, int column) {
        int maxDev = data.getDevelopmentCount();
        int maxRowDev = data.getDevelopmentCount(row);
        int correction = maxDev - maxRowDev;
        return column - correction;
    }
}
