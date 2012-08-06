package org.jreserve.database;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DatabaseNode extends AbstractNode {

    private final static Image NORMAL = ImageUtilities.loadImage("resources/database.png");
    private final static Image CONNECTED = ImageUtilities.loadImage("resources/database_connected.png");
    
    private final static Comparator<Action> ACTION_COMPARATOR = new Comparator<Action>() {
        @Override
        public int compare(Action a1, Action a2) {
            if(a1 == null)
                return a2==null? 0 : 1;
            return a2==null? -1 : compareNames(a1, a2);
        }
        
        private int compareNames(Action a1, Action a2) {
            String n1 = (String) a1.getValue(Action.NAME);
            String n2 = (String) a2.getValue(Action.NAME);
            return n1.compareTo(n2);
        }
    };
    
    private PropertyChangeListener dbListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals(AbstractDatabase.IS_USED))
                fireIconChange();
        }
    };
    
    private AbstractDatabase database;
    
    public DatabaseNode(AbstractDatabase database) {
        super(Children.LEAF, Lookups.singleton(database));
        this.database = database;
        database.addPropertyChangeListener(WeakListeners.propertyChange(dbListener, database));
        setDisplayName(database.getShortName());
    }
    
    @Override
    public Image getIcon(int type) {
        if(database.isUsed())
            return CONNECTED;
        return NORMAL;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    protected Sheet createSheet() {
        
        return super.createSheet();
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath("Actions/Database/Database");
        Collections.sort(actions, ACTION_COMPARATOR);
        return actions.toArray(new Action[actions.size()]);
    }    
}
