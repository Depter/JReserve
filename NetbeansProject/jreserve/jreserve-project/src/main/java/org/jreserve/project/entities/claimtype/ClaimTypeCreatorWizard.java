package org.jreserve.project.entities.claimtype;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@ElementCreatorWizard.Registration(
    category=ElementCreatorWizard.Category.PROJECT,
    iconBase="resources/claim_type.png",
    displayName="#ClaimTypeCreatorWizard.name"
)
@Messages({
    "ClaimTypeCreatorWizard.name=Claim Type",
    "ClaimTypeCreatorWizard.description=Create a new claim type. A claim type represents the different kind of claims within a LoB."
})
public class ClaimTypeCreatorWizard implements ElementCreatorWizard {

    private List<WizardDescriptor.Panel> panels = null;
    
    @Override
    public String getDescription() {
        return Bundle.ClaimTypeCreatorWizard_description();
    }

    @Override
    public List<Panel> getPanels() {
        if(panels == null)
            createPanels();
        return panels;
    }

    private void createPanels() {
        panels = new ArrayList<Panel>(1);
        panels.add(new ClaimTypeCreatorWizardPanel());
    }
    
}
