package org.jreserve.database.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.explorer.DatabaseRootChildren;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Database/Database",
id = "org.jreserve.persistence.databaseexplorer.actions.DeleteDatabaseAction")
@ActionRegistration(iconBase = "resources/database_delete.png",
displayName = "#CTL_DeleteDatabaseAction")
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3533)
})
@Messages({
    "CTL_DeleteDatabaseAction=Delete Database",
    "# {0} - database name",
    "CTL_deleteQuestionOneDatabase=Do you want to delete database: {0}",
    "# {0} - names of databases",
    "CTL_deleteQuestionManyDatabase=Do you want to delete databases:{0}",
    "CTL_deleteQuestionTitle=Delete database",
    "# {0} - name of database",
    "MSG_DeleteDatabaseAction.dbopened=Database \"{0}\" can not be deleted, while it is connected!"
})
public final class DeleteDatabaseAction implements ActionListener {

    private final static Logger logger = Logging.getLogger(DeleteDatabaseAction.class.getName());
    
    private final List<AbstractDatabase> databases;

    public DeleteDatabaseAction(List<AbstractDatabase> context) {
        this.databases = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if(isUsedDatabase() || !confirmByUser())
            return;
        deleteDatabases();
        refreshDatabases();
    }
    
    private boolean confirmByUser() {
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
            getUserMessage(), Bundle.CTL_deleteQuestionTitle(), 
            NotifyDescriptor.YES_NO_OPTION);
        return DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.YES_OPTION;
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
    
    private String getUserMessage() {
        if(databases.size()<2)
            return Bundle.CTL_deleteQuestionOneDatabase(databases.get(0).getShortName());
        return Bundle.CTL_deleteQuestionManyDatabase(getDbNameList());
    }
    
    private String getDbNameList() {
        StringBuilder sb = new StringBuilder();
        for(AbstractDatabase db : databases) 
            sb.append("\n").append(db.getShortName());
        return sb.toString();
    }
    
    private void deleteDatabases() {
        for(AbstractDatabase database : databases) {
            try {
                logger.info("Deleting database: %s", database.getShortName());
                database.deleteDatabase();
            } catch (IOException ex) {
                logger.error(ex, "Unable to delete database '%s'!", database.getShortName());
                Exceptions.printStackTrace(ex);
                return;
            }
        }
    }
    
    private void refreshDatabases() {
        DatabaseUtil.refresh();
        DatabaseRootChildren.getInstance().refreshDatabases();
    }
}
