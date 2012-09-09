package org.jreserve.project.entities.project;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
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
    
    private final static Logger logger = Logging.getLogger(ProjectCreatorWizardPanel.class.getName());
    
    final static String LOB_VALUE = "SELECTED_LOB";
    final static String CLAIM_TYPE_VALUE = "SELECTED_CLAIM_TYPE";
    final static String PROJECT_ELEMENT_VALUE = "SELECTED_PROJECT_ELEMENT";
    final static String NAME_VALUE = "SELECTED_NAME";
    final static String DESCRIPTION_VALUE = "SELECTED_DESCRIPTION";
    
    private PersistenceUnit persistenceUnit;
    private ProjectCreatorVisualPanel panel;
    private WizardDescriptor wizard = null;
    private boolean isValid = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    ProjectCreatorWizardPanel() {
        persistenceUnit = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
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
        validatePanel();
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
        Project project = createProject(ct);
        parent.addChild(new ProjectElement(project));
    }
    
    private org.jreserve.project.system.ProjectElement getParentElement() {
        Object parent = panel.getClientProperty(PROJECT_ELEMENT_VALUE);
        return (org.jreserve.project.system.ProjectElement) parent;
    }
    
    private Project createProject(ClaimType ct) throws WizardValidationException {
        Session session = null;
        try {
            session = persistenceUnit.getSession();
            Project project = createPersistedProject(session, ct);
            logger.info("Project '%s' in ClaimType '%s' created.", project.getName(), ct);
            return project;
        } catch (Exception ex) {
            if(session != null)
                session.rollBackTransaction();
            logger.error(ex, "Unable to create Project '%s' in ClaimType '%s'!", getName(), ct);
            throw new WizardValidationException(panel, ex.getMessage(), ex.getLocalizedMessage());
        }
    }
    
    private Project createPersistedProject(Session session, ClaimType ct) {
        session.beginTransaction();
        session.update(ct);
        
        Project project = new Project(getName());
        setDescription(project);
        ct.addProject(project);
        session.persist(project);
        
        ChangeLog log = new ChangeLog(Bundle.MSG_ProjectCreatorWizardPanel_projectcreated());
        project.addChange(log);
        session.persist(log);
        
        session.comitTransaction();
        return project;
    }
    
    private void setDescription(Project project) {
        String description = (String) panel.getClientProperty(DESCRIPTION_VALUE);
        if(description != null)
            project.setDescription(description);
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
