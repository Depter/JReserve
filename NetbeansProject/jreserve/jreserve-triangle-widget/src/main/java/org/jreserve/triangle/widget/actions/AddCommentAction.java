package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.util.AddCommentDialog;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AddCommentAction extends AbstractSingleCellPopUpAction {
    
    protected AddCommentAction() {
    }
    
    protected AddCommentAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Comment comment = AddCommentDialog.getComment();
        if(comment == null)
            return;
        comment = createComment(comment);
        WidgetData<Comment> wc = new WidgetData<Comment>(cell.getAccidentBegin(), cell.getDevelopmentBegin(), comment);
        widget.addComment(wc);
    }
    
    protected abstract Comment createComment(Comment comment);
}
