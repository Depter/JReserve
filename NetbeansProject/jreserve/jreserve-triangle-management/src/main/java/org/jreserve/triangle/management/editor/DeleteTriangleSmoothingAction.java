package org.jreserve.triangle.management.editor;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.management.TriangleProjectElement;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.actions.AbstractPopUpAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DeleteTriangleSmoothingAction.Name=Delete smoothing"
})
public class DeleteTriangleSmoothingAction extends AbstractPopUpAction implements Presenter.Popup {
    
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
        return element != null && element.getValue().getSmoothings() != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteTriangleSmoothingAction(lkp);
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.LBL_DeleteTriangleSmoothingAction_Name());
        menu.setEnabled(isEnabled());
        if(menu.isEnabled())
            addSmoothings(menu);
        return menu;
    }

    private void addSmoothings(JMenu menu) {
        for(Smoothing smoothing : getSmoothings())
            menu.add(new DeleteSmoothingAction(smoothing));
    }
    
    private List<Smoothing> getSmoothings() {
        List<Smoothing> smoothings = element.getValue().getSmoothings();
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
    
    private void deleteSmoothing(Smoothing smoothing) {
        List<Smoothing> smoothings = element.getValue().getSmoothings();
        smoothings.remove(smoothing);
        setWidgetSmoothings(smoothings);
        element.setProperty(TriangleProjectElement.SMOOTHING_PROPERTY, smoothings);
    }
    
    private void setWidgetSmoothings(List<Smoothing> smoothings) {
        widget.setValueLayer(TriangleCell.SMOOTHING_LAYER, Collections.EMPTY_LIST);
        Smoother smoother = new Smoother(widget, TriangleCell.SMOOTHING_LAYER);
        for(Smoothing smoothing : smoothings)
            smoother.smooth(smoothing);
    }
    
    private class DeleteSmoothingAction extends AbstractAction {
        private Smoothing smoothing;
        
        private DeleteSmoothingAction(Smoothing smoothing) {
            this.smoothing = smoothing;
            putValue(NAME, smoothing.getName());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteSmoothing(smoothing);
        }
    }
}
