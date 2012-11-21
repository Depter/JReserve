package org.jreserve.triangle.management.editor.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.actions.AddSmoothingAction;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.management.VectorProjectElement;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "CTL.VectorSmoothingAction=Smooth"
})
public class VectorSmoothingAction  extends AddSmoothingAction {
    
    private Result<VectorProjectElement> tResult;
    private VectorProjectElement element;
    
    public VectorSmoothingAction() {
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.VectorSmoothingAction"));
    }
    
    public VectorSmoothingAction(Lookup lookup) {
        super(lookup);
        putValue(NAME, NbBundle.getMessage(AddTriangleCommentAction.class, "CTL.VectorSmoothingAction"));
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(VectorProjectElement.class);
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
        VectorSmoothingAction action = new VectorSmoothingAction(lkp);
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
        smoothings.add(smoothing);
        element.setProperty(Vector.SMOOTHING_PROPERTY, smoothings);
    }
}
