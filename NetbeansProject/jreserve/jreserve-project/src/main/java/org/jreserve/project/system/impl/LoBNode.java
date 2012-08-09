package org.jreserve.project.system.impl;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
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
