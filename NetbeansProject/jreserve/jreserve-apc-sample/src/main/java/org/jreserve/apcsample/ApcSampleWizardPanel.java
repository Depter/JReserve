package org.jreserve.apcsample;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.RootElement;
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
    "MSG.ApcSampleWizardPanel.NameEmpty=Field 'Name' is empty!",
    "# {0} - name",
    "MSG.ApcSampleWizardPanel.NameExists=Name \"{0}\" already exists!"
})
public class ApcSampleWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor>, ChangeListener {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;
    private ApcSampleVisualPanel panel;
    private WizardDescriptor wizard;
    private SampleBuilder builder;
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new ApcSampleVisualPanel();
            panel.addChangeListener(this);
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
        panel.readSettings(wizard);
        validateName();
    }

    @Override
    public void storeSettings(WizardDescriptor wizard) {
        panel.storeSettings(wizard);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        validateName();
    }
    
    private void validateName() {
        isValid = isNameValid(panel.getSampleName());
        if(isValid)
            showError(null);
        fireChange();
    }
    
    private boolean isNameValid(String name) {
        if(name == null) {
            showError(Bundle.MSG_ApcSampleWizardPanel_NameEmpty());
            return false;
        }
        return checkNewName(name);
    }
    
    private void showError(String msg) {
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean checkNewName(String name) {
        for(ProjectElement element : getRootChildren()) {
            if(isSameName(name, element)) {
                showError(Bundle.MSG_ApcSampleWizardPanel_NameExists(name));
                return false;
            }
        }
        return true;
    }
    
    private List<ProjectElement> getRootChildren() {
        return RootElement.getDefault().getChildren();
    }

    private boolean isSameName(String name, ProjectElement element) {
        Object o = element.getProperty(ProjectElement.NAME_PROPERTY);
        if(o instanceof String)
            return name.equalsIgnoreCase((String) o);
        return false;
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    @Override
    public void prepareValidation() {
        String name = panel.getSampleName();
        builder = new SampleBuilder(name);
    }

    @Override
    public void validate() throws WizardValidationException {
        Transaction tx = null;
        try {
            panel.startWorking();
            tx = beginTransaction();
            ProjectElement element = builder.createLoB();
            addElement(element);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            //TODO log
            throw new WizardValidationException(panel, "Unable to create sample.", "Unable to create sample.");
        }
    }
    
    private Transaction beginTransaction() {
        Session session = SessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        return tx;
    }
    
    private void addElement(final ProjectElement element) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RootElement.getDefault().addChild(element);
                panel.stopWorking();
            }
        });
    }
}
