package org.jreserve.triangle.visual.widget.action;

import org.jreserve.triangle.entities.TriangleCell;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSingleCellPopUpAction extends AbstractPopUpAction {
    
    protected TriangleCell cell;
    
    protected AbstractSingleCellPopUpAction() {
    }
    
    protected AbstractSingleCellPopUpAction(Lookup lookup) {
        super(lookup);
    }
   
    @Override
    protected void init(Lookup lookup) {
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return initCell();
        return false;
    }
    
    private boolean initCell() {
        cell = lookupOne(TriangleCell.class);
        return cell != null;
    }
}
