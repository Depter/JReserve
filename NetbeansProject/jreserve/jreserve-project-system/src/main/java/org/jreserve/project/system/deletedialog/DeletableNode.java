package org.jreserve.project.system.deletedialog;

import java.awt.Image;
import org.jreserve.project.system.management.Deletable;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DeletableNode extends AbstractNode {
    
    private Node nodeDelegate;

    DeletableNode(Deletable deletable) {
        super(new DeletableChildren(deletable), Lookups.singleton(deletable));
        nodeDelegate = deletable.getNode();
    }

    @Override
    public Image getIcon(int type) {
        return nodeDelegate.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return nodeDelegate.getOpenedIcon(type);
    }

    @Override
    public String getDisplayName() {
        return nodeDelegate.getDisplayName();
    }
}
