package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class DataValueRenderer extends DefaultTableCellRenderer {

    private DoubleParser parser;
    
    DataValueRenderer(DecimalFormat format) {
        parser = new DoubleParser(format);
    }
    
    void setFormat(DecimalFormat format) {
        parser = new DoubleParser(format);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setForeground(isValid(value)? Color.BLACK : Color.RED);
        return this;
    }

    private boolean isValid(Object value) {
        if(value instanceof String) {
            return isValid((String) value);
        } else {
            return false;
        }
    }
    
    private boolean isValid(String value) {
        try {
            if(parser.parse((String) value) >= 0)
                setText(" " + value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
