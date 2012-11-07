package org.jreserve.persistence.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.persistence.connection.HibernateUtil;
import org.jreserve.persistence.visual.CloseConfirmDialog;
import org.netbeans.api.actions.Savable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "JReserve/Database/Database",
    id = "org.jreserve.persistence.action.DisconnectDatabaseAction"
)
@ActionRegistration(
    iconBase = "resources/disconnect.png",
    displayName = "#CTL_DisconnectDatabaseAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Database", position = 3634),
    @ActionReference(path = "JReserve/Popup/DatabaseRoot-Databases-Database", position = 220)
})
@Messages({
    "CTL_DisconnectDatabaseAction=Disconnect database",
    "LBL.DisconnectDatabaseAction.UnsavedTitle=Unsaved elements"
})
public class DisconnectDatabaseAction implements ActionListener {
    
    private final static boolean APP_CLOSING = false;
    private final PersistenceDatabase database;
    
    public DisconnectDatabaseAction(PersistenceDatabase database) {
        this.database = database;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(canClose())
            HibernateUtil.close(APP_CLOSING);
    }
    
    private boolean canClose() {
        if(Savable.REGISTRY.lookupAll(Savable.class).isEmpty())
            return true;
        String title = Bundle.LBL_DisconnectDatabaseAction_UnsavedTitle();
        return CloseConfirmDialog.save(Savable.REGISTRY, title);
    }
}
