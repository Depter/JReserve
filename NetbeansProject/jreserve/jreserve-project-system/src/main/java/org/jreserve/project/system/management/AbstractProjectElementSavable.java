package org.jreserve.project.system.management;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jreserve.project.system.ProjectElement;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
public abstract class AbstractProjectElementSavable<T> extends AbstractSavable implements Icon, PropertyChangeListener {

    protected Map<String, Object> originalProperties = new HashMap<String, Object>();
    
    protected ProjectElement<T> element;
    private Node elementNode;
    private Icon icon;
    
    protected AbstractProjectElementSavable(ProjectElement<T> element) {
        this.element = element;
        this.elementNode = element.createNodeDelegate();
        element.addPropertyChangeListener(this);
        initIcon();
        initOriginalProperties();
    }
    
    private void initIcon() {
        Image img = elementNode.getIcon(BeanInfo.ICON_COLOR_16x16);
        if(img != null)
            icon = new ImageIcon(img);
    }
    
    protected abstract void initOriginalProperties();
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(isChanged())
            registerSavable();
        else
            unregisterSavable();
    }

    protected boolean isChanged() {
        for(String property : originalProperties.keySet())
            if(isChanged(property))
                return true;
        return false;
    }

    protected boolean isChanged(String property) {
        Object original = originalProperties.get(property);
        Object current = element.getProperty(property);
        return isChanged(original, current);
    }

    protected boolean isChanged(Object o1, Object o2) {
        if(o1 == null)
            return o2 == null;
        return !o1.equals(o2);
    }

    protected void registerSavable() {
        if(!isRegisteredSavable())
            element.addToLookup(this);
        super.register();
    }

    private boolean isRegisteredSavable() {
        for(AbstractProjectElementSavable s : element.getLookup().lookupAll(AbstractProjectElementSavable.class))
            if(this == s)
                return true;
        return false;
    }

    protected void unregisterSavable() {
        if(isRegisteredSavable())
            element.removeFromLookup(this);
        super.unregister();
    }

    @Override
    protected void handleSave() throws IOException {
        saveElement();
        initOriginalProperties();
        unregisterSavable();
    }
    
    protected abstract void saveElement() throws IOException;
    
    @Override
    protected String findDisplayName() {
        return elementNode.getDisplayName();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if(icon != null)
            icon.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return icon==null? 0 : icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return icon==null? 0 : icon.getIconHeight();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof AbstractProjectElementSavable)
            return equals((AbstractProjectElementSavable) o);
        return false;
    }

    private boolean equals(AbstractProjectElementSavable t) {
        Object o1 = element.getValue();
        Object o2 = t.element.getValue();
        return o1==null? o2==null : o1.equals(o2);
    }

    @Override
    public int hashCode() {
        Object value = element.getValue();
        return value==null? 0 : value.hashCode();
    }
}