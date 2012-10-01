package org.jreserve.data.importdialog.clipboardtable;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.DataImportWizard;
import org.jreserve.data.DataTable;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.WizardPanel.Error.EmptyDataType=Field 'Data type' is empty!",
    "MSG.WizardPanel.Error.EmptyDateFormat=Field 'Date format' is empty!",
    "MSG.WizardPanel.Error.IllegalDateFormat=Value of field 'Date format' is invalid!",
    "MSG.WizardPanel.Error.EmptyImportMethod=Field 'Import method' is empty!",
    "# {0} - row number",
    "MSG.WizardPanel.Error.InvalidDataRow=Invalid value at row {0}!",
    "MSG.WizardPanel.Error.NoData=There is no data selected to import."
})
class WizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    private VisualPanel panel;
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    private boolean isValid = false;
    private WizardDescriptor wizard;
    
    private ChangeListener panelListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            validatePanel();
        }
    };     
    
    WizardPanel() {
    }
    
    @Override
    public VisualPanel getComponent() {
        if(panel == null) {
            panel = new VisualPanel();
            panel.addChangeListeners(WeakListeners.change(panelListener, panel));
        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        this.wizard = wiz;
        ProjectElement<Project> element = (ProjectElement<Project>) wiz.getProperty(DataImportWizard.PROJECT_PROPERTY);
        getComponent().setProject(element);
    }
    
    @Override
    public void storeSettings(WizardDescriptor wiz) {
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        synchronized(listeners) {
            if(!listeners.contains(cl))
                listeners.add(cl);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        synchronized(listeners) {
            listeners.remove(cl);
        }
    }
    
    private void validatePanel() {
        checkInput();
        storeValues();
        fireChangeEvent();
    }
    
    private void checkInput() {
        isValid = checkDataTypeSet() && checkDateFormat() && 
                checkImportMethod() && checkInputData();
        if(isValid)
            clearErrorMsg();
    }
    
    private boolean checkDataTypeSet() {
        ProjectDataType dataType = panel.getDataType();
        if(dataType == null) {
            showError(Bundle.MSG_WizardPanel_Error_EmptyDataType());
            return false;
        }
        return true;
    }
    
    private void showError(String msg) {
        wizard.getNotificationLineSupport().setErrorMessage(msg);
    }
    
    private boolean checkDateFormat() {
        if(panel.getDateFormat() != null)
            return true;
        showError(Bundle.MSG_WizardPanel_Error_IllegalDateFormat());
        return false;
    }
    
    private boolean checkImportMethod() {
        if(panel.getImportType() == null) {
            showError(Bundle.MSG_WizardPanel_Error_EmptyImportMethod());
            return false;
        }
        return true;
    }
    
    private boolean checkInputData() {
        List<DataDummy> dummies = panel.getDummies();
        if(dummies.isEmpty()) {
            showError(Bundle.MSG_WizardPanel_Error_NoData());
            return false;
        }
        return checkDummies(dummies);
    }
    
    private boolean checkDummies(List<DataDummy> dummies) {
        boolean isTriangle = panel.getDataType().isTriangle();
        DataDummyValidator validator = new DataDummyValidator(dummies, isTriangle);
        validator.setDateFormat(panel.getDateFormat());
        validator.setNumberFormat(panel.getDecimalFormat());
        return checkInputData(validator);
    }
    
    private boolean checkInputData(DataDummyValidator validator) {
        if(validator.checkDummies())
            return true;
        int firstRow = validator.getErrorRows()[0]+1;
        showError(Bundle.MSG_WizardPanel_Error_InvalidDataRow(firstRow));
        return false;
    }
    
    private void clearErrorMsg() {
        wizard.getNotificationLineSupport().clearMessages();
    }
    
    private void fireChangeEvent() {
        List<ChangeListener> ls;
        synchronized(listeners) {
            ls = new ArrayList<ChangeListener>(listeners);
        }
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : ls)
            listener.stateChanged(evt);
    }
    
    private void storeValues() {
        if(isValid) {
            wizard.putProperty(DataImportWizard.DATA_TABLE_PROPERTY, buildTable());
            wizard.putProperty(DataImportWizard.IMPORT_METHOD_PROPERTY, panel.getImportType());
            wizard.putProperty(DataImportWizard.CUMMULATED_PROPERTY, panel.isCummulated());
        } else {
            wizard.putProperty(DataImportWizard.DATA_TABLE_PROPERTY, null);
            wizard.putProperty(DataImportWizard.IMPORT_METHOD_PROPERTY, null);
            wizard.putProperty(DataImportWizard.CUMMULATED_PROPERTY, null);
        }
    }
    
    private DataTable buildTable() {
        ProjectDataType dt = panel.getDataType();
        DataTable table = new DataTable(dt);
        DateFormat df = panel.getDateFormat();
        DoubleParser dp = new DoubleParser(panel.getDecimalFormat());
        for(DataDummy dummy : panel.getDummies())
            table.addData(getData(dummy, dt, df, dp));
        return table;
    }
    
    private Data getData(DataDummy dummy, ProjectDataType dt, DateFormat df, DoubleParser dp) {
        try {
            return new Data().setDataType(dt)
                 .setAccidentDate(df.parse(dummy.getAccident()))
                .setDevelopmentDate(df.parse(dummy.getDevelopment()))
                .setValue(dp.parse(dummy.getValue()));
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}
