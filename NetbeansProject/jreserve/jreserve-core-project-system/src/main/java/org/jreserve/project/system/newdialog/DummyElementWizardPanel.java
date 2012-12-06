/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.project.system.newdialog;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

class DummyElementWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    private DummyElementVisualPanel component;

    @Override
    public DummyElementVisualPanel getComponent() {
        if(component == null)
            component = new DummyElementVisualPanel();
        return component;
    }
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
    }
}
