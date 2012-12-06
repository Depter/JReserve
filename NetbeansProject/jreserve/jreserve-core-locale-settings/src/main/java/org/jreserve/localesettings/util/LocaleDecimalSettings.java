package org.jreserve.localesettings.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LocaleDecimalSettings {
    
    private final static Logger logger = Logger.getLogger(LocaleDecimalSettings.class.getName());

    private final static String DECIMAL_FORMAT = "DECIMAL_FORMAT";
    private final static String DECIMAL_SEPARATOR = "DECIMAL_SEPARATOR";
    private final static String NAN = "NaN";
    private final static String DEFAULT_NAN = "NaN";
    private final static String THOUSAND_SEPARATOR = "THOUSAND_SEPARATOR";
    private final static String DEFAULT_NUMBER_FORMAT = " #,##0.##;-#,##0.##";
    
    static String getDefaultDecimalFormatString() {
        return getDefaultDecimalFormatString(LocaleSettings.getLocale());
    }
    
    static String getDefaultDecimalFormatString(Locale locale) {
        return DEFAULT_NUMBER_FORMAT;
    }
    
    static String getDecimalFormatString() {
        String format = LocaleSettings.getPreferences().get(DECIMAL_FORMAT, null);
        if(format != null)
            return format;
        return getDefaultDecimalFormatString();
    }
    
    static void setDecimalFormatString(String format) {
        DecimalFormat df = new DecimalFormat(format);
        LocaleSettings.getPreferences().put(DECIMAL_FORMAT, format);
        logger.log(Level.INFO, "Default decimal format set to \"{0}\".", format);
    }
    
    static DecimalFormatSymbols getDefaultDecimalSymbols() {
        return getDefaultDecimalSymbols(LocaleSettings.getLocale());
    }
    
    static DecimalFormatSymbols getDefaultDecimalSymbols(Locale locale) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        symbols.setNaN(NAN);
        return symbols;
    }
    
    static DecimalFormatSymbols getDecimalSymbols() {
        DecimalFormatSymbols symbols = getDefaultDecimalSymbols();
        symbols.setDecimalSeparator(getDecimalSeparator());
        symbols.setGroupingSeparator(getGroupingSeparator());
        symbols.setNaN(getNaN());
        return symbols;
    }
    
    static char getDecimalSeparator() {
        char separator = (char) LocaleSettings.getPreferences().getInt(DECIMAL_SEPARATOR, 0);
        if(separator != 0)
            return separator;
        return getDefaultDecimalSymbols().getDecimalSeparator();
    }
    
    static void setDecimalSeparator(char separator) {
        LocaleSettings.getPreferences().putInt(DECIMAL_SEPARATOR, separator);
        logger.log(Level.INFO, "Default decimal separator set to \"{0}\".", separator);
    }
    
    static char getGroupingSeparator() {
        char separator = (char) LocaleSettings.getPreferences().getInt(THOUSAND_SEPARATOR, 0);
        if(separator != 0)
            return separator;
        return getDefaultDecimalSymbols().getGroupingSeparator();
    }
    
    static void setGroupingSeparator(char separator) {
        LocaleSettings.getPreferences().putInt(THOUSAND_SEPARATOR, separator);
        logger.log(Level.INFO, "Default thousand format set to \"{0}\".", separator);
    }
    
    static DecimalFormat getDecimalFormatter() {
        String pattern = getDecimalFormatString();
        DecimalFormatSymbols symbols = getDecimalSymbols();
        DecimalFormat format = new DecimalFormat(pattern, symbols);
        format.setMaximumFractionDigits(0);
        return format;
    }
    
    static String getNaN() {
        String nan = LocaleSettings.getPreferences().get(NAN, null);
        if(nan != null)
            return nan;
        return DEFAULT_NAN;
    }
    
    static void setNaN(String nan) {
        if(nan == null || nan.trim().length() == 0) {
            setNaN(DecimalFormatSymbols.getInstance().getNaN());
        } else {
            LocaleSettings.getPreferences().put(NAN, nan);
            logger.log(Level.INFO, "NaN set to \"{0}\".", nan);
        }
    }
    
    private LocaleDecimalSettings() {}
}
