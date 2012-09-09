package org.jreserve.project.entities.project.editor;

import java.awt.Image;
import org.jreserve.project.entities.project.ProjectElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ProjectLogDescriptor.title=Log"
})
class ProjectLogDescriptor implements MultiViewDescription {

    final static String PREFERRED_ID = "panel_2";
    private ProjectElement element;

    public ProjectLogDescriptor(ProjectElement element) {
        this.element = element;
    }
    
    @Override
    public MultiViewElement createElement() {
        return new ProjectLogView(element);
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_ProjectLogDescriptor_title();
    }

    @Override
    public Image getIcon() {
        return null;
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
