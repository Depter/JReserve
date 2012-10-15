package org.jreserve.triangle.createdialog;

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
    displayName="#LBL.TriangleImportWizard.Name",
    iconBase="resources/triangle.png",
    position=100
)
@Messages({
    "LBL.TriangleImportWizard.Name=Triangle",
    "LBL.TriangleImportWizard.Description=Create new triangle from existing data."
})
public class TriangleCreatorWizard implements ElementCreatorWizard {
    
    private List<WizardDescriptor.Panel> panels = null;

    @Override
    public String getDescription() {
        return Bundle.LBL_TriangleImportWizard_Description();
    }

    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        if(panels == null)
            createPanels();
        return panels;
    }
    
    private void createPanels() {
        panels = new ArrayList<WizardDescriptor.Panel>(2);
        panels.add(new NameSelectWizardPanel(true));
        panels.add(new TriangleFormatWizard());
    }
}
