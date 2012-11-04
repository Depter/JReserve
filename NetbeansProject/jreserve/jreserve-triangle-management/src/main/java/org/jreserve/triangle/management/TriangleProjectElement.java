package org.jreserve.triangle.management;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.management.editor.Editor;
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
    "LOG.TriangleProjectElement.GeometryChange=Geometry of triangle \"{0}\" changed {1} => {2}.",
    "MSG.TriangleProjectElement.UndoRedo.Geometry=geometry change",
    "MSG.TriangleProjectElement.UndoRedo.Correction=correction change"
})
public class TriangleProjectElement extends ProjectElement<Triangle> {
    
    public final static String GEOMETRY_PROPERTY = "TRIANGLE_GEOMETRY_PROPERTY";
    public final static String CORRECTION_PROPERTY = "TRIANGLE_CORRECTION_PROPERTY";
    public final static String COMMENT_PROPERTY = "TRIANGLE_COMMENT_PROPERTY";
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        initProperties(triangle);
        initLookup();
    }
    
    private void initProperties(Triangle triangle) {
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.setProperty(DESCRIPTION_PROPERTY, triangle.getDescription());
        super.setProperty(GEOMETRY_PROPERTY, triangle.getGeometry());
        super.setProperty(CORRECTION_PROPERTY, triangle.getCorrections());
        super.setProperty(COMMENT_PROPERTY, triangle.getComments());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentObjectDeletable(this, "Triangle"));
        super.addToLookup(new TriangleOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        super.addToLookup(new TriangleUndoRedo());
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
        else if(CORRECTION_PROPERTY.equals(property))
            getValue().setCorrections((List<TriangleCorrection>) value);
        else if(COMMENT_PROPERTY.equals(property))
            getValue().setComments((List<TriangleComment>) value);
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
            originalProperties.put(CORRECTION_PROPERTY, triangle.getCorrections());
            originalProperties.put(COMMENT_PROPERTY, triangle.getComments());
        }
        
        @Override
        protected boolean isChanged(String property, Object o1, Object o2) {
            if(GEOMETRY_PROPERTY.equals(property)) {
                return isChanged((TriangleGeometry) o1, (TriangleGeometry) o2);
            } else if(CORRECTION_PROPERTY.equals(property)) {
                return isChanged((List<TriangleCorrection>) o1, (List<TriangleCorrection>) o2);
            } else if(COMMENT_PROPERTY.equals(property)) {
                return isChanged((List<TriangleComment>) o1, (List<TriangleComment>) o2);
            } else {
                return super.isChanged(property, o1, o2);
            }
        }
        
        private boolean isChanged(TriangleGeometry g1, TriangleGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }
        
        private boolean isChanged(List c1, List c2) {
            if(getSize(c1) != getSize(c2)) return true;
            //if both size is 0, but one is empty aother is null => not changed
            if(c1 == null || c2 == null) return false;
            
            for(Object c : c1)
                if(!c2.contains(c))
                    return true;
            return false;
        }
        
        private int getSize(List list) {
            return list==null? 0 : list.size();
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
    
    private class TriangleUndoRedo extends ProjectElementUndoRedo {
        
        private TriangleUndoRedo() {
            super(TriangleProjectElement.this);
        }

        @Override
        protected boolean isChange(PropertyChangeEvent evt) {
            if(GEOMETRY_PROPERTY.equals(evt.getPropertyName()))
                return isChnage((TriangleGeometry) evt.getOldValue(), (TriangleGeometry) evt.getNewValue());
            return super.isChange(evt);
        }
        
        private boolean isChnage(TriangleGeometry g1, TriangleGeometry g2) {
            if(g1 == null) return g2 != null;
            return !g1.isEqualGeometry(g2);
        }
        
        @Override
        protected String getPropertyName(String property) {
            if(GEOMETRY_PROPERTY.equals(property))
                return Bundle.MSG_TriangleProjectElement_UndoRedo_Geometry();
            else if(CORRECTION_PROPERTY.equals(property))
                return Bundle.MSG_TriangleProjectElement_UndoRedo_Correction();
            else
                return super.getPropertyName(property);
        }
        
    }
    
}
