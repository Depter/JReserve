package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.util.DeleteCommentDialog;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class DeleteCommentsAction extends AbstractSingleCellPopUpAction {
    
    protected DeleteCommentsAction() {
    }
    
    protected DeleteCommentsAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return checkComments();
        return false;
    }
    
    private boolean checkComments() {
        return cell.hasComments();
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<Comment> comments = DeleteCommentDialog.getDeletedComments(cell.getComments());
        deleteComments(comments);
        deleteTableComments(comments);
    }
    
    protected abstract void deleteComments(List<Comment> comments);
    
    private void deleteTableComments(List<Comment> deleted) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        for(Comment comment : deleted) {
            WidgetData<Comment> wc = new WidgetData<Comment>(accident, development, comment);
            widget.removeComment(wc);
        }
    }
}
