package org.jreserve.triangle.widget.actions;

import java.util.List;
import org.jreserve.triangle.widget.WidgetCell;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMultiCellPopUpAction extends AbstractPopUpAction {
  
    protected List<WidgetCell> cells;
    
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
        cells = widget.getSelectedCells();
        return cells != null && !cells.isEmpty();
    }
}
