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
class DatabaseRootNode extends AbstractNode {

    private final static String OPENED_IMG = "resources/database_folder_opened.png";
    private final static String CLOSED_IMG = "resources/database_folder_closed.png";
    
    public DatabaseRootNode() {
        super(DatabaseRootChildren.getInstance());
        setDisplayName("Databases");
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
        List<? extends Action> actions = Utilities.actionsForPath("Actions/Database/Database/Root");
        Action[] result = new Action[actions.size()];
        return actions.toArray(result);
    }
    
}
