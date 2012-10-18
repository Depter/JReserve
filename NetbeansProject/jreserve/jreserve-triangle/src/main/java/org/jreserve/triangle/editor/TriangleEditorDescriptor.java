package org.jreserve.triangle.editor;

import java.awt.Image;
import java.io.Serializable;
import org.jreserve.triangle.TriangleProjectElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleEditorDescriptor.Title=Triangle"
})
class TriangleEditorDescriptor implements MultiViewDescription, Serializable {
    
    private final static String PREFERRED_ID = "TriangleEditorDescriptor";
    final static Image IMG = ImageUtilities.loadImage("resources/triangle.png", false);

    private TriangleProjectElement element;
    
    TriangleEditorDescriptor(TriangleProjectElement element) {
        this.element = element;
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_TriangleEditorDescriptor_Title();
    }

    @Override
    public Image getIcon() {
        return IMG;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public MultiViewElement createElement() {
        return new TriangleEditorView(element);
    }

}
