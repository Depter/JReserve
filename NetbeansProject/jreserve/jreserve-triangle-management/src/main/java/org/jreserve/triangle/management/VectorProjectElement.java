package org.jreserve.triangle.management;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.jreserve.audit.AuditableProjectElement;
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
    
    public final static String GEOMETRY_PROPERTY = "VECTOR_GEOMETRY_PROPERTY";
    public final static String CORRECTION_PROPERTY = "VECTOR_CORRECTION_PROPERTY";
    public final static String COMMENT_PROPERTY = "VECTOR_COMMENT_PROPERTY";
    public final static String SMOOTHING_PROPERTY = "VECTOR_SMOOTHING_PROPERTY";
    
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
            } else if(CORRECTION_PROPERTY.equals(property)) {
                return isChanged((List<TriangleCorrection>) o1, (List<TriangleCorrection>) o2);
            } else if(COMMENT_PROPERTY.equals(property)) {
                return isChanged((List<TriangleComment>) o1, (List<TriangleComment>) o2);
            } else if(SMOOTHING_PROPERTY.equals(property)) {
                return isChanged((List<Smoothing>) o1, (List<Smoothing>) o2);
            } else {
                return super.isChanged(property, o1, o2);
            }
        }
        
        private boolean isChanged(VectorGeometry g1, VectorGeometry g2) {
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

        @Override
        protected void saveEntity() {
            super.saveEntity();
        }
    }
    
    private class VectorOpenable extends PersistentOpenable {
    
        private boolean added = false;
        
        @Override
        protected void opened() {
//            if(!added) {
//                added = true;
//                VectorProjectElement.this.addToLookup(component);
//            }
        }
    
        @Override
        protected void closing() {
//            if(added) {
//                added = false;
//                VectorProjectElement.this.removeFromLookup(component);
//            }
        }

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
