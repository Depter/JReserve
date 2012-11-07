package org.jreserve.project.system.visual;

import org.jreserve.persistence.visual.CloseConfirmDialog;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.netbeans.api.actions.Savable;
import org.netbeans.core.spi.multiview.CloseOperationHandler;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.openide.awt.UndoRedo;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "LBL.ProjectElementCloseHandler.Title=Save \"{0}\"?"
})
public class ProjectElementCloseHandler extends CloseConfirmDialog implements CloseOperationHandler {
        
    private ProjectElement element;

    public ProjectElementCloseHandler(ProjectElement element) {
        super(element.getLookup(), Bundle.LBL_ProjectElementCloseHandler_Title((String) element.getProperty(ProjectElement.NAME_PROPERTY)));
        this.element = element;
    }

    @Override
    public boolean resolveCloseOperation(CloseOperationState[] coss) {
        if(element.getLookup().lookupAll(Savable.class).isEmpty()) {
            clearUndo();
            return true;
        }

        super.setVisible(true);
        return super.savables.isEmpty();
    }

    private void clearUndo() {
        ProjectElementUndoRedo ur = element.getLookup().lookup(ProjectElementUndoRedo.class);
        if(ur != null)
            ur.clear();
    }

    @Override
    protected void discard(Savable savable) {
        super.discard(savable);
        UndoRedo ur = element.getLookup().lookup(UndoRedo.class);
        if(ur != null)
            undo(ur);
    }

    private void undo(UndoRedo ur) {
        while(ur.canUndo())
            ur.undo();
    }
}
