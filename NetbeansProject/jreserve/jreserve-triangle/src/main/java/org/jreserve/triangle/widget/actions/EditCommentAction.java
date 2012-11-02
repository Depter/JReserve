package org.jreserve.triangle.widget.actions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class EditCommentAction extends AbstractSingleCellPopUpAction {
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return cell.hasComments();
        return false;
    }
    
}
