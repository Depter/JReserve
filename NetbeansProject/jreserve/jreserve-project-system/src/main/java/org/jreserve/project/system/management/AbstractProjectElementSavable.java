package org.jreserve.project.system.management;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.BeanInfo;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jreserve.project.system.ProjectElement;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
public abstract class AbstractProjectElementSavable<T> extends AbstractSavable implements Icon {

    protected ProjectElement<T> element;
    private Node elementNode;
    private Icon icon;
    
    protected AbstractProjectElementSavable(ProjectElement<T> element) {
        this.element = element;
        this.elementNode = element.createNodeDelegate();
        initIcon();
        register();
    }
    
    private void initIcon() {
        Image img = elementNode.getIcon(BeanInfo.ICON_COLOR_16x16);
        if(img != null)
            icon = new ImageIcon(img);
    }
    
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
            return element.equals(((AbstractProjectElementSavable)o).element);
        return false;
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }
}