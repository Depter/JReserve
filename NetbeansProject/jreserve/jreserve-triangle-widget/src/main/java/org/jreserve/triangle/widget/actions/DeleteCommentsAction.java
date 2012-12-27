package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.comment.Commentable;
import org.jreserve.triangle.comment.TriangleComment;
import org.jreserve.triangle.comment.visual.DeleteCommentDialog;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.DeleteCommentsAction=Delete comment"
})
public class DeleteCommentsAction extends AbstractSingleCellPopUpAction {
    
    private Result<Commentable> tResult;
    private Commentable commentable;
    
    public DeleteCommentsAction() {
        putValue(NAME, Bundle.CTL_DeleteCommentsAction());
    }
    
    public DeleteCommentsAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.CTL_DeleteCommentsAction());
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(Commentable.class);
        tResult.addLookupListener(this);
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return initCommentable() && checkComments();
        return false;
    }
    
    private boolean initCommentable() {
        commentable = lookupOne(tResult);
        return commentable != null;
    }
    
    private boolean checkComments() {
        return !(getComments().isEmpty());
    }
    
    private List<TriangleComment> getComments() {
        List<TriangleComment> comments = new ArrayList<TriangleComment>();
        for(TriangleComment comment : commentable.getComments())
            if(isCellComment(comment))
                comments.add(comment);
        return comments;
    }
    
    private boolean isCellComment(TriangleComment comment) {
        return comment.getAccidentPeriod() == cell.getAccident() &&
               comment.getDevelopmentPeriod() == cell.getDevelopment();
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<TriangleComment> comments = DeleteCommentDialog.getDeletedComments(getComments());
        deleteComments(comments);
    }
    
    private void deleteComments(List<TriangleComment> comments) {
        List<TriangleComment> tcs = commentable.getComments();
        tcs.removeAll(comments);
        commentable.setComments(tcs);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteCommentsAction(lkp);
    }
}
