package org.jreserve.triangle.widget.model;

import java.util.Date;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
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
    protected Double getData(int row, int column) {
        int development = getDevelopmentIndex(row, column);
        if(development < 0)
            return null;
        return data.getValue(row, development);
    }
    
    private int getDevelopmentIndex(int row, int column) {
        int maxDev = data.getDevelopmentCount();
        int maxRowDev = data.getDevelopmentCount(row);
        int correction = maxDev - maxRowDev;
        return column - correction;
    }
}
