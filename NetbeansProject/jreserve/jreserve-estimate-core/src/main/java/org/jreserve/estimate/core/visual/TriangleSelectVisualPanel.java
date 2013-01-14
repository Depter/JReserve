package org.jreserve.estimate.core.visual;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.visual.ProjectElementComboBox;
import org.jreserve.triangle.data.factories.ProjectDataContainerFactoy;
import org.jreserve.triangle.entities.Triangle;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleSelectVisualPanel.PanelName=Select triangle",
    "LBL.TriangleSelectVisualPanel.Triangle=Triangle:"
})
class TriangleSelectVisualPanel extends JPanel {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private ProjectElementComboBox triangleCombo;
    
    public TriangleSelectVisualPanel() {
        initComponents();
        setName(Bundle.LBL_TriangleSelectVisualPanel_PanelName());
    }

    void setProject(ProjectElement element) {
        element = element.getFirstChild(ProjectDataContainerFactoy.POSITION, ProjectElementContainer.class);
        List<ProjectElement> triangles = element.getChildren(Triangle.class);
        triangleCombo.setElements(triangles);
    }
    
    Triangle getTriangle() {
        return triangleCombo.getSelectedItem(Triangle.class);
    }
    
    void setTriangle(Triangle triangle) {
        triangleCombo.setSelectedItem(triangle);
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
        triangleCombo.addLookupListener(Triangle.class, new TriangleListener());

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());
        GridBagConstraints gc;

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 0, 5);
        add(new JLabel(Bundle.LBL_TriangleSelectVisualPanel_Triangle()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 0, 0);
        add(triangleCombo, gc);
        
        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0; 
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.weightx = 1d;
        gc.weighty = 1d;
        gc.anchor = java.awt.GridBagConstraints.NORTH;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        add(Box.createVerticalGlue(), gc);
    }

    
    private class TriangleListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            ProjectElement element = triangleCombo.getSelectedItem(ProjectElement.class);
            Object value = element==null? null : element.getValue();
            putClientProperty(TriangleSelectWizardPanel.PROP_TRIANGLE, value);
            fireChange();
        }
    }
}
