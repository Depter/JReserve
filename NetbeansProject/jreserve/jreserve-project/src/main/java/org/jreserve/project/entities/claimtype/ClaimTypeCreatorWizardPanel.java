package org.jreserve.project.entities.claimtype;

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
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG_ClaimTypeCreatorWizardPanel_nolob=Line of business not selected!",
    "MSG_ClaimTypeCreatorWizardPanel_noname=Field 'name' is empty!",
    "# {0} - name of claim type, typed by the user",
    "# {1} - name of LoB, selected by the user",
    "MSG_ClaimTypeCreatorWizardPanel_nameexists=Claim type \"{0}\" already exists in LoB \"{1}\"!"
})
class ClaimTypeCreatorWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    final static String LOB_VALUE = "SELECTED_LOB";
    final static String PROJECT_ELEMENT_VALUE = "SELECTED_PROJECT_ELEMENT";
    final static String NAME_VALUE = "SELECTED_NAME";
    private final static Logger logger = Logging.getLogger(ClaimTypeCreatorWizardPanel.class.getName());
    
    private PersistenceUnit persistenceUnit;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private ClaimTypeCreatorVisualPanel panel;
    private boolean isValid = false;

    ClaimTypeCreatorWizardPanel() {
        persistenceUnit = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
    }

    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new ClaimTypeCreatorVisualPanel();
            panel.addPropertyChangeListener(new PanelListener());
        }
        return panel;
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
    public void storeSettings(WizardDescriptor wizard) {
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
    
    private void validatePanel() {
        isValid = checkLoB() && checkName();
        if(isValid)
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, null);
        fireChanged();
    }
        
    private boolean checkLoB() {
        if(getLoB() != null)
            return true;
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_ClaimTypeCreatorWizardPanel_nolob());
        return false;
    }
    
    private LoB getLoB() {
        return (LoB) panel.getClientProperty(LOB_VALUE);
    }
    
    private boolean checkName() {
        String name = getName();
        return checkNameSet(name) && checkNewName(name);
    }
    
    private String getName() {
        return (String) panel.getClientProperty(NAME_VALUE);
    }
        
    private boolean checkNameSet(String name) {
        if(name != null)
            return true;
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_ClaimTypeCreatorWizardPanel_noname());
        return false;
    }
        
    private boolean checkNewName(String name) {
        LoB lob = getLoB();
        if(!containsName(lob, name))
            return true;
        String err = Bundle.MSG_ClaimTypeCreatorWizardPanel_nameexists(name, lob.getName());
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, err);
        return false;
    }
    
    private boolean containsName(LoB lob, String name) {
        for(ClaimType ct : lob.getClaimTypes())
            if(ct.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
        
    private void fireChanged() {
        ChangeEvent evt = new ChangeEvent(ClaimTypeCreatorWizardPanel.this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    @Override
    public void validate() throws WizardValidationException {
        LoB lob = getLoB();
        ProjectElement parent = getParentElement();
        ClaimType ct = createClaimType(lob);
        ProjectElement child = new ClaimTypeElement(ct);
        parent.addChild(getIndex(parent.getChildren(), ct.getName()), child);
    }
    
    private ProjectElement getParentElement() {
        Object parent = panel.getClientProperty(PROJECT_ELEMENT_VALUE);
        return (ProjectElement) parent;
    }
    
    private ClaimType createClaimType(LoB lob) throws WizardValidationException {
        Session session = null;
        try {
            session = persistenceUnit.getSession();
            ClaimType ct = createPersistedClaimType(session, lob);
            logger.info("ClaimType '%s' created.", ct.getPath());
            return ct;
        } catch (Exception ex) {
            if(session != null)
                session.rollBackTransaction();
            logger.error(ex, "Unable to create ClaimType '%s' in LoB '%s'!", getName(), lob.getPath());
            throw new WizardValidationException(panel, ex.getMessage(), ex.getLocalizedMessage());
        }
    }
    
    private ClaimType createPersistedClaimType(Session session, LoB lob) {
        session.beginTransaction();
        ClaimType ct = new ClaimType(getName());
        lob.addClaimType(ct);
        session.persist(ct);
        session.comitTransaction();
        return ct;
    }
    
    private int getIndex(List<ProjectElement> children, String name) {
        int index = 0;
        int size = children.size();
        while(index<size && isAfter(name, children.get(index).getValue()))
            index++;
        return index;
    }
    
    private boolean isAfter(String name, Object child) {
        if(child instanceof ClaimType) {
            String childName = ((ClaimType)child).getName();
            return childName.compareToIgnoreCase(name) < 0;
        }
        return false;
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
                   NAME_VALUE.equals(propertyName);
        }
    }
}
