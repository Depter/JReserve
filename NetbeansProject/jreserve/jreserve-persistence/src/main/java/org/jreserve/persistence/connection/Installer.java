package org.jreserve.persistence.connection;

import org.openide.modules.ModuleInstall;

/**
 * Perform tasks related to startup and close.
 * 
 * <p>At startup:
 * <ul>
 *   <li>The Javassist classloader is {@link JavaassistClassLoader set}.</li>
 *   <li>if there is a databses, with used status, the 
 *       {@link LoginDialog LoginDialog} is shown to the user.</li>
 * </ul>
 * </p>
 * 
 * <p>After all modules are agreed to close, the Hibernate 
 * SessionFactory is closed, if there was any.</p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class Installer extends ModuleInstall {
    
    private final static boolean APP_CLOSING = true;
    
    @Override
    public void restored() {
        JavaassistClassLoader.initialize();
        new StartupLogin().login();
    }
    
    @Override
    public void close() {
        super.close();
        HibernateUtil.close(APP_CLOSING);
    }
    
}
