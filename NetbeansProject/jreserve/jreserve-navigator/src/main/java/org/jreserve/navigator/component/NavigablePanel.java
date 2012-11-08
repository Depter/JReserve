package org.jreserve.navigator.component;

import java.awt.Image;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jreserve.navigator.NavigableComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NavigablePanel extends JPanel implements NavigableComponent {

    private NavigableComponent parent;
    private Image icon;
    private JLabel titleLabel;

    public NavigablePanel() {
    }
    
    public NavigablePanel(String displayName, Image icon) {
        super.setName(displayName);
        this.icon = icon;
    }
    
    @Override
    public Image getIcon() {
        return icon;
    }

    @Override
    public String getDisplayName() {
        return super.getName();
    }

    @Override
    public List<NavigableComponent> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void navigateTo() {
        if(parent != null)
            parent.navigateToChild(this);
    }

    @Override
    public void setParent(NavigableComponent parent) {
        this.parent = parent;
    }

    @Override
    public void navigateToChild(NavigableComponent component) {
    }
}
