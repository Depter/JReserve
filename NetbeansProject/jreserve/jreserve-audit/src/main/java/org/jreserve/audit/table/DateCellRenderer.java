package org.jreserve.audit.table;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DateCellRenderer extends DefaultTableCellRenderer {

    private DateFormat df;

    public DateCellRenderer(DateFormat df) {
        this.df = df;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof Date)
            setText(df.format((Date) value));
        return this;
    }

    
}
