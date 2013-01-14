package org.jreserve.estimate.core.visual;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.triangle.entities.Triangle;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TriangleSelectWizardPanel.Triangle.Empty=Triangle not selected!"
})
public abstract class TriangleSelectWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor>, ChangeListener  {
    
    final static String PROP_TRIANGLE = "SELECTED TRIANGLE";
    
    private TriangleSelectVisualPanel component;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;

    @Override
    public Component getComponent() {
        if(component == null) {
            component = new TriangleSelectVisualPanel();
            component.addChangeListener(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor data) {
        this.wizard = data;
        setProject();
        validatePanel();
    }
    
    private void setProject() {
        ProjectElement project = (ProjectElement) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT);
        component.setProject(project);
        setTriangle();
    }
    
    private void setTriangle() {
        Lookup lkp = (Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP);
        Project project = (Project) wizard.getProperty(NameSelectWizardPanel.PROP_PROJECT);
        Triangle triangle = lkp.lookup(Triangle.class);
        if(triangle != null && triangle.getProject().equals(project))
            component.setTriangle(triangle);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
    }

    @Override
    public boolean isValid() {
        return isValid;
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
    public void stateChanged(ChangeEvent e) {
        validatePanel();
        fireChange();
    }
    
    private void validatePanel() {
        isValid = checkTriangle();
        if(isValid)
            showError(null);
    }
    
    private boolean checkTriangle() {
        Triangle triangle = component.getTriangle();
        if(triangle == null) {
            showError(Bundle.MSG_TriangleSelectWizardPanel_Triangle_Empty());
            return false;
        }
        return true;
    }
    
    private void showError(String msg) {
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

}
