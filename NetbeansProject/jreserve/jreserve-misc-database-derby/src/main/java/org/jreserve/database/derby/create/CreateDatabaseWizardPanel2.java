package org.jreserve.database.derby.create;

import java.io.File;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 * 
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "ERR_createDb=Unable to create database!"
})
class CreateDatabaseWizardPanel2 implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private CreateDatabaseVisualPanel2 panel;
    private File dbPath;
    private boolean isValid = true;
    private DerbyDatabaseFactory dbFactory;
    private boolean dbCreated;

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
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        readDbFile(settings);
        readUserName(settings);
    }
    
    private void readDbFile(WizardDescriptor wiz) {
        dbPath = new File((String) wiz.getProperty(CreateDatabaseWizard.DB_LOCATION));
        dbPath = new File(dbPath, (String) wiz.getProperty(CreateDatabaseWizard.DB_NAME));
    }
    
    private void readUserName(WizardDescriptor wiz) {
        String userName = (String) wiz.getProperty(CreateDatabaseWizard.DB_USER_NAME);
        if(userName!=null)
            panel.setUserName(userName);
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        String userName = panel.getUserName();
        if(userName != null)
            settings.putProperty(CreateDatabaseWizard.DB_USER_NAME, userName);
        settings.putProperty(CreateDatabaseWizard.DATABASE_CREATED, dbCreated);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void prepareValidation() {
        panel.startCreatingDb();
        String userName = panel.getUserName();
        String password = panel.getPassword();
        dbFactory = new DerbyDatabaseFactory(dbPath, userName, password);
    }

    @Override
    public void validate() throws WizardValidationException {
        boolean success = false;
        try {
            dbFactory.createDb();
            success = true;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            throw new WizardValidationException(panel, Bundle.ERR_createDb(), Bundle.ERR_createDb());
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
