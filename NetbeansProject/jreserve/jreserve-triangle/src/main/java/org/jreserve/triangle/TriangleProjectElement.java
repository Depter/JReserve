package org.jreserve.triangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.editor.Editor;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.netbeans.api.actions.Openable;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - triangle name",
    "LOG.TriangleProjectElement.Deleted=Triangle \"{0}\" deleted.",
    "# {0} - old name",
    "# {1} - new name",
    "LOG.TriangleProjectElement.NameChange=Triangle name changed \"{0}\" => \"{1}\"",
    "# {0} - name",
    "LOG.TriangleProjectElement.DescriptionChange=Description of triangle \"{0}\" changed.",
    "# {0} - name",
    "# {1} - old geometry",
    "# {2} - new geometry",
    "LOG.TriangleProjectElement.GeometryChange=Geometry of triangle \"{0}\" changed {1} => {2}."
})
public class TriangleProjectElement extends ProjectElement<Triangle> {
    
    public final static String GEOMETRY_PROPERTY = "TRIANGLE_GEOMETRY_PROPERTY";
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        initProperties(triangle);
        initLookup();
    }
    
    private void initProperties(Triangle triangle) {
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.setProperty(DESCRIPTION_PROPERTY, triangle.getDescription());
        super.setProperty(GEOMETRY_PROPERTY, triangle.getGeometry());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentDeletable(this));
        super.addToLookup(new TriangleOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        new TriangleSavable();
    }

    @Override
    public Node createNodeDelegate() {
        return new TriangleNode(this);
    }
    
    @Override
    public int getPosition() {
        return Triangle.POSITION;
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        else if(GEOMETRY_PROPERTY.equals(property))
            getValue().setGeometry((TriangleGeometry) value);
        super.setProperty(property, value);
    }
    
    private class TriangleSavable extends PersistentSavable<Triangle> {

        public TriangleSavable() {
            super(TriangleProjectElement.this);
        }

        @Override
        protected void initOriginalProperties() {
            Triangle triangle = element.getValue();
            originalProperties.put(NAME_PROPERTY, triangle.getName());
            originalProperties.put(DESCRIPTION_PROPERTY, triangle.getDescription());
            originalProperties.put(GEOMETRY_PROPERTY, triangle.getGeometry());
        }        
        
        @Override
        protected boolean isChanged(Object o1, Object o2) {
            if(super.isChanged(o1, o2)) {
                if((o1 instanceof TriangleGeometry) || (o2 instanceof TriangleGeometry))
                    return isChanged((TriangleGeometry) o1, (TriangleGeometry) o2);
                return true;
            }
            return false;
        }
        
        private boolean isChanged(TriangleGeometry g1, TriangleGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }
    }
    
    private class TriangleOpenable implements Openable, PropertyChangeListener {
        
        private TopComponent editor;
        
        TriangleOpenable() {
            TopComponent.Registry registry = TopComponent.getRegistry();
            PropertyChangeListener listener = WeakListeners.propertyChange(this, TopComponent.getRegistry());
            registry.addPropertyChangeListener(listener);
        }
        
        @Override
        public void open() {
            createEditor();
            openEditor();
        }
        
        private void createEditor() {
            if(editor != null)
                return;
            editor = Editor.createTopComponent(TriangleProjectElement.this);
        }
        
        private void openEditor() {
            if(!editor.isOpened())
                editor.open();
            editor.requestActive();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();
            if(TopComponent.Registry.PROP_TC_CLOSED.equals(property))
                checkEditorClosed();
        }
        
        private void checkEditorClosed() {
            for(TopComponent component : TopComponent.getRegistry().getOpened())
                if(component == editor)
                    return;
            editor = null;
        }
    }
    
}
