package org.jreserve.triangle.importutil;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.management.ElementCreatorWizard;
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
    "MSG.NameSelectWizardPanel.NoProject=Project not selected!",
    "MSG.NameSelectWizardPanel.NoDataType=Data type not selected!",
    "MSG.NameSelectWizardPanel.NoName=Field 'Name' is empty!",
    "MSG.NameSelectWizardPanel.NameExists=Name already exists!"
})
public class NameSelectWizardPanel implements WizardDescriptor.Panel<WizardDescriptor>, ChangeListener {

    public final static String PROP_DATA_NAME = "DATA_NAME_PROPERTY";
    public final static String PROP_DATA_TYPE = "DATA_TYPE_PROPERTY";
    public final static String PROP_PROJECT = "PROJECT_PROPERTY";
    
    private boolean isTrinagle;
    
    private NameSelectVisualPanel component;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;
    
    public NameSelectWizardPanel(boolean isTriangle) {
        this.isTrinagle = isTriangle;
    }
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new NameSelectVisualPanel(isTrinagle);
            component.addChangeListener(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        Lookup lkp = (Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP);
        boolean set = setProject(lkp) || setClaimType(lkp) || setLoB(lkp);
        setDataType();
        setDataName();
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
    
    private void setDataType() {
        ProjectDataType dataType = (ProjectDataType) wizard.getProperty(PROP_DATA_TYPE);
        if(dataType != null)
            component.setDataType(dataType);
    }
    
    private void setDataName() {
        String name = (String) wizard.getProperty(PROP_DATA_NAME);
        if(name != null)
            component.setDataName(name);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
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
    public void stateChanged(ChangeEvent e) {
        validatePanel();
    }

    private void validatePanel() {
        isValid = checkProject() && checkDataType() && checkName();
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
    
    private boolean checkDataType() {
        ProjectDataType dataType = component.getDataType();
        if(dataType == null) {
            showError(Bundle.MSG_NameSelectWizardPanel_NoDataType());
            return false;
        }
        return true;
    }
    
    private boolean checkName() {
        String name = component.getDataName();
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
        showError(Bundle.MSG_NameSelectWizardPanel_NameExists());
        return false;
    }
    
    //private void fireChangeEvent() {}
}
