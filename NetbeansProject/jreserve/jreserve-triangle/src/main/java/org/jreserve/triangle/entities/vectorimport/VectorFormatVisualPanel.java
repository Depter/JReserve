package org.jreserve.triangle.entities.vectorimport;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.importutil.AxisGeometryPanel;
import org.jreserve.triangle.importutil.ImportTableModel;
import org.jreserve.triangle.importutil.NameSelectWizardPanel;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.VectorFormatVisualPanel.PanelName=Geometry",
    "LBL.VectorFormatVisualPanel.AccidentGeometry=Accident",
    "LBL.VectorFormatVisualPanel.DevelopmentGeometry=Development"
})
class VectorFormatVisualPanel extends JPanel implements ChangeListener {

    private AxisGeometryPanel accidentGeometry;
    private AxisGeometryPanel developmentGeometry;
    private ImportTableModel tableModel = new ImportTableModel();
    private JTable table;
    
    private Date accidentStart;
    private Date accidentEnd;
    private Date developmentStart;
    private Date developmentEnd;
    
    VectorFormatVisualPanel() {
        setName(Bundle.LBL_VectorFormatVisualPanel_PanelName());
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        
        accidentGeometry = new AxisGeometryPanel();
        accidentGeometry.setBorder(createTitleBorder(Bundle.LBL_VectorFormatVisualPanel_AccidentGeometry()));
        accidentGeometry.addChangeListener(this);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx=0d; gc.weighty=0d;
        gc.insets = new Insets(0, 0, 5, 5);
        add(accidentGeometry, gc);
        
        developmentGeometry = new AxisGeometryPanel();
        developmentGeometry.setBorder(createTitleBorder(Bundle.LBL_VectorFormatVisualPanel_DevelopmentGeometry()));
        developmentGeometry.addChangeListener(this);
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 5, 0);
        add(developmentGeometry, gc);
        
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 2; gc.gridy = 0;
        gc.weightx=1d;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 5, 0);
        add(Box.createHorizontalGlue(), gc);
        
        table = new JTable(tableModel);
        table.setPreferredSize(new Dimension(250, 250));
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Double.class, DoubleRenderer.getTableRenderer());
        table.setDefaultRenderer(Date.class, DateRenderer.getTableRenderer());
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth=3;
        gc.weightx=1d; gc.weighty=1d;
        gc.fill=GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 0, 0, 0);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(250, 250));
        add(scroll, gc);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private Border createTitleBorder(String title) {
        Border outter = BorderFactory.createTitledBorder(title);
        Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        return BorderFactory.createCompoundBorder(outter, inner);
    }
    
    void readSettings(WizardDescriptor wizard) {
        List<Data> datas = (List<Data>) wizard.getProperty(NameSelectWizardPanel.PROP_DATA);
        tableModel.setDatas(datas);
        readDates(datas);
        initGeometry();
    }

    private void readDates(List<Data> datas) {
        if(datas == null)
            return;
        for(Data data : datas) {
            setAccidnetDates(data.getAccidentDate());
            setDevelopmentDates(data.getDevelopmentDate());
        }
    }
    
    private void setAccidnetDates(Date accidentDate) {
        if(accidentStart==null || accidentStart.after(accidentDate))
            accidentStart = accidentDate;
        if(accidentEnd==null || accidentEnd.before(accidentDate))
            accidentEnd = accidentDate;
    }
    
    private void setDevelopmentDates(Date developmentDate) {
        if(developmentStart==null || developmentStart.after(developmentDate))
            developmentStart = developmentDate;
        if(developmentEnd==null || developmentEnd.before(developmentDate))
            developmentEnd = developmentDate;
    }
    
    private void initGeometry() {
        accidentGeometry.setFromDate(accidentStart);
        developmentGeometry.setFromDate(developmentStart);
        developmentGeometry.setPeriods(1);
        developmentGeometry.setMonthPerStep(getDevelopmentMonthCount());
    }
    
    private int getDevelopmentMonthCount() {
        Calendar c = Calendar.getInstance();
        c.setTime(developmentStart);
        int count = 1;
        while(c.getTime().before(developmentEnd)) {
            c.add(Calendar.MONTH, 1);
            count++;
        }
        return count;
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        tableModel.setGeometry(getGeometry());
    }
    
    private TriangleGeometry getGeometry() {
        Date aStart = accidentGeometry.getFromDate();
        int aPeriods = accidentGeometry.getPeriods();
        int aMonths = accidentGeometry.getMonthPerStep();
        
        Date dStart = developmentGeometry.getFromDate();
        int dPeriods = developmentGeometry.getPeriods();
        int dMonths = developmentGeometry.getMonthPerStep();
        if(aStart==null || dStart==null || 
           aPeriods < 1 || dPeriods < 1 ||
           aMonths < 1 || dMonths < 1)
            return null;
        return new TriangleGeometry(aStart, aPeriods, aMonths, dStart, dPeriods, dMonths);
    }
}
