package org.jreserve.project.filesystem;

import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DefaultProjectNode extends AbstractNode {

    private ProjectElement element;
    
    DefaultProjectNode(ProjectElement element) {
        super(new ProjectChildren(element), Lookups.proxy(element));
        setDisplayName(element.getName());
    }

    
}
