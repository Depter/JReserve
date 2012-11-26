package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.data.Commentable;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.util.DeleteCommentDialog;
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
    "CTL.DeleteCommentsAction=Add comment"
})
public class DeleteCommentsAction extends AbstractSingleCellPopUpAction {
    
    private Result<Commentable> tResult;
    private Commentable commentable;
    
    public DeleteCommentsAction() {
        putValue(NAME, NbBundle.getMessage(DeleteCommentsAction.class, "CTL.DeleteCommentsAction"));
    }
    
    public DeleteCommentsAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, NbBundle.getMessage(DeleteCommentsAction.class, "CTL.DeleteCommentsAction"));
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
            return checkComments() && initCommentable();
        return false;
    }
    
    private boolean checkComments() {
        return cell.hasComments();
    }
    
    private boolean initCommentable() {
        commentable = lookupOne(tResult);
        return commentable != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<Comment> comments = DeleteCommentDialog.getDeletedComments(cell.getComments());
        deleteComments(comments);
        deleteTableComments(comments);
    }
    
    private void deleteComments(List<Comment> comments) {
        List<TriangleComment> tcs = commentable.getComments();
        tcs.removeAll(comments);
        commentable.setComments(tcs);
    }
    
    private void deleteTableComments(List<Comment> deleted) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        for(Comment comment : deleted) {
            WidgetData<Comment> wc = new WidgetData<Comment>(accident, development, comment);
            widget.removeComment(wc);
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteCommentsAction(lkp);
    }
}
