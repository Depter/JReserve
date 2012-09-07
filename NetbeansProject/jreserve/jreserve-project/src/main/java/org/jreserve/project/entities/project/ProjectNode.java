package org.jreserve.project.entities.project;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectNode extends DefaultProjectNode {
    
    private final static ImageIcon PROJECT_ICON = ImageUtilities.loadImageIcon("resources/project.png", false);
    
    ProjectNode(ProjectElement element) {
        super(element);
    }
    
    @Override
    public Image getIcon(int type) {
        return PROJECT_ICON.getImage();
    }
}
