package org.jreserve.estimates.factors.actions;

import java.util.Date;
import javax.swing.Action;
import org.jreserve.estimates.factors.Excludables;
import org.jreserve.estimates.factors.FactorExclusion;
import org.jreserve.triangle.widget.actions.ExcludeCellAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcludeFactorAction extends ExcludeCellAction {
    
    private Result<Excludables> tResult;
    private Excludables excludables;
    
    public ExcludeFactorAction() {
    }
    
    public ExcludeFactorAction(Lookup lkp) {
        super(lkp);
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
        if(super.checkEnabled())
            return initElement();
        return false;
    }
    
    private boolean initElement() {
        excludables = lookupOne(tResult);
        return excludables != null;
    }

    @Override
    protected void excludeCell() {
        FactorExclusion fe = createFactorExclusion();
        excludables.addExclusion(fe);
    }
    
    private FactorExclusion createFactorExclusion() {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new FactorExclusion(excludables.getOwner(), accident, development);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ExcludeFactorAction(lkp);
    }
}
