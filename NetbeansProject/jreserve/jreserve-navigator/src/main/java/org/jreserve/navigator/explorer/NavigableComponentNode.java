package org.jreserve.navigator.explorer;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.navigator.NavigableComponent;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NavigableComponentNode extends AbstractNode {

    private NavigableComponent component;
    private Image icon;
    private NavigateAction prefferedAction;
    
    public NavigableComponentNode(NavigableComponent component) {
        super(new NavigableComponentChildren(component.getChildren()));
        this.component = component;
        this.icon = component.getIcon();
        this.prefferedAction = new NavigateAction();
        setDisplayName(component.getDisplayName());
    }

    @Override
    public Image getIcon(int type) {
        if(icon == null)
            return super.getIcon(type);
        return icon;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action getPreferredAction() {
        return prefferedAction;
    }
    
    private class NavigateAction extends AbstractAction {
        
        private NavigateAction() {
            putValue(NAME, component.getDisplayName());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            component.navigateTo();
        }
    }
    
}
