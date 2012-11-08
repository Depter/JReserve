package org.jreserve.navigator.explorer;

import java.util.Collection;
import org.jreserve.navigator.NavigableComponent;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NavigableComponentChildren extends Children.Keys<NavigableComponent> {

    NavigableComponentChildren(Collection<? extends NavigableComponent> components) {
        super.setKeys(components);
    }
    
    @Override
    protected Node[] createNodes(NavigableComponent t) {
        NavigableComponentNode node = new NavigableComponentNode(t);
        return new Node[]{node};
    }
}
