package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.jreserve.triangle.comment.Commentable;
import org.jreserve.triangle.comment.TriangleComment;
import org.jreserve.triangle.comment.visual.AddCommentDialog;
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
    
    private Result<Commentable> tResult;
    private Commentable commentable;
    
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
        tResult = lookup.lookupResult(Commentable.class);
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
        String comment = AddCommentDialog.getComment();
        if(comment != null)
            addComment(comment);
    }
    
    private void addComment(String comment) {
        TriangleComment triangleComment = createTriangleComment(comment);
        commentable.addComment(triangleComment);
    }
    
    private TriangleComment createTriangleComment(String comment) {
        return new TriangleComment(commentable.getOwner(), 
                cell.getAccident(), cell.getDevelopment(), 
                System.getProperty("user.name"), 
                comment);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddCommentAction(lkp);
    }
}
