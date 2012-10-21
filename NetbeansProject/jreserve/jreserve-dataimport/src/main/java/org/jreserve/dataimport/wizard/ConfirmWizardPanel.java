package org.jreserve.dataimport.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.DataImport;
import org.jreserve.data.DataTable;
import org.jreserve.data.ProjectDataType;
import org.jreserve.dataimport.DataImportWizard;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
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
    "MSG.ConfirmWizardPanel.NoClaimType=ClaimType not selected!",
    "MSG.ConfirmWizardPanel.NoImportMethod=Import method not selected!",
    "MSG.ConfirmWizardPanel.NoDate=There is no data specified to import!",
    "MSG.ConfirmWizardPanel.ImportError=Unable to import values!",
    "# {0} - the dbId of the data type",
    "# {1} - the name of the data type",
    "# {2} - the name of the import method",
    "MSG.ConfirmWizardPanel.ChangeLog=Imported values to \"{0} - {1}\", using method \"{2}\"."
})
class ConfirmWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private WizardDescriptor wizard;
    private ConfirmVisualPanel panel;
    private boolean isValid = false;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    private ClaimType claimType;
    private boolean cummulated;
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
        readData();
        validateData();
        fireChangeEvent();
    }
    
    private void readData() {
        readProject();
        importType = (DataImport.ImportType) wizard.getProperty(DataImportWizard.IMPORT_METHOD_PROPERTY);
        readCummulated();
        readTable();
    }
    
    private void readProject() {
        ProjectElement<ClaimType> element = (ProjectElement<ClaimType>) wizard.getProperty(DataImportWizard.CLAIM_TYPE_PROPERTY);
        if(element != null)
            claimType = element.getValue();
    }
    
    private void readCummulated() {
        Boolean c = (Boolean) wizard.getProperty(DataImportWizard.CUMMULATED_PROPERTY);
        cummulated = c==null? false : c;
    }

    private void readTable() {
        table = (DataTable) wizard.getProperty(DataImportWizard.DATA_TABLE_PROPERTY);
        if(cummulated && table != null)
            table.deCummulate();
    }
    
    private void validateData() {
        isValid = checkProject() && checkImportType() && checkDataTable();
        if(isValid)
            showError(null);
    }
    
    private boolean checkProject() {
        if(claimType != null)
            return true;
        showError(Bundle.MSG_ConfirmWizardPanel_NoClaimType());
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
        if(importType != null)
            return true;
        showError(Bundle.MSG_ConfirmWizardPanel_NoImportMethod());
        return false;
    }
    
    private boolean checkDataTable() {
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
