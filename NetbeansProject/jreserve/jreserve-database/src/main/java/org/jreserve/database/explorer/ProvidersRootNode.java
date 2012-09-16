package org.jreserve.database.explorer;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProvidersRootNode extends AbstractNode {
    
    private final static String ACTION_PATH = "JReserve/Popup/DatabaseRoot-Providers";
    private final static String OPENED_IMG = "resources/provider_folder_opened.png";
    private final static String CLOSED_IMG = "resources/provider_folder_closed.png";

    public ProvidersRootNode() {
        super(new ProviderRootChildren());
        setDisplayName("Providers");
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(CLOSED_IMG);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return ImageUtilities.loadImage(OPENED_IMG);
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        Action[] result = new Action[actions.size()];
        return actions.toArray(result);
    }
}
