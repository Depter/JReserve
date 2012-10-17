package org.jreserve.project.entities.project.editor;

import java.awt.Image;
import java.io.Serializable;
import org.jreserve.project.entities.project.ProjectElement;
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
    "LBL.ProjectEditorDescriptor.title=Settings"
})
class ProjectEditorDescriptor implements MultiViewDescription, Serializable {

    final static Image IMG = ImageUtilities.loadImage("resources/project.png", false);
    final static String PREFERRED_ID = "panel_1";
    private ProjectElement element;

    ProjectEditorDescriptor(ProjectElement element) {
        this.element = element;
    }
    
    @Override
    public MultiViewElement createElement() {
        return new ProjectEditorView(element);
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_ProjectEditorDescriptor_title();
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
}
