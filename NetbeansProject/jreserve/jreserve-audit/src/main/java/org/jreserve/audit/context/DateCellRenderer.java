package org.jreserve.audit.context;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.localesettings.util.LocaleSettings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DateCellRenderer extends DefaultTableCellRenderer {

    private DateFormat df;

    public DateCellRenderer() {
        String format = LocaleSettings.getDateFormatString();
        format += " hh:mm:ss";
        Locale locale = LocaleSettings.getLocale();
        df = new SimpleDateFormat(format, locale);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof Date)
            setText(df.format((Date) value));
        return this;
    }

    
}
