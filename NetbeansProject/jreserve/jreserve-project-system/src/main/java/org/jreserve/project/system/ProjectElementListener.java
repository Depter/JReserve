package org.jreserve.project.system;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProjectElementListener {
    
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

}
