package org.jreserve.triangle.project;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.entities.*;
import org.jreserve.triangle.visual.editor.Editor;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
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
    
    public final static int TRIANGLE_POSITION = 100;
    public final static int VECTOR_POSITION = 200;

    private int position;
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        triangle.addTriangleListener(new TriangleUpdateListener());
        this.position = triangle.isTriangle()? TRIANGLE_POSITION : VECTOR_POSITION;
        initProperties(triangle);
        initLookup();
    }
    
    private void initProperties(Triangle triangle) {
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.setProperty(DESCRIPTION_PROPERTY, triangle.getDescription());
        super.setProperty(Triangle.GEOMETRY_PROPERTY, triangle.getGeometry());
        super.setProperty(TriangleModification.MODIFICATION_PROPERTY, triangle.getModifications());
        super.setProperty(TriangleComment.COMMENT_PROPERTY, triangle.getComments());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentDeletable<Triangle>(this));
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
        return position;
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        super.setProperty(property, value);
    }
    
    private class TriangleUpdateListener implements TriangleListener {
        @Override
        public void nameChanged(Triangle triangle) {
        }

        @Override
        public void descriptionChanged(Triangle triangle) {
        }

        @Override
        public void geometryChanged(Triangle triangle) {
            setProperty(Triangle.GEOMETRY_PROPERTY, triangle.getGeometry());
        }

        @Override
        public void modificationsChanged(ModifiableTriangle triangle) {
            setProperty(TriangleModification.MODIFICATION_PROPERTY, triangle.getModifications());
        }

        @Override
        public void commentsChanged(CommentableTriangle triangle) {
            setProperty(TriangleComment.COMMENT_PROPERTY, triangle.getComments());
        }
    }
    
    private class TriangleSavable extends PersistentSavable<Triangle> {
        
        private TriangleSavable() {
            super(TriangleProjectElement.this);
        }

        @Override
        protected void initOriginalProperties() {
            Triangle triangle = element.getValue();
            originalProperties.put(NAME_PROPERTY, triangle.getName());
            originalProperties.put(DESCRIPTION_PROPERTY, triangle.getDescription());
            originalProperties.put(Triangle.GEOMETRY_PROPERTY, triangle.getGeometry());
            originalProperties.put(TriangleModification.MODIFICATION_PROPERTY, triangle.getModifications());
            originalProperties.put(TriangleComment.COMMENT_PROPERTY, triangle.getComments());
        }
        
        @Override
        protected boolean isChanged(String property, Object o1, Object o2) {
            if(Triangle.GEOMETRY_PROPERTY.equals(property)) {
                return isChanged((TriangleGeometry) o1, (TriangleGeometry) o2);
            } else {
                return super.isChanged(property, o1, o2);
            }
        }
        
        private boolean isChanged(TriangleGeometry g1, TriangleGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }
    }
    
    private class TriangleOpenable extends PersistentOpenable {
        @Override
        protected TopComponent createComponent() {
            return Editor.createTriangleEditor(TriangleProjectElement.this);
        }
    
        @Override
        public String toString() {
            String msg = "Openable [value = %s]";
            return String.format(msg, getValue());
        }
    }
    
    private class TriangleUndoRedo extends ProjectElementUndoRedo {

        private TriangleUndoRedo() {
            super(TriangleProjectElement.this);
        }

        @Override
        protected boolean isChange(PropertyChangeEvent evt) {
            if(Triangle.GEOMETRY_PROPERTY.equals(evt.getPropertyName()))
                return isChnage((TriangleGeometry) evt.getOldValue(), (TriangleGeometry) evt.getNewValue());
            return super.isChange(evt);
        }
        
        private boolean isChnage(TriangleGeometry g1, TriangleGeometry g2) {
            if(g1 == null) return g2 != null;
            return !g1.isEqualGeometry(g2);
        }
        
        @Override
        protected String getPropertyName(String property) {
            if(Triangle.GEOMETRY_PROPERTY.equals(property))
                return Bundle.MSG_TriangleProjectElement_UndoRedo_Geometry();
            else
                return super.getPropertyName(property);
        }

        @Override
        protected void redo(ProjectElementUndoRedo.Event evt) {
            myChange = true;
            if(interestingProperty(evt.property))
                setProperty(evt.property, evt.newValue);
            else
                element.setProperty(evt.property, evt.newValue);
            myChange = false;
        }
        
        private boolean interestingProperty(String property) {
            return Triangle.GEOMETRY_PROPERTY.equals(property) ||
                   TriangleModification.MODIFICATION_PROPERTY.equals(property) ||
                   TriangleComment.COMMENT_PROPERTY.equals(property);
        }
        
        private void setProperty(String property, Object value) {
            if(Triangle.GEOMETRY_PROPERTY.equals(property))
                getValue().setGeometry((TriangleGeometry)value);
            else if(TriangleModification.MODIFICATION_PROPERTY.equals(property))
                getValue().setModifications((List<TriangleModification>) value);
            else if(TriangleComment.COMMENT_PROPERTY.equals(property))
                getValue().setComments((List<TriangleComment>) value);
        }
        
        @Override
        protected void undo(ProjectElementUndoRedo.Event evt) {
            myChange = true;
            if(interestingProperty(evt.property))
                setProperty(evt.property, evt.oldValue);
            else
                element.setProperty(evt.property, evt.oldValue);
            myChange = false;
        }
    
        @Override
        public String toString() {
            String msg = "UndoRedo [value = %s]";
            return String.format(msg, getValue());
        }
    }   
}
