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
    
    private ProjectElement element;
    private ProjectElementCloseHandler closeHandler;
    private Lookup lookup;
    
    private Editor(List<NavigableComponent> components, ProjectElement element) {
        super(components);
        this.element = element;
        this.closeHandler = new ProjectElementCloseHandler(element);
        createLookup();
        initTopComponent();
    }
    
    private void createLookup() {
        List<Lookup> lkps = new ArrayList<Lookup>();
        lkps.add(element.getLookup());
        for(NavigableComponent comp : super.getChildren())
            if(comp instanceof Lookup.Provider)
                lkps.add(((Lookup.Provider) comp).getLookup());
        this.lookup = new ProxyLookup(lkps.toArray(new Lookup[0]));
    }
    
    private void initTopComponent() {
        initTab();
        TabNameAdapter.createAdapter(this, element);
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
    
//    @Override
//    public Lookup getLookup() {
//        return lookup;
//        //return element.getLookup();
//    }
}
