package org.jreserve.project.entities.lob;

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
    category=ElementCreatorWizard.Category.PROJECT,
    iconBase="resources/lob.png",
    displayName="#LoBCreatorWizard.name",
    position=10
)
@Messages({
    "LoBCreatorWizard.name=LoB",
    "LoBCreatorWizard.description=Create a new line of business (LoB)."
})
public class LoBCreatorWizard implements ElementCreatorWizard {
    
    private List<WizardDescriptor.Panel> panels;
    
    @Override
    public String getDescription() {
        return Bundle.LoBCreatorWizard_description();
    }
    
    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        if(panels == null)
            createPanels();
        return panels;
    }
    
    private void createPanels() {
        panels = new ArrayList<WizardDescriptor.Panel>();
        panels.add(new LoBCreatorWizardPanel());
    }
}
