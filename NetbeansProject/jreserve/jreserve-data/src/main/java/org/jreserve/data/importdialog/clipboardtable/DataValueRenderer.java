package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class DataValueRenderer extends DefaultTableCellRenderer {

    private final NumberFormat nf = NumberFormat.getNumberInstance();
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setForeground(isValid(value)? Color.BLACK : Color.RED);
        return this;
    }

    private boolean isValid(Object value) {
        if(value instanceof String) {
            try {
                Number d = nf.parse((String) value);
                if(d.doubleValue() >= 0)
                    setText(" "+d);
                return true;
            } catch (ParseException ex) {
                return false;
            }
        } else {
            return false;
        }
    }
}
