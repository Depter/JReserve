package org.jreserve.dbexplorer;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.jreserve.branding.img.Images;
import org.jreserve.database.api.Database;
import org.jreserve.database.api.DatabaseProvider;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DbNode extends AbstractNode {
    
    private Database db;
    
    DbNode(DatabaseProvider provider, Database db) {
        super(Children.LEAF, Lookups.fixed(provider, db));
        this.db = db;
        super.setShortDescription(db.getToolTip());
    }
    
    @Override
    public String getDisplayName() {
        return db.getName();
    }
    
    @Override
    public Image getIcon(int type) {
        if(db.isSelected())
            return Images.DB_SELECTED.getImage();
        return Images.DATABASE.getImage();
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath("Actions/Database");
        return actions.toArray(new Action[actions.size()]);
    }
}
