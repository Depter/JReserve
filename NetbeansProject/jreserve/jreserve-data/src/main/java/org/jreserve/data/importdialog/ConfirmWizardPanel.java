package org.jreserve.data.importdialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.DataImport;
import org.jreserve.data.DataImportWizard;
import org.jreserve.data.DataTable;
import org.openide.NotificationLineSupport;
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
    "MSG.ConfirmWizardPanel.NoProject=Project not selected!",
    "MSG.ConfirmWizardPanel.NoImportMethod=Import method not selected!",
    "MSG.ConfirmWizardPanel.NoDate=There is no data specified to import!",
    "MSG.ConfirmWizardPanel.ImportError=Unable to import values!"
})
class ConfirmWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private WizardDescriptor wizard;
    private ConfirmVisualPanel panel;
    private boolean isValid = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private DataTable table;
    private DataImport.ImportType importType;
    private ImportData importData;
    
    @Override
    public Component getComponent() {
        return getPanel();
    }
    
    private ConfirmVisualPanel getPanel() {
        if(panel == null)
            panel = new ConfirmVisualPanel();
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor data) {
        this.wizard = data;
        panel.readSettings(wizard);
        validateData();
        fireChangeEvent();
    }

    private void validateData() {
        isValid = checkProject() && checkImportType() && checkDataTable();
        if(isValid)
            showError(null);
    }
    
    private boolean checkProject() {
        if(wizard.getProperty(DataImportWizard.PROJECT_PROPERTY) != null)
            return true;
        showError(Bundle.MSG_ConfirmWizardPanel_NoProject());
        return false;
    }
    
    private void showError(String msg) {
        NotificationLineSupport nls = wizard.getNotificationLineSupport();
        if(msg == null) {
            nls.clearMessages();
        } else {
            nls.setErrorMessage(msg);
        }
    }
    
    private boolean checkImportType() {
        importType = (DataImport.ImportType) wizard.getProperty(DataImportWizard.IMPORT_METHOD_PROPERTY);
        if(importType != null)
            return true;
        showError(Bundle.MSG_ConfirmWizardPanel_NoImportMethod());
        return false;
    }
    
    private boolean checkDataTable() {
        table = (DataTable) wizard.getProperty(DataImportWizard.DATA_TABLE_PROPERTY);
        if(table != null && table.getDataCount() > 0)
            return true;
        showError(Bundle.MSG_ConfirmWizardPanel_NoImportMethod());
        return false;
    }
    
    @Override
    public void storeSettings(WizardDescriptor data) {
    }

    @Override
    public boolean isValid() {
        return isValid;
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
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }

    @Override
    public void prepareValidation() {
        panel.showProgressBar();
        synchronized(this) {
            importData = new ImportData(importType, table);
        }
    }

    @Override
    public void validate() throws WizardValidationException {
        boolean success = true;
        try {
            DataImport.importTable(importData.table, importData.importType);
        } catch (Exception ex) {
            success = false;
            String localized = Bundle.MSG_ConfirmWizardPanel_ImportError();
            throw new WizardValidationException(panel, ex.getMessage(), localized);
        } finally {
            finnishedValidating(success);
        }
    }
    
    private void finnishedValidating(final boolean success) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                isValid = success;
                panel.hideProgressBar();
            }
        });
        
    }
    
    private static class ImportData {
        private final DataImport.ImportType importType;
        private final DataTable table;
        
        ImportData(DataImport.ImportType importType, DataTable table) {
            this.importType = importType;
            this.table = table;
        }
    }
}
