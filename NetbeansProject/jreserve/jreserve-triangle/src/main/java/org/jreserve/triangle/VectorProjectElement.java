package org.jreserve.triangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.editor.Editor;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.netbeans.api.actions.Openable;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - vector name",
    "LOG.VectorProjectElement.Deleted=Vector \"{0}\" deleted.",
    "# {0} - old name",
    "# {1} - new name",
    "LOG.VectorProjectElement.NameChange=Vector name changed \"{0}\" => \"{1}\"",
    "# {0} - name",
    "LOG.VectorProjectElement.DescriptionChange=Description of vector \"{0}\" changed.",
    "# {0} - name",
    "# {1} - old geometry",
    "# {2} - new geometry",
    "LOG.VectorProjectElement.GeometryChange=Geometry of vector \"{0}\" changed {1} => {2}."
})
public class VectorProjectElement extends ProjectElement<Vector> {
    
    public final static String GEOMETRY_PROPERTY = "VECTOR_GEOMETRY_PROPERTY";
    
    public VectorProjectElement(Vector vector) {
        super(vector);
        initProperties(vector);
        initLookup();
    }
    
    private void initProperties(Vector vector) {
        super.setProperty(NAME_PROPERTY, vector.getName());
        super.setProperty(DESCRIPTION_PROPERTY, vector.getDescription());
        super.setProperty(GEOMETRY_PROPERTY, vector.getGeometry());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentDeletable(this));
        super.addToLookup(new VectorOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        new VectorSavable();
    }

    @Override
    public Node createNodeDelegate() {
        return new VectorNode(this);
    }
    
    @Override
    public int getPosition() {
        return Vector.POSITION;
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        else if(GEOMETRY_PROPERTY.equals(property))
            getValue().setGeometry((VectorGeometry) value);
        super.setProperty(property, value);
    }
    
    private class VectorSavable extends PersistentSavable<Vector> {

        public VectorSavable() {
            super(VectorProjectElement.this);
        }

        @Override
        protected void initOriginalProperties() {
            Vector vector = element.getValue();
            originalProperties.put(NAME_PROPERTY, vector.getName());
            originalProperties.put(DESCRIPTION_PROPERTY, vector.getDescription());
            originalProperties.put(GEOMETRY_PROPERTY, vector.getGeometry());
        }        
        
        @Override
        protected boolean isChanged(Object o1, Object o2) {
            if(super.isChanged(o1, o2)) {
                if((o1 instanceof VectorGeometry) || (o2 instanceof VectorGeometry))
                    return isChanged((VectorGeometry) o1, (VectorGeometry) o2);
                return true;
            }
            return false;
        }
        
        private boolean isChanged(VectorGeometry g1, VectorGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }
    }
    
    private class VectorOpenable implements Openable, PropertyChangeListener {
        
        private TopComponent editor;
        
        VectorOpenable() {
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
            editor = Editor.createTopComponent(VectorProjectElement.this);
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
