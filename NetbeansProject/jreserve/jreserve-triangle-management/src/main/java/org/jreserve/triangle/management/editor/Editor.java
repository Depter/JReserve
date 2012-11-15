package org.jreserve.triangle.management.editor;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.navigator.NavigableComponent;
import org.jreserve.navigator.NavigableTopComponent;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.visual.ProjectElementCloseHandler;
import org.jreserve.project.system.visual.TabNameAdapter;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.management.editor.charts.AccidentPeriodsChartData;
import org.jreserve.triangle.management.editor.charts.AccidentTotalChartData;
import org.jreserve.triangle.management.editor.charts.CalendarYearTotalChartData;
import org.jreserve.triangle.management.editor.charts.DevelopmentPeriodsChartData;
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.Editor.Triangle=Triangle",
    "LBL.Editor.Vector=Vector"
})
public class Editor extends NavigableTopComponent implements UndoRedo.Provider {
    
    final static Image TRIANGLE_IMG = ImageUtilities.loadImage("resources/triangle.png", false);
    final static Image VECTOR_IMG = ImageUtilities.loadImage("resources/vector.png", false);
    private final static int SCROLL_INCREMENT = 40;
    
    public static TopComponent createTriangle(ProjectElement<Triangle> element) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        components.add(new EditorView(Bundle.LBL_Editor_Triangle(), TRIANGLE_IMG, element));
        TriangleDataEditorView ew = new TriangleDataEditorView(element);
        components.add(ew);
        components.add(AccidentTotalChartData.createPanel(ew.triangle));
        components.add(AccidentPeriodsChartData.createPanel(ew.triangle));
        components.add(AccidentPeriodsChartData.createScaledPanel(ew.triangle));
        components.add(CalendarYearTotalChartData.createPanel(ew.triangle));
        components.add(DevelopmentPeriodsChartData.createPanel(ew.triangle));
        components.add(DevelopmentPeriodsChartData.createScaledPanel(ew.triangle));
        components.add(RCodeView.getTriangleView(element, ew.triangle));
        return new Editor(components, element);
    }
    
    public static TopComponent createVector(ProjectElement<Vector> element) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        components.add(new EditorView(Bundle.LBL_Editor_Vector(), VECTOR_IMG, element));
        VectorDataEditorView ew = new VectorDataEditorView(element);
        components.add(ew);
        components.add(DevelopmentPeriodsChartData.createPanel(ew.triangle));
        components.add(RCodeView.getVectorView(element, ew.triangle));
        return new Editor(components, element);
    }
    
    private ProjectElement element;
    private ProjectElementCloseHandler closeHandler;
    
    private Editor(List<NavigableComponent> components, ProjectElement element) {
        super(components);
        this.element = element;
        this.closeHandler = new ProjectElementCloseHandler(element);
        initTopComponent();
    }
    
    private void initTopComponent() {
        initTab();
        TabNameAdapter.createAdapter(this, element);
        scroller.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scroller.getVerticalScrollBar().setBlockIncrement(SCROLL_INCREMENT);
    }
    
    private void initTab() {
        setTabIcon();
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        setDisplayName(name);
    }
    
    private void setTabIcon() {
        if(element.getValue() instanceof Triangle)
            setIcon(TRIANGLE_IMG);
        else
            setIcon(VECTOR_IMG);
    }
    
    @Override
    public UndoRedo getUndoRedo() {
        UndoRedo ur = element.getLookup().lookup(UndoRedo.class);
        return ur!=null? ur : UndoRedo.NONE;
    }
    
    @Override
    public boolean canClose() {
        return closeHandler.canClose();
    }
}
