package org.jreserve.dbexplorer;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.jreserve.database.api.DatabaseProvider;
import org.openide.nodes.AbstractNode;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DbProviderNode extends AbstractNode {
    
    private DatabaseProvider provider;
    
    DbProviderNode(DatabaseProvider provider) {
        super(new DbChildren(provider), Lookups.singleton(provider));
        //super(Children.create(new DbNodeFactory(provider), true), Lookups.singleton(provider));
        this.provider = provider;
    }
    
    void refresh() {
        ((DbChildren) super.getChildren()).refreshDatabases();
    }
    
    @Override
    public String getDisplayName() {
        return provider.getName();
    }
    
    @Override
    public Image getIcon(int type) {
        return provider.getIcon();
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return provider.getIcon();
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath("Actions/Database/DatabaseProvider");
        return actions.toArray(new Action[actions.size()]);
    }
}
