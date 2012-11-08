package org.jreserve.navigator;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Peter Decsi
 */
public abstract class AbstractNavigableComponnt implements NavigableComponent {
    
    private Image icon;
    private String displayName;
    protected NavigableComponent parent;
    protected List<NavigableComponent> components = new ArrayList<NavigableComponent>();
    
    protected AbstractNavigableComponnt() {
    }
    
    protected AbstractNavigableComponnt(String displayName) {
        this.displayName = displayName;
    }
    
    protected AbstractNavigableComponnt(String displayName, Image icon) {
        this.displayName = displayName;
        this.icon = icon;
    }
    
    @Override
    public Image getIcon() {
        return icon;
    }

    protected void setIcon(Image icon) {
        this.icon = icon;
    }
    
    @Override
    public String getDisplayName() {
        return displayName;
    }

    protected void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setParent(NavigableComponent parent) {
        this.parent = parent;
    }

    @Override
    public List<NavigableComponent> getChildren() {
        return components;
    }

    @Override
    public void navigateTo() {
        if(parent != null)
            parent.navigateToChild(this);
    }

    @Override
    public void navigateToChild(NavigableComponent component) {
    }
}
