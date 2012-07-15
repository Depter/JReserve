package org.jreserve.derbydatabase.createdialog;

import java.io.File;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.jreserve.branding.msg.MessageUtil;
import org.jreserve.derbydatabase.DerbyDatabase;
import org.jreserve.derbydatabase.DerbyDbCreator;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
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
    private DerbyDbCreator dbFactory;
    private DerbyDatabase database;

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
        if(database != null)
            settings.putProperty(CreateDatabaseWizard.DATABASE, database);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void prepareValidation() {
        panel.startCreatingDb();
        dbFactory = new DerbyDbCreator(dbPath, panel.getUserName(), panel.getPassword(), true);
    }

    @Override
    public void validate() throws WizardValidationException {
        DerbyDatabase db = null;
        try {
            db = dbFactory.createDb();
        } catch (Exception ex) {
            MessageUtil.error(Bundle.ERR_createDb(), ex);
            throw new WizardValidationException(panel, Bundle.ERR_createDb(), Bundle.ERR_createDb());
        } finally {
            finnishWorking(db);
        }
    }
    
    private void finnishWorking(final DerbyDatabase db) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.finishCreatingDb();
                database = db;
            }
        });
    }
}
