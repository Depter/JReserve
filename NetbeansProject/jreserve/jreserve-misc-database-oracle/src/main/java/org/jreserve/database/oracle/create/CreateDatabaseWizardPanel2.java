package org.jreserve.database.oracle.create;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.jreserve.database.oracle.OracleDatabaseProvider;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@Messages({
    "ERR_driverNotFound=Driver \"{0}\" not found!",
    "ERR_connectionError=Unable to establish connection!",
    "ERR_fileNotCreated=Unable to create database property file!"
})
class CreateDatabaseWizardPanel2 implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private CreateDatabaseVisualPanel2 panel;
    private boolean isValid = true;
    private DummyDatabase db;
    private OracleDatabaseFactory dbFacory;
    private boolean dbCreated;
    
    private String userName;
    private String pass;
    
    @Override
    public CreateDatabaseVisualPanel2 getComponent() {
        if (panel == null)
            panel = new CreateDatabaseVisualPanel2();
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

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        db = (DummyDatabase) wiz.getProperty(CreateDatabaseWizard.DATABASE);
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        if(db != null)
            wiz.putProperty(CreateDatabaseWizard.DATABASE, db);
        wiz.putProperty(CreateDatabaseWizard.DATABASE_CREATED, dbCreated);
    }

    @Override
    public void prepareValidation() {
        panel.startCreatingDb();
        userName = panel.getUserName();
        pass = panel.getPassword();
        dbFacory = new OracleDatabaseFactory(db, userName, pass);
    }

    @Override
    public void validate() throws WizardValidationException {
        boolean success = false;
        try {
            dbFacory.createDb();
            success = true;
        } catch (ClassNotFoundException ex) {
            String msg = Bundle.ERR_driverNotFound(OracleDatabaseProvider.DRIVER_NAME);
            throw new WizardValidationException(panel, msg, msg);
        } catch (SQLException ex) {
            String msg = Bundle.ERR_connectionError() + "\n" + ex.getLocalizedMessage();
            throw new WizardValidationException(panel, msg, msg);
        } catch(IOException ex) {
            String msg = Bundle.ERR_fileNotCreated() + "\n" + ex.getLocalizedMessage();
            throw new WizardValidationException(panel, msg, msg);
        } finally {
            finnishWorking(success);
        }
    }
    
    private void finnishWorking(final boolean success) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.finishCreatingDb();
                dbCreated = success;
            }
        });
    }
    
}
