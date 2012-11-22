package org.jreserve.triangle.management.editor.actions;

import java.util.List;
import javax.swing.Action;
import org.jreserve.smoothing.actions.DeleteSmoothingAction;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public class DeleteTriangleSmoothingAction extends DeleteSmoothingAction {
    
    private Lookup.Result<TriangleProjectElement> tResult;
    private TriangleProjectElement element;
    
    public DeleteTriangleSmoothingAction() {
    }
    
    public DeleteTriangleSmoothingAction(Lookup lkp) {
        super(lkp);
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
        return element != null && !element.getValue().getSmoothings().isEmpty();
    }

    @Override
    protected List<Smoothing> getSmoothings() {
        return element.getValue().getSmoothings();
    }

    @Override
    protected void setElementSmoothings(List<Smoothing> smoothings) {
        element.setProperty(Triangle.SMOOTHING_PROPERTY, smoothings);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteTriangleSmoothingAction(lkp);
    }
}
