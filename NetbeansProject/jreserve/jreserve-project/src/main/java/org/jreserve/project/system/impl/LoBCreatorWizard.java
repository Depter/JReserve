package org.jreserve.project.system.impl;

import java.util.List;
import org.jreserve.project.system.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ElementCreatorWizard.Registration(
    category=ElementCreatorWizard.Category.PROJECT,
    iconBase="resources/lob.png",
    displayName="#LoBCreatorWizard.name"
)
@Messages({
    "LoBCreatorWizard.name=LoB"
})
public class LoBCreatorWizard implements ElementCreatorWizard {

    @Override
    public void setWizardLookup(Lookup lookup) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
