package org.jreserve.estimates.visual;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.estimates.EstimateContainerFactory;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "MSG.NameSelectWizardPanel.NoProject=Project not selected!",
    "MSG.NameSelectWizardPanel.NoName=Field 'Name' is empty!",
    "MSG.NameSelectWizardPanel.NameExists=Name already exists!"
})
public class NameSelectWizardPanel implements WizardDescriptor.Panel<WizardDescriptor>, ChangeListener  {
    
    public final static String PROP_NAME = "ESTIMATE_NAME_PROPERTY";
    public final static String PROP_DESCRIPTION = "ESTIMATE_DESCRIPTION_PROPERTY";
    public final static String PROP_PROJECT = "PROJECT_PROPERTY";
    public final static String PROP_PROJECT_ELEMENT = "PROJECT_ELEMENT_PROPERTY";
    
    private NameSelectVisualPanel component;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new NameSelectVisualPanel();
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
        Lookup lkp = (Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP);
        if((!setProject(lkp) || setClaimType(lkp))) 
            setLoB(lkp);
        setEstimateName();
        setEstimateDescription();
        validatePanel();
    }
    
    private boolean setProject(Lookup lookup) {
        Project project = lookup.lookup(Project.class);
        if(project != null) {
            component.setProject(project);
            return true;
        }
        return false;
    }
    
    private boolean setClaimType(Lookup lookup) {
        ClaimType ct = lookup.lookup(ClaimType.class);
        if(ct != null) {
            component.setClaimType(ct);
            return true;
        }
        return false;
    }
    
    private boolean setLoB(Lookup lookup) {
        LoB lob = lookup.lookup(LoB.class);
        if(lob != null) {
            component.setLoB(lob);
            return true;
        }
        return false;
    }
    
    private void setEstimateName() {
        String name = (String) wizard.getProperty(PROP_NAME);
        component.setEstimateName(name);
    }
    
    private void setEstimateDescription() {
        String description = (String) wizard.getProperty(PROP_DESCRIPTION);
        component.setDescription(description);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
        data.putProperty(PROP_PROJECT_ELEMENT, component.getProjectElement());
        data.putProperty(PROP_PROJECT, component.getProject());
        data.putProperty(PROP_NAME, component.getEstimateName());
        data.putProperty(PROP_DESCRIPTION, component.getDescription());
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
        isValid = checkProject() && checkName();
        if(isValid)
            showError(null);
    }
    
    private boolean checkProject() {
        Project project = component.getProject();
        if(project == null) {
            showError(Bundle.MSG_NameSelectWizardPanel_NoProject());
            return false;
        }
        return true;
    }
    
    private void showError(String msg) {
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean checkName() {
        String name = component.getEstimateName();
        return checkNameNotEmpty(name) && checkNewName(name); 
    }
    
    private boolean checkNameNotEmpty(String name) {
        if(name == null || name.trim().length() == 0) {
            showError(Bundle.MSG_NameSelectWizardPanel_NoName());
            return false;
        }
        return true;
    }
    
    private boolean checkNewName(String name) {
        ProjectElementContainer container = getDataContainer();
        if(container.containsName(name)) {
            showError(Bundle.MSG_NameSelectWizardPanel_NameExists());
            return false;
        }
        return true;
    }
    
    private ProjectElementContainer getDataContainer() {
        ProjectElement element = component.getProjectElement();
        element = element.getFirstChild(EstimateContainerFactory.POSITION, ProjectElementContainer.class);
        return (ProjectElementContainer) element.getValue();
    }

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
}
