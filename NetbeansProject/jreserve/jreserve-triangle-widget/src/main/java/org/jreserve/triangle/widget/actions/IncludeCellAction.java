package org.jreserve.triangle.widget.actions;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.jreserve.triangle.data.Excludables;
import org.jreserve.triangle.data.TriangleExclusion;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.IncludeCellAction.Name=Include"
})
public class IncludeCellAction extends AbstractSingleCellPopUpAction {
    
    private Result<Excludables> tResult;
    private Excludables excludables;
    
    public IncludeCellAction() {
        putValue(NAME, Bundle.LBL_IncludeCellAction_Name());
    }
    
    public IncludeCellAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.LBL_IncludeCellAction_Name());
    }
    
    @Override
    protected void init(Lookup lookup) {
        if(tResult != null)
            return;
        tResult = lookup.lookupResult(Excludables.class);
        tResult.addLookupListener(this);
        super.init(lookup);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled() && cell.isExcluded())
            return initElement();
        return false;
    }
    
    private boolean initElement() {
        excludables = lookupOne(tResult);
        return excludables != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        cell.setExcluded(false);
        includeCell();
        widget.fireTriangleValuesChanged();
    }

    private void includeCell() {
        List<TriangleExclusion> exclusions = excludables.getExclusions();
        for(Iterator<TriangleExclusion> it=exclusions.iterator(); it.hasNext();)
            if(isSelectedCell(it.next()))
                it.remove();
        excludables.setExclusions(exclusions);
    }
    
    private boolean isSelectedCell(TriangleExclusion exclusion) {
        Date accident = exclusion.getAccidentDate();
        Date development = exclusion.getDevelopmentDate();
        return cell.acceptsDates(accident, development);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new IncludeCellAction(lkp);
    }
}