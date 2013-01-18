package org.jreserve.triangle.visual.widget.model;

import java.text.DateFormat;
import java.util.Date;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.triangle.visual.widget.AbstractWidgetTableModel;
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
    protected int getAccident(int valueRow, int valueColumn) {
        return valueRow;
    }

    @Override
    protected int getDevelopment(int valueRow, int valueColumn) {
        int maxDev = data.getDevelopmentCount();
        int maxRowDev = data.getDevelopmentCount(valueRow);
        int correction = maxDev - maxRowDev;
        return valueColumn - correction;
    }
}
