package org.jreserve.triangle.project;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.triangle.TriangleModification;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.TriangleListener;
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
    private final static String MODIFICATION_PROPERTY = "MODIFICATION_PROPERTY"; 
            
    private int position;
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        this.position = triangle.isTriangle()? TRIANGLE_POSITION : VECTOR_POSITION;
        initProperties(triangle);
        initLookup();
    }
    
    private void initProperties(Triangle triangle) {
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.setProperty(DESCRIPTION_PROPERTY, triangle.getDescription());
        super.setProperty(Triangle.GEOMETRY_PROPERTY, triangle.getGeometry());
        //TODO modifications
        //TODO comments
    }
    
    private void initLookup() {
//        super.addToLookup(new PersistentDeletable<Triangle>(this));
//        super.addToLookup(new TriangleOpenable());
//        super.addToLookup(new RenameableProjectElement(this));
//        super.addToLookup(new AuditableProjectElement(this));
//        super.addToLookup(new TriangleUndoRedo());
//        super.addToLookup(new AbstractCommentable(this, getValue()));
//        new TriangleSavable();
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
    public Object getProperty(Object property) {
//        if(MODIFICATION_PROPERTY.equals(property)) {
//            List<TriangleModification> mods = (List<TriangleModification>) super.getProperty(property);
//            return new ArrayList<TriangleModification>(mods);
//        }
        return super.getProperty(property);
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
//        else if(Triangle.GEOMETRY_PROPERTY.equals(property))
//            setTriangleGeometryProperty((TriangleGeometry) value);
//        else if(MODIFICATION_PROPERTY.equals(property)) {
//            List<TriangularDataModification> mods = (List<TriangularDataModification>) value;
//            data.setModifications(mods);
//            value = new ArrayList<TriangularDataModification>(mods);
//        } else if(CommentableTriangle.COMMENT_PROPERTY.equals(property)) {
//            value = new ArrayList<TriangleComment>((List<TriangleComment>) value);
//        }
        super.setProperty(property, value);
    }
    
//    private void setTriangleGeometryProperty(TriangleGeometry geometry) {
//        getValue().setGeometry(geometry);
//        input.setTriangleGeometry(geometry);
//    }
    
    private class TriangleUpdateListener implements TriangleListener {
        @Override
        public void nameChanged(Triangle triangle) {
            setProperty(NAME_PROPERTY, triangle.getName());
        }

        @Override
        public void descriptionChanged(Triangle triangle) {
            setProperty(DESCRIPTION_PROPERTY, triangle.getDescription());
        }

        @Override
        public void geometryChanged(Triangle triangle) {
            setProperty("GEOEMTRY", triangle.getGeometry());
        }

        @Override
        public void modificationsChanged(Triangle triangle) {
            setProperty("MODIFICATIONS", triangle.getModifications());
        }

        @Override
        public void commentsChanged(Triangle triangle) {
            setProperty("COMMENTS", triangle.getComments());
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
            originalProperties.put(MODIFICATION_PROPERTY, triangle.getModifications());
//            originalProperties.put(CommentableTriangle.COMMENT_PROPERTY, element.getProperty(CommentableTriangle.COMMENT_PROPERTY));
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
    }
    
    private class TriangleUndoRedo extends ProjectElementUndoRedo {
        
        //TODO modify super class, by addin an abstract method,
        //which sets the property on the underlying value.

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
    }   
}
