package org.jreserve.triangle.visual.widget.util;

import java.awt.Image;
import org.jreserve.triangle.visual.widget.WidgetTableModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetTableModelImpl implements Comparable<WidgetTableModelImpl> {
    
    private final Image icon;
    private final String displayName;
    private final int position;
    private final WidgetTableModel model;

    public WidgetTableModelImpl(Image icon, String displayName, int position, WidgetTableModel model) {
        this.icon = icon;
        this.displayName = displayName;
        this.position = position;
        this.model = model;
    }
    
    public Image getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WidgetTableModel getModel() {
        return model;
    }

    public int getPosition() {
        return position;
    }
    
    @Override
    public int compareTo(WidgetTableModelImpl o) {
        if(o == null) return -1;
        return position - o.position;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof WidgetTableModelImpl)
            return position==((WidgetTableModelImpl)o).position;
        return false;
    }
    
    @Override
    public int hashCode() {
        return position;
    }
    
    @Override
    public String toString() {
        return String.format(
                "WidgetTableModel [%s; %d]",
                displayName, position);
    }
}
