package org.jreserve.triangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.jreserve.triangle.editor.Editor;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
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
    "LOG.TriangleProjectElement.Deleted=Triangle \"{0}\" deleted."
})
public class TriangleProjectElement extends ProjectElement<Triangle> {
    
    public final static String GEOMETRY_PROPERTY = "TRIANGLE_GEOMETRY_PROPERTY";
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.setProperty(DESCRIPTION_PROPERTY, triangle.getName());
        initLookup();
    }
    
    private void initLookup() {
        super.addToLookup(new TriangleDeletable());
        super.addToLookup(new TriangleOpenable());
        super.addToLookup(new RenameableProjectElement(this));
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
        }        
        
        @Override
        protected boolean isChanged(Object o1, Object o2) {
            if(super.isChanged(o1, o2)) {
                if((o1 instanceof TriangleGeometry) || (o2 instanceof TriangleGeometry))
                    return isChanged((TriangleGeometry) o1, (TriangleGeometry) o2);
                return true;
            }
            return false;
        }
        
        private boolean isChanged(TriangleGeometry g1, TriangleGeometry g2) {
            if(g1==null) return g2!=null;
            if(g2==null) return true;
            return !g1.isEqualGeometry(g2);
        }
    }
    
    private class TriangleDeletable extends PersistentDeletable {
        
        TriangleDeletable() {
            super(TriangleProjectElement.this);
        }

        @Override
        protected void cleanUpAfterEntity(Session session) {
            Triangle triangle = TriangleProjectElement.this.getValue();
            String name = triangle.getName();
            String msg = Bundle.LOG_TriangleProjectElement_Deleted(name);
            logDeletion(triangle.getProject(), msg);
        }
        
        private void logDeletion(Project project, String msg) {
            ChangeLogUtil util = ChangeLogUtil.getDefault();
            util.addChange(project, ChangeLog.Type.DATA, msg);
            util.saveValues(project);
        }
    }
    
    private class TriangleOpenable implements Openable, PropertyChangeListener {
        
        private TopComponent editor;
        private PropertyChangeListener listener;
        
        TriangleOpenable() {
            listener = WeakListeners.propertyChange(this, TopComponent.getRegistry());
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
    
}
