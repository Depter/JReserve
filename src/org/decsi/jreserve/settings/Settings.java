package org.decsi.jreserve.settings;

import java.util.Properties;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Settings {
    
    private final static String SETTINGS_FILE = "/res/settings.properties";
    private final static String PERSISTANCE = "persistance.manager";
    private final static Properties SETTINGS = new Properties();
    
    public static void load() throws Exception {
        try {
            SETTINGS.load(Settings.class.getResourceAsStream(SETTINGS_FILE));
        } catch (Exception ex) {
            //TODO log exception.
            ex.printStackTrace();
            throw ex;
        }
    }
    
    public static String getPersistanceClass() {
        return get(PERSISTANCE);
    }
    
    public static String get(String key) {
        return SETTINGS.getProperty(key);
    }
}
