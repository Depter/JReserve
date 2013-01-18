package org.jreserve.triangle.visual.widget.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.comment.TriangleCommentUtil;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.visual.DeleteTriangleCommentDialog;
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
    
    private Result<CommentableTriangle> tResult;
    private CommentableTriangle commentable;
    
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
        tResult = lookup.lookupResult(CommentableTriangle.class);
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
        return TriangleCommentUtil.getComments(commentable, cell);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<TriangleComment> comments = DeleteTriangleCommentDialog.getDeletedComments(getComments());
        for(TriangleComment comment : comments)
            commentable.removeComment(comment);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteCommentsAction(lkp);
    }
}
