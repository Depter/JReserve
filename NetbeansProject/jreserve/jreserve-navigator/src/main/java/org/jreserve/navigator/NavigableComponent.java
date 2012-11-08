package org.jreserve.navigator;

import java.awt.Image;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface NavigableComponent {
    
    public Image getIcon();
    
    public String getDisplayName();
    
    public JComponent getComponent();
    
    public List<NavigableComponent> getChildren();
    
    public void navigateTo();
    
    public void navigateToChild(NavigableComponent component);
    
    public void setParent(NavigableComponent parent);
}
