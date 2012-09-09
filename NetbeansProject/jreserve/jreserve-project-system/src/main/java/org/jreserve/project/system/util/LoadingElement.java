package org.jreserve.project.system.util;

import java.awt.Image;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoadingElement extends ProjectElement {

    private final static String IMG_BASE = "resources/load_%d.png";
    private final static int LAST_IMG = 9;
    
    private LoadingNode node;
    
    public LoadingElement() {
        super("Loading...");
    }
    
    @Override
    public Node createNodeDelegate() {
        if(node == null)
            node = new LoadingNode("Loading...");
        return node;
    }
    
    public void stop() {
        node.stop();
    }
    
    private class LoadingNode extends AbstractNode {
        
        private Image icon;
        private ChangeIconTask task;
        
        LoadingNode(String caption) {
            super(Children.LEAF);
            setDisplayName(caption);
            task = new ChangeIconTask(this);
        }
        
        private void stop() {
            task.cancel();
        }
        
        @Override
        public void destroy() throws IOException {
            super.destroy();
            task.cancel();
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
    
    private class ChangeIconTask implements Runnable {
        
        private final LoadingNode node;
        private volatile int id = 0;
        private RequestProcessor.Task task;
        
        public ChangeIconTask(LoadingNode node) {
            this.node = node;
            this.task = RequestProcessor.getDefault().create(this);
            run();
        }
        
        @Override
        public void run() {
            String name = String.format(IMG_BASE, nextId());
            Image img = ImageUtilities.loadImage(name);
            setIcon(img);
            task.schedule(100);
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
        
        void cancel() {
            task.cancel();
        }
    }
}
