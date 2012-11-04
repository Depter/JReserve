package org.jreserve.triangle.widget.actions;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.triangle.widget.TriangleCell;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCellPopUpAction extends AbstractPopUpAction {
    
    private Lookup.Result<TriangleCell> cResult;
    
    protected List<TriangleCell> cells = new ArrayList<TriangleCell>();
    
    protected AbstractCellPopUpAction() {
    }
    
    protected AbstractCellPopUpAction(Lookup lookup) {
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
        cells.clear();
        cells.addAll(cResult.allInstances());
        return !cells.isEmpty();
    }

}
