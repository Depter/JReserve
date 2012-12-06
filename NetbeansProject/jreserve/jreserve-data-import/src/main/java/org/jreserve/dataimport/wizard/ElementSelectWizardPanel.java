package org.jreserve.dataimport.wizard;

import java.awt.Component;
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
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ElementSelectWizardPanel.noimporter=Select import method!"
})
class ElementSelectWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    
    final static String DATA_IMPORT_WIZARD = "DATA_IMPORT_WIZARD";
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private WizardDescriptor wizard;
    private ElementSelectVisualPanel component;
    private boolean isValid = false;
    private final PropertyChangeListener elementWizardListenr = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(DATA_IMPORT_WIZARD.equals(evt.getPropertyName()))
                validateSelection();
        }
    };
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ElementSelectVisualPanel();
            component.addPropertyChangeListener(WeakListeners.propertyChange(elementWizardListenr, component));
        }
        return component;
    }
    
    private void validateSelection() {
        isValid = (component.getClientProperty(DATA_IMPORT_WIZARD) != null);
        if(isValid) {
            clearErrorMessage();
        } else {
            setErrorMessage(Bundle.MSG_ElementSelectWizardPanel_noimporter());
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
    public void readSettings(WizardDescriptor wiz) {
        this.wizard = wiz;
        setInitialErrorMessage();
    }

    private void setInitialErrorMessage() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                validateSelection();
            }
        });
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        Object value = component.getClientProperty(DATA_IMPORT_WIZARD);
        wiz.putProperty(DATA_IMPORT_WIZARD, value);
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

}
