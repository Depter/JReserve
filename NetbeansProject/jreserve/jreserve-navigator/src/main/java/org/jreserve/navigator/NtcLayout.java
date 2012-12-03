package org.jreserve.navigator;

import java.awt.*;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NtcLayout implements LayoutManager2 {

    private final static int MININUM = 0;
    private final static int MAXIMUM = 1;
    private final static int PREFFERED = 2;
    
    private List<Component> components = new java.util.ArrayList<Component>();
    private int count = 0;
    
    private int spacing = 5;

    
    
    private Dimension prefferedSize;
    private Dimension minSize;
    private Dimension maxSize;
    
    public NtcLayout(int spacing) {
        this.spacing = spacing;
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, null);
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(components.contains(comp))
            throw new IllegalArgumentException("Component already added!"+comp);
        components.add(comp);
        count++;
        invalidateLayout(null);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        if(components.remove(comp)) {
            invalidateLayout(null);
            count--;
        }
        
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0f; //left alignment
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0f; //top alignment
    }

    @Override
    public void invalidateLayout(Container target) {
        prefferedSize = null;
        maxSize = null;
        minSize = null;
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        if(maxSize == null)
            maxSize = calculateLayoutSize(parent, MAXIMUM);
        return maxSize;
    }
    
    private Dimension calculateLayoutSize(Container parent, int type) {
        int height = 0;
        for(int i=0; i<count; i++) {
            if(i>0) height += spacing;
            height += getComponentHeight(components.get(i), type);
        }
        int width = parent.getSize().width;
        return getBorderedSize(width, height, parent);
    }
    
    private int getComponentHeight(Component c, int type) {
        switch(type) {
            case MININUM: return c.getMinimumSize().height;
            case MAXIMUM: return c.getMaximumSize().height;
            default: return c.getPreferredSize().height;
        }
    }
    
    private Dimension getBorderedSize(int width, int height, Container parent) {
        Insets border = getBorderSize(parent);
        height = height + border.top + border.bottom;
        return new Dimension(width, height);
    }
    
    private Insets getBorderSize(Container parent) {
        if(parent instanceof JComponent) {
            Border border = ((JComponent) parent).getBorder();
            return border.getBorderInsets(parent);
        }
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        if(prefferedSize == null)
            prefferedSize = calculateLayoutSize(parent, PREFFERED);
        return prefferedSize;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        if(minSize == null)
            minSize = calculateLayoutSize(parent, MININUM);
        return minSize;
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets border = getBorderSize(parent);
        int width = parent.getSize().width - border.left - border.right;
        int top = border.top;
        int left = border.left;
        
        for(int i=0; i<count; i++) {
            Component c = components.get(i);
            setSize(c, width);
            c.setLocation(left, top);
            top += (c.getSize().height + spacing);
        }
    }
    
    private void setSize(Component c, int width) {
        Dimension size = c.getPreferredSize();
        c.setSize(new Dimension(width, size.height));
    }
}
