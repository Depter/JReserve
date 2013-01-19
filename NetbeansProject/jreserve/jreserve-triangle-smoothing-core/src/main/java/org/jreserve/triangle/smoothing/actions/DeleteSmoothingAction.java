package org.jreserve.triangle.smoothing.actions;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingTriangleStackQuery;
import org.jreserve.triangle.smoothing.TriangleSmoothing;
import org.jreserve.triangle.visual.widget.action.AbstractSingleCellPopUpAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DeleteSmoothingAction.Name=Delete smoothing"
})
public class DeleteSmoothingAction extends AbstractSingleCellPopUpAction implements Presenter.Popup {
    
    private final static Comparator<TriangleSmoothing> COMPARATOR = new Comparator<TriangleSmoothing>() {
        @Override
        public int compare(TriangleSmoothing o1, TriangleSmoothing o2) {
            String n1 = o1.getSmoothing().getName();
            String n2 = o2.getSmoothing().getName();
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    public DeleteSmoothingAction() {
    }
    
    public DeleteSmoothingAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    protected boolean checkEnabled() {
        return super.checkEnabled() &&
               hasSmoothings();
    }
    
    private boolean hasSmoothings() {
        return !(getSmoothings().isEmpty());
    }

    private List<Smoothing> getSmoothings() {
        List<Smoothing> result = SmoothingTriangleStackQuery.getSmoothings(triangle, cell);
        Collections.sort(result);
        return result;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteSmoothingAction(lkp);
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.LBL_DeleteSmoothingAction_Name());
        menu.setEnabled(isEnabled());
        if(menu.isEnabled())
            addSubActions(menu);
        return menu;
    }
    
    private void addSubActions(JMenu menu) {
        for(Smoothing smoothing : getSmoothings())
            menu.add(new DeleteSmoothingSubAction(smoothing));
    }
    
    private class DeleteSmoothingSubAction extends AbstractAction {
        
        private final Smoothing smoothing;
        
        private DeleteSmoothingSubAction(Smoothing smoothing) {
            this.smoothing = smoothing;
            putValue(NAME, smoothing.getName());
            setEnabled(DeleteSmoothingAction.this.isEnabled());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            triangle.removeModification(smoothing);
        }
    }
}
