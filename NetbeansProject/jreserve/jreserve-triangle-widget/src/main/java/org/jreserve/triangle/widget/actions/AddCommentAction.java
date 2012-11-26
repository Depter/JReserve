package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.Action;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.data.Commentable;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.util.AddCommentDialog;
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
    "CTL.AddCommentAction=Add comment"
})
public class AddCommentAction extends AbstractSingleCellPopUpAction {
    
    private Result<Commentable> tResult;
    private Commentable commentable;
    
    public AddCommentAction() {
        putValue(NAME, NbBundle.getMessage(AddCommentAction.class, "CTL.AddCommentAction"));
    }
    
    public AddCommentAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, NbBundle.getMessage(AddCommentAction.class, "CTL.AddCommentAction"));
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
            return initCommentable();
        return false;
    }
    
    private boolean initCommentable() {
        commentable = lookupOne(tResult);
        return commentable != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Comment comment = AddCommentDialog.getComment();
        if(comment != null)
            addComment(comment);
    }
    
    private void addComment(Comment comment) {
        TriangleComment triangleComment = createTriangleComment(comment);
        commentable.addComment(triangleComment);
        addWidgetComment(triangleComment);
    }
    
    private TriangleComment createTriangleComment(Comment comment) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        String user = comment.getUserName();
        String text = comment.getCommentText();
        return new TriangleComment(commentable.getOwner(), accident, development, user, text);
    }
    
    private void addWidgetComment(TriangleComment comment) {
        WidgetData<Comment> wc = new WidgetData<Comment>(cell.getAccidentBegin(), cell.getDevelopmentBegin(), comment);
        widget.addComment(wc);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddCommentAction(lkp);
    }
}
