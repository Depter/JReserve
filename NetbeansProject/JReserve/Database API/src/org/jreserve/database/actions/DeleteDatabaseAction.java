/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    "CTL_deleteQuestionOneDatabase=Do you want to delete database: {0}",
    "CTL_deleteQuestionManyDatabase=Do you want to delete databases:{0}",
    "CTL_deleteQuestionTitle=Delete database"
})
public final class DeleteDatabaseAction implements ActionListener {

    private final static Logger logger = Logging.getLogger(DeleteDatabaseAction.class.getName());
    
    private final List<AbstractDatabase> databases;

    public DeleteDatabaseAction(List<AbstractDatabase> context) {
        this.databases = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if(!confirmByUser())
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
