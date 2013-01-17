package org.jreserve.triangle.data.project;

import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.value.TriangleUtil;
import org.jreserve.triangle.value.TriangleBundle;
import org.jreserve.triangle.TriangularDataModification;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.comment.AbstractCommentable;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.comment.TriangleComment;
import org.jreserve.triangle.comment.TriangleCommentUtil;
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
public class TriangleProjectElement extends ProjectElement<Triangle> implements TriangularData.Provider, ModifiableTriangularData {
    
    public final static int TRIANGLE_POSITION = 100;
    public final static int VECTOR_POSITION = 200;
    private final static String MODIFICATION_PROPERTY = "MODIFICATION_PROPERTY"; 
            
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
        initModifications();
        initComments();
        //super.setProperty(Smoothable.SMOOTHING_PROPERTY, triangle.getSmoothings());
    }
    
    private void initModifications() {
        List<TriangularDataModification> mods = TriangleUtil.loadData(getValue());
        super.setProperty(MODIFICATION_PROPERTY, mods);
        data.setModifications(mods);
    }
    
    private void initComments() {
        List<TriangleComment> comments = TriangleCommentUtil.loadComments(getValue());
        super.setProperty(CommentableTriangle.COMMENT_PROPERTY, comments);
    }
    
    private void initLookup() {
        super.addToLookup(new TriangleDeletable());
        super.addToLookup(new TriangleOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        super.addToLookup(new TriangleUndoRedo());
        super.addToLookup(new AbstractCommentable(this, getValue()));
//        super.addToLookup(new AbstractSmoothable<Triangle>(this, getValue()));
//        super.addToLookup(new AbstractCorrectable<Triangle>(this, getValue()));
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
    public Object getProperty(Object property) {
        if(MODIFICATION_PROPERTY.equals(property)) {
            List<TriangularDataModification> mods = (List<TriangularDataModification>) super.getProperty(property);
            return new ArrayList<TriangularDataModification>(mods);
        }
        return super.getProperty(property);
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            getValue().setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            getValue().setDescription((String) value);
        else if(Triangle.GEOMETRY_PROPERTY.equals(property))
            setTriangleGeometryProperty((TriangleGeometry) value);
        else if(MODIFICATION_PROPERTY.equals(property)) {
            List<TriangularDataModification> mods = (List<TriangularDataModification>) value;
            data.setModifications(mods);
            value = new ArrayList<TriangularDataModification>(mods);
        } else if(CommentableTriangle.COMMENT_PROPERTY.equals(property)) {
            value = new ArrayList<TriangleComment>((List<TriangleComment>) value);
        }
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

    @Override
    public PersistentObject getOwner() {
        return getValue();
    }

    @Override
    public TriangularData getBaseData() {
        return data.getSourceData();
    }
    
    @Override
    public int getMaxModificationOrder() {
        int order = 0;
        for(TriangularDataModification modification : getModifications())
            if(modification.getOrder() > order)
                order = modification.getOrder();
        return order;
    }
    
    @Override
    public List<TriangularDataModification> getModifications() {
        return (List<TriangularDataModification>) getProperty(MODIFICATION_PROPERTY);
    }

    @Override
    public void addModification(TriangularDataModification modification) {
        checkOrder(modification);
        List<TriangularDataModification> mods = getModifications();
        mods.add(modification);
        setProperty(MODIFICATION_PROPERTY, mods);
    }
    
    private void checkOrder(TriangularDataModification modification) {
        int order = modification.getOrder();
        if(containsOrder(order)) {
            String msg = "Modification with order %d already contained! %s can not be added!";
            msg = String.format(msg, order, modification);
            throw new IllegalArgumentException(msg);
        }
    }
    
    private boolean containsOrder(int order) {
        for(TriangularDataModification modification : getModifications())
            if(modification.getOrder() == order)
                return true;
        return false;
    }

    @Override
    public void removeModification(TriangularDataModification modification) {
        List<TriangularDataModification> mods = getModifications();
        mods.remove(modification);
        setProperty(MODIFICATION_PROPERTY, mods);
    }
    
    public List<TriangleComment> getComments() {
        return (List<TriangleComment>) getProperty(CommentableTriangle.COMMENT_PROPERTY);
    }
    
    private class TriangleDeletable extends PersistentDeletable<Triangle> {
        
        private TriangleDeletable() {
            super(TriangleProjectElement.this);
        }
    
        @Override
        protected void cleanUpAfterEntity(Session session) {
            for(TriangularDataModification mod : getModifications())
                mod.delete(session);
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
            originalProperties.put(MODIFICATION_PROPERTY, getModifications());
            originalProperties.put(CommentableTriangle.COMMENT_PROPERTY, element.getProperty(CommentableTriangle.COMMENT_PROPERTY));
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

        @Override
        protected void saveEntity() {
            super.saveEntity();
            TriangleUtil.save(session, getOriginalModifications(), getModifications());
            TriangleCommentUtil.save(session, getOriginalComments(), getComments());
        }
        
        private List<TriangularDataModification> getOriginalModifications() {
            Object mods = originalProperties.get(MODIFICATION_PROPERTY);
            return (List<TriangularDataModification>) mods;
        }
        
        private List<TriangleComment> getOriginalComments() {
            Object comments = originalProperties.get(CommentableTriangle.COMMENT_PROPERTY);
            return (List<TriangleComment>) comments;
        }
    }
    
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