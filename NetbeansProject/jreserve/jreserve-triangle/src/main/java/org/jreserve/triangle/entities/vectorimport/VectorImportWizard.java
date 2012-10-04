package org.jreserve.triangle.entities.vectorimport;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.management.ElementCreatorWizard.Registration;
import org.jreserve.triangle.importutil.NameSelectWizardPanel;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Registration(
    category=Category.DATA, 
    displayName="#LBL.VectorImportWizard.Name",
    iconBase="resources/vector.png",
    position=100
)
@Messages({
    "LBL.VectorImportWizard.Name=Vector",
    "LBL.VectorImportWizard.Description=Create new vector from existing data."
})
public class VectorImportWizard implements ElementCreatorWizard {
    
    private List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>();
    
    @Override
    public String getDescription() {
        return Bundle.LBL_VectorImportWizard_Description();
    }

    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        if(panels.isEmpty()) {
            panels.add(new NameSelectWizardPanel(false));
        }
        return panels;
    }
}
