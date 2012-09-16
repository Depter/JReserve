package org.jreserve.project.entities.claimtype;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.jreserve.project.system.DefaultProjectNode;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTypeNode extends DefaultProjectNode {

    private final static ImageIcon CLAIM_TYPE_ICON = ImageUtilities.loadImageIcon("resources/claim_type.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-ClaimTypeNode";
    
    ClaimTypeNode(ClaimTypeElement element) {
        super(element);
        super.addActionPath(ACTION_PATH);
    }
    
    @Override
    public Image getIcon(int type) {
        return CLAIM_TYPE_ICON.getImage();
    }
}
