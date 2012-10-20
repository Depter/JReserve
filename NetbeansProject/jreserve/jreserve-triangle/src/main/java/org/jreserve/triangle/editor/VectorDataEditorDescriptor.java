package org.jreserve.triangle.editor;

import java.awt.Image;
import java.io.Serializable;
import org.jreserve.triangle.VectorProjectElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.VectorDataEditorDescriptor.title=Geometry"
})
public class VectorDataEditorDescriptor implements MultiViewDescription, Serializable {
    
    final static Image IMG = ImageUtilities.loadImage("resources/vector.png", false);
    private final static String PREFERRED_ID = "VectorDataEditorDescriptor";
    private VectorProjectElement element;

    VectorDataEditorDescriptor(VectorProjectElement element) {
        this.element = element;
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_VectorDataEditorDescriptor_title();
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
        return new VectorDataEditorView(element);
    }
}