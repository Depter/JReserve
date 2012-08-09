package org.jreserve.project.system;

import java.awt.Image;
import java.util.List;
import org.openide.WizardDescriptor;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ElementCreatorWizard {

    public String getName();
    
    public void setWizardLookup(Lookup lookup);
    
    public List<WizardDescriptor.Panel> getPanels();
}
