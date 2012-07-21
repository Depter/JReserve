package org.jreserve.database;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DatabaseNode extends AbstractNode {

    private final static Image NORMAL = ImageUtilities.loadImage("resources/database.png");
    
    private AbstractDatabase database;
    
    public DatabaseNode(AbstractDatabase database) {
        super(Children.LEAF, Lookups.singleton(database));
        this.database = database;
        setDisplayName(database.getShortName());
    }

    @Override
    public Image getIcon(int type) {
        return NORMAL;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return NORMAL;
    }

    @Override
    protected Sheet createSheet() {
        
        return super.createSheet();
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath("Actions/Database/Database");
        return actions.toArray(new Action[actions.size()]);
    }    
}
