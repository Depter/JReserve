package org.jreserve.localesettings.util;

import java.text.DecimalFormat;
import javax.swing.table.TableCellRenderer;
import org.jreserve.resources.textfieldfilters.ClassTableRenderer;
import org.jreserve.resources.textfieldfilters.TextRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleRenderer implements TextRenderer<Double> {

    public static TableCellRenderer getTableRenderer() {
        DecimalFormat format = LocaleSettings.getDecimalFormat();
        return getTableRenderer(format);
    }
    
    public static TableCellRenderer getTableRenderer(DecimalFormat format) {
        DoubleRenderer renderer = new DoubleRenderer(format);
        return new ClassTableRenderer(Double.class, renderer);
    }
    
    private DecimalFormat format;
    private String nan;
    
    public DoubleRenderer() {
        this(LocaleSettings.getDecimalFormat());
    }
    
    public DoubleRenderer(DecimalFormat format) {
        this.format = format;
        nan = format.getDecimalFormatSymbols().getNaN();
    }
    
    public int getMaximumFractionDigits() {
        return format.getMaximumFractionDigits();
    }
    
    public void setMaximumFractionDigits(int count) {
        format.setMinimumFractionDigits(count);
    }
    
    @Override
    public String toString(Double value) {
        if(value == null)
            return null;
        return toString(value.doubleValue());
    }

    private String toString(double value) {
        if(Double.isNaN(value))
            return nan;
        return format.format(value);
    }
}
