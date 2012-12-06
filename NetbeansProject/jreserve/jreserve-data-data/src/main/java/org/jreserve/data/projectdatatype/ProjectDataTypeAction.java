package org.jreserve.data.projectdatatype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "JReserve/Data",
    id = "org.jreserve.data.projectdatatype.ProjectDataTypeAction"
)
@ActionRegistration(displayName = "#CTL_ProjectDataTypeAction")
@ActionReferences({
    @ActionReference(path= "JReserve/Popup/ProjectRoot-ClaimTypeNode", position = 230)
})
@Messages({
    "CTL_ProjectDataTypeAction=Data types"
})
public class ProjectDataTypeAction implements ActionListener {

    private ClaimType claimType;
    
    public ProjectDataTypeAction(ClaimType claimType) {
        this.claimType = claimType;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectElement<ClaimType> element = getProjectElement();
        ProjectDataTypeDialog.showDialog(element);
    }
    
    private ProjectElement<ClaimType> getProjectElement() {
        Lookup lkp = Utilities.actionsGlobalContext();
        return lkp.lookup(ProjectElement.class);
    }

}
