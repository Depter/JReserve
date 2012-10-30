package org.jreserve.triangle.guiutil;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box.Filler;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.GeometrySettingPanel;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleFormatVisualPanel extends JPanel {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    protected TriangleGeometry geometry;
    protected GeometrySettingPanel geometrySetting;
    protected TriangleWidget triangle;
    
    public TriangleFormatVisualPanel() {
        initComponents();
        componentsInitialized();
    }
    
    protected void componentsInitialized() {
    }
    
    public GeometrySettingPanel getGeometrySetting() {
        return geometrySetting;
    }
    
    public TriangleWidget getTriangleWidget() {
        return triangle;
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
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
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gc;

        geometrySetting = new GeometrySettingPanel();
        geometrySetting.addPropertyChangeListener(new GeometryListener());
        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new java.awt.Insets(0, 0, 10, 0);
        add(geometrySetting, gc);
        
        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.VERTICAL;
        gc.weightx = 1.0;
        Dimension min = new Dimension(0, 0);
        Dimension max = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        add(new Filler(min, min, max), gc);

        triangle = new TriangleWidget();
        triangle.setPreferredSize(new java.awt.Dimension(400, 200));
        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        add(triangle, gc);
    }

    private class GeometryListener implements PropertyChangeListener {
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
            triangle.setTriangleGeometry(geometry);
        }
    }

}
