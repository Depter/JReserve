package org.jreserve.data.projectdatatype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.entities.Project;
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
    category = "JReserve/Data",
    id = "org.jreserve.data.projectdatatype.ProjectDataTypeAction"
)
@ActionRegistration(displayName = "#CTL_ProjectDataTypeAction")
@ActionReferences({
    @ActionReference(path= "JReserve/Popup/ProjectRoot-ProjectNode", position = 250)
})
@Messages({
    "CTL_ProjectDataTypeAction=Data types"
})
public class ProjectDataTypeAction implements ActionListener {

    private Project project;
    
    public ProjectDataTypeAction(Project project) {
        this.project = project;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectDataTypeDialog.showDialog(project);
    }

}
