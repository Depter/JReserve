package org.jreserve.triangle.util;

import java.awt.Image;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ProjectDataContainerNode.Name=Data"
})
public class ProjectDataContainerFactory extends ProjectDataElementContainerFactory {
    
    private static ProjectElementContainer PATH_ELEMENT;
    
    public static ProjectElementContainer createPathElement() {
        if(PATH_ELEMENT == null)
            PATH_ELEMENT = ProjectElementContainer.getPathElement(POSITION);
        return PATH_ELEMENT;
    }
                   
    private static ProjectDataContainerFactory INSTANCE = null;
    
    public static ProjectDataContainerFactory getInstance() {
        if(INSTANCE == null)
            INSTANCE = new ProjectDataContainerFactory();
        return INSTANCE;
    }
    
    
    private final static Image ICON = ImageUtilities.loadImage("resources/data_container.png", false);
    public final static int POSITION = 200;
    private final static String[] ACTION_PATHES = {
        "JReserve/Popup/ProjectRoot-ProjectNode-DataContainerNode"
    };
    
    private ProjectDataContainerFactory() {
    }
    
    @Override
    protected String getName() {
        return Bundle.LBL_ProjectDataContainerNode_Name();
    }

    @Override
    protected int getPosition() {
        return POSITION;
    }

    @Override
    protected Image getImage() {
        return ICON;
    }

    @Override
    protected String[] getActionPathes() {
        return ACTION_PATHES;
    }
}
