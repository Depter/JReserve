package org.jreserve.triangle.data.project;

import java.beans.PropertyChangeEvent;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.TriangleBundle;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.data.editor.Editor;
import org.jreserve.triangle.data.util.AsynchronousTriangleInput;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
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
public class TriangleProjectElement extends ProjectElement<Triangle> implements TriangularData.Provider {
    
    public final static int TRIANGLE_POSITION = 100;
    public final static int VECTOR_POSITION = 200;
    
    private int position;
    private AsynchronousTriangleInput input;
    private TriangleBundle data;
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        this.position = triangle.isTriangle()? TRIANGLE_POSITION : VECTOR_POSITION;
        initData(triangle);
        initProperties(triangle);
        initLookup();
    }

    private void initData(Triangle triangle) {
        input = new AsynchronousTriangleInput(triangle.getGeometry(), triangle.getDataType());
        data = new TriangleBundle(input);
    }
    
    private void initProperties(Triangle triangle) {
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.setProperty(DESCRIPTION_PROPERTY, triangle.getDescription());
        super.setProperty(Triangle.GEOMETRY_PROPERTY, triangle.getGeometry());
        //super.setProperty(Correctable.CORRECTION_PROPERTY, triangle.getCorrections());
        //super.setProperty(Commentable.COMMENT_PROPERTY, triangle.getComments());
        //super.setProperty(Smoothable.SMOOTHING_PROPERTY, triangle.getSmoothings());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentDeletable(this));
        super.addToLookup(new TriangleOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        super.addToLookup(new TriangleUndoRedo());
//        super.addToLookup(new AbstractSmoothable<Triangle>(this, getValue()));
//        super.addToLookup(new AbstractCommentable<Triangle>(this, getValue()));
//        super.addToLookup(new AbstractCorrectable<Triangle>(this, getValue()));
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
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        else if(Triangle.GEOMETRY_PROPERTY.equals(property))
            setTriangleGeometryProperty((TriangleGeometry) value);
//        else if(Correctable.CORRECTION_PROPERTY.equals(property))
//            getValue().setCorrections((List<TriangleCorrection>) value);
//        else if(Commentable.COMMENT_PROPERTY.equals(property))
//            getValue().setComments((List<TriangleComment>) value);
//        else if(Smoothable.SMOOTHING_PROPERTY.equals(property))
//            getValue().setSmoothings((List<Smoothing>) value);
        super.setProperty(property, value);
    }
    
    private void setTriangleGeometryProperty(TriangleGeometry geometry) {
        getValue().setGeometry(geometry);
        input.setTriangleGeometry(geometry);
    }

    @Override
    public TriangularData getTriangularData() {
        return data;
    }
    
//    private class TriangleSavable extends PersistentSavable<Triangle> {
//        
//        public TriangleSavable() {
//            super(TriangleProjectElement.this);
//        }
//
//        @Override
//        protected void initOriginalProperties() {
//            Triangle triangle = element.getValue();
//            originalProperties.put(NAME_PROPERTY, triangle.getName());
//            originalProperties.put(DESCRIPTION_PROPERTY, triangle.getDescription());
//            originalProperties.put(GEOMETRY_PROPERTY, triangle.getGeometry());
//            originalProperties.put(Correctable.CORRECTION_PROPERTY, triangle.getCorrections());
//            originalProperties.put(Commentable.COMMENT_PROPERTY, triangle.getComments());
//            originalProperties.put(Smoothable.SMOOTHING_PROPERTY, triangle.getSmoothings());
//        }
//        
//        @Override
//        protected boolean isChanged(String property, Object o1, Object o2) {
//            if(GEOMETRY_PROPERTY.equals(property)) {
//                return isChanged((TriangleGeometry) o1, (TriangleGeometry) o2);
//            } else {
//                return super.isChanged(property, o1, o2);
//            }
//        }
//        
//        private boolean isChanged(TriangleGeometry g1, TriangleGeometry g2) {
//            if(g1==null) return g2!=null;
//            if(g2==null) return true;
//            return !g1.isEqualGeometry(g2);
//        }
//
//        @Override
//        protected void saveEntity() {
//            super.saveEntity();
//            DeleteUtil deleter = new DeleteUtil();
//            addEntities(deleter, Smoothing.class, Smoothable.SMOOTHING_PROPERTY);
//            addEntities(deleter, TriangleComment.class, Commentable.COMMENT_PROPERTY);
//            addEntities(deleter, TriangleCorrection.class, Correctable.CORRECTION_PROPERTY);
//            deleter.delete(session);
//        }
//    }
    
    private class TriangleOpenable extends PersistentOpenable {
        @Override
        protected TopComponent createComponent() {
            return Editor.createTriangleEditor(TriangleProjectElement.this);
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
    }    
}
