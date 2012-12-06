package org.jreserve.resources.lookupexplorer;

import java.util.Collection;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LookupChildFactory extends Children.Keys<Object>{
    
    public LookupChildFactory(Collection<? extends Object> content) {
        setKeys(content);
    }
    
    @Override
    protected Node[] createNodes(Object t) {
        AbstractNode node = new AbstractNode(Children.LEAF);
        node.setDisplayName(t.toString());
        return new Node[]{node};
    }
}
