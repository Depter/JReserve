package org.jreserve.project.system;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.project.system.management.Renameable;
import org.jreserve.resources.ActionUtil;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle.Messages;
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
    
    private final static int MAX_TOOLTIP_LENGTH = 30;
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-DefaultNode";
    
    private Set<String> actionPathes = new TreeSet<String>();
    private Image img;
    
    public DefaultProjectNode(ProjectElement element) {
        super(new ProjectElementChildren(element), Lookups.proxy(element));
        initToolTip((String) element.getProperty(ProjectElement.DESCRIPTION_PROPERTY));
        element.addPropertyChangeListener(WeakListeners.propertyChange(this, element));
        Object name = element.getProperty(ProjectElement.NAME_PROPERTY);
        setDisplayName(name==null? "null" : name.toString());
        actionPathes.add(ACTION_PATH);
    }
    
    public DefaultProjectNode(ProjectElement element, Image img) {
        this(element);
        this.img = img;
    }
    
    public DefaultProjectNode(ProjectElement element, Image img, String... actionPathes) {
        this(element, img);
        for(String path : actionPathes)
            addActionPath(path);
    }
    
    protected void initToolTip(String description) {
        String tooltip = getToolTippText(description);
        super.setShortDescription(tooltip);
    }
    
    private String getToolTippText(String description) {
        String str = getFirstSentence(description);
        if(str != null && str.length() > MAX_TOOLTIP_LENGTH)
            str = str.substring(0, MAX_TOOLTIP_LENGTH-3) + "...";
        return str;
    }
    
    private String getFirstSentence(String description) {
        if(description == null || description.trim().length()==0)
            return null;
        int index = description.indexOf('.');
        return index < 0? description : description.substring(0, index+1);
    }

    protected void addActionPath(String path) {
        actionPathes.add(path);
    }
    
    protected void removeActionPath(String path) {
        actionPathes.remove(path);
    }
    
    @Override
    public Image getIcon(int type) {
        return img==null? super.getIcon(type) : img;
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = ActionUtil.actionsForPath(actionPathes);
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
        else if(ProjectElement.DESCRIPTION_PROPERTY.equals(evt.getPropertyName()))
            initToolTip((String) evt.getNewValue());
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
