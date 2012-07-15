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
id = "org.nbtest.dbexplorer.actions.CloseDatabaseAction")
@ActionRegistration(
        displayName = "#CTL_CloseDatabaseAction",
        iconBase = "org/nbtest/dbexplorer/actions/db_close.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3335)
})
@Messages({
    "CTL_CloseDatabaseAction=Close",
    "ERR_closeError=Unable to close database \"{0}\" from provider \"{1}\"."
})
public final class CloseDatabaseAction implements ActionListener {

    private final Database database;

    public CloseDatabaseAction(Database database) {
        this.database = database;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        //TODO check if opened
        DatabaseProvider provider = Utilities.actionsGlobalContext().lookup(DatabaseProvider.class);
        closeDatabase(provider);
    }
    
    private void closeDatabase(DatabaseProvider provider) {
        try {
            database.close();
            DbExplorerTopComponent.refreshNodes(provider);
        } catch (Exception ex) {
            MessageUtil.error(Bundle.ERR_closeError(database.getName(), provider.getName()), ex);
        }
    }
}
