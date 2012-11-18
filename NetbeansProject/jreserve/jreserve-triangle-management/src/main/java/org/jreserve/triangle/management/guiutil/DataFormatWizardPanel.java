package org.jreserve.triangle.management.guiutil;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.triangle.widget.DataUtil;
import org.jreserve.triangle.widget.WidgetData;
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
    private boolean isValid = false;
    
    protected TriangleFormatVisualPanel panel;
    protected WizardDescriptor wizard;

    @Override
    public Component getComponent() {
        if (panel == null) {
            panel = createPanel();
            panel.addChangeListener(this);
        }
        return panel;
    }

    protected abstract TriangleFormatVisualPanel createPanel();

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor wizard) {
        this.wizard = wizard;
        List<ClaimValue> datas = (List<ClaimValue>) wizard.getProperty(NameSelectWizardPanel.PROP_DATA);
        ProjectDataType dt = (ProjectDataType) wizard.getProperty(NameSelectWizardPanel.PROP_DATA_TYPE);
        setFirstDate(datas);
        panel.triangle.addValueLayer(getWidgetData(datas));
        validate();
    }
    
    private List<WidgetData<Double>> getWidgetData(List<ClaimValue> datas) {
        List<ClaimValue> escaped = new ArrayList<ClaimValue>(datas);
        return DataUtil.convertDatas(escaped);
    }
    
    private void setFirstDate(List<ClaimValue> datas) {
        Date start = getFirstDate(datas);
        if(start != null)
            panel.geometrySetting.setStartDate(start);
    }
    
    private Date getFirstDate(List<ClaimValue> datas) {
        Date first = null;
        for(ClaimValue data : datas) {
            Date date = data.getAccidentDate();
            if(first==null || date.before(first))
                first = date;
        }
        return first;
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
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        validate();
        fireChange();
    }

    private void validate() {
        isValid = validateInput() && validateTable();
        if (isValid) {
            showError(null);
        }
    }

    private boolean validateInput() {
        if (panel.isInputValid()) {
            return true;
        }
        showError(panel.getErrorMsg());
        return false;
    }

    private boolean validateTable() {
        double[][] values = panel.triangle.flatten();
        if (values == null || !validTable(values)) {
            showError(Bundle.LBL_DataFormatWizardPanel_NoData());
            return false;
        }
        return true;
    }

    private boolean validTable(double[][] values) {
        for (double[] row : values)
            if (validRow(row))
                return true;
        return false;
    }

    private boolean validRow(double[] row) {
        for (double value : row)
            if(!Double.isNaN(value))
                return true;
        return false;
    }

    private void showError(String msg) {
        wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for (ChangeListener listener : new ArrayList<ChangeListener>(listeners)) {
            listener.stateChanged(evt);
        }
    }
    
    protected void clearProperties() {
        synchronized(wizard) {
            wizard.putProperty(NameSelectWizardPanel.PROP_DATA_NAME, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_DATA_TYPE, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_PROJECT, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_PROJECT_ELEMENT, null);
            wizard.putProperty(NameSelectWizardPanel.PROP_DATA_DESCRIPTION, null);
        }
    }
}
