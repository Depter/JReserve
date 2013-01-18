package org.jreserve.triangle.visual.widget.action;

import java.util.List;
import org.jreserve.triangle.entities.TriangleCell;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMultiCellPopUpAction extends AbstractPopUpAction {
  
    protected List<TriangleCell> cells;
    
    protected AbstractMultiCellPopUpAction() {
    }
    
    protected AbstractMultiCellPopUpAction(Lookup lookup) {
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
        cells = lookupAll(TriangleCell.class);
        return cells != null && !cells.isEmpty();
    }
}
