package org.jreserve.project.system.deletedialog;

import java.util.Iterator;
import java.util.List;
import org.jreserve.project.system.management.Deletable;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DeletableChildren extends Children.Keys<Deletable>{

    private Deletable root;
    
    DeletableChildren(Deletable root) {
        this.root = root;
    }
    
    @Override
    protected void addNotify() {
        List<Deletable> keys = root.getChildDeletables();
        for(Iterator<Deletable> it=keys.iterator(); it.hasNext();) {
            if(it.next().getNode() == null)
                it.remove();
        }
        setKeys(keys);
    }
    
    @Override
    protected Node[] createNodes(Deletable t) {
        Node baseNode = t.getNode();
        DeletableChildNode node = new DeletableChildNode(baseNode);
        return new Node[]{node};
    }
}
