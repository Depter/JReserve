package org.jreserve.triangle.smoothing.actions;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.util.SmootherImpl;
import org.jreserve.triangle.smoothing.util.SmoothingMethodRegistry;
import org.jreserve.triangle.visual.widget.action.ContinuousSelectionAction;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.AddSmoothingAction=Add Smoothing"
})
public class AddSmoothingAction extends ContinuousSelectionAction implements Presenter.Popup {
    
    private Result<TriangularData> sResult;
    private TriangularData source;
    
    public AddSmoothingAction() {
    }
    
    public AddSmoothingAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(sResult != null)
            return;
        sResult = lookup.lookupResult(TriangularData.class);
        sResult.addLookupListener(this);
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return hasSmoothers() && hasSourceData();
        return false;
    }
    
    private boolean hasSmoothers() {
        List<SmootherImpl> smoothers = SmoothingMethodRegistry.getSmoothers();
        return !smoothers.isEmpty();
    }
    
    private boolean hasSourceData() {
        source = lookupOne(sResult);
        return source != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddSmoothingAction(lkp);
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.CTL_AddSmoothingAction());
        for(SmootherImpl smoother : SmoothingMethodRegistry.getSmoothers())
            menu.add(new AddSmoothingSubAction(smoother));
        menu.setEnabled(isEnabled());
        return menu;
    }
    
    private class AddSmoothingSubAction extends AbstractAction {
        
        private final SmootherImpl smoother;
        
        private AddSmoothingSubAction(SmootherImpl smoother) {
            this.smoother = smoother;
            putValue(NAME, smoother.getDisplayName());
            setEnabled(AddSmoothingAction.this.isEnabled());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Smoothing smoothing = smoother.createSmoothing(lookup);
            if(smoothing != null)
                triangle.addModification(smoothing);
        }
    }
}
