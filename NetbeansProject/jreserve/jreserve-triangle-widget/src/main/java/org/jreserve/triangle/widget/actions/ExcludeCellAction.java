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
    "LBL.ExcludeCellAction.Name=Exclude"
})
public abstract class ExcludeCellAction extends AbstractSingleCellPopUpAction {
    
    protected ExcludeCellAction() {
        putValue(NAME, Bundle.LBL_ExcludeCellAction_Name());
    }
    
    protected ExcludeCellAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.LBL_ExcludeCellAction_Name());
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return !cell.isExcluded();
        return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean excluded = !cell.isExcluded();
        excludeCell();
        //cell.setExcluded(excluded);
        //widget.fireTriangleValuesChanged();
    }
    
    protected abstract void excludeCell();
}
