package org.jreserve.apcsample;

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
    category= ElementCreatorWizard.Category.OTHER,
    displayName="#LBL.ApcSampleCreator.Name",
    iconBase="resources/lob.png"
)
@Messages({
    "LBL.ApcSampleCreator.Name=APC Sample",
    "LBL.ApcSampleCreator.Description=Sample data from APC Module IV, 2012."
})
public class ApcSampleCreator implements ElementCreatorWizard {

    @Override
    public String getDescription() {
        return Bundle.LBL_ApcSampleCreator_Description();
    }
    

    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>(1);
        panels.add(new ApcSampleWizardPanel());
        return panels;
    }

}
