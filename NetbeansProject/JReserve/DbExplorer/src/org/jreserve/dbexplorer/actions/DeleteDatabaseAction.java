/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.dbexplorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.branding.msg.MessageUtil;
import org.jreserve.database.api.Database;
import org.jreserve.database.api.DatabaseProvider;
import org.jreserve.dbexplorer.DbExplorerTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(category = "Database",
id = "org.nbtest.dbexplorer.actions.DeleteDatabaseAction")
@ActionRegistration(
        displayName = "#CTL_DeleteDatabaseAction",
        iconBase = "org/nbtest/dbexplorer/actions/db_delete.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3336)
})
@Messages({
    "CTL_DeleteDatabaseAction=Delete",
    "ERR_deleteError=Unable to delete database \"{0}\" from provider \"{1}\"."
})
public final class DeleteDatabaseAction implements ActionListener {

    private final Database database;

    public DeleteDatabaseAction(Database database) {
        this.database = database;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        //TODO check if opened
        DatabaseProvider provider = Utilities.actionsGlobalContext().lookup(DatabaseProvider.class);
        deleteDatabase(provider);
    }
    
    private void deleteDatabase(DatabaseProvider provider) {
        try {
            database.delete();
            DbExplorerTopComponent.refreshNodes(provider);
        } catch (Exception ex) {
            MessageUtil.error(Bundle.ERR_deleteError(database.getName(), provider.getName()), ex);
        }
    }    
}
