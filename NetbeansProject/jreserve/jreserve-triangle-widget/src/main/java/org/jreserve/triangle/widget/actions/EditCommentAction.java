package org.jreserve.triangle.widget.actions;

import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class EditCommentAction extends AbstractSingleCellPopUpAction {
    
    protected EditCommentAction() {
    }
    
    protected EditCommentAction(Lookup lookup) {
        super(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return cell.hasComments();
        return false;
    }
    
}
