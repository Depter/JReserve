package org.jreserve.estimates.factors.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.estimates.factors.FactorSelectionOwner;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.actions.DeleteSmoothingAction;
import org.jreserve.smoothing.core.Smoothing;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DeleteFactorSmoothingAction extends DeleteSmoothingAction {
    
    private Result<ProjectElement> tResult;
    private ProjectElement element;
    
    public DeleteFactorSmoothingAction() {
    }
    
    public DeleteFactorSmoothingAction(Lookup lkp) {
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
        if(element == null) return false;
        return !getFactorSelection().getSmoothings().isEmpty();
    }
    
    private ProjectElement getElement() {
        for(ProjectElement e : tResult.allInstances())
            if(e.getValue() instanceof FactorSelectionOwner)
                return e;
        return null;
    }
    
    private FactorSelection getFactorSelection() {
        return ((FactorSelectionOwner) element.getValue()).getFactorSelection();
    }

    @Override
    protected List<Smoothing> getSmoothings() {
        return getFactorSelection().getSmoothings();
    }

    @Override
    protected void setElementSmoothings(List<Smoothing> smoothings) {
        element.setProperty(FactorSelection.FACTOR_SELECTION_SMOOTHINGS, smoothings);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteFactorSmoothingAction(lkp);
    }
}
