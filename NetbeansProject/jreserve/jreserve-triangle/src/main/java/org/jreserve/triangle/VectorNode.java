package org.jreserve.triangle;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Vector;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
class VectorNode extends DefaultProjectNode {
    
    private final static ImageIcon ICON = ImageUtilities.loadImageIcon("resources/vector.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-DataContainerNode-VectorNode";

    VectorNode(ProjectElement<Vector> element) {
        super(element);
        super.addActionPath(ACTION_PATH);
        initToolTip(element.getValue().getDataType());
    }
    
    private void initToolTip(ProjectDataType dt) {
        String toolTip = String.format("%d - %s", dt.getDbId(), dt.getName());
        setShortDescription(toolTip);
    }
    
    @Override
    public Image getIcon(int type) {
        return ICON.getImage();
    }
    
}
