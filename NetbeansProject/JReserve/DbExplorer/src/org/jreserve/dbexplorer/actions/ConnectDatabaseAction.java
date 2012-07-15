package org.jreserve.dbexplorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.jreserve.branding.msg.MessageUtil;
import org.jreserve.database.api.Database;
import org.jreserve.database.api.DatabaseProvider;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
       id="org.nbtest.dbexplorer.actions.ConnectDatabaseAction",
       category="Database"
)
@ActionRegistration(
        displayName = "#CTL_connectDatabase",
        iconBase = "org/nbtest/dbexplorer/actions/db_connect.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3337)
})
@Messages({
    "CTL_connectDatabase=Connect",
    "ERR_deselectConnectedDatabase=Unable to close connected database!"
})
public class ConnectDatabaseAction implements ActionListener {
    
    private final Database database;
    
    public ConnectDatabaseAction(Database database) {
        this.database = database;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(database.isSelected() || !closeSelected())
            return;
        try {
            database.setSelected(true);
        } catch (Exception ex) {
            MessageUtil.error("Unable to connect to database!", ex);
        }
    }
    
    private boolean closeSelected() {
        try {
            Database selected = getSelectedDatabase();
            if(selected == null)
                return true;
            selected.setSelected(false);
            return true;
        } catch (Exception ex) {
            MessageUtil.error(Bundle.ERR_deselectConnectedDatabase(), ex);
            return false;
        }
    }
    
    private Database getSelectedDatabase() throws Exception {
        List<DatabaseProvider> providers = (List<DatabaseProvider>) Lookup.getDefault().lookupAll(DatabaseProvider.class); 
        for(DatabaseProvider provider : providers) {
            Database selected = getSelectedDatabase(provider);
            if(selected != null)
                return selected;
        }
        return null;
    }
    
    private Database getSelectedDatabase(DatabaseProvider provider) throws Exception {
        for(Database db : provider.getDatabases())
            if(db.isSelected())
                return db;
        return null;
    }

}
