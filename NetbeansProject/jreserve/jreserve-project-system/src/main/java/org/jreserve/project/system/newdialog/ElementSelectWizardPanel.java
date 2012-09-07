package org.jreserve.project.system.newdialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.NotificationLineSupport;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_ElementSelectWizardPanel.err.select=Select 'Category' and 'Element'."
})
class ElementSelectWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    final static String ELEMENT_CREATOR_WIZARD = "ELEMENT_CREATOR_WIZARD";
    
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private final PropertyChangeListener elementWizardListenr = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(ELEMENT_CREATOR_WIZARD.equals(evt.getPropertyName()))
                validateSelection();
        }
    };
    private WizardDescriptor wizard;
    private ElementSelectVisualPanel component;
    private boolean isValid = false;
    
    @Override
    public ElementSelectVisualPanel getComponent() {
        if(component == null) {
            component = new ElementSelectVisualPanel();
            component.addPropertyChangeListener(elementWizardListenr);
        }
        return component;
    }
    
    private void validateSelection() {
        isValid = (component.getClientProperty(ELEMENT_CREATOR_WIZARD) != null);
        if(isValid) {
            clearErrorMessage();
        } else {
            setErrorMessage(Bundle.LBL_ElementSelectWizardPanel_err_select());
        }
        fireChangeEvent();
    }
    
    private void setErrorMessage(final String msg) {
        NotificationLineSupport nls = wizard.getNotificationLineSupport();
        nls.setErrorMessage(msg);
    }
    
    private void clearErrorMessage() {
        NotificationLineSupport nls = wizard.getNotificationLineSupport();
        nls.clearMessages();
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        if(!listeners.contains(l))
            listeners.add(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        this.wizard = wiz;
        setInitialErrorMessage();
        // use wiz.getProperty to retrieve previous panel state
    }

    private void setInitialErrorMessage() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                validateSelection();
                //setErrorMessage(Bundle.LBL_ElementSelectWizardPanel_err_select());
            }
        });
    }
    
    @Override
    public void storeSettings(WizardDescriptor wiz) {
        Object value = component.getClientProperty(ELEMENT_CREATOR_WIZARD);
        wiz.putProperty(ELEMENT_CREATOR_WIZARD, value);
    }
}
