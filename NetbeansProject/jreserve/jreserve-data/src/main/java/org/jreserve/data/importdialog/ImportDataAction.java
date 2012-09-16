package org.jreserve.data.importdialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.entities.ClaimType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.data.importdialog.ImportDataAction"
)
@ActionRegistration(displayName = "#CTL_ImportDataAction")
@ActionReferences({
    @ActionReference(path = "Menu/Project", position = 1300),
    @ActionReference(path= "JReserve/Popup/ProjectRoot-ClaimTypeNode", position = 250)
})
@Messages({
    "CTL_ImportDataAction=Import data"
})
public class ImportDataAction implements ActionListener {
    
    private ClaimType claimType;
    
    public ImportDataAction(ClaimType claimType) {
        this.claimType = claimType;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
