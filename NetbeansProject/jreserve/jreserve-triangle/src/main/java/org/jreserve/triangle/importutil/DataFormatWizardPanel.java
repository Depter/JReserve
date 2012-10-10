package org.jreserve.triangle.importutil;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.model.DataRow;
import org.jreserve.data.model.DataTable;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DataFormatWizardPanel.NoData=There is no data selected!",
    "MSG.DataFormatWizardPanel.SaveError=Unable to save vector!"
})
public abstract class DataFormatWizardPanel implements WizardDescriptor.Panel<WizardDescriptor>, ChangeListener {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    protected DataFormatVisualPanel panel;
    private boolean isValid = false;
    protected WizardDescriptor wizard;
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = createPanel();
            panel.addChangeListener(this);
        }
        return panel;
    }

    protected abstract DataFormatVisualPanel createPanel();
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        List<Data> datas = (List<Data>) wizard.getProperty(NameSelectWizardPanel.PROP_DATA);
        panel.setData(datas);
        validatePanel();
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        validatePanel();
        fireChange();
    }
    
    private void validatePanel() {
        DataTable table = panel.getTable();
        isValid = checkTable(table);
        if(isValid)
            showError(null);
    }
    
    private boolean checkTable(DataTable table) {
        if(table == null || !validTable(table)) {
            showError(Bundle.LBL_DataFormatWizardPanel_NoData());
            return false;
        }
        return true;
    }
    
    private boolean validTable(DataTable table) {
        for(int r=0, count=table.getRowCount(); r<count; r++)
            if(validRow(table.getRow(r)))
                return true;
        return false;
    }
    
    private boolean validRow(DataRow row) {
        for(int c=0, count=row.getCellCount(); c<count; c++)
            if(!Double.isNaN(row.getCell(c).getValue()))
                return true;
        return false;
    }
    
    private void showError(String msg) {
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
}
