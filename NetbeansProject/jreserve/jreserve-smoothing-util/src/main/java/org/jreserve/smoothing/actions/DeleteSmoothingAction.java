package org.jreserve.smoothing.actions;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.actions.AbstractPopUpAction;
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
public abstract class DeleteSmoothingAction extends AbstractPopUpAction implements Presenter.Popup {
    
    public DeleteSmoothingAction() {
    }
    
    public DeleteSmoothingAction(Lookup lkp) {
        super(lkp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.LBL_DeleteSmoothingAction_Name());
        menu.setEnabled(isEnabled());
        if(menu.isEnabled())
            addSmoothings(menu);
        return menu;
    }

    private void addSmoothings(JMenu menu) {
        for(Smoothing smoothing : getSortedSmoothings())
            menu.add(new DeleteGivenSmoothingAction(smoothing));
    }
    
    private List<Smoothing> getSortedSmoothings() {
        List<Smoothing> smoothings = getSmoothings();
        Collections.sort(smoothings, new Comparator<Smoothing>() {
            @Override
            public int compare(Smoothing o1, Smoothing o2) {
                String s1 = o1.getName();
                String s2 = o2.getName();
                return s1.compareToIgnoreCase(s2);
            }
        });
        return smoothings;
    }
    
    protected abstract List<Smoothing> getSmoothings();
    
    protected void deleteSmoothing(Smoothing smoothing) {
        List<Smoothing> smoothings = getSmoothings();
        smoothings.remove(smoothing);
        setWidgetSmoothings(smoothings);
        setElementSmoothings(smoothings);
    }
    
    protected abstract void setElementSmoothings(List<Smoothing> smoothings);
    
    private void setWidgetSmoothings(List<Smoothing> smoothings) {
        widget.setValueLayer(TriangleCell.SMOOTHING_LAYER, Collections.EMPTY_LIST);
        Smoother smoother = new Smoother(widget, TriangleCell.SMOOTHING_LAYER);
        for(Smoothing smoothing : smoothings)
            smoother.smooth(smoothing);
    }
    
    private class DeleteGivenSmoothingAction extends AbstractAction {
        private Smoothing smoothing;
        
        private DeleteGivenSmoothingAction(Smoothing smoothing) {
            this.smoothing = smoothing;
            putValue(NAME, smoothing.getName());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteSmoothing(smoothing);
        }
    }
    
}
