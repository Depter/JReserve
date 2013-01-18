package org.jreserve.triangle.visual.createdialog;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class DataCreatorWizard implements ElementCreatorWizard {
    
    private final String description;
    private final boolean isTriangle;
    private List<WizardDescriptor.Panel> panels = null;

    protected DataCreatorWizard(String description, boolean isTriangle) {
        this.description = description;
        this.isTriangle = isTriangle;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<WizardDescriptor.Panel> getPanels() {
        if(panels == null)
            createPanels();
        return panels;
    }
    
    private void createPanels() {
        panels = new ArrayList<WizardDescriptor.Panel>(2);
        panels.add(new NameSelectWizardPanel(isTriangle));
        panels.add(new TriangleFormatWizardPanel(isTriangle));
    }
}
