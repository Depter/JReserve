package org.jreserve.project.system.util;

import java.awt.Image;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoadingElement extends ProjectElement {

    private final static String IMG_BASE = "resources/load_%d.png";
    private final static int LAST_IMG = 9;
    
    public LoadingElement() {
        super("Loading...");
    }
    
    @Override
    public Node createNodeDelegate() {
        return new LoadingNode("Loading...");
    }
    
    private class LoadingNode extends AbstractNode {
        
        private Image icon;
        private Timer timer;
        
        LoadingNode(String caption) {
            super(Children.LEAF);
            setDisplayName(caption);
            startTimer();
        }

        private void startTimer() {
            timer = new Timer(true);
            timer.schedule(new ChangeIconTask(LoadingNode.this), 0, 100);
        }
        
        @Override
        public void destroy() throws IOException {
            super.destroy();
            timer.cancel();
        }

        
        private void setIcon(Image icon) {
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
    
    private class ChangeIconTask extends TimerTask {
        
        
        private final LoadingNode node;
        private int id;
        
        ChangeIconTask(LoadingNode node) {
            this.node = node;
        }
        
        @Override
        public void run() {
            String name = String.format(IMG_BASE, nextId());
            Image img = ImageUtilities.loadImage(name);
            setIcon(img);
        }
        
        private int nextId() {
            id++;
            if(id > LAST_IMG)
                id = 0;
            return id;
        }
        
        private void setIcon(final Image image) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    node.setIcon(image);
                }
            });
        }
    }
}
