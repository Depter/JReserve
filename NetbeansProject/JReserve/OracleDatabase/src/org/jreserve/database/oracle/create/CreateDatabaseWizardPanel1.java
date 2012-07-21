package org.jreserve.database.oracle.create;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

class CreateDatabaseWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {


    private CreateDatabaseVisualPanel1 panel;
    private boolean isValid = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    @Override
    public CreateDatabaseVisualPanel1 getComponent() {
        if (panel == null) {
            panel = new CreateDatabaseVisualPanel1(this);
        }
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
        fireChange();
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
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        List<ChangeListener> dummies = new ArrayList<ChangeListener>(listeners);
        for(ChangeListener l : dummies)
            l.stateChanged(evt);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        DummyDatabase db = (DummyDatabase) wiz.getProperty(CreateDatabaseWizard.DATABASE);
        if(db != null)
            readSettings(db);
    }
    
    private void readSettings(DummyDatabase db) {
        panel.setServer(db.getHost());
        panel.setPort(db.getPort());
        panel.setSid(db.getSid());
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        String host = panel.getServer();
        int port = panel.getPort();
        String sid = panel.getSid();
        wiz.putProperty(CreateDatabaseWizard.DATABASE, new DummyDatabase(host, port, sid));
    }
}
