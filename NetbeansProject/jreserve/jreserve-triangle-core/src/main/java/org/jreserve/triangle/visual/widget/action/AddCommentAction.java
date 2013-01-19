package org.jreserve.triangle.visual.widget.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.visual.AddTriangleCommentDialog;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
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
    
    private Result<CommentableTriangle> tResult;
    private CommentableTriangle commentable;
    
    public AddCommentAction() {
        putValue(NAME, Bundle.CTL_AddCommentAction());
    }
    
    public AddCommentAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.CTL_AddCommentAction());
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
            return initElement();
        return false;
    }
    
    private boolean initElement() {
        commentable = lookupOne(tResult);
        return commentable != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comment = AddTriangleCommentDialog.getComment();
        if(comment != null)
            commentable.addComment(new TriangleComment(cell, comment));
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddCommentAction(lkp);
    }
}
