package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.util.AddCommentDialog;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AddCommentAction extends AbstractSingleCellPopUpAction {
    
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