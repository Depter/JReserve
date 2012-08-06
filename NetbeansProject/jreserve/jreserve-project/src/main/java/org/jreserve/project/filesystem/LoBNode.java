package org.jreserve.project.filesystem;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoBNode extends DefaultProjectNode {

    private final static ImageIcon LOB_ICON = ImageUtilities.loadImageIcon("resources/lob.png", false);
    
    LoBNode(LoBElement element) {
        super(element);
    }
    
    @Override
    public Image getIcon(int type) {
        return LOB_ICON.getImage();
    }
    
}