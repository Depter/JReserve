package org.jreserve.triangle;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleNode extends DefaultProjectNode {
    
    private final static ImageIcon ICON = ImageUtilities.loadImageIcon("resources/triangle.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-DataContainerNode-TriangleNode";

    TriangleNode(ProjectElement element) {
        super(element);
        super.addActionPath(ACTION_PATH);
    }
    
    @Override
    public Image getIcon(int type) {
        return ICON.getImage();
    }
}
