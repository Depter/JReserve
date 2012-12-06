package org.jreserve.project.entities.project;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.jreserve.project.factories.ProjectFactory;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ProjectCreatorWizardPanel.nolob=Line of business not selected!",
    "MSG.ProjectCreatorWizardPanel.noclaimtype=Claim type not selected!",
    "MSG.ProjectCreatorWizardPanel.noname=Field 'name' is empty!",
    "# {0} - name of project, typed by the user",
    "# {1} - name of LoB, typed by the user",
    "# {2} - name of ClaimType, typed by the user",
    "MSG.ProjectCreatorWizardPanel.nameexists=Project \"{0}\" already exists in \"{1}\\{2}\"!",
    "MSG.ProjectCreatorWizardPanel.projectcreated=Created."
})
class ProjectCreatorWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor>{
    
    private final static Logger logger = Logger.getLogger(ProjectCreatorWizardPanel.class.getName());
    
    final static String LOB_VALUE = "SELECTED_LOB";
    final static String CLAIM_TYPE_VALUE = "SELECTED_CLAIM_TYPE";
    final static String PROJECT_ELEMENT_VALUE = "SELECTED_PROJECT_ELEMENT";
    final static String NAME_VALUE = "SELECTED_NAME";
    final static String DESCRIPTION_VALUE = "SELECTED_DESCRIPTION";
    
    private ProjectCreatorVisualPanel panel;
    private WizardDescriptor wizard = null;
    private boolean isValid = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    ProjectCreatorWizardPanel() {
    }
    
    @Override
    public Component getComponent() {
        if(panel == null)
            createComponent();
        return panel;
    }
    
    private void createComponent() {
        panel = new ProjectCreatorVisualPanel();
        panel.addPropertyChangeListener(new PanelListener());
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        setSelections((Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP));
        validatePanel();
    }
    
    private void setSelections(Lookup lookup) {
        if(!setClaimType(lookup))
            setLoB(lookup);
    }
    
    private boolean setClaimType(Lookup lookup) {
        ClaimType ct = lookup.lookup(ClaimType.class);
        if(ct != null) {
            panel.setClaimType(ct);
            return true;
        }
        return false;
    }
    
    private void setLoB(Lookup lookup) {
        LoB lob = lookup.lookup(LoB.class);
        if(lob != null)
            panel.setLoB(lob);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
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
    public boolean isValid() {
        return isValid;
    }
    
    private void validatePanel() {
        isValid = checkLoB() && checkClaimType() && checkName();
        if(isValid)
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, null);
        fireChanged();
    }
    
    private boolean checkLoB() {
        if(getLoB() != null)
            return true;
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_ProjectCreatorWizardPanel_nolob());
        return false;
    }
    
    private LoB getLoB() {
        return (LoB) panel.getClientProperty(LOB_VALUE);
    }
    
    private boolean checkClaimType() {
        if(getClaimType() != null)
            return true;
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_ProjectCreatorWizardPanel_noclaimtype());
        return false;
    }
    
    private ClaimType getClaimType() {
        return (ClaimType) panel.getClientProperty(CLAIM_TYPE_VALUE);
    }
    
    private boolean checkName() {
        String name = getName();
        return checkNameSet(name) && checkNewName(name);
    }
    
    private String getName() {
        return (String) panel.getClientProperty(NAME_VALUE);
    }
        
    private boolean checkNameSet(String name) {
        if(name != null )
            return true;
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_ProjectCreatorWizardPanel_noname());
        return false;
    }
        
    private boolean checkNewName(String name) {
        ClaimType ct = getClaimType();
        if(!containsName(ct, name))
            return true;
        String err = Bundle.MSG_ProjectCreatorWizardPanel_nameexists(name, ct.getLoB().getName(), ct.getName());
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, err);
        return false;
    }
    
    private boolean containsName(ClaimType ct, String name) {
        for(Project project : ct.getProjects())
            if(project.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }

    private void fireChanged() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    @Override
    public void validate() throws WizardValidationException {
        ClaimType ct = getClaimType();
        org.jreserve.project.system.ProjectElement parent = getParentElement();
        org.jreserve.project.system.ProjectElement child = createProject(ct, getName());
        parent.addChild(child);
    }
    
    private org.jreserve.project.system.ProjectElement getParentElement() {
        Object parent = panel.getClientProperty(PROJECT_ELEMENT_VALUE);
        return (org.jreserve.project.system.ProjectElement) parent;
    }
    
    private org.jreserve.project.system.ProjectElement createProject(ClaimType ct, String name) throws WizardValidationException {
        try {
            ProjectFactory factory = new ProjectFactory(ct, name);
            factory.setDescription((String) panel.getClientProperty(DESCRIPTION_VALUE));
            return SessionTask.withOpenSession(factory);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, String.format("Unable to create Project '%s' in ClaimType '%s'!", name, ct.getPath()), ex);
            throw new WizardValidationException(panel, ex.getMessage(), ex.getLocalizedMessage());
        }
    }
    
    private class PanelListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(!interestingProperty(evt))
                return;
            validatePanel();
        }
        
        private boolean interestingProperty(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            return LOB_VALUE.equals(propertyName) ||
                   CLAIM_TYPE_VALUE.equals(propertyName) ||
                   NAME_VALUE.equals(propertyName);
        }
    }

}
