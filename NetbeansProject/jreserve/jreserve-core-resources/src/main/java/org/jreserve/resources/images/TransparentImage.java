package org.jreserve.resources.images;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TransparentImage implements Icon {

    public static Icon getIocn(int size) {
        return getIcon(size, size);
    }
    
    public static Icon getIcon(int width, int height) {
        return new TransparentImage(width, height);
    }
    
    public static Image getImage(int size) {
        return getImage(size, size);
    }
    
    public static Image getImage(int width, int height) {
        Icon icon = getIcon(width, height);
        return ImageUtilities.icon2Image(icon);
    }
    
    private final int width;
    private final int height;
    
    private TransparentImage(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

}
