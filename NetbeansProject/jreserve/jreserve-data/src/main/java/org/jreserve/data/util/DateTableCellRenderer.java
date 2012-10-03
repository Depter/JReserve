package org.jreserve.data.util;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.localesettings.util.LocaleSettings;

/**
 *
 * @author Peter Decsi
 */
public class DateTableCellRenderer extends DefaultTableCellRenderer {

    private DateFormat format = new SimpleDateFormat(LocaleSettings.getDateFormat());

    public void setFormat(DateFormat format) {
        if(format == null)
            this.format = new SimpleDateFormat(LocaleSettings.getDateFormat());
        else
            this.format = format;
    }
    
    public DateFormat getFormat() {
        return format;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof Date)
            setText(format.format((Date)value));
        return this;
    }
}
