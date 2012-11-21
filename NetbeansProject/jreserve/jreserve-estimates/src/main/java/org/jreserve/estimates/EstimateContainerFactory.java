package org.jreserve.estimates;

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
    "LBL.EstimateContainerFactory.Name=Estimates"
})
public class EstimateContainerFactory extends ProjectDataElementContainerFactory {
    
    private static ProjectElementContainer PATH_ELEMENT;
    
    public static ProjectElementContainer createPathElement() {
        if(PATH_ELEMENT == null)
            PATH_ELEMENT = ProjectElementContainer.getPathElement(POSITION);
        return PATH_ELEMENT;
    }
    
    private static EstimateContainerFactory INSTANCE = null;
    
    public static EstimateContainerFactory getInstance() {
        if(INSTANCE == null)
            INSTANCE = new EstimateContainerFactory();
        return INSTANCE;
    }
    
    
    private final static Image ICON = ImageUtilities.loadImage("resources/estimate.png", false);
    public final static int POSITION = 300;
    private final static String[] ACTION_PATHES = {
        //"JReserve/Popup/ProjectRoot-ProjectNode-DataContainerNode"
    };

    @Override
    protected String getName() {
        return Bundle.LBL_EstimateContainerFactory_Name();
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
