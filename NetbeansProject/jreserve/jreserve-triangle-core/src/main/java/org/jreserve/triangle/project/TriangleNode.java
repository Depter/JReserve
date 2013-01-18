package org.jreserve.triangle.project;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleNode extends DefaultProjectNode {
    
    private final static ImageIcon TRIANGLE_ICON = ImageUtilities.loadImageIcon("resources/triangle.png", false);
    private final static String TRIANGLE_ACTION_PATH = "JReserve/Popup/ProjectRoot-DataContainerNode-TriangleNode";
    private final static ImageIcon VECTOR_ICON = ImageUtilities.loadImageIcon("resources/vector.png", false);
    private final static String VECTOR_ACTION_PATH = "JReserve/Popup/ProjectRoot-DataContainerNode-VectorNode";

    private boolean isTriangle;
    
    TriangleNode(ProjectElement<Triangle> element) {
        super(element);
        isTriangle = element.getValue().isTriangle();
        super.addActionPath(isTriangle? TRIANGLE_ACTION_PATH : VECTOR_ACTION_PATH);
        initToolTip(element.getValue().getDataType());
    }
    
    private void initToolTip(ProjectDataType dt) {
        String toolTip = String.format("%d - %s", dt.getDbId(), dt.getName());
        setShortDescription(toolTip);
    }
    
    @Override
    public Image getIcon(int type) {
        if(isTriangle)
            return TRIANGLE_ICON.getImage();
        return VECTOR_ICON.getImage();
    }
}
