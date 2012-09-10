package org.jreserve.project.system;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.project.system.management.Renameable;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - the name of the object",
    "MSG.DefaultProjectNode.saveError=Unable to save \"{0}\"!"
})
public class DefaultProjectNode extends AbstractNode implements PropertyChangeListener {
    
    private final static String ACTION_PATH = "Menu/Project";
    
    public DefaultProjectNode(ProjectElement element) {
        super(new ProjectElementChildren(element), Lookups.proxy(element));
        element.addPropertyChangeListener(WeakListeners.propertyChange(this, element));
        Object name = element.getProperty(ProjectElement.NAME_PROPERTY);
        setDisplayName(name==null? "null" : name.toString());
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        return actions.toArray(new Action[actions.size()]);
    }
    
    @Override
    public boolean canRename() {
        return getLookup().lookup(Renameable.class) != null;
    }
    
    @Override
    public void setName(String name) {
        Renameable renameable = getLookup().lookup(Renameable.class);
        renameable.setName(name);
        saveIfSavable();
    }
    
    private void saveIfSavable() {
        Savable s = getLookup().lookup(Savable.class);
        if(s == null)
            return;
        try {
            s.save();
        } catch (IOException ex) {
            showSaveError(ex);
        }
    }
    
    private void showSaveError(IOException ex) {
        String msg = Bundle.MSG_DefaultProjectNode_saveError(getDisplayName());
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(ProjectElement.NAME_PROPERTY.equalsIgnoreCase(evt.getPropertyName()))
            setDisplayName((String) evt.getNewValue());
    }
    
    @Override
    public Action getPreferredAction() {
        Openable openable = getLookup().lookup(Openable.class);
        if(openable == null)
            return super.getPreferredAction();
        return getOpenAction(openable);
    }
    
    private Action getOpenAction(final Openable openable) {
        return new AbstractAction(getDisplayName(), null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openable.open();
            }
        };
    }
}
