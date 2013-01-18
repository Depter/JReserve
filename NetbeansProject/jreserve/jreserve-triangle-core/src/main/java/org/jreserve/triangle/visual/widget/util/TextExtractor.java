package org.jreserve.triangle.visual.widget.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.table.TableModel;
import org.jreserve.localesettings.util.LocaleSettings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TextExtractor {
        
    private final DateFormat df = LocaleSettings.getDateFormat();
    private final DecimalFormat nf = LocaleSettings.getDecimalFormat();
    private final TableModel model;
    private final String lineSeparator;
    private final String cellSeparator;
    private final StringBuilder sb = new StringBuilder();
    private final int rows;
    private final int columns;
    
    public TextExtractor(TableModel model) {
        this(model, "\n", "\t");
    }
    
    public TextExtractor(TableModel model, String lineSeparator, String cellSeparator) {
        this.model = model;
        this.rows = model.getRowCount();
        this.columns = model.getColumnCount();
        this.lineSeparator = lineSeparator;
        this.cellSeparator = cellSeparator;
    }
    
    public String extract() {
        appendHeader();
        appendRows();
        return sb.toString();
    }
    
    private void appendHeader() {
        for(int c=0; c<columns; c++) {
            if(c>0) sb.append(cellSeparator);
            String name = model.getColumnName(c);
            if(name != null)
                sb.append(name);
        }
    }
    
    private void appendRows() {
        for(int r=0; r<rows; r++)
            appendRow(r);
    }
    
    private void appendRow(int row) {
        sb.append(lineSeparator);
        for(int c=0; c<columns; c++) {
            if(c>0) sb.append(cellSeparator);
            
            String value = parseValue(model.getValueAt(row, c));
            if(value != null)
                sb.append(value);
        }
    }
    
    private String parseValue(Object value) {
        if(value == null)
            return null;
        if(value instanceof Double)
            return nf.format((Double) value);
        if(value instanceof Date)
            return df.format((Date) value);
        return value.toString();
    }
}
