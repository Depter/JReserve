package org.jreserve.data.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DateFormatCombo extends AbstractFormatCombo<DateFormat> {

    private final static String[] FORMATS = {
        "yyyy-MM-dd",
        "dd-MM-yyyy",
        "MM-dd-yyyy",
        "yyyy.MM.dd",
        "dd.MM.yyyy",
        "MM.dd.yyyy",
        "yyyy/MM/dd",
        "dd/MM/yyyy",
        "MM/dd/yyyy"
    };
    
    public DateFormatCombo() {
        super(FORMATS);
    }

    @Override
    protected DateFormat createFormat(String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            return df;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
