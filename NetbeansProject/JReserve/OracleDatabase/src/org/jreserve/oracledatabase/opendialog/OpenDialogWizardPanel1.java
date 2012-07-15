package org.jreserve.oracledatabase.opendialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.oracledatabase.OracleDatabase;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

class OpenDialogWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    private OpenDialogVisualPanel1 panel;
    private boolean isValid = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    @Override
    public OpenDialogVisualPanel1 getComponent() {
        if (panel == null)
            panel = new OpenDialogVisualPanel1(this);
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }
    
    void setValid(boolean isValid) {
        this.isValid = isValid;
        fireChangeEvent();
    }

    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
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
        OracleDatabase db = (OracleDatabase) wiz.getProperty(OpenDialogWizard.DATABASE);
        panel.setDatabase(db);
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        OracleDatabase db = panel.getDatabase();
        if(db != null)
            wiz.putProperty(OpenDialogWizard.DATABASE, db);
    }
}
