package org.jreserve.project.entities.project;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ElementCreatorWizard.Registration(
    category = ElementCreatorWizard.Category.PROJECT,
    displayName = "#ProjectCreatorWizard.name",
    iconBase = "resources/project.png",
    position=30
)
@Messages({
    "ProjectCreatorWizard.name=Project",
    "ProjectCreatorWizard.description=Create a new project for estimating reserves."
})
public class ProjectCreatorWizard implements ElementCreatorWizard {

    private List<WizardDescriptor.Panel> panels = null;
    
    @Override
    public String getDescription() {
        return Bundle.ProjectCreatorWizard_description();
    }

    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        if(panels == null)
            createPanels();
        return panels;
    }

    private void createPanels() {
        panels = new ArrayList<WizardDescriptor.Panel>(1);
        panels.add(new ProjectCreatorWizardPanel());
    }
}
