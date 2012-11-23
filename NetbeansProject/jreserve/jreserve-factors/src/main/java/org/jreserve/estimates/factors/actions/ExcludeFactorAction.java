package org.jreserve.estimates.factors.actions;

import java.util.Date;
import java.util.List;
import javax.swing.Action;
import org.jreserve.estimates.factors.FactorExclusion;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.estimates.factors.FactorSelectionOwner;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.widget.actions.ExcludeCellAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcludeFactorAction extends ExcludeCellAction {
    
    private Result<ProjectElement> tResult;
    private ProjectElement element;
    
    public ExcludeFactorAction() {
    }
    
    public ExcludeFactorAction(Lookup lkp) {
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
    protected void excludeCell() {
        FactorExclusion fe = createFactorExclusion();
        List<FactorExclusion> exclusions = (List<FactorExclusion>) element.getProperty(FactorSelection.FACTOR_SELECTION_EXCLUSIONS);
        exclusions.add(fe);
        element.setProperty(FactorSelection.FACTOR_SELECTION_EXCLUSIONS, exclusions);
    }
    
    private FactorExclusion createFactorExclusion() {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new FactorExclusion(getFactorSelection(), accident, development);
    }
    
    private FactorSelection getFactorSelection() {
        return ((FactorSelectionOwner) element.getValue()).getFactorSelection();
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ExcludeFactorAction(lkp);
    }
}
