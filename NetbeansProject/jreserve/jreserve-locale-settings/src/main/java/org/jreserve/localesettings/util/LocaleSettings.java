package org.jreserve.localesettings.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LocaleSettings {

    private final static Logger logger = Logger.getLogger(LocaleSettings.class.getName());
    
    private final static String LOCALE = "LOCALE";
    private final static String DATE_FORMAT = "DATE_FORMAT";
    private final static String DECIMAL_FORMAT = "DECIMAL_FORMAT";
    private final static String DECIMAL_SEPARATOR = "DECIMAL_SEPARATOR";
    private final static String NAN = "NaN";
    private final static String DEFAULT_NAN = "NaN";
    private final static String THOUSAND_SEPARATOR = "THOUSAND_SEPARATOR";
    private final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static Locale getLocale() {
        String language = getPreferences().get(LOCALE, null);
        if(language != null)
            return new Locale(language);
        return Locale.getDefault();
    }
    
    public static void setLocale(Locale locale) {
        String language = locale==null? null : locale.getISO3Language();
        getPreferences().put(LOCALE, language);
    }
    
    public static String getDateFormat() {
        String format = getPreferences().get(DATE_FORMAT, null);
        if(format != null)
            return format;
        return new SimpleDateFormat().toPattern();
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(LocaleSettings.class);
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
        return getDefaultDecimalSymbols().getDecimalSeparator();
    }
    
    private static DecimalFormatSymbols getDefaultDecimalSymbols() {
        Locale locale = getLocale();
        return DecimalFormatSymbols.getInstance(locale);
    }
    
    public static void setDecimalSeparator(char separator) {
        getPreferences().putInt(DECIMAL_SEPARATOR, separator);
        logger.log(Level.INFO, "Default decimal separator set to \"{0}\".", separator);
    }
    
    public static char getThousandSeparator() {
        char separator = (char) getPreferences().getInt(THOUSAND_SEPARATOR, 0);
        if(separator != 0)
            return separator;
        return getDefaultDecimalSymbols().getGroupingSeparator();
    }
    
    public static void setThousandSeparator(char separator) {
        getPreferences().putInt(THOUSAND_SEPARATOR, separator);
        logger.log(Level.INFO, "Default thousand format set to \"{0}\".", separator);
    }
    
    public static DecimalFormatSymbols getDecimalSymbols() {
        DecimalFormatSymbols symbols = getDefaultDecimalSymbols();
        symbols.setDecimalSeparator(getDecimalSeparator());
        symbols.setGroupingSeparator(getThousandSeparator());
        symbols.setNaN(getNaN());
        return symbols;
    }
    
    public static DecimalFormat getDecimalFormatter() {
        String pattern = getDecimalFormat();
        DecimalFormatSymbols symbols = getDecimalSymbols();
        return new DecimalFormat(pattern, symbols);
    }
    
    public static String getNaN() {
        String nan = getPreferences().get(NAN, null);
        if(nan != null)
            return nan;
        return DEFAULT_NAN;
    }
    
    public static void setNaN(String nan) {
        if(nan == null || nan.trim().length() == 0) {
            setNaN(DecimalFormatSymbols.getInstance().getNaN());
        } else {
            getPreferences().put(NAN, nan);
            logger.log(Level.INFO, "NaN set to \"{0}\".", nan);
        }
    }
}
