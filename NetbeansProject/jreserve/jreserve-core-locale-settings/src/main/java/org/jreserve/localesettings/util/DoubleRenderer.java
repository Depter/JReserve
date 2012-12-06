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
    private char grouping;
    private char decimal;
    private String nan;
    
    public DoubleRenderer() {
        this(LocaleSettings.getDecimalFormat());
    }
    
    public DoubleRenderer(DecimalFormat format) {
        this.format = format;
        this.format.setMinimumFractionDigits(format.getMaximumFractionDigits());
        nan = format.getDecimalFormatSymbols().getNaN();
        this.grouping = format.getDecimalFormatSymbols().getGroupingSeparator();
        this.decimal = format.getDecimalFormatSymbols().getDecimalSeparator();
    }
    
    public int getFractionDigits() {
        return format.getMaximumFractionDigits();
    }
    
    public void setFractionDigits(int count) {
        format.setMaximumFractionDigits(count);
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
    
    public String toSimpleString(Double value) {
        if(value == null)
            return null;
        return toSimpleString(value.doubleValue());
    }
    
    private String toSimpleString(double value) {
        String str = ""+value;
        return str.replace('.', decimal);
    }

    @Override
    public Double parse(String str) {
        if(str == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for(char ch : str.toCharArray()) {
            if(grouping == ch)
                continue;
            sb.append(ch==decimal? '.' : ch);
        }
        return Double.parseDouble(sb.toString());
    }
}
