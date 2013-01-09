package org.jreserve.triangle.widget.actions;

import java.util.List;
import org.jreserve.triangle.TriangleCoordiante;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSingleCellPopUpAction extends AbstractPopUpAction {
    
    protected TriangleCoordiante cell;
    
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
        List<TriangleCoordiante> cells = widget.getSelectedCells();
        cell = null;
        if(cells.size() == 1)
            cell = cells.get(0);
        return cell != null;
    }
}