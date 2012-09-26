package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class DataValueRenderer extends DefaultTableCellRenderer {

    private char grouping;
    private char decimal;
    private StringBuilder sb = new StringBuilder();
    
    DataValueRenderer(DecimalFormat format) {
        setFormat(format);
    }
    
    void setFormat(DecimalFormat format) {
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        grouping = symbols.getGroupingSeparator();
        decimal = symbols.getDecimalSeparator();
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
                double d = Double.parseDouble(escapeSymbols((String) value));
                if(d >= 0)
                    setText(" "+d);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private String escapeSymbols(String str) {
        sb.setLength(0);
        for(char c : str.toCharArray())
            if(decimal == c) {
                sb.append('.');
            } else if(grouping != c) {
                sb.append(c);
            }
        return sb.toString();
    }
}
