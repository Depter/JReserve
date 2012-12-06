package org.jreserve.project.system;

import java.util.EventListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProjectElementListener extends EventListener {
    
    /**
     * Called after a child added to the ProjectElement.
     * 
     * @param child the new child element.
     */
    public void childAdded(ProjectElement child);

    /**
     * Called after a child is removed from the ProjectElement.
     * 
     * @param child the removed element.
     */
    public void childRemoved(ProjectElement child);
    
    /**
     * Called when the children of this element is completly changed.
     */
    public void childrenChanged();

    /**
     * Called when the ProjectElement is removed from it's parent.
     * 
     * @param parent The old parent element.
     */
    public void removedFromParent(ProjectElement parent);
}
