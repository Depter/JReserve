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
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ProxyLookup;
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
    
    public static TopComponent createTriangle(ProjectElement<Triangle> element) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        components.add(new EditorView(Bundle.LBL_Editor_Triangle(), TRIANGLE_IMG, element));
        components.add(new TriangleDataEditorView(element));
        return new Editor(components, element);
    }
    
    public static TopComponent createVector(ProjectElement<Vector> element) {
        List<NavigableComponent> components = new ArrayList<NavigableComponent>();
        components.add(new EditorView(Bundle.LBL_Editor_Vector(), VECTOR_IMG, element));
        components.add(new VectorDataEditorView(element));
        return new Editor(components, element);
    }
    
    private Lookup lookup;
    private ProjectElement element;
    private ProjectElementCloseHandler closeHandler;
    
    private Editor(List<NavigableComponent> components, ProjectElement element) {
        super(components);
        this.element = element;
        this.closeHandler = new ProjectElementCloseHandler(element);
        initTopComponent();
    }
    
    private void initTopComponent() {
        initLookup();
        initTab();
        TabNameAdapter.createAdapter(this, element);
    }
    
    private void initLookup() {
        lookup = new ProxyLookup(super.getLookup(), element.getLookup());
    }
    
    private void initTab() {
        setIcon(TRIANGLE_IMG);
        String name = (String) element.getProperty(ProjectElement.NAME_PROPERTY);
        setDisplayName(name);
        setHtmlDisplayName("<html>"+name+"</html>");
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
    public Lookup getLookup() {
        return lookup;
    }
}
