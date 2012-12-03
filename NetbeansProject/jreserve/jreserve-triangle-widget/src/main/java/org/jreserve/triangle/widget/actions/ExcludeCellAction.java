package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.Action;
import org.jreserve.triangle.data.Excludables;
import org.jreserve.triangle.data.TriangleExclusion;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExcludeCellAction.Name=Exclude"
})
public class ExcludeCellAction extends AbstractSingleCellPopUpAction {
    
    private Result<Excludables> tResult;
    private Excludables excludables;
    
    public ExcludeCellAction() {
        putValue(NAME, Bundle.LBL_ExcludeCellAction_Name());
    }
    
    public ExcludeCellAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.LBL_ExcludeCellAction_Name());
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(Excludables.class);
        tResult.addLookupListener(this);
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled() && !cell.isExcluded())
            return initElement();
        return false;
    }
    
    private boolean initElement() {
        excludables = lookupOne(tResult);
        return excludables != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        TriangleExclusion fe = createTriangleExclusion();
        excludables.addExclusion(fe);
    }
    
    private TriangleExclusion createTriangleExclusion() {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new TriangleExclusion(excludables.getOwner(), accident, development);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ExcludeCellAction(lkp);
    }
}
