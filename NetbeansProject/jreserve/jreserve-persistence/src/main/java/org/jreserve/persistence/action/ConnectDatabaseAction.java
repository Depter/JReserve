package org.jreserve.persistence.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.persistence.connection.HibernateUtil;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "JReserve/Database/Database",
    id = "org.jreserve.persistence.action.ConnectDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/connect.png",
    displayName = "#CTL_ConnectDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3633),
    @ActionReference(path = "JReserve/Popup/DatabaseRoot-Databases-Database", position = 210)
})
@NbBundle.Messages({
    "CTL_ConnectDatabaseAction=Connect Database"
})
public class ConnectDatabaseAction implements ActionListener {
    
    private final static boolean APP_CLOSING = false;
    private final PersistenceDatabase database;

    public ConnectDatabaseAction(PersistenceDatabase database) {
        this.database = database;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        if(database.isUsed() || !HibernateUtil.close(APP_CLOSING))
            return;
        HibernateUtil.login(database);
    }    
}
