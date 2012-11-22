package org.jreserve.estimates.factors.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.estimates.factors.FactorSelectionOwner;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.widget.actions.DeleteCommentsAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "CTL.DeleteFactorCommentAction=Delete comments"
})
public class DeleteFactorCommentAction extends DeleteCommentsAction {
    
    private Result<ProjectElement> tResult;
    private ProjectElement element;
    
    public DeleteFactorCommentAction() {
        putValue(NAME, NbBundle.getMessage(DeleteFactorCommentAction.class, "CTL.DeleteFactorCommentAction"));
    }
    
    public DeleteFactorCommentAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(DeleteFactorCommentAction.class, "CTL.DeleteFactorCommentAction"));
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(ProjectElement.class);
        tResult.addLookupListener(this);
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return initElement();
        return false;
    }
    
    private boolean initElement() {
        element = getElement();
        return element != null;
    }
    
    private ProjectElement getElement() {
        for(org.jreserve.project.system.ProjectElement e : tResult.allInstances())
            if(e.getValue() instanceof FactorSelectionOwner)
                return e;
        return null;
    }

    @Override
    protected void deleteComments(List<Comment> comments) {
        List<TriangleComment> tcs = (List<TriangleComment>) element.getProperty(FactorSelection.FACTOR_SELECTION_COMMENTS);
        tcs.removeAll(comments);
        element.setProperty(FactorSelection.FACTOR_SELECTION_COMMENTS, tcs);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteFactorCommentAction(lkp);
    }
}