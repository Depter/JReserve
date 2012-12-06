package org.jreserve.data.projectdatatype;

import java.awt.Image;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.resources.images.TransparentImage;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectDataTypeNode extends AbstractNode {
    
    private final static Image ICON = TransparentImage.getImage(16);
    private ProjectElement<ProjectDataType> element;
    private final static String FORMAT = "%d - %s";
    
    ProjectDataTypeNode(ProjectElement<ProjectDataType> element) {
        super(Children.LEAF, element.getLookup());
        this.element = element;
        initName();
    }
    
    private void initName() {
        ProjectDataType dt = element.getValue();
        String name = String.format(FORMAT, dt.getDbId(), dt.getName());
        setDisplayName(name);
    }

    @Override
    public Image getIcon(int type) {
        return ICON;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return ICON;
    }
}
