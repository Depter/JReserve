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
import org.jreserve.triangle.entities.Triangle;
import static org.jreserve.triangle.entities.Triangle.*;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.management.editor.Editor;
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
        super.setProperty(SMOOTHING_PROPERTY, triangle.getSmoothings());
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
        else if(SMOOTHING_PROPERTY.equals(property))
            getValue().setSmoothings((List<Smoothing>) value);
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
            originalProperties.put(SMOOTHING_PROPERTY, triangle.getSmoothings());
        }
        
        @Override
        protected boolean isChanged(String property, Object o1, Object o2) {
            if(GEOMETRY_PROPERTY.equals(property)) {
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
            DeleteUtil deleter = new DeleteUtil();
            addEntities(deleter, Smoothing.class, SMOOTHING_PROPERTY);
            addEntities(deleter, TriangleComment.class, COMMENT_PROPERTY);
            addEntities(deleter, TriangleCorrection.class, CORRECTION_PROPERTY);
            deleter.delete(session);
        }
    }
    
    private class TriangleOpenable extends PersistentOpenable {
        @Override
        protected TopComponent createComponent() {
            return Editor.createTriangle(TriangleProjectElement.this);
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
