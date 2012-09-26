package org.jreserve.data.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataImportSettings {

    private final static Logger logger = Logger.getLogger(DataImportSettings.class.getName());
    
    private final static String DATE_FORMAT = "DATE_FORMAT";
    private final static String DECIMAL_FORMAT = "DECIMAL_FORMAT";
    private final static String DECIMAL_SEPARATOR = "DECIMAL_SEPARATOR";
    private final static String THOUSAND_SEPARATOR = "THOUSAND_SEPARATOR";
    private final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static String getDateFormat() {
        String format = getPreferences().get(DATE_FORMAT, null);
        if(format != null)
            return format;
        return DEFAULT_DATE_FORMAT;
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(DataImportSettings.class);
    }
    
    public static void setDateFormat(String format) {
        DateFormat df = new SimpleDateFormat(format);
        getPreferences().put(DATE_FORMAT, format);
        logger.log(Level.INFO, "Default date format set to \"{0}\".", format);
    }
    
    public static String getDecimalFormat() {
        String format = getPreferences().get(DECIMAL_FORMAT, null);
        if(format != null)
            return format;
        return new DecimalFormat().toPattern();
    }
    
    public static void setDecimalFormat(String format) {
        DecimalFormat df = new DecimalFormat(format);
        getPreferences().put(DECIMAL_FORMAT, format);
        logger.log(Level.INFO, "Default decimal format set to \"{0}\".", format);
    }
    
    public static char getDecimalSeparator() {
        char separator = (char) getPreferences().getInt(DECIMAL_SEPARATOR, 0);
        if(separator != 0)
            return separator;
        return new DecimalFormatSymbols().getDecimalSeparator();
    }
    
    public static void setDecimalSeparator(char separator) {
        getPreferences().putInt(DECIMAL_SEPARATOR, separator);
        logger.log(Level.INFO, "Default decimal separator set to \"{0}\".", separator);
    }
    
    public static char getThousandSeparator() {
        char separator = (char) getPreferences().getInt(THOUSAND_SEPARATOR, 0);
        if(separator != 0)
            return separator;
        return new DecimalFormatSymbols().getGroupingSeparator();
    }
    
    public static void setThousandSeparator(char separator) {
        getPreferences().putInt(THOUSAND_SEPARATOR, separator);
        logger.log(Level.INFO, "Default thousand format set to \"{0}\".", separator);
    }
    
    public static DecimalFormatSymbols getDecimalSymbols() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(getDecimalSeparator());
        symbols.setGroupingSeparator(getThousandSeparator());
        return symbols;
    }
    
    public static DecimalFormat getDecimalFormatter() {
        String pattern = getDecimalFormat();
        DecimalFormatSymbols symbols = getDecimalSymbols();
        return new DecimalFormat(pattern, symbols);
    }
}
