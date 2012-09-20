package org.jreserve.project.entities.lob;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.RootElement;
import org.openide.NotificationLineSupport;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_LoBCreatorWizardPanel.error.connection=There is no connected database!",
    "LBL_LoBCreatorWizardPanel.error.name.empty=Name is empty!",
    "# {0} - The name entered by the user",
    "LBL_LoBCreatorWizardPanel.error.name.exists=Name \"{0}\" already used!"
})
class LoBCreatorWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(LoBCreatorWizardPanel.class.getName());
    
    final static String LOB_NAME = "LoBCreatorWizardPanel_LOB_NAME";
    
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private final PropertyChangeListener listener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(LOB_NAME.equals(evt.getPropertyName()))
                validatePanel();
        }
    };
    
    private PersistenceUnit persistenceUnit;
    private LoBCreatorVisualPanel panel;
    private WizardDescriptor wizard;
    private boolean isValid = false;
    private List<LoB> lobs = null;
    
    LoBCreatorWizardPanel() {
         persistenceUnit = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
    }
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new LoBCreatorVisualPanel();
            panel.addPropertyChangeListener(listener);
        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    private void validatePanel() {
        if(checkPersistenceUnit())
            checkName();
        fireChangeEvent();
    }

    private boolean checkPersistenceUnit() {
        if(persistenceUnit != null)
            return true;
        setErrorMessage(Bundle.LBL_LoBCreatorWizardPanel_error_connection());
        return false;
    }
    
    private void setErrorMessage(String msg) {
        isValid = false;
        NotificationLineSupport nls = wizard.getNotificationLineSupport();
        nls.setErrorMessage(msg);
    }
    
    private void checkName() {
        String name = getLoBName();
        if(name != null) {
            checkNewName(name);
        } else {
            setErrorMessage(Bundle.LBL_LoBCreatorWizardPanel_error_name_empty());
        }
    }
    
    private String getLoBName() {
        return (String) panel.getClientProperty(LOB_NAME);
    }
    
    private void checkNewName(String name) {
        if(nameExists(name)) {
            setErrorMessage(Bundle.LBL_LoBCreatorWizardPanel_error_name_exists(name));
        } else {
            clearMessages();
        }
    }
    
    private boolean nameExists(String name) {
        for(LoB lob : getLoBs())
            if(lob.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
    
    private List<LoB> getLoBs() {
        if(lobs == null)
            lobs = RootElement.getDefault().getChildValues(LoB.class);
        return lobs;
    }
    
    private void clearMessages() {
        isValid = true;
        NotificationLineSupport nls = wizard.getNotificationLineSupport();
        nls.clearMessages();
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    @Override
    public void readSettings(WizardDescriptor wiz) {
        this.wizard = wiz;
        validatePanel();
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
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
    public void validate() throws WizardValidationException {
        ProjectElement parent = RootElement.getDefault();
        LoB lob = createLoB();
        ProjectElement child = new LoBElement(lob);
        parent.addChild(getIndex(parent.getChildren(), lob.getName()), child);
    }
    
    private int getIndex(List<ProjectElement> children, String name) {
        int index = 0;
        int size = children.size();
        while(index<size && isAfter(name, children.get(index).getValue()))
            index++;
        return index;
    }
    
    private boolean isAfter(String name, Object child) {
        if(child instanceof LoB) {
            String childName = ((LoB)child).getName();
            return childName.compareToIgnoreCase(name) < 0;
        }
        return false;
    }
    
    private LoB createLoB() throws WizardValidationException {
        Session session = null;
        try {
            session = persistenceUnit.getSession();
            LoB lob = createPersistedLoB(session);
            logger.log(Level.INFO, "LoB created: %s", lob.getName());
            return lob;
        } catch (Exception ex) {
            if(session != null)
                session.rollBackTransaction();
            logger.log(Level.SEVERE, String.format("Unable to create LoB with name '%s'!", getLoBName()), ex);
            throw new WizardValidationException(panel, ex.getMessage(), ex.getLocalizedMessage());
        }
    }
    
    private LoB createPersistedLoB(Session session) {
        session.beginTransaction();
        LoB lob = new LoB(getLoBName());
        session.persist(lob);
        session.comitTransaction();
        return lob;
    }
}
