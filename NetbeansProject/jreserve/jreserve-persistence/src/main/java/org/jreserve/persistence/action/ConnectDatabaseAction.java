package org.jreserve.persistence.action;

import org.jreserve.persistence.connection.LoginDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.hibernate.SessionFactory;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.persistence.connection.SessionFactoryBuilder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "Database/Database",
    id = "org.jreserve.persistence.action.ConnectDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/connect.png",
    displayName = "#CTL_ConnectDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3633)
})
@NbBundle.Messages({
    "CTL_ConnectDatabaseAction=Connect Database"
})
public class ConnectDatabaseAction implements ActionListener {
    
    private final PersistenceDatabase database;

    public ConnectDatabaseAction(PersistenceDatabase database) {
        this.database = database;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        if(database.isUsed())
            return;
        //TODO check if there is an opened database, with unsaved changes.
        LoginDialog login = login();
        if(login != null) {
            setUsed();
            createSessionFactory(login.getSessionFactory());
        }
    }
    
    private boolean setUsed() {
        try {
            database.setUsed(true);
            return true;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }
    }
    
    private LoginDialog login() {
        LoginDialog dialog = new LoginDialog(database);
        dialog.setVisible(true);
        return dialog.isCancelled()? null : dialog;
    }
    
    private void createSessionFactory(SessionFactory sessionFactory) {
        sessionFactory.close();
    }
}
