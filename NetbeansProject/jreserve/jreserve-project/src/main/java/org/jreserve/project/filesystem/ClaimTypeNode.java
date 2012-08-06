package org.jreserve.project.filesystem;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeNode extends DefaultProjectNode {

    private final static ImageIcon CLAIM_TYPE_ICON = ImageUtilities.loadImageIcon("resources/claim_type.png", false);
    
    ClaimTypeNode(ClaimTypeElement element) {
        super(element);
    }
    
    @Override
    public Image getIcon(int type) {
        return CLAIM_TYPE_ICON.getImage();
    }
}
