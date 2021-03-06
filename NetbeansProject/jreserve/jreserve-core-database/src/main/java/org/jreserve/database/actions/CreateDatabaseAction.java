package org.jreserve.database.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.database.DatabaseProvider;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.explorer.DatabaseRootChildren;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "JReserve/Database/Provider",
    id = "org.jreserve.database.actions.CreateDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/database_add.png",
    displayName = "#CTL_CreateDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3333),
    @ActionReference(path = "JReserve/Popup/DatabaseRoot-Providers-Provider", position = 200)
})
@Messages(
    "CTL_CreateDatabaseAction=New Database"
)
public final class CreateDatabaseAction implements ActionListener {

    private final DatabaseProvider provider;

    public CreateDatabaseAction(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if(!provider.createDatabase())
            return;
        DatabaseUtil.refresh();
        DatabaseRootChildren.getInstance().refreshDatabases();
    }
}
