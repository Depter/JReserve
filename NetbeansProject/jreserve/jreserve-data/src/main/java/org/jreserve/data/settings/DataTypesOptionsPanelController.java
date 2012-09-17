/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.data.settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(location = "Advanced",
displayName = "#AdvancedOption_DisplayName_DataTypes",
keywords = "#AdvancedOption_Keywords_DataTypes",
keywordsCategory = "Advanced/DataTypes")
@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_DataTypes=Data types", "AdvancedOption_Keywords_DataTypes=Data Types; data types"})
public final class DataTypesOptionsPanelController extends OptionsPanelController {

    private DataTypesPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    @Override
    public void update() {
        getPanel().load();
        changed = false;
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
        return getPanel().valid();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private DataTypesPanel getPanel() {
        if(panel == null)
            panel = new DataTypesPanel(this);
        return panel;
    }

    void changed() {
        if(!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
