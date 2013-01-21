package org.jreserve.triangle.visual.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Box.Filler;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.project.TriangleProjectElement;
import org.jreserve.triangle.visual.createdialog.GeometrySettingPanel;
import org.jreserve.triangle.visual.widget.TriangleWidget;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionReferences({
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.visual.widget.action.AddCommentAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/TriangleDataEditor",
        position=100,
        separatorBefore=90
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.visual.widget.action.DeleteCommentsAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/TriangleDataEditor",
        position=200
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.smoothing.actions.AddSmoothingAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/TriangleDataEditor",
        position=300,
        separatorBefore=290
    ),
    @ActionReference(
        id=@ActionID(id="org.jreserve.triangle.smoothing.actions.DeleteSmoothingAction", category="JReserve/TriangleWidget"),
        path="JReserve/Popup/TriangleDataEditor",
        position=400
    )
})
@Messages({
    "LBL.DataEditorView.Title=Geometry",
    "LBL.DataEditorView.GeometrySetting.Title=Geometry"
})
class DataEditorView extends NavigablePanel {
    
    private final static String POP_UP_PATH = "JReserve/Popup/TriangleDataEditor";
    private final static String EDITOR_CATEGORY = "Triangle";
    
    protected GeometrySettingPanel geometrySetting;
    protected TriangleWidget triangle;
    
    protected TriangleProjectElement element;
    private PropertyChangeListener elementListener;
    private PropertyChangeListener weakElementListener;
    private boolean fireGeoemtryChange = true;
    
    DataEditorView(TriangleProjectElement element) {
        super(Bundle.LBL_DataEditorView_Title(), Editor.getImage(element));
        this.element = element;
        registerElementListener();
        initComponents();
    }
    
    private void registerElementListener() {
        elementListener = new ElementGeometryPropertyListener();
        weakElementListener = WeakListeners.propertyChange(elementListener, element);
        element.addPropertyChangeListener(weakElementListener);
    }
    
    private void initComponents() {
        JPanel panel = new LookupPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new GridBagLayout());

        geometrySetting = new GeometrySettingPanel(element.getValue().isTriangle());
        geometrySetting.setGeometry(getElementGeometry());
        geometrySetting.addChangeListener(new GeometryListener());
        
        geometrySetting.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(Bundle.LBL_DataEditorView_GeometrySetting_Title()), 
                BorderFactory.createEmptyBorder(0, 5, 5, 5))
        );
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.insets = new Insets(0, 0, 10, 0);
        panel.add(geometrySetting, gc);
        
        gc.gridx = 1;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.weightx = 1.0;
        Dimension min = new Dimension(0, 0);
        Dimension max = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        panel.add(new Filler(min, min, max), gc);

        triangle = new TriangleWidget();
        triangle.setPreferredSize(new java.awt.Dimension(400, 200));
        triangle.setSource(element.getValue());
        triangle.setWidgetEditorFolder(EDITOR_CATEGORY);
        triangle.setPopupActionPath(POP_UP_PATH);
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth = 2;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        gc.weightx=1d; gc.weighty=1d;
        panel.add(triangle, gc);
        super.setContent(panel);
        super.setLookup(triangle.getLookup());
    }
    
    private TriangleGeometry getElementGeometry() {
        return element.getValue().getGeometry();
    }
    
    public void closed() {
        element.removePropertyChangeListener(weakElementListener);
    }
    
    private class GeometryListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if(!fireGeoemtryChange)
                return;
            TriangleGeometry geometry = geometrySetting.getTriangleGeometry();
            if(geometry != null)
                element.getValue().setGeometry(geometry);
        }
    }
    
    private class LookupPanel extends JPanel implements Lookup.Provider {

        @Override
        public Lookup getLookup() {
            return triangle.getLookup();
        }    
    }
    
    private class ElementGeometryPropertyListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(Triangle.GEOMETRY_PROPERTY.equals(evt.getPropertyName())) {
                fireGeoemtryChange = false;
                geometrySetting.setGeometry(getElementGeometry());
                fireGeoemtryChange = true;
            }
        }
    }
}
