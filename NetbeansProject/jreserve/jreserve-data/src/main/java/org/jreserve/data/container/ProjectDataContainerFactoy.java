package org.jreserve.data.container;

import java.awt.Image;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.ProjectDataContainerNode.Name=Data"
})
public class ProjectDataContainerFactoy extends ProjectDataElementContainerFactory {
    
    private static ProjectDataContainerFactoy INSTANCE = null;
    
    public static ProjectDataContainerFactoy getInstance() {
        if(INSTANCE == null)
            INSTANCE = new ProjectDataContainerFactoy();
        return INSTANCE;
    }
    
    
    private final static Image ICON = ImageUtilities.loadImage("resources/data_container.png", false);
    public final static int POSITION = 200;
    private final static String[] ACTION_PATHES = {
        "JReserve/Popup/ProjectRoot-ProjectNode-DataContainerNode"
    };
    
    private ProjectDataContainerFactoy() {
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
