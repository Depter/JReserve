package org.jreserve.estimate.expectedratio.creator;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.estimate.core.visual.NameSelectWizardPanel;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ElementCreatorWizard.Registration(
    category= ElementCreatorWizard.Category.ESTIMATE,
    displayName="#LBL.ExpectedRatioCreator.Name",
    iconBase="resources/estimate.png"
)
@Messages({
    "LBL.ExpectedRatioCreator.Name=Expected Ratio",
    "LBL.ExpectedRatioCreator.Description=Calculates the reserves based on expected claim ratios."
})
public class ExpectedRatioCreator implements ElementCreatorWizard {

    @Override
    public String getDescription() {
        return Bundle.LBL_ExpectedRatioCreator_Description();
    }

    @Override
    public List<Panel> getPanels() {
        List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>(2);
        panels.add(new NameSelectWizardPanel());
        panels.add(new DataSelectWizardPanel());
        return panels;
    }
}
