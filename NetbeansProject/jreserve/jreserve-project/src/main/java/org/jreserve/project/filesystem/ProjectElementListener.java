package org.jreserve.project.filesystem;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProjectElementListener {
    
    public void childAdded(ProjectElement element);
    
    public void childRemoved(ProjectElement element);
}
