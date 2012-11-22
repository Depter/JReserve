package org.jreserve.estimates.factors.actions;

import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.estimates.factors.FactorSelectionOwner;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.widget.actions.AddCommentAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.AddFactorCommentAction=Add comment"
})
public class AddFactorCommentAction extends AddCommentAction {
    
    private Result<ProjectElement> tResult;
    private ProjectElement element;
    
    public AddFactorCommentAction() {
        putValue(NAME, NbBundle.getMessage(AddFactorCommentAction.class, "CTL.AddFactorCommentAction"));
    }
    
    public AddFactorCommentAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, NbBundle.getMessage(AddFactorCommentAction.class, "CTL.AddFactorCommentAction"));
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
        for(ProjectElement e : tResult.allInstances())
            if(e.getValue() instanceof FactorSelectionOwner)
                return e;
        return null;
    }

    @Override
    protected Comment createComment(Comment comment) {
        TriangleComment tc = createTriangleComment(comment);
        List<TriangleComment> comments = (List<TriangleComment>) element.getProperty(FactorSelection.FACTOR_SELECTION_COMMENTS);
        comments.add(tc);
        element.setProperty(FactorSelection.FACTOR_SELECTION_COMMENTS, comments);
        return tc;
    }
    
    private TriangleComment createTriangleComment(Comment comment) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        String user = comment.getUserName();
        String text = comment.getCommentText();
        FactorSelection fs = ((FactorSelectionOwner) element.getValue()).getFactorSelection();
        return new TriangleComment(fs, accident, development, user, text);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddFactorCommentAction(lkp);
    }
}
