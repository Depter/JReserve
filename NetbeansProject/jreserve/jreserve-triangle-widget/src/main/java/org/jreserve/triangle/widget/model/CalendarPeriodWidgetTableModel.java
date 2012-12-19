package org.jreserve.triangle.widget.model;

import java.text.DateFormat;
import java.util.Date;
import org.jreserve.localesettings.util.LocaleSettings;
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
    
    private DateFormat df = LocaleSettings.getDateFormat();
    
    public CalendarPeriodWidgetTableModel() {
    }
    
    public CalendarPeriodWidgetTableModel(TriangularData data) {
        super(data);
    }

    @Override
    protected String getRowName(int row) {
        Date date = data.getAccidentName(row);
        return df.format(date);
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "";
        Date date = data.getDevelopmentName(0, column-1);
        return df.format(date);
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
