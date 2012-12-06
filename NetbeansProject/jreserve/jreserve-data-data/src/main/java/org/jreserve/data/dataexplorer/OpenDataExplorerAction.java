package org.jreserve.data.dataexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.data.dataexplorer.OpenDataExplorerAction"
)
@ActionRegistration(
    iconBase = "resources/database.png",
    displayName = "#CTL_OpenDataExplorerAction"
)
@ActionReferences({
    @ActionReference(path="JReserve/Popup/ProjectRoot-ClaimTypeNode", position=260)
})
@Messages("CTL_OpenDataExplorerAction=View data")
public class OpenDataExplorerAction implements ActionListener {

    private ClaimType claimType;
    
    public OpenDataExplorerAction(ClaimType claimType) {
        this.claimType = claimType;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectElement<ClaimType> element = Utilities.actionsGlobalContext().lookup(ProjectElement.class);
        DataExplorerTopComponent explorer = DataExplorerRegistry.getComponent(element);
        if(!explorer.isOpened())
            explorer.open();
        explorer.requestActive();
    }

}
