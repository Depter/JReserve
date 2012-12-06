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
    private final static String FOLDER_OPENED = "folder_opened.png";
    
    public static ImageIcon error() {
        return getIcon(ERROR);
    }
    
    public static ImageIcon folderOpened() {
        return getIcon(FOLDER_OPENED);
    }
    
    private static ImageIcon getIcon(String name) {
        return ImageUtilities.loadImageIcon(IMG_HOME+name, true);
    }
    
    private ImageResources() {}
}
