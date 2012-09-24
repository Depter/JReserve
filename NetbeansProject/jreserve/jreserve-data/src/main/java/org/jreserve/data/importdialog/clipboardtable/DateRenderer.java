package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class DateRenderer extends DefaultTableCellRenderer {

    private SimpleDateFormat df;

    public DateRenderer(String dateFormat) {
        df = new SimpleDateFormat(dateFormat);
    }
    
    public void setDateFormat(String dateFormat) {
        df = new SimpleDateFormat(dateFormat);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setForeground(isValid(value)? Color.BLACK : Color.RED);
        return this;
    }

    private boolean isValid(Object value) {
        if(value instanceof String) {
            try {
                df.parse((String) value);
                return true;
            } catch (ParseException ex) {
                return false;
            }
        } else {
            return false;
        }
    }
}
