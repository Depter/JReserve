package org.jreserve.estimate.expectedratio.creator;

import org.jreserve.estimate.core.visual.NameSelectWizardPanel;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.triangle.entities.Triangle;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
  "# {0} - triangle name",
  "LBL.ERNameSelectWizardPanel.DefaultName=ER {0}"  
})
public class ERNameSelectWizardPanel extends NameSelectWizardPanel {

    @Override
    protected void setEstimateName() {
        String name = getDefaultName();
        if(name != null)
            component.setEstimateName(name);
    }

    private String getDefaultName() {
        Triangle triangle = getSelectedTriangle();
        if(triangle == null)
            return null;
        return Bundle.LBL_ERNameSelectWizardPanel_DefaultName(triangle.getName());
    }
    
    private Triangle getSelectedTriangle() {
        Lookup lkp = (Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP);
        Project project = lkp.lookup(Project.class);
        Triangle triangle = lkp.lookup(Triangle.class);
        if(triangle != null && triangle.getProject().equals(project) && triangle.isTriangle())
            return triangle;
        return null;
    }
}
