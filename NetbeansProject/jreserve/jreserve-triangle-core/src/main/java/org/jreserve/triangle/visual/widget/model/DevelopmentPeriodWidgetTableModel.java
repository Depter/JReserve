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
    "LBL.DevelopmentPeriodWidgetTableModel.Name=Development periods"
})
public class DevelopmentPeriodWidgetTableModel extends AbstractWidgetTableModel {
    
    private DateFormat df = LocaleSettings.getDateFormat();
    
    public DevelopmentPeriodWidgetTableModel() {
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
        return Integer.toString(column);
    }

    @Override
    protected int getAccident(int valueRow, int valueColumn) {
        return valueRow;
    }

    @Override
    protected int getDevelopment(int valueRow, int valueColumn) {
        return valueColumn;
    }
}
