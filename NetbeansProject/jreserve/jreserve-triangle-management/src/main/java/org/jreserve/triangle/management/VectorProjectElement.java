package org.jreserve.triangle.management;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.persistence.DeleteUtil;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;
import org.jreserve.triangle.entities.Vector;
import static org.jreserve.triangle.entities.Vector.*;
import org.jreserve.triangle.entities.VectorGeometry;
import org.jreserve.triangle.management.editor.Editor;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
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
    
    public VectorProjectElement(Vector vector) {
        super(vector);
        initProperties(vector);
        initLookup();
    }
    
    private void initProperties(Vector vector) {
        super.setProperty(NAME_PROPERTY, vector.getName());
        super.setProperty(DESCRIPTION_PROPERTY, vector.getDescription());
        super.setProperty(GEOMETRY_PROPERTY, vector.getGeometry());
        super.setProperty(CORRECTION_PROPERTY, vector.getCorrections());
        super.setProperty(COMMENT_PROPERTY, vector.getComments());
        super.setProperty(SMOOTHING_PROPERTY, vector.getSmoothings());
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentObjectDeletable(this, "Vector"));
        super.addToLookup(new VectorOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
        super.addToLookup(new VectorUndoRedo());
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
        else if(CORRECTION_PROPERTY.equals(property))
            getValue().setCorrections((List<TriangleCorrection>) value);
        else if(COMMENT_PROPERTY.equals(property))
            getValue().setComments((List<TriangleComment>) value);
        else if(SMOOTHING_PROPERTY.equals(property))
            getValue().setSmoothings((List<Smoothing>) value);
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
            originalProperties.put(CORRECTION_PROPERTY, vector.getCorrections());
            originalProperties.put(COMMENT_PROPERTY, vector.getComments());
            originalProperties.put(SMOOTHING_PROPERTY, vector.getSmoothings());
        }        
        
        @Override
        protected boolean isChanged(String property, Object o1, Object o2) {
            if(GEOMETRY_PROPERTY.equals(property)) {
                return isChanged((VectorGeometry) o1, (VectorGeometry) o2);
            } else {
                return super.isChanged(property, o1, o2);
            }
        }
        
        private boolean isChanged(VectorGeometry g1, VectorGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }

        @Override
        protected void saveEntity() {
            super.saveEntity();
            DeleteUtil deleter = new DeleteUtil();
            addEntities(deleter, Smoothing.class, SMOOTHING_PROPERTY);
            addEntities(deleter, TriangleComment.class, COMMENT_PROPERTY);
            addEntities(deleter, TriangleCorrection.class, CORRECTION_PROPERTY);
            deleter.delete(session);
        }
    }

    private class VectorOpenable extends PersistentOpenable {

        @Override
        protected TopComponent createComponent() {
            return Editor.createVector(VectorProjectElement.this);
        }
    }
    
    
    private class VectorUndoRedo extends ProjectElementUndoRedo {
        
        private VectorUndoRedo() {
            super(VectorProjectElement.this);
        }

        @Override
        protected boolean isChange(PropertyChangeEvent evt) {
            if(GEOMETRY_PROPERTY.equals(evt.getPropertyName()))
                return isChnage((VectorGeometry) evt.getOldValue(), (VectorGeometry) evt.getNewValue());
            return super.isChange(evt);
        }
        
        private boolean isChnage(VectorGeometry g1, VectorGeometry g2) {
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
