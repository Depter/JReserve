/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.persistence.connection;

import javax.swing.SwingUtilities;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.PersistenceDatabase;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        super.restored();
        PersistenceDatabase database = DatabaseUtil.getUsedDatabase();
        if(database != null)
            loginFromAwtThread(database);
    }
    
    private void loginFromAwtThread(final PersistenceDatabase database) {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                HibernateUtil.startupLogin(database);
            }
        });
    }

    @Override
    public void close() {
        super.close();
        HibernateUtil.close();
    }
}
