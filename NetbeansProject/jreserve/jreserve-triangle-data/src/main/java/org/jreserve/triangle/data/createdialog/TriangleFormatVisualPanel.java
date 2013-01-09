package org.jreserve.triangle.data.createdialog;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box.Filler;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.data.util.TriangleInputFactory;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.GeometrySettingPanel;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleFormatVisualPanel.Name=Geometry",
    "LBL.TriangleFormatVisualPanel.Geometry.Title=Geometry"
})
class TriangleFormatVisualPanel extends JPanel {
 
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private GeometrySettingPanel geometrySetting;
    private TriangleWidget triangle;
    private List<ClaimValue> datas = new ArrayList<ClaimValue>();
    private TriangularData data;
    
    TriangleFormatVisualPanel(boolean isTriangle) {
        initComponents(isTriangle);
        setName(Bundle.LBL_TriangleFormatVisualPanel_Name());
    }
    
    private void initComponents(boolean isTriangle) {
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gc;

        geometrySetting = new GeometrySettingPanel(isTriangle);
        geometrySetting.addChangeListener(new GeometryChangeListener());
        
        geometrySetting.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(Bundle.LBL_TriangleFormatVisualPanel_Geometry_Title()), 
                BorderFactory.createEmptyBorder(0, 5, 5, 5))
        );

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
    
    public void setStartDate(Date date) {
        geometrySetting.setStartDate(date);
    }
    
    public void setClaims(List<ClaimValue> claims) {
        this.datas.clear();
        this.datas.addAll(claims);
        createTriangleData();
    }
    
    private void createTriangleData() {
        data = createData();
        triangle.setData(data);
        fireChangeEvent();
    }
    
    private TriangularData createData() {
        TriangleGeometry geometry = getTriangleGeometry();
        if(geometry == null)
            return TriangularData.EMPTY;
        return new TriangleInputFactory(geometry).buildTriangle(datas);
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return geometrySetting.getTriangleGeometry();
    }
    
    public boolean isInputValid() {
        return geometrySetting.isInputValid();
    }
    
    public String getErrorMsg() {
        return geometrySetting.getErrorMsg();
    }
    
    public double[][] getTriangleValues() {
        return data==null? null : data.toArray();
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
    
    private class GeometryChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            createTriangleData();
        }
    }
}
