package org.jreserve.estimates.factors.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.estimates.factors.FactorSelection;
import org.jreserve.estimates.factors.FactorSelectionOwner;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.smoothing.actions.AddSmoothingAction;
import org.jreserve.smoothing.core.Smoothing;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.AddFactorSmoothingAction=Smooth"
})
public class AddFactorSmoothingAction extends AddSmoothingAction {
    
    private Result<ProjectElement> tResult;
    private ProjectElement element;
    
    public AddFactorSmoothingAction() {
        putValue(NAME, NbBundle.getMessage(AddFactorSmoothingAction.class, "CTL.AddFactorSmoothingAction"));
    }
    
    public AddFactorSmoothingAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(AddFactorSmoothingAction.class, "CTL.TriangleSmoothingAction"));
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
    protected FactorSelection getOwner() {
        return ((FactorSelectionOwner) element.getValue()).getFactorSelection();
    }

    @Override
    protected void smoothingCreated(Smoothing smoothing) {
        List<Smoothing> smoothings = getOwner().getSmoothings();
        smoothing.setOrder(getOwner().getMaxSmoothingOrder()+1);
        smoothings.add(smoothing);
        element.setProperty(FactorSelection.FACTOR_SELECTION_SMOOTHINGS, smoothings);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        AddFactorSmoothingAction action = new AddFactorSmoothingAction(lkp);
        action.init();
        return action;
    }
}
