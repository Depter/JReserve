package org.jreserve.triangle.management.editor.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.actions.AddSmoothingAction;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "CTL.TriangleSmoothingAction=Smooth"
})
public class TriangleSmoothingAction extends AddSmoothingAction {
    
    private Result<TriangleProjectElement> tResult;
    private TriangleProjectElement element;
    
    public TriangleSmoothingAction() {
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.TriangleSmoothingAction"));
    }
    
    public TriangleSmoothingAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.TriangleSmoothingAction"));
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(TriangleProjectElement.class);
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
        element = lookupOne(tResult);
        return element != null;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        TriangleSmoothingAction action = new TriangleSmoothingAction(lkp);
        action.init();
        return action;
    }

    @Override
    protected PersistentObject getOwner() {
        return element.getValue();
    }

    @Override
    protected void smoothingCreated(Smoothing smoothing) {
        List<Smoothing> smoothings = element.getValue().getSmoothings();
        smoothing.setOrder(element.getValue().getMaxSmoothingOrder()+1);
        smoothings.add(smoothing);
        element.setProperty(Triangle.SMOOTHING_PROPERTY, smoothings);
    }
}
