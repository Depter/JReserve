package org.jreserve.project.system.management;

import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.RootElement;
import org.jreserve.project.system.util.ProjectSystemListener;

/**
 * If someone is wishes to get notified about the creation of a 
 * {@link ProjectElement ProjectElement} should implement this service.
 * 
 * <p>The implementing service will get notified when a new child is inserted
 * into the project system. This means that there is a new node created 
 * somewhere under the {@link RootElement RootElement} (ie. the tree, what you
 * see in the project explorer). There will be no notifications about 
 * independent nodes or trees.
 * </p>
 * 
 * @author Peter Decsi
 */
public interface ProjectSystemCreationListener extends ProjectSystemListener {
    
    public void created(ProjectElement element);
}
