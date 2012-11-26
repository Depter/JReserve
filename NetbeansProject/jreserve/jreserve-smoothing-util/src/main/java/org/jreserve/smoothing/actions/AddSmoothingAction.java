package org.jreserve.smoothing.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.SmoothingFactory;
import org.jreserve.smoothing.core.Smoothable;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.util.SmoothingMethodRegistry;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.actions.ContinuousSelectionAction;
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
    "CTL.AddSmoothingAction.Smoothing=Add Smoothing"
})
public class AddSmoothingAction  extends ContinuousSelectionAction implements Presenter.Popup {
                          
    protected Result<Smoothable> tResult;
    protected Smoothable smoothable;

    public AddSmoothingAction() {
    }
    
    public AddSmoothingAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(Smoothable.class);
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
        smoothable = super.lookupOne(tResult);
        return smoothable != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.CTL_AddSmoothingAction_Smoothing());
        for(SmoothingFactory factory : SmoothingMethodRegistry.getFactories())
            menu.add(new FactoryAction(factory));
        menu.setEnabled(isEnabled());
        return menu;
    }
    
    private void addSmoothing(Smoothing smoothing) {
        if(smoothing == null)
            return;
        smoothable.addSmoothing(smoothing);
        doSmooth(smoothing);
    }
    
    private void doSmooth(Smoothing smoothing) {
        Smoother smoother = new Smoother(widget, TriangleCell.SMOOTHING_LAYER);
        smoother.smooth(smoothing);
        widget.fireTriangleValuesChanged();
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new AddSmoothingAction(lkp);
    }
    
    private class FactoryAction extends AbstractAction {

        private SmoothingFactory factory;
        
        private FactoryAction(SmoothingFactory factory) {
            this.factory = factory;
            putValue(NAME, factory.getDisplayName());
            setEnabled(AddSmoothingAction.this.isEnabled());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            TriangleCell[] selection = cells.toArray(new TriangleCell[0]);
            addSmoothing(factory.createSmoothing(smoothable, widget, selection));
        }
    }

}
