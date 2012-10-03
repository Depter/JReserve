package org.jreserve.dataimport.clipboardtable;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class DateRenderer extends DefaultTableCellRenderer {

    private DateFormat df;

    public DateRenderer(DateFormat dateFormat) {
        this.df = dateFormat;
    }
    
    public void setFormat(DateFormat df) {
        this.df = df;
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
