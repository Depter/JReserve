package org.jreserve.triangle.importutil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.model.TriangleTableFactory;

/**
 *
 * @author Peter Decsi
 */
public class DataFormatVisualPanel extends javax.swing.JPanel implements PropertyChangeListener {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    protected Date accidentStart;
    protected Date accidentEnd;
    protected Date developmentStart;
    protected Date developmentEnd;
    
    protected TriangleGeometry geometry;
    protected List<Data> datas = new ArrayList<Data>();
    protected TriangleTable table;
    
    public DataFormatVisualPanel() {
        initComponents();
        componentsInitialized();
    }
    
    protected void componentsInitialized() {
    }
    
    public TriangleGeometry getGeometry() {
        return geometrySetting.getGeometry();
    }
    
    public TriangleTable getTable() {
        return table;
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if(GeometrySettingPanel.PROPERTY_TRIANGLE_GEOMETRY.equals(property)) {
            setGeometry();
            fireChangeEvent();
        }
    }
    
    private void setGeometry() {
        Object value = geometrySetting.getClientProperty(GeometrySettingPanel.PROPERTY_TRIANGLE_GEOMETRY);
        geometry = (TriangleGeometry) value;
        resetTable();
    }
    
    private void resetTable() {
        this.table = null;
        if(geometry != null)
            createTable();
        triangle.addTriangleTable(table);
    }
    
    private void createTable() {
        table = new TriangleTableFactory(geometry).buildTable();
        table.setValues(datas);
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    public boolean isInputValid() {
        return geometrySetting.isInputValid();
    }
    
    public String getErrorMsg() {
        return geometrySetting.getErrorMsg();
    }
    
    public void setDatas(List<Data> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        resetTable();
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
    
    protected void initGeometry() {
        geometrySetting.setAccidentStartDate(accidentStart);
        if(!geometrySetting.isSymmetricFromDate())
            geometrySetting.setDevelopmentStartDate(developmentStart);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        geometrySetting = new org.jreserve.triangle.importutil.GeometrySettingPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        triangle = new org.jreserve.triangle.widget.TriangleWidget();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        geometrySetting.addPropertyChangeListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(geometrySetting, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        add(filler1, gridBagConstraints);

        triangle.setPreferredSize(new java.awt.Dimension(400, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(triangle, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    protected org.jreserve.triangle.importutil.GeometrySettingPanel geometrySetting;
    private org.jreserve.triangle.widget.TriangleWidget triangle;
    // End of variables declaration//GEN-END:variables
}
