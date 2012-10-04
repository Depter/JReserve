package org.jreserve.project.system.management;

import java.util.List;
import org.jreserve.persistence.Session;
import org.openide.nodes.Node;

/**
 * This interface represents the possibility that
 * something can be deleted.
 * 
 * <p> Implementations should be aware, that the programs ultimatly
 * operates on top of a database, and some of their childrens may want
 * to do something in the database, before they are deleted. To ensure
 * this, before deletion the implementation should call all deletables
 * registerd as their ascendant.
 * </p>
 * 
 * @author Peter Decsi
 */
public interface Deletable {

    /**
     * Should delete this element, after calling all
     * deletables of the children.
     */
    public void delete();
    
    /**
     * This method should be called from a parent deletable, when it 
     * get's deleted. Calling this method is a notification, that
     * one of the parent's being deleted, and so do you. 
     * 
     * <p>The provided session should not be commited or rolled back
     * by this implementation. It is the task of the first deletable,
     * which created the session.
     * </p>
     * 
     * <p>Callers of this method should be aware, that database operations
     * via the session could throw unchecked exceptions. These exceptions
     * should be catched only by the deletable which manages the session.
     * </p>
     * 
     * @param session 
     */
    public void delete(Session session);
    
    /**
     * Returns the node representing this deletable.
     */
    public Node getNode();
    
    /**
     * Returns the list of Deletable elements, that depend on this deletable.
     * In the case of a project element, this is a list of all deletables from
     * the tree under this project element.
     */
    public List<Deletable> getChildDeletables();
}
