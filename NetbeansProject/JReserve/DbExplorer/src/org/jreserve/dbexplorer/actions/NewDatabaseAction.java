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

@ActionID(category = "Database/DatabaseProvider",
id = "org.nbtest.dbexplorer.actions.NewDatabaseAction")
@ActionRegistration(
        displayName = "#CTL_NewDatabaseAction",
        iconBase = "org/nbtest/dbexplorer/actions/db_add.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3333)
})
@Messages({
    "CTL_NewDatabaseAction=New",
    "ERR_createError=Provider \"{0}\" is unable to create database."
})
public final class NewDatabaseAction implements ActionListener {
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DatabaseProvider provider = getProvider();
        if(provider != null)
            createDatabase(provider);
    }
    
    private DatabaseProvider getProvider() {
        DatabaseProvider provider = Utilities.actionsGlobalContext().lookup(DatabaseProvider.class);
        if(provider == null)
            provider = SelectDbProviderForm.getDatabaseProvider();
        return provider;
    }
    
    private void createDatabase(DatabaseProvider provider) {
        try {
            provider.createDatabase();
            DbExplorerTopComponent.refreshNodes(provider);
        } catch (Exception ex) {
            
            MessageUtil.error(Bundle.ERR_createError(provider.getName()), ex);
        }
    }    
}
