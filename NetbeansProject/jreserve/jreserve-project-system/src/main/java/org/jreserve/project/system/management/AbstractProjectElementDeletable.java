package org.jreserve.project.system.management;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.util.ProjectElementUtil;
import org.openide.nodes.Node;

/**
 * Basic implementation of the {@link Deletable Deletable} interface for
 * {@link ProjectElement ProjectElements}. The implementation iterates through
 * each children and calls {@link #delete() delete} on them, beggining with
 * the leafs.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractProjectElementDeletable implements Deletable {
    
    protected ProjectElement element;
    
    public AbstractProjectElementDeletable(ProjectElement element) {
        this.element = element;
    }

    @Override
    public Node getNode() {
        return element.createNodeDelegate();
    }
    
    @Override
    public void delete() {
        handleSave(null);
    }

    @Override
    public void delete(Session session) {
        handleSave(session);
    }
    
    /**
     * Extending classes should provide their implementations here.
     * If a session was provided for this deletable, it will be forwarded 
     * to the child deletables.
     */
    protected void handleSave(Session session) {
        deleteChildren(session);
        deleteProjectElement();
    }
    
    /**
     * Deletes all deletables on the childrens, using <i>deletable()</i> or
     * <i>deletable(session)</i> if session is not <i>null</i>.
     */
    protected void deleteChildren(Session session) {
        List<ProjectElement> children = element.getChildren();
        for(ProjectElement child : children)
            deleteChildren(child, session);
    }
    
    private void deleteChildren(ProjectElement e, Session session) {
        List<ProjectElement> children = e.getChildren();
        for(ProjectElement child : children)
            deleteChildren(child, session);
        deleteDeletable(e, session);
    }
    
    private void deleteDeletable(ProjectElement e, Session session) {
        Deletable deletable = e.getLookup().lookup(Deletable.class);
        if(deletable != null)
            deleteDeletable(deletable, session);
    }
    
    private void deleteDeletable(Deletable deletable, Session session) {
        if(session == null)
            deletable.delete();
        else
            deletable.delete(session);
    }
    
    /**
     * Removes the representing {@link ProjectElement ProjectElement}
     * from it's parent. At first, if this element is 
     * {@link ProjectElement#isAttached() attached}, the registered 
     * {@link ProjectSystemDeletionListener ProjectSystemDeletionListeners} will
     * be called from the current thread. After this the element will be removed
     * from the project tree on the event dispatcher thread.
     */
    protected void deleteProjectElement() {
        notifyDeleteListeners();
        deleteElementFromParent();
    }
    
    private void notifyDeleteListeners() {
            boolean attached = element.isAttached();
            ProjectElement parent = element.getParent();
            if(attached)
                ProjectElementUtil.deleted(parent, element);
    }
    
    private void deleteElementFromParent() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                element.removeFromParent();
            }
        });
    }

    @Override
    public List<Deletable> getChildDeletables() {
        List<Deletable> result = new ArrayList<Deletable>();
        addChildDeletables(result, element);
        return result;
    }
    
    private void addChildDeletables(List<Deletable> deletables, ProjectElement<?> parent) {
        for(ProjectElement child : parent.getChildren()) {
            Deletable d = getDeletable(child);
            if(d != null)
                deletables.add(d);
            addChildDeletables(deletables, child);
        }
    }
    
    private Deletable getDeletable(ProjectElement child) {
        return child.getLookup().lookup(Deletable.class);
    }
}
