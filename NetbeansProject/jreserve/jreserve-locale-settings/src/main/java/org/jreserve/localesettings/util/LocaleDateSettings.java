package org.jreserve.localesettings.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LocaleDateSettings {

    private final static Logger logger = Logger.getLogger(LocaleDateSettings.class.getName());
    private final static String DATE_FORMAT = "DATE_FORMAT";
    
    static DateFormat getDateFormat() {
        return new SimpleDateFormat(getDateFormatString());
    }
    
    static String getDateFormatString() {
        String format = LocaleSettings.getPreferences().get(DATE_FORMAT, null);
        if(format != null)
            return format;
        return getDefaultDateFormatString();
    }
    
    static String getDefaultDateFormatString() {
        return getDefaultDateFormatString(LocaleSettings.getLocale());
    }
    
    static String getDefaultDateFormatString(Locale locale) {
        Locale sys = Locale.getDefault();
        Locale.setDefault(locale);
        String pattern = new SimpleDateFormat().toPattern();
        Locale.setDefault(sys);
        return getDefaultDateFormat(pattern);
    }
    
    private static String getDefaultDateFormat(String pattern) {
        char sep = getDateSeparator(pattern);
        switch (pattern.charAt(0)) {
            case 'y': return "yyyy"+sep+"MM"+sep+"dd";
            case 'd': return "dd"+sep+"MM"+sep+"yyyy";
            default: return "MM"+sep+"dd"+sep+"yyyy";
        }
    }
    
    private static char getDateSeparator(String pattern) {
        if(pattern.indexOf('.') > -1)
            return '.';
        if(pattern.indexOf('/') > -1)
            return '/';
        return '-';
    }
    
    static void setDateFormat(String format) {
        DateFormat df = new SimpleDateFormat(format);
        LocaleSettings.getPreferences().put(DATE_FORMAT, format);
        logger.log(Level.INFO, "Default date format set to \"{0}\".", format);
    }
    
    private LocaleDateSettings() {}
}
