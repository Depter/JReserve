package org.jreserve.data.util;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.localesettings.util.LocaleSettings;

/**
 *
 * @author Peter Decsi
 */
public class DoubleTableCellRenderer extends DefaultTableCellRenderer {

    private DecimalFormat format = LocaleSettings.getDecimalFormat();

    public void setFormat(DecimalFormat format) {
        if(format == null)
            this.format = LocaleSettings.getDecimalFormat();
        else
            this.format = format;
    }
    
    public DecimalFormat getFormat() {
        return format;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof Double)
            setText((Double)value);
        return this;
    }
    
    private void setText(Double value) {
        String str = format.format(value);
        if(!(value < 0))
            str = " " + str;
        setText(str);
    }
}
