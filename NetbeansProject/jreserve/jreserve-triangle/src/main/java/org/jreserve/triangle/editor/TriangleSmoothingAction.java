package org.jreserve.triangle.editor;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.widget.actions.ContinuousSelectionAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
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
public class TriangleSmoothingAction extends ContinuousSelectionAction {
    
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
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        TriangleSmoothingAction action = new TriangleSmoothingAction(lkp);
        action.init();
        return action;
    }
}
