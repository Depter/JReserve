package org.jreserve.data.container;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataContainerNode extends DefaultProjectNode {

    private final static ImageIcon CLAIM_TYPE_ICON = ImageUtilities.loadImageIcon("resources/database.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-ProjectNode-DataContainerNode";
    
    ProjectDataContainerNode(ProjectDataContainerProjectElement element) {
        super(element);
        super.addActionPath(ACTION_PATH);
    }
    
    @Override
    public Image getIcon(int type) {
        return CLAIM_TYPE_ICON.getImage();
    }
}
