package org.jreserve.database.derby.create;

import java.io.File;
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
    
    void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        List<ChangeListener> dummies = new ArrayList<ChangeListener>(listeners);
        for(ChangeListener l : dummies)
            l.stateChanged(evt);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        readLocation(wiz);
        readName(wiz);
    }
    
    private void readLocation(WizardDescriptor wiz) {
        String location = (String) wiz.getProperty(CreateDatabaseWizard.DB_LOCATION);
        if(location == null)
            location = new File(new File(System.getProperty("user.home")), "Desktop").getAbsolutePath();
        panel.setLocation(location);
    }
    
    private void readName(WizardDescriptor wiz) {
        String name = (String) wiz.getProperty(CreateDatabaseWizard.DB_NAME);
        if(name != null)
            panel.setDbName(name);
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        storeLocation(wiz);
        storeDbName(wiz);
    }
    
    private void storeLocation(WizardDescriptor wiz) {
        String location = panel.getLocationText();
        location = location.length()>0 ? location : null;
        wiz.putProperty(CreateDatabaseWizard.DB_LOCATION, location);
    }
    
    private void storeDbName(WizardDescriptor wiz) {
        String name = panel.getNameText();
        name = name.length()>0 ? name : null;
        wiz.putProperty(CreateDatabaseWizard.DB_NAME, name);
    }
}
