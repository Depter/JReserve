package org.jreserve.smoothing.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.SmoothingFactory;
import org.jreserve.smoothing.SmoothingUtil;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.actions.ContinuousSelectionAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.AddSmoothingAction.Smoothing=Smoothing"
})
public abstract class AddSmoothingAction  extends ContinuousSelectionAction implements Presenter.Popup {

    protected AddSmoothingAction() {
    }
    
    protected AddSmoothingAction(Lookup lkp) {
        super(lkp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.CTL_AddSmoothingAction_Smoothing());
        for(SmoothingFactory factory : SmoothingUtil.getFactories())
            menu.add(new FactoryAction(factory));
        menu.setEnabled(isEnabled());
        return menu;
    }
    
    protected abstract PersistentObject getOwner();
    
    protected abstract void smoothingCreated(Smoothing smoothing);
    
    private void addSmoothing(Smoothing smoothing) {
        if(smoothing == null)
            return;
        smoothingCreated(smoothing);
        doSmooth(smoothing);
    }
    
    private void doSmooth(Smoothing smoothing) {
        Smoother smoother = new Smoother(widget, TriangleCell.SMOOTHING_LAYER);
        smoother.smooth(smoothing);
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
            PersistentObject owner = getOwner();
            TriangleCell[] selection = cells.toArray(new TriangleCell[0]);
            addSmoothing(factory.createSmoothing(owner, widget, selection));
        }
    }

}
