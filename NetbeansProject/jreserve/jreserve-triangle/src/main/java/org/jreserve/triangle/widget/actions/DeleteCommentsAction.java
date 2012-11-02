package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.widget.data.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class DeleteCommentsAction extends AbstractCellPopUpAction {
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return checkComments();
        return false;
    }
    
    private boolean checkComments() {
        for(TriangleCell cell : cells)
            if(cell.hasComments())
                return true;
        return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        List<Comment> comments = getComments();
        deleteComments(comments);
        deleteTableComments();
    }
    
    private List<Comment> getComments() {
        List<Comment> comments = new ArrayList<Comment>();
        for(TriangleCell cell : cells)
            comments.addAll(cell.getComments());
        return comments;
    }
    
    protected abstract void deleteComments(List<Comment> comments);
    
    private void deleteTableComments() {
        for(TriangleCell cell : cells)
            cell.clearComments();
        widget.refreshTableData();
    }
}
