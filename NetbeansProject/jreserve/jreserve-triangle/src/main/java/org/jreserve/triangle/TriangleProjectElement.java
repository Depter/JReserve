package org.jreserve.triangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.triangle.editor.Editor;
import org.jreserve.triangle.entities.Triangle;
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
    
    public TriangleProjectElement(Triangle triangle) {
        super(triangle);
        super.setProperty(NAME_PROPERTY, triangle.getName());
        super.addToLookup(new TriangleDeletable());
        super.addToLookup(new TriangleOpenable());
    }

    @Override
    public Node createNodeDelegate() {
        return new TriangleNode(this);
    }
    
    @Override
    public int getPosition() {
        return Triangle.POSITION;
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
