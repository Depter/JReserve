package org.jreserve.estimates.factors.actions;

import java.util.Date;
import java.util.List;
import java.util.Iterator;
import javax.swing.Action;
import org.jreserve.estimates.factors.FactorExclusion;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.estimates.factors.FactorSelectionOwner;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.widget.actions.IncludeCellAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class IncludeFactorAction extends IncludeCellAction {
    
    private Result<ProjectElement> tResult;
    private ProjectElement element;
    
    public IncludeFactorAction() {
    }
    
    public IncludeFactorAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(ProjectElement.class);
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
        element = getElement();
        return element != null;
    }
    
    private ProjectElement getElement() {
        for(ProjectElement e : tResult.allInstances())
            if(e.getValue() instanceof FactorSelectionOwner)
                return e;
        return null;
    }

    @Override
    protected void includeCell() {
        List<FactorExclusion> exclusions = (List<FactorExclusion>) element.getProperty(FactorSelection.FACTOR_SELECTION_EXCLUSIONS);
        for(Iterator<FactorExclusion> it=exclusions.iterator(); it.hasNext();)
            if(isSelectedCell(it.next()))
                it.remove();
        element.setProperty(FactorSelection.FACTOR_SELECTION_EXCLUSIONS, exclusions);
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
