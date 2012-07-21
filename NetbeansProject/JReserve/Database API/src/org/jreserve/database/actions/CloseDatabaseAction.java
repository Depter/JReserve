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
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Database/Database",
    id = "org.jreserve.persistence.databaseexplorer.actions.CloseDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/database_close.png",
    displayName = "#CTL_CloseDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3433)
})
@Messages({
    "CTL_CloseDatabaseAction=Close Database"
})
public final class CloseDatabaseAction implements ActionListener {

    private final List<AbstractDatabase> context;

    public CloseDatabaseAction(List<AbstractDatabase> context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        for (AbstractDatabase database : context) {
            if(!saveDatabase(database))
                break;
        }
        refresh();
    }
    
    private boolean saveDatabase(AbstractDatabase database) {
        database.setOpened(false);
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
