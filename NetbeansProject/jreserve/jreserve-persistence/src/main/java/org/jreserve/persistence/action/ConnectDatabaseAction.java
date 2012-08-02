package org.jreserve.persistence.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
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

    private final static Logger logger = Logging.getLogger(ConnectDatabaseAction.class.getName());
    
    private final AbstractDatabase database;

    public ConnectDatabaseAction(AbstractDatabase database) {
        this.database = database;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        //TODO check if there is an opened database, with unsaved changes.
        LoginDialog dialog = new LoginDialog(database);
        dialog.setVisible(true);
        if(dialog.isCancelled())
            return;
        
    }
}
