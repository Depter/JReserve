package org.jreserve.estimates.factors.actions;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.jreserve.estimates.factors.Excludables;
import org.jreserve.estimates.factors.FactorExclusion;
import org.jreserve.triangle.widget.actions.IncludeCellAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class IncludeFactorAction extends IncludeCellAction {
    
    private Result<Excludables> tResult;
    private Excludables excludables;
    
    public IncludeFactorAction() {
    }
    
    public IncludeFactorAction(Lookup lkp) {
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
    protected void includeCell() {
        List<FactorExclusion> exclusions = excludables.getExclusions();
        for(Iterator<FactorExclusion> it=exclusions.iterator(); it.hasNext();)
            if(isSelectedCell(it.next()))
                it.remove();
        excludables.setExclusions(exclusions);
    }
    
    private boolean isSelectedCell(FactorExclusion exclusion) {
        Date accident = exclusion.getAccidentDate();
        Date development = exclusion.getDevelopmentDate();
        return cell.acceptsDates(accident, development);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new IncludeFactorAction(lkp);
    }
}
