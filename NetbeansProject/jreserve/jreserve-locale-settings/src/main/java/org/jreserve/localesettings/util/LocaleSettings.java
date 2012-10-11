package org.jreserve.localesettings.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    
    public static Locale getLocale() {
        String language = getPreferences().get(LOCALE, null);
        Locale locale = getLocaleFromLanguage(language);
        return locale==null? Locale.getDefault() : locale;
    }
    
    private static Locale getLocaleFromLanguage(String language) {
        for(Locale locale : Locale.getAvailableLocales())
            if(locale.getISO3Language().equals(language))
                return locale;
        return null;
    }
    
    public static void setLocale(Locale locale) {
        String language = locale==null? null : locale.getISO3Language();
        logger.log(Level.INFO, "Language set to \"{0}\"", language);
        getPreferences().put(LOCALE, language);
    }
    
    static Preferences getPreferences() {
        return NbPreferences.forModule(LocaleSettings.class);
    }
    
    public static String getDefaultDateFormatString() {
        return LocaleDateSettings.getDefaultDateFormatString();
    }
    
    public static String getDefaultDateFormatString(Locale locale) {
        return LocaleDateSettings.getDefaultDateFormatString(locale);
    }
    
    public static DateFormat getDateFormat() {
        return LocaleDateSettings.getDateFormat();
    }
    
    public static String getDateFormatString() {
        return LocaleDateSettings.getDateFormatString();
    }
    
    public static void setDateFormat(String format) {
        LocaleDateSettings.setDateFormat(format);
    }
    
    public static String getDefaultDecimalFormatString() {
        return LocaleDecimalSettings.getDefaultDecimalFormatString();
    }
    
    public static String getDefaultDecimalFormatString(Locale locale) {
        return LocaleDecimalSettings.getDefaultDecimalFormatString(locale);
    }
    
    public static String getDecimalFormatString() {
        return LocaleDecimalSettings.getDecimalFormatString();
    }
    
    public static void setDecimalFormatString(String format) {
        LocaleDecimalSettings.setDecimalFormatString(format);
    }
    
    public static DecimalFormatSymbols getDefaultDecimalSymbols() {
        return LocaleDecimalSettings.getDefaultDecimalSymbols();
    }
    
    public static DecimalFormatSymbols getDefaultDecimalSymbols(Locale locale) {
        return LocaleDecimalSettings.getDefaultDecimalSymbols(locale);
    }
    
    public static DecimalFormatSymbols getDecimalSymbols() {
        return LocaleDecimalSettings.getDecimalSymbols();
    }
    
    public static char getDecimalSeparator() {
        return LocaleDecimalSettings.getDecimalSeparator();
    }
    
    public static void setDecimalSeparator(char separator) {
        LocaleDecimalSettings.setDecimalSeparator(separator);
    }
    
    public static char getGroupingSeparator() {
        return LocaleDecimalSettings.getGroupingSeparator();
    }
    
    public static void setGroupingSeparator(char separator) {
        LocaleDecimalSettings.setGroupingSeparator(separator);
    }
    
    public static DecimalFormat getDecimalFormat() {
        return LocaleDecimalSettings.getDecimalFormatter();
    }
    
    public static String getNaN() {
        return LocaleDecimalSettings.getNaN();
    }
    
    public static void setNaN(String nan) {
        LocaleDecimalSettings.setNaN(nan);
    }
}
