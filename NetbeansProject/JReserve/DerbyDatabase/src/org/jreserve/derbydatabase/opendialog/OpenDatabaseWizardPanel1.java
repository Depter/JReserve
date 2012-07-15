package org.jreserve.derbydatabase.opendialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.derbydatabase.DerbyDatabase;
import org.jreserve.derbydatabase.DerbyDbCreator;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

class OpenDatabaseWizardPanel1 implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private OpenDatabaseVisualPanel1 panel;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isValid = false;
    private DerbyDbCreator dbFactory;
    private DerbyDatabase database;
    
    @Override
    public OpenDatabaseVisualPanel1 getComponent() {
        if (panel == null)
            panel = new OpenDatabaseVisualPanel1(this);
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
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        if(database != null)
            wiz.putProperty(OpenDerbyDbWizard.DATABASE, database);
    }

    @Override
    public void prepareValidation() {
        panel.startOpeningDb();
        dbFactory = new DerbyDbCreator(panel.getDbFile(), panel.getUserName(), panel.getPassword(), false);
    }

    @Override
    public void validate() throws WizardValidationException {
        DerbyDatabase db = null;
        try {
            db = dbFactory.createDb();
        } catch (Exception ex) {
            throw new WizardValidationException(panel, ex.getMessage(), ex.getLocalizedMessage());
        } finally {
            finnishWorking(db);
        }
    }
    
    private void finnishWorking(final DerbyDatabase db) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.stopOpeningDb();
                OpenDatabaseWizardPanel1.this.database = db;
            }
        });
    }
}
