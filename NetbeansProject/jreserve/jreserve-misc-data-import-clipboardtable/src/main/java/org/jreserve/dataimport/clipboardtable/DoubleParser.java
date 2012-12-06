package org.jreserve.dataimport.clipboardtable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleParser {
    
    private final char decimal;
    private final char grouping;
    private StringBuilder sb = new StringBuilder();
    
    public DoubleParser(DecimalFormat df) {
        this(df.getDecimalFormatSymbols());
    }
    
    public DoubleParser(DecimalFormatSymbols symbols) {
        this(symbols.getDecimalSeparator(), symbols.getGroupingSeparator());
    }
    
    public DoubleParser(char decimal, char grouping) {
        this.decimal = decimal;
        this.grouping = grouping;
    }
    
    public double parse(String str) {
        if(str == null || str.length() == 0)
            return 0d;
        str = escapeSymbols(str);
        return Double.parseDouble(str);
    }
    
    private String escapeSymbols(String str) {
        sb.setLength(0);
        for(char c : str.toCharArray())
            escapeChar(c);
        return sb.toString();
    }
    
    private void escapeChar(char c) {
        if(decimal == c)
            sb.append('.');
        else if(grouping != c)
            sb.append(c);
    }
}
