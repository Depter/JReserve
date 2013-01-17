package org.jreserve.triangle.widget.actions;

import org.jreserve.triangle.value.TriangleCoordiante;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class ContinuousSelectionAction extends AbstractMultiCellPopUpAction {
    
    protected ContinuousSelectionAction() {
    }
    
    protected ContinuousSelectionAction(Lookup lkp) {
        super(lkp);
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return checkSelection();
        return false;
    }

    private boolean checkSelection() {
        if(cells.size() < 2)
            return false;
        return checkSameAccident() || checkSameDevelopment();
    }
    
    private boolean checkSameAccident() {
        int accident = -1;
        int development = -1;
        for(TriangleCoordiante cell : cells) {
            int a = cell.getAccident();
            int d = cell.getDevelopment();
            if(accident == -1) {
                accident = a;
                development = d;
            } else {
                if(accident != a || d!=++development) 
                    return false;
            }
        }
        return true;
    }
    
    private boolean checkSameDevelopment() {
        int accident = -1;
        int development = -1;
        for(TriangleCoordiante cell : cells) {
            int a = cell.getAccident();
            int d = cell.getDevelopment();
            if(development == -1) {
                accident = a;
                development = d;
            } else {
                if(d != development || a!=++accident) 
                    return false;
            }
        }
        return true;
    }
}
