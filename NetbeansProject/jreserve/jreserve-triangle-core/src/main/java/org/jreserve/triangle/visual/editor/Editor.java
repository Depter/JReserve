package org.jreserve.triangle.visual.editor;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.navigator.NavigableComponent;
import org.jreserve.navigator.NavigableTopComponent;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.visual.ProjectElementCloseHandler;
import org.jreserve.project.system.visual.TabNameAdapter;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.project.TriangleProjectElement;
import org.jreserve.triangle.visual.widget.chart.AccidentPeriodsChartData;
import org.jreserve.triangle.visual.widget.chart.AccidentTotalChartData;
import org.jreserve.triangle.visual.widget.chart.CalendarYearTotalChartData;
import org.jreserve.triangle.visual.widget.chart.DevelopmentPeriodsChartData;
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Editor extends NavigableTopComponent implements UndoRedo.Provider {
    
    final static Image TRIANGLE_IMG = ImageUtilities.loadImage("resources/triangle.png", false);
    final static Image VECTOR_IMG = ImageUtilities.loadImage("resources/vector.png", false);
    private final static int SCROLL_INCREMENT = 40;

    public static TopComponent createTriangleEditor(TriangleProjectElement element) {
        boolean isTriangle = element.getValue().isTriangle();
        
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        components.add(new EditorView(element));
        
        DataEditorView ew = new DataEditorView(element);
        components.add(ew);
        
        if(isTriangle)
            addTriangleCharts(ew, components);
        else
            addVectorCharts(ew, components);
        
        components.add(new RCodeDataView(element));
        
        return new Editor(components, element);
    }
    
    private static void addTriangleCharts(DataEditorView ew, List<NavigableComponent> components) {
        components.add(AccidentTotalChartData.createPanel(ew.triangle));
        components.add(AccidentPeriodsChartData.createPanel(ew.triangle));
        components.add(AccidentPeriodsChartData.createScaledPanel(ew.triangle));
        components.add(CalendarYearTotalChartData.createPanel(ew.triangle));
        components.add(DevelopmentPeriodsChartData.createPanel(ew.triangle));
        components.add(DevelopmentPeriodsChartData.createScaledPanel(ew.triangle));
    }
    
    private static void addVectorCharts(DataEditorView ew, List<NavigableComponent> components) {
        components.add(DevelopmentPeriodsChartData.createPanel(ew.triangle));
    }
    
    static Image getImage(TriangleProjectElement element) {
        if(element.getValue().isTriangle())
            return TRIANGLE_IMG;
        return VECTOR_IMG;
    }
    
    private TriangleProjectElement element;
    private TriangularData data;
    private ProjectElementCloseHandler closeHandler;
    
    private Editor(List<NavigableComponent> components, TriangleProjectElement element) {
        super(components);
        this.element = element;
        this.data = element.getValue().getTriangularData();
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
        setIcon(getImage(element));
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        setDisplayName(name);
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
    
    @Override
    protected void componentClosed() {
        super.componentClosed();
        this.data.detach();
    }
    
    @Override
    public String toString() {
        return String.format(
            "TriangleEdtior [%s]", element.toString());
    }
}
