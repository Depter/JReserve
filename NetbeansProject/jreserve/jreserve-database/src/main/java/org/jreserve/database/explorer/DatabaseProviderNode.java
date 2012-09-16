package org.jreserve.database.explorer;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.jreserve.database.DatabaseProvider;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL_nameUnknown=Unknown"
})
class DatabaseProviderNode extends AbstractNode {

    private final static String ACTION_PATH = "JReserve/Popup/DatabaseRoot-Providers-Provider";
    private final static String ICON = "resources/gear.png";
    
    private DatabaseProvider provider;
    
    public DatabaseProviderNode(DatabaseProvider provider) {
        super(Children.LEAF, Lookups.singleton(provider));
        this.provider = provider;
        initNode();
    }

    private void initNode() {
        String name = provider.getName();
        setDisplayName(name!=null? name : Bundle.CTL_nameUnknown());
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        return actions.toArray(new Action[actions.size()]);
    }
}
