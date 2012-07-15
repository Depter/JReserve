package org.jreserve.oracledatabase.createdialog;

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
        readServer(wiz);
        readPort(wiz);
        readSid(wiz);
    }
    
    private void readServer(WizardDescriptor wiz) {
        String server = (String) wiz.getProperty(CreateDatabaseWizard.DB_SERVER);
        if(server != null)
            panel.setServer(server);
    }
    
    private void readPort(WizardDescriptor wiz) {
        String port = (String) wiz.getProperty(CreateDatabaseWizard.DB_PORT);
        if(port != null)
            panel.setPort(Integer.parseInt(port));
    }
    
    private void readSid(WizardDescriptor wiz) {
        String sid = (String) wiz.getProperty(CreateDatabaseWizard.DB_SERVER);
        if(sid != null)
            panel.setSid(sid);
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        storeServer(wiz);
        storePort(wiz);
        storeSid(wiz);
    }
    
    private void storeServer(WizardDescriptor wiz) {
        String server = panel.getServer();
        wiz.putProperty(CreateDatabaseWizard.DB_SERVER, server);
    }
    
    private void storePort(WizardDescriptor wiz) {
        int port = panel.getPort();
        wiz.putProperty(CreateDatabaseWizard.DB_PORT, port==0? null : port);
    }
    
    private void storeSid(WizardDescriptor wiz) {
        String sid = panel.getSid();
        wiz.putProperty(CreateDatabaseWizard.DB_SID, sid);
    }
}
