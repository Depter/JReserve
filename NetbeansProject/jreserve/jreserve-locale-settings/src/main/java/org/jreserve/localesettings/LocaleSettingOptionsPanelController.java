package org.jreserve.localesettings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@OptionsPanelController.SubRegistration(
    location = "Advanced",
    displayName = "#LBL.LocaleSetting.DisplayName",
    keywords = "#LBL.LocaleSetting.Keywords",
    keywordsCategory = "Advanced/LocaleSetting"
)
@Messages({
    "LBL.LocaleSetting.DisplayName=Locale Settings", 
    "LBL.LocaleSetting.Keywords=input setting;Input setting; Input Setting"
})
public class LocaleSettingOptionsPanelController extends OptionsPanelController implements ChangeListener {
    
    private LocaleSettingPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }
    
    private LocaleSettingPanel getPanel() {
        if(panel == null) {
            panel = new LocaleSettingPanel();
            panel.addChangeListener(WeakListeners.change(this, panel));
        }
        return panel;
    }

    @Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isValid() {
        return getPanel().isValidSettings();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public JComponent getComponent(Lookup lkp) {
        return getPanel();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    void changed() {
        if(!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        changed = true;
        changed();
    }
}
