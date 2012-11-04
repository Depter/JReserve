package org.jreserve.triangle.widget.actions;

import java.util.Collections;
import java.util.Comparator;
import org.jreserve.triangle.widget.TriangleCell;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public abstract class ContinuousSelectionAction extends AbstractCellPopUpAction {
    
    private final static Comparator<TriangleCell> COMPARATOR = new Comparator<TriangleCell>() {
        @Override
        public int compare(TriangleCell o1, TriangleCell o2) {
            int dif = o1.getRow() - o2.getRow();
            if(dif != 0) return dif;
            return o1.getColumn() - o2.getColumn();
        }
    };
    
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
        Collections.sort(cells, COMPARATOR);
        return checkSameRow() || checkSameColumn();
    }
    
    private boolean checkSameRow() {
        int row = -1;
        int column = -1;
        for(TriangleCell cell : cells) {
            int r = cell.getRow();
            int c = cell.getColumn();
            if(row == -1) {
                row = r;
                column = c;
            } else {
                if(r != row || c!=++column) 
                    return false;
            }
        }
        return true;
    }
    
    private boolean checkSameColumn() {
        int row = -1;
        int column = -1;
        for(TriangleCell cell : cells) {
            int r = cell.getRow();
            int c = cell.getColumn();
            if(column == -1) {
                row = r;
                column = c;
            } else {
                if(c != column || r!=++row) 
                    return false;
            }
        }
        return true;
    }
    
    
}
