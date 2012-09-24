package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.data.DataImportWizard;
import org.jreserve.project.entities.Project;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class WizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    private VisualPanel panel;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    WizardPanel() {
    }
    
    @Override
    public VisualPanel getComponent() {
        if(panel == null)
            panel = new VisualPanel();
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        Project project = (Project) wiz.getProperty(DataImportWizard.PROJECT_PROPERTY);
        getComponent().setProject(project);
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        if(!listeners.contains(cl))
            listeners.add(cl);
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        listeners.remove(cl);
    }

    @Override
    public void validate() throws WizardValidationException {
    }
}
