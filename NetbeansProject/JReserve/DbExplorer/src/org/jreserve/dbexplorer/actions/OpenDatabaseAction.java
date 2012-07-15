/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.dbexplorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.branding.msg.MessageUtil;
import org.jreserve.database.api.DatabaseProvider;
import org.jreserve.dbexplorer.DbExplorerTopComponent;
import org.jreserve.dbexplorer.newdb.SelectDbProviderForm;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(
        category = "Database/DatabaseProvider",
        id = "org.nbtest.dbexplorer.actions.OpenDatabaseAction"
)
@ActionRegistration(
        iconBase = "org/nbtest/dbexplorer/actions/db_open.png",
        displayName = "#CTL_OpenDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3334)
})
@Messages({
    "CTL_OpenDatabaseAction=Open",
    "ERR_openError=Provider \"{0}\" is unable to open the database."
})
public final class OpenDatabaseAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ev) {
        DatabaseProvider provider = getProvider();
        if(provider != null)
            openDatabase(provider);
    }
    
    private DatabaseProvider getProvider() {
        DatabaseProvider provider = Utilities.actionsGlobalContext().lookup(DatabaseProvider.class);
        if(provider == null)
            provider = SelectDbProviderForm.getDatabaseProvider();
        return provider;
    }
    
    private void openDatabase(DatabaseProvider provider) {
        try {
            provider.openDatabase();
            DbExplorerTopComponent.refreshNodes(provider);
        } catch (Exception ex) {
            MessageUtil.error(Bundle.ERR_openError(provider.getName()), ex);
        }
    }    
}
