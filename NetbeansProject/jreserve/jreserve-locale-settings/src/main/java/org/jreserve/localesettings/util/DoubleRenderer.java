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
        DecimalFormat format = LocaleSettings.getDecimalFormatter();
        return getTableRenderer(format);
    }
    
    public static TableCellRenderer getTableRenderer(DecimalFormat format) {
        DoubleRenderer renderer = new DoubleRenderer(format);
        return new ClassTableRenderer(Double.class, renderer);
    }
    
    public static TextRenderer<Double> getRenderer() {
        DecimalFormat format = LocaleSettings.getDecimalFormatter();
        return getRenderer(format);
    }
    
    public static TextRenderer<Double> getRenderer(DecimalFormat format) {
        return new DoubleRenderer(format);
    }
    
    private DecimalFormat format;
    private String nan;
    
    private DoubleRenderer(DecimalFormat format) {
        this.format = format;
        nan = format.getDecimalFormatSymbols().getNaN();
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
