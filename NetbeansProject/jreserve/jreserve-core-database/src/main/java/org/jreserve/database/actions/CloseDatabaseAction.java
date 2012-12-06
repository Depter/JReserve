package org.jreserve.database.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.explorer.DatabaseRootChildren;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "JReserve/Database/Database",
    id = "org.jreserve.database.actions.CloseDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/database_close.png",
    displayName = "#CTL_CloseDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3433),
    @ActionReference(path = "JReserve/Popup/DatabaseRoot-Databases-Database", position = 200)
})
@Messages({
    "CTL_CloseDatabaseAction=Close Database",
    "# {0} - name of database",
    "MSG_CloseDatabaseAction_databaseconnected=Database \"{0}\" can not be closed, while it is connected!"
})
public final class CloseDatabaseAction implements ActionListener {

    private final static Logger logger = Logger.getLogger(CloseDatabaseAction.class.getName());
    
    private final List<AbstractDatabase> databases;

    public CloseDatabaseAction(List<AbstractDatabase> context) {
        this.databases = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        if(isUsedDatabase())
            return;
        closeDatabases();
        refresh();
    }
    
    private boolean isUsedDatabase() {
        for(AbstractDatabase db : databases)
            if(db.isUsed()) {
                notifyOpenedDatabase(db);
                return true;
            }
        return false;
    }
    
    private void notifyOpenedDatabase(AbstractDatabase db) {
        String msg = Bundle.MSG_DeleteDatabaseAction_dbopened(db.getName());
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.WARNING_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
    
    private void closeDatabases() {
        for (AbstractDatabase database : databases)
            if(!saveDatabase(database))
                break;
    }
    
    private boolean saveDatabase(AbstractDatabase database) {
        logger.log(Level.INFO, "Closing database: \"{0}\"", database.getShortName());
        try {
            closeDatabase(database);
            return true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Unable to save database '%s'!", database.getShortName()), ex);
            Exceptions.printStackTrace(ex);
            return false;
        }
    }
    
    private void closeDatabase(AbstractDatabase database) throws IOException {
        database.setOpened(false);
        if(database.isUsed())
            database.setUsed(false);
        database.save();
    }
    
    private void refresh() {
        DatabaseUtil.refresh();
        DatabaseRootChildren.getInstance().refreshDatabases();
    }
}
