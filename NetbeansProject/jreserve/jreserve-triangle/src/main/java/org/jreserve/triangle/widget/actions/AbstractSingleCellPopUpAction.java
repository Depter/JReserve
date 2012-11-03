package org.jreserve.triangle.widget.actions;

import org.jreserve.triangle.widget.data.TriangleCell;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSingleCellPopUpAction extends AbstractPopUpAction {
    
    private Result<TriangleCell> cResult;
    
    protected TriangleCell cell;
     
    protected AbstractSingleCellPopUpAction() {
    }
    
    protected AbstractSingleCellPopUpAction(Lookup lookup) {
        super(lookup);
    }
   
    @Override
    protected void init(Lookup lookup) {
        if(cResult != null)
            return;
        cResult = lookup.lookupResult(TriangleCell.class);
        cResult.addLookupListener(this);
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return initCell();
        return false;
    }
    
    private boolean initCell() {
        cell = lookupOne(cResult);
        return cell != null;
    }

}
