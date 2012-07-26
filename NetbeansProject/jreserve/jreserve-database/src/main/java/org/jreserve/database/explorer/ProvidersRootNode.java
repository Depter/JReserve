package org.jreserve.database.explorer;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProvidersRootNode extends AbstractNode {
    private final static String OPENED_IMG = "resources/provider_folder_opened.png";
    private final static String CLOSED_IMG = "resources/provider_folder_closed.png";

    public ProvidersRootNode() {
        super(new ProviderRootChildren());
        setDisplayName("Providers");
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(CLOSED_IMG);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return ImageUtilities.loadImage(OPENED_IMG);
    }
}
