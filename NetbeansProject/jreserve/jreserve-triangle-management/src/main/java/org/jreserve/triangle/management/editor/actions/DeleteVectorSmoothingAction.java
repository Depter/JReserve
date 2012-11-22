package org.jreserve.triangle.management.editor.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.smoothing.actions.DeleteSmoothingAction;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.management.VectorProjectElement;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public class DeleteVectorSmoothingAction extends DeleteSmoothingAction {
    
    private Lookup.Result<VectorProjectElement> tResult;
    private VectorProjectElement element;
    
    public DeleteVectorSmoothingAction() {
    }
    
    public DeleteVectorSmoothingAction(Lookup lkp) {
        super(lkp);
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
        return element != null && !element.getValue().getSmoothings().isEmpty();
    }

    @Override
    protected List<Smoothing> getSmoothings() {
        return element.getValue().getSmoothings();
    }

    @Override
    protected void setElementSmoothings(List<Smoothing> smoothings) {
        element.setProperty(Vector.SMOOTHING_PROPERTY, smoothings);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteVectorSmoothingAction(lkp);
    }
}
