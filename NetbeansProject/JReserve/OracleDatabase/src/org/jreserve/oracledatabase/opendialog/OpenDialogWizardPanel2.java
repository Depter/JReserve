/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.oracledatabase.opendialog;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.jreserve.oracledatabase.OracleDatabase;
import org.jreserve.oracledatabase.OracleProvider;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@Messages({
    "ERR_driverNotFound=Driver \'{0}\' not found!",
    "ERR_connectionError=Unable to establish connection!"
})
class OpenDialogWizardPanel2 implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private OpenDialogVisualPanel2 panel;

    private boolean isValid = true;
    private OracleDatabase database;
    private OracleDatabase outputDb = null;
    
    private String userName;
    private String pass;
    private String server;
    private int port;
    private String sid;

    @Override
    public OpenDialogVisualPanel2 getComponent() {
        if (panel == null)
            panel = new OpenDialogVisualPanel2();
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
        database = (OracleDatabase) wiz.getProperty(OpenDialogWizard.DATABASE);
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        // use wiz.putProperty to remember current panel state
    }

    @Override
    public void prepareValidation() {
        panel.startCreatingDb();
        userName = panel.getUserName();
        pass = panel.getPassword();
        server = database.getServer();
        port = database.getPort();
        sid = database.getName();
    }

    @Override
    public void validate() throws WizardValidationException {
        OracleDatabase db = null;
        try {
            Class.forName(OracleProvider.DRIVER);    
            String url = String.format(OracleDatabase.URL, server, port, sid);
            DriverManager.getConnection(url, userName, pass).close();
            db = database;
        } catch (ClassNotFoundException ex) {
            String msg = Bundle.ERR_driverNotFound(OracleProvider.DRIVER);
            throw new WizardValidationException(panel, msg, msg);
        } catch (SQLException ex) {
            String msg = Bundle.ERR_connectionError() + "\n" + ex.getLocalizedMessage();
            throw new WizardValidationException(panel, msg, msg);
        } finally {
            finnishWorking(db);
        }
    }
    
    private void finnishWorking(final OracleDatabase db) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.finishCreatingDb();
                outputDb = db;
            }
        });
    }
}
