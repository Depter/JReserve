package org.jreserve.data.dataexplorer;

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
    category = "JReserve/ProjectSystem",
    id = "org.jreserve.data.dataexplorer.OpenDataExplorerAction"
)
@ActionRegistration(
    iconBase = "resources/database.png",
    displayName = "#CTL_OpenDataExplorerAction"
)
@ActionReferences({
    @ActionReference(path="JReserve/Popup/ProjectRoot-ProjectNode", position=260)
})
@Messages("CTL_OpenDataExplorerAction=View data")
public class OpenDataExplorerAction implements ActionListener {

    private Project project;
    
    public OpenDataExplorerAction(Project project) {
        this.project = project;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DataExplorerTopComponent explorer = DataExplorerTopComponent.getComponent(project);
        if(!explorer.isOpened())
            explorer.open();
        explorer.requestActive();
    }

}
