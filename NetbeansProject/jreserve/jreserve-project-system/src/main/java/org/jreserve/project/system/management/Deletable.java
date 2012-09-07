package org.jreserve.project.system.management;

import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
public interface Deletable {

    public void delete();
    
    public Node getNode();
}
