package org.decsi.jreserve.persistance;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Persistance {
    
    private static PersistanceManager persistance;
    
    public static void load(String className) {
        try {
            Class clazz = Class.forName(className);
            checkPersistanceClass(clazz);
            persistance = (PersistanceManager) clazz.newInstance();
        } catch (Exception ex) {
            //TODO log exception
        }
    }
    
    private static void checkPersistanceClass(Class clazz) {
        if(PersistanceManager.class.isAssignableFrom(clazz))
            return;
        String msg = "Class %s does not implement the Persistance interface!";
        throw new IllegalArgumentException(String.format(msg, clazz.getName()));
    }
    
    public static PersistanceManager getManager() {
        return persistance;
    }
}
