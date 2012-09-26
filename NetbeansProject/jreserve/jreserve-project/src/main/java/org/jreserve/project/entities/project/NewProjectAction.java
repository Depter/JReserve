package org.jreserve.project.entities.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.project.system.management.NewElementWizard;
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
    id = "org.jreserve.project.entities.project.NewProjectAction"
)
@ActionRegistration(
    iconBase = "resources/project.png",
    displayName = "#CTL_NewProjectAction"
)
@ActionReferences({
    @ActionReference(path=NewElementWizard.DIRECT_ACTION_PATH, position=300)
})
@Messages("CTL_NewProjectAction=Project")
public class NewProjectAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NewElementWizard.showWizard(ElementCreatorWizard.Category.PROJECT, new ProjectCreatorWizard());
    }
}
