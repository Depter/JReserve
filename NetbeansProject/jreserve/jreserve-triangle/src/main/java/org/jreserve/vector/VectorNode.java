package org.jreserve.vector;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
class VectorNode extends DefaultProjectNode {
    
    private final static ImageIcon PROJECT_ICON = ImageUtilities.loadImageIcon("resources/vector.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-DataContainerNode-VectorNode";

    VectorNode(ProjectElement element) {
        super(element);
        super.addActionPath(ACTION_PATH);
    }
    
    @Override
    public Image getIcon(int type) {
        return PROJECT_ICON.getImage();
    }
    
}
