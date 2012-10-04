package org.jreserve.project.system.deletedialog;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DeletableChildNode extends AbstractNode {
    
    private Node baseNode;

    public DeletableChildNode(Node baseNode) {
        super(Children.LEAF);
        this.baseNode = baseNode;
    }

    @Override
    public Image getIcon(int type) {
        return baseNode.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return baseNode.getOpenedIcon(type);
    }

    @Override
    public String getHtmlDisplayName() {
        return baseNode.getHtmlDisplayName();
    }

    @Override
    public String getDisplayName() {
        return baseNode.getDisplayName();
    }
}
