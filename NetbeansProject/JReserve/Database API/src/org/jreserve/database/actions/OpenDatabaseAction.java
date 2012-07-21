package org.jreserve.database.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.explorer.DatabaseRootChildren;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Database/Database/Root",
id = "org.jreserve.persistence.databaseexplorer.actions.OpenDatabaseAction")
@ActionRegistration(iconBase = "resources/database_open.png",
displayName = "#CTL_OpenDatabaseAction")
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3233)
})
@Messages("CTL_OpenDatabaseAction=Open Database")
public final class OpenDatabaseAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AbstractDatabase> databases = new OpenDatabaseDialog().getDatabases();
        if(!databases.isEmpty())
            openDatabases(databases);
    }
    
    private void openDatabases(List<AbstractDatabase> databases) {
        for(AbstractDatabase database : databases)
            if(!openDatabase(database))
                break;
        refresh();
    }
    
    private boolean openDatabase(AbstractDatabase database) {
        database.setOpened(true);
        try {
            database.save();
            return true;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }
    }
    
    private void refresh() {
        DatabaseUtil.refresh();
        DatabaseRootChildren.getInstance().refreshDatabases();
    }
}
