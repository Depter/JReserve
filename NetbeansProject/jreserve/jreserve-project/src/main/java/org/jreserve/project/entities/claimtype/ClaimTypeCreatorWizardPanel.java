package org.jreserve.project.entities.claimtype;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Session;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
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
    private final static Logger logger = Logger.getLogger(ClaimTypeCreatorWizardPanel.class.getName());
    
    //private PersistenceUnit persistenceUnit;
    private WizardDescriptor wizard;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private ClaimTypeCreatorVisualPanel panel;
    private boolean isValid = false;

    ClaimTypeCreatorWizardPanel() {
        //persistenceUnit = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
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
        setLoB((Lookup) wizard.getProperty(ElementCreatorWizard.PROP_ELEMENT_LOOKUP));
        validatePanel();
    }
    
    private void setLoB(Lookup lookup) {
        LoB lob = lookup.lookup(LoB.class);
        if(lob != null)
            panel.setLoB(lob);
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
        try {
            ClaimType ct = new ClaimTypeCreator(lob).getResult();
            logger.log(Level.INFO, "ClaimType \"{0}\" created.", ct.getPath());
            return ct;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, String.format("Unable to create ClaimType '%s' in LoB '%s'!", getName(), lob.getPath()), ex);
            throw new WizardValidationException(panel, ex.getMessage(), ex.getLocalizedMessage());
        }
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
    
    private class ClaimTypeCreator extends SessionTask<ClaimType> {
        
        private LoB lob;
        
        private ClaimTypeCreator(LoB lob) {
            this.lob = lob;
        }
        
        @Override
        protected ClaimType doTask() throws Exception {
            ClaimType ct = new ClaimType(getName());
            lob.addClaimType(ct);
            session.persist(ct);
            return ct;
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
                   NAME_VALUE.equals(propertyName);
        }
    }
}
