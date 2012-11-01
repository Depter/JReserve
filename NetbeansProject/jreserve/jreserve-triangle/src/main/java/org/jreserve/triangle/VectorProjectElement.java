package org.jreserve.triangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.hibernate.Query;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.editor.Editor;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorCorrection;
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
    public final static String CORRECTION_PROPERTY = "VECTOR_CORRECTION_PROPERTY";
    
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
    }
    
    private void initLookup() {
        super.addToLookup(new PersistentObjectDeletable(this, "Vector"));
        super.addToLookup(new VectorOpenable());
        super.addToLookup(new RenameableProjectElement(this));
        super.addToLookup(new AuditableProjectElement(this));
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
            getValue().setCorrections((List<VectorCorrection>) value);
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
        }        
        
        @Override
        protected boolean isChanged(String property, Object o1, Object o2) {
            if(GEOMETRY_PROPERTY.equals(property)) {
                return isChanged((VectorGeometry) o1, (VectorGeometry) o2);
            } else if(CORRECTION_PROPERTY.equals(property)) {
                return isChanged((List<VectorCorrection>) o1, (List<VectorCorrection>) o2);
            } else {
                return super.isChanged(property, o1, o2);
            }
        }
        
        private boolean isChanged(VectorGeometry g1, VectorGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }
        
        private boolean isChanged(List<VectorCorrection> c1, List<VectorCorrection> c2) {
            if(getSize(c1) != getSize(c2)) return true;
            //if both size is 0, but one is empty aother is null => not changed
            if(c1 == null || c2 == null) return false;
            
            for(VectorCorrection c : c1)
                if(!c2.contains(c))
                    return true;
            return false;
        }
        
        private int getSize(List list) {
            return list==null? 0 : list.size();
        }

        @Override
        protected void saveEntity() {
            saveCorrections();
            super.saveEntity();
        }
        
        private void saveCorrections() {
            deleteCorrections();
            for(VectorCorrection correction : element.getValue().getCorrections())
                session.persist(correction);
        }
        
        private void deleteCorrections() {
            Query query = session.createQuery("delete from VectorCorrection where triangle.id = :triangleId");
            query.setString("triangleId", element.getValue().getId());
            query.executeUpdate();
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
