package org.jreserve.localesettings.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.table.TableCellRenderer;
import org.jreserve.resources.textfieldfilters.ClassTableRenderer;
import org.jreserve.resources.textfieldfilters.TextRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DateRenderer implements TextRenderer<Date> {

    public static TableCellRenderer getTableRenderer() {
        DateFormat format = LocaleSettings.getDateFormat();
        return getTableRenderer(format);
    }
    
    public static TableCellRenderer getTableRenderer(DateFormat format) {
        DateRenderer renderer = new DateRenderer(format);
        return new ClassTableRenderer(Date.class, renderer);
    }
    
    private DateFormat format;
    
    public DateRenderer() {
        this(LocaleSettings.getDateFormat());
    }
    
    public DateRenderer(DateFormat format) {
        this.format = format;
    }
    
    @Override
    public String toString(Date value) {
        if(value == null)
            return null;
        return format.format(value);
    }

    @Override
    public Date parse(String str) {
        try {
            return format.parse(str);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
