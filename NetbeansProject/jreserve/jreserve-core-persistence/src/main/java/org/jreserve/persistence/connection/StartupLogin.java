package org.jreserve.persistence.connection;

import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.database.util.DatabaseChildren;
import org.jreserve.database.util.SelectDatabaseDialog;
import org.jreserve.database.util.SelectDatabaseDialog.SelectionMode;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "CTL_SelectDbDialogTitle=Select database to use"
})
class StartupLogin {
    
    StartupLogin() {
    }
    
    void login() {
        PersistenceDatabase database = DatabaseUtil.getUsedDatabase();
        if(database != null)
            loginFromAwtThread(database);
        else
            userSelectsDbFromAwtThread();
    }
    
    private void loginFromAwtThread(final PersistenceDatabase database) {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                HibernateUtil.startupLogin(database);
            }
        });
    }
    
    private void userSelectsDbFromAwtThread() {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                PersistenceDatabase db = userSelectsDatabase();
                if(db != null)
                    HibernateUtil.startupLogin(db);
            }
        });
    }
    
    private PersistenceDatabase userSelectsDatabase() {
        List<AbstractDatabase> openedDbs = DatabaseUtil.getOpenedDatabases();
        if(openedDbs.isEmpty())
            return null;
        return selectOpenedDatabase(openedDbs);
    }
    
    
    private PersistenceDatabase selectOpenedDatabase(List<AbstractDatabase> openedDbs) {
        SelectDatabaseDialog dialog = createDialog(openedDbs);
        List<AbstractDatabase> dbs = dialog.getDatabases();
        return dbs.isEmpty()? null : dbs.get(0);
    }
    
    private SelectDatabaseDialog createDialog(List<AbstractDatabase> openedDbs) {
        DatabaseChildren children = DatabaseChildren.getFixedChildren(openedDbs);
        SelectionMode mode = SelectionMode.SINGLE;
        String title = Bundle.CTL_SelectDbDialogTitle();
        return new SelectDatabaseDialog(title, children, mode);
    }
}
