package org.jreserve.triangle.correction;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleModification;
import org.jreserve.triangle.util.AbstractTriangleStackQuery;
import org.jreserve.triangle.visual.widget.action.AbstractSingleCellPopUpAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "CTL.DeleteCorrectionAction=Delete correction"
})
public class DeleteCorrectionAction extends AbstractSingleCellPopUpAction {

    private final static CorrectionQuery FILTER = new CorrectionQuery();
    private TriangleCorrection correction;
    
    public DeleteCorrectionAction() {
        putValue(NAME, Bundle.CTL_DeleteCorrectionAction());
    }
    
    public DeleteCorrectionAction(Lookup lkp) {
        super(lkp);
        putValue(NAME, Bundle.CTL_DeleteCorrectionAction());
    }
    
    @Override
    protected boolean checkEnabled() {
        if(super.checkEnabled())
            return initCorrection();
        return false;
    }

    private boolean initCorrection() {
        FILTER.cell = cell;
        correction = FILTER.query(triangle);
        return correction != null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        triangle.removeModification(correction);
    }
    
    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteCorrectionAction(lkp);
    }

    private static class CorrectionQuery extends AbstractTriangleStackQuery<TriangleCorrection> {

        private TriangleCell cell;
        private TriangleCorrection correction;
        
        @Override
        protected void initQuery() {
        }

        @Override
        protected boolean accepts(TriangleModification modification) {
            return correction == null &&
                   cell != null &&
                   (modification instanceof TriangleCorrection) &&
                   ((TriangleCorrection)modification).getTriangleCell().equals(cell);
        }

        @Override
        protected void process(TriangleModification modification) {
            this.correction = (TriangleCorrection) modification;
        }

        @Override
        protected TriangleCorrection getResult() {
            return this.correction;
        }
    
    }
}
