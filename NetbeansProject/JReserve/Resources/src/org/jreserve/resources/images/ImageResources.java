package org.jreserve.resources.images;

import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ImageResources {
    
    private final static String IMG_HOME = "org/jreserve/resources/images/";
    private final static String ERROR = "error.png";
    
    public static ImageIcon error() {
        return getIcon(ERROR);
    }
    
    private static ImageIcon getIcon(String name) {
        return ImageUtilities.loadImageIcon(IMG_HOME+name, true);
    }
    
    private ImageResources() {}
}
