package org.jreserve.estimate.expectedratio.creator;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.visual.ProjectElementComboBox;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.util.ProjectDataContainerFactory;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataSelectVisualPanel.PanelName=Select data",
    "LBL.DataSelectVisualPanel.Triangle=Triangle:",
    "LBL.DataSelectVisualPanel.Exposure=Exposure:"
})
class DataSelectVisualPanel extends JPanel {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private ProjectElementComboBox triangleCombo;
    private ProjectElementComboBox exposureCombo;
    
    DataSelectVisualPanel() {
        initComponents();
        //setName(Bundle.LBL_DataSelectVisualPanel_PanelName());
    }

    void setProject(ProjectElement element) {
        element = element.getFirstChild(ProjectDataContainerFactory.POSITION, ProjectElementContainer.class);
        triangleCombo.setElements(getTriangles(element));
        exposureCombo.setElements(getExposures(element));
    }
    
    private List<ProjectElement> getTriangles(ProjectElement container) {
        List<ProjectElement> triangles = container.getChildren(Triangle.class);
        for(Iterator<ProjectElement> it = triangles.iterator(); it.hasNext();)
            if(!isTriangle(it.next()))
                it.remove();
        return triangles;
    }
    
    private boolean isTriangle(ProjectElement element) {
        return ((Triangle) element.getValue()).isTriangle();
    }
    
    private List<ProjectElement> getExposures(ProjectElement container) {
        List<ProjectElement> triangles = container.getChildren(Triangle.class);
        for(Iterator<ProjectElement> it = triangles.iterator(); it.hasNext();)
            if(isTriangle(it.next()))
                it.remove();
        return triangles;
    }
    
    Triangle getTriangle() {
        return triangleCombo.getSelectedItem(Triangle.class);
    }
    
    void setTriangle(Triangle triangle) {
        triangleCombo.setSelectedItem(triangle);
    }
    
    TriangularData getTriangleData() {
        return triangleCombo.getSelectedItem(TriangularData.class);
    }
    
    Triangle getExposure() {
        return exposureCombo.getSelectedItem(Triangle.class);
    }
    
    void setExposure(Triangle exposure) {
        exposureCombo.setSelectedItem(exposure);
    }
    
    TriangularData getExposureData() {
        return exposureCombo.getSelectedItem(TriangularData.class);
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listenener) {
        listeners.remove(listenener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    private void initComponents() {
        
        triangleCombo = new ProjectElementComboBox();
        triangleCombo.addLookupListener(Triangle.class, new TriangleListener(triangleCombo, DataSelectWizardPanel.PROP_TRIANGLE));

        exposureCombo = new ProjectElementComboBox();
        exposureCombo.addLookupListener(Triangle.class, new TriangleListener(exposureCombo, DataSelectWizardPanel.PROP_EXPOSURE));
        
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());
        GridBagConstraints gc;

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_DataSelectVisualPanel_Triangle()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 5, 0);
        add(triangleCombo, gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 0, 5);
        add(new JLabel(Bundle.LBL_DataSelectVisualPanel_Exposure()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 1;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 0, 0);
        add(exposureCombo, gc);
        
        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0; 
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.weightx = 1d;
        gc.weighty = 1d;
        gc.anchor = java.awt.GridBagConstraints.NORTH;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        add(Box.createVerticalGlue(), gc);
    }
    
    private class TriangleListener implements LookupListener {

        private final ProjectElementComboBox combo;
        private final String property;
        
        private TriangleListener(ProjectElementComboBox combo, String property) {
            this.combo = combo;
            this.property = property;
        }
        
        @Override
        public void resultChanged(LookupEvent le) {
            ProjectElement element = combo.getSelectedItem(ProjectElement.class);
            Object value = element==null? null : element.getValue();
            putClientProperty(property, value);
            fireChange();
        }
    }
}
