package org.jreserve.triangle.entities.vectorimport;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 */
public class VectorFormatWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private VectorFormatVisualPanel panel;
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new VectorFormatVisualPanel();
        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor data) {
        panel.readSettings(data);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
    }

    @Override
    public boolean isValid() {
        return false;
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
}
