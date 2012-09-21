package org.jreserve.project.system.util;

import java.awt.Image;
import java.io.IOException;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.LoadingElement.text=Loading..."
})
public class LoadingElement extends ProjectElement<String> {

    private final static String IMG = "org/openide/nodes/wait.gif";
    private final static String LOADING_ELEMENT = "LOADING_ELEMENT";
    
    private LoadingNode node;
    
    public LoadingElement() {
        super(LOADING_ELEMENT);
    }
    
    @Override
    public Node createNodeDelegate() {
        if(node == null)
            node = new LoadingNode(Bundle.LBL_LoadingElement_text());
        return node;
    }
    
    private class LoadingNode extends AbstractNode {
        
        private Image icon;
        
        LoadingNode(String caption) {
            super(Children.LEAF);
            setDisplayName(caption);
            icon = ImageUtilities.loadImage(IMG, false);
        }
        
        @Override
        public Image getIcon(int type) {
            return icon;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }
}
