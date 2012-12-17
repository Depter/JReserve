package org.jreserve.triangle.data.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box.Filler;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.data.project.TriangleProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.GeometrySettingPanel;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataEditorView.Title=Geometry",
    "LBL.DataEditorView.GeometrySetting.Title=Geometry"
})
class DataEditorView extends NavigablePanel {
    
    private final static String POP_UP_PATH = "JReserve/Popup/DataEditor";
    private final static String EDITOR_CATEGORY = "Triangle";
    
    protected GeometrySettingPanel geometrySetting;
    protected TriangleWidget triangle;
    
    protected TriangleProjectElement element;
    
    DataEditorView(TriangleProjectElement element, TriangularData data) {
        super(Bundle.LBL_DataEditorView_Title(), Editor.getImage(element));
        this.element = element;
        initComponents(data);
    }
    
    private void initComponents(TriangularData data) {
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

        triangle = new TriangleWidget(element);
        triangle.setPreferredSize(new java.awt.Dimension(400, 200));
        triangle.setData(data);
        triangle.setWidgetEditorFolder(EDITOR_CATEGORY);
        //triangle.setPopUpActionPath(POP_UP_PATH);
        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth = 2;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        gc.weightx=1d; gc.weighty=1d;
        panel.add(triangle, gc);
        super.setContent(panel);
        super.setLookup(triangle.getLookup());
    }
    
    private TriangleGeometry getElementGeometry() {
        return (TriangleGeometry) element.getProperty(Triangle.GEOMETRY_PROPERTY);
    }
    
    public void closed() {
    }
    
    private class GeometryListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            TriangleGeometry geometry = geometrySetting.getTriangleGeometry();
            if(geometry != null)
                element.setProperty(Triangle.GEOMETRY_PROPERTY, geometry);
        }
    }
    
    private class LookupPanel extends JPanel implements Lookup.Provider {

        @Override
        public Lookup getLookup() {
            return triangle.getLookup();
        }    
    }
}