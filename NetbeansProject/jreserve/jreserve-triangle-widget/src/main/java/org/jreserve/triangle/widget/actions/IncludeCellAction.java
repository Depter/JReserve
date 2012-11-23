package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.IncludeCellAction.Name=Include"
})
public abstract class IncludeCellAction extends AbstractSingleCellPopUpAction {
    
    protected IncludeCellAction() {
        putValue(NAME, Bundle.LBL_IncludeCellAction_Name());
    }
    
    protected IncludeCellAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.LBL_IncludeCellAction_Name());
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return cell.isExcluded();
        return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean excluded = !cell.isExcluded();
        includeCell();
        cell.setExcluded(excluded);
        widget.fireTriangleValuesChanged();
    }
    
    protected abstract void includeCell();
}