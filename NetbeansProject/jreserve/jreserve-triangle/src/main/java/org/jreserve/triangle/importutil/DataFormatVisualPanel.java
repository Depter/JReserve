package org.jreserve.triangle.importutil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.model.DataTable;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DataFormatVisualPanel.PanelName=Geometry",
    "LBL.DataFormatVisualPanel.AccidentGeometry=Accident",
    "LBL.DataFormatVisualPanel.DevelopmentGeometry=Development"
})
public class DataFormatVisualPanel extends JPanel implements PropertyChangeListener {

    public final static String PROPERTY_ACCIDENT_FROM = "ACCIDNET FROM DATE";
    public final static String PROPERTY_ACCIDENT_PERIODS = "ACCIDNET PERIODS";
    public final static String PROPERTY_ACCIDENT_MONTHS = "ACCIDNET MONTHS";
    public final static String PROPERTY_DEVELOPMENT_FROM = "DEVELOPMENT FROM DATE";
    public final static String PROPERTY_DEVELOPMENT_PERIODS = "DEVELOPMENT PERIODS";
    public final static String PROPERTY_DEVELOPMENT_MONTHS = "DEVELOPMENT MONTHS";
    
    protected AxisGeometryPanel accidentGeometry;
    protected AxisGeometryPanel developmentGeometry;
    protected ImportTableModel tableModel = new ImportTableModel();
    protected JTable table;
    
    private Date accidentStart;
    private Date accidentEnd;
    private Date developmentStart;
    private Date developmentEnd;
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public DataFormatVisualPanel() {
        setName(Bundle.LBL_DataFormatVisualPanel_PanelName());
        initComponents();
    }

    protected void initComponents() {
        setLayout(new GridBagLayout());
        
        accidentGeometry = new AxisGeometryPanel();
        accidentGeometry.setBorder(createTitleBorder(Bundle.LBL_DataFormatVisualPanel_AccidentGeometry()));
        accidentGeometry.addPropertyChangeListener(this);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx=0d; gc.weighty=0d;
        gc.insets = new Insets(0, 0, 5, 5);
        add(accidentGeometry, gc);
        
        developmentGeometry = new AxisGeometryPanel();
        developmentGeometry.setBorder(createTitleBorder(Bundle.LBL_DataFormatVisualPanel_DevelopmentGeometry()));
        developmentGeometry.addPropertyChangeListener(this);
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
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth=3;
        gc.weightx=1d; gc.weighty=1d;
        gc.fill=GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 0, 0, 0);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(250, 250));
        table.setBackground(scroll.getBackground());
        add(scroll, gc);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private Border createTitleBorder(String title) {
        Border outter = BorderFactory.createTitledBorder(title);
        Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        return BorderFactory.createCompoundBorder(outter, inner);
    }

    public void setData(List<Data> datas) {
        setRenderers();
        tableModel.setDatas(datas);
        readDates(datas);
        initGeometry();
    }
    
    private void setRenderers() {
        table.setDefaultRenderer(Double.class, new DoubleTriangleTableRenderer(tableModel));
        table.setDefaultRenderer(Date.class, DateRenderer.getTableRenderer());
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
    
    protected void initGeometry() {
        accidentGeometry.setFromDate(accidentStart);
        developmentGeometry.setFromDate(developmentStart);
        developmentGeometry.setPeriods(1);
        developmentGeometry.setMonthPerStep(getDevelopmentMonthCount(developmentStart));
    }
    
    protected int getDevelopmentMonthCount(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int count = 1;
        while(c.getTime().before(developmentEnd)) {
            c.add(Calendar.MONTH, 1);
            count++;
        }
        return count;
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
    
    public DataTable getTable() {
        return tableModel.getTable();
    }
    
    public Date getAccidentStartDate() {
        return accidentGeometry.getFromDate();
    }
    
    public Date getDevelopmentStartDate() {
        return developmentGeometry.getFromDate();
    }
    
    public int getAccidentPeriodCount() {
        return accidentGeometry.getPeriods();
    }
    
    public int getDevelopmentPeriodCount() {
        return developmentGeometry.getPeriods();
    }
    
    public int getAccidentMonthsPerStep() {
        return accidentGeometry.getMonthPerStep();
    }
    
    public int getDevelopmentMonthsPerStep() {
        return developmentGeometry.getMonthPerStep();
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(processProperty(evt)) {
            tableModel.setGeometry(getGeometry());
            fireChange();
        }
    }
    
    private boolean processProperty(PropertyChangeEvent evt) {
        if(accidentGeometry == evt.getSource()) {
            return processAccidnetProperty(evt.getPropertyName(), evt.getNewValue());
        } else if(developmentGeometry == evt.getSource()) {
            return processDevelopmentProperty(evt.getPropertyName(), evt.getNewValue());
        } else {
            return false;
        }
    }
    
    private boolean processAccidnetProperty(String property, Object value) {
        if(AxisGeometryPanel.PROPERTY_FROM.equals(property)) {
            putClientProperty(PROPERTY_ACCIDENT_FROM, value);
            return true;
        } else if(AxisGeometryPanel.PROPERTY_PERIODS.equals(property)) {
            putClientProperty(PROPERTY_ACCIDENT_PERIODS, value);
            return true;
        } else if(AxisGeometryPanel.PROPERTY_MONTHS.equals(property)) {
            putClientProperty(PROPERTY_ACCIDENT_MONTHS, value);
            return true;
        } else {
            return false;
        }
    }
    
    private boolean processDevelopmentProperty(String property, Object value) {
        if(AxisGeometryPanel.PROPERTY_FROM.equals(property)) {
            putClientProperty(PROPERTY_DEVELOPMENT_FROM, value);
            return true;
        } else if(AxisGeometryPanel.PROPERTY_PERIODS.equals(property)) {
            putClientProperty(PROPERTY_DEVELOPMENT_PERIODS, value);
            return true;
        } else if(AxisGeometryPanel.PROPERTY_MONTHS.equals(property)) {
            putClientProperty(PROPERTY_DEVELOPMENT_MONTHS, value);
            return true;
        } else {
            return false;
        }
    }
}
