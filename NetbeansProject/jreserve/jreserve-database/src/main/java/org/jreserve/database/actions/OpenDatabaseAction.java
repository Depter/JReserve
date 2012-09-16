package org.jreserve.database.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.explorer.DatabaseRootChildren;
import org.jreserve.database.util.DatabaseChildren;
import org.jreserve.database.util.SelectDatabaseDialog;
import org.jreserve.database.util.SelectDatabaseDialog.SelectionMode;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "JReserve/Database/Database",
    id = "org.jreserve.database.actions.OpenDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/database_open.png",
    displayName = "#CTL_OpenDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3233),
    @ActionReference(path = "JReserve/Popup/DatabaseRoot-Databases", position = 100),
    @ActionReference(path = "JReserve/Popup/DatabaseRoot-Databases-Database", position = 100)
})
@Messages({
    "CTL_OpenDatabaseAction=Open Database",
    "CTL_openDatabaseActionDialogTitle=Open Database"
})
public final class OpenDatabaseAction implements ActionListener {

    private final static Logger logger = Logging.getLogger(OpenDatabaseAction.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AbstractDatabase> databases = getDatabasesToOpen();
        if(!databases.isEmpty())
            openDatabases(databases);
    }
    
    private List<AbstractDatabase> getDatabasesToOpen() {
        String title = Bundle.CTL_openDatabaseActionDialogTitle();
        DatabaseChildren children = DatabaseChildren.getFixedChildren(DatabaseUtil.getClosedDatabases());
        SelectionMode mode = SelectionMode.MULTIPLE_INTERVAL;
        return new SelectDatabaseDialog(title, children, mode).getDatabases();
    }
    
    private void openDatabases(List<AbstractDatabase> databases) {
        for(AbstractDatabase database : databases)
            if(!openDatabase(database))
                break;
        refresh();
    }
    
    private boolean openDatabase(AbstractDatabase database) {
        logger.info("Opening database: %s", database.getShortName());
        database.setOpened(true);
        try {
            database.save();
            return true;
        } catch (IOException ex) {
            logger.error(ex, "Unable to save database '%s'!", database.getShortName());
            Exceptions.printStackTrace(ex);
            return false;
        }
    }
    
    private void refresh() {
        DatabaseUtil.refresh();
        DatabaseRootChildren.getInstance().refreshDatabases();
    }
}
