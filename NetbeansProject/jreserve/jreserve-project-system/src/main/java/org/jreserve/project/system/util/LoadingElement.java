package org.jreserve.project.system.util;

import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoadingElement extends ProjectElement {

    public LoadingElement() {
        super("Loading...");
    }
    
    private static class LoadingNode extends AbstractNode {
        
        private Image icon;
        private Timer timer;
        
        LoadingNode(String caption) {
            super(Children.LEAF);
            setDisplayName(caption);
        }

        private synchronized void setIcon(Image icon) {
            this.icon = icon;
            super.fireIconChange();
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
    
    private static class ChangeIconTask extends TimerTask {
        
        private final LoadingNode node;
        
        ChangeIconTask(LoadingNode node) {
            this.node = node;
        }
        
        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
    }
}
