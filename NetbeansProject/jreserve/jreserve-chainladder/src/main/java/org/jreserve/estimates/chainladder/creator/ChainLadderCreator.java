package org.jreserve.estimates.chainladder.creator;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.estimates.visual.NameSelectWizardPanel;
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
    displayName="#LBL.ChainLadderCreator.Name",
    iconBase="resources/estimate.png"
)
@Messages({
    "LBL.ChainLadderCreator.Name=Chain-Ladder",
    "LBL.ChainLadderCreator.Description=Standard chain-ladder method."
})
public class ChainLadderCreator implements ElementCreatorWizard {

    @Override
    public String getDescription() {
        return Bundle.LBL_ChainLadderCreator_Description();
    }

    @Override
    public List<Panel> getPanels() {
        List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>(2);
        panels.add(new NameSelectWizardPanel());
        panels.add(new TriangleSelectWizardPanel());
        return panels;
    }

}
