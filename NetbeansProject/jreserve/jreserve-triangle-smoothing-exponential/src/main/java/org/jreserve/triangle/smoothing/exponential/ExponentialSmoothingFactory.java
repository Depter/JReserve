package org.jreserve.triangle.smoothing.exponential;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangleCoordiante;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.SmoothingNameChecker;
import org.jreserve.triangle.smoothing.TriangleSmoothing;
import static org.jreserve.triangle.smoothing.exponential.ExponentialSmoothingCreatorPanel.SMOOTHING_ALPHA_PROPARTY;
import static org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel.SMOOTHING_APPLIED_CELLS_PROPERTY;
import static org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel.SMOOTHING_NAME_PROPERTY;
import org.jreserve.triangle.util.ClassCounterTriangleStackQuery;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotificationLineSupport;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExponentialSmoothingFactory.Dialog.Title=Geometric smoothing",
    "# {0} - smoothing id",
    "LBL.ExponentialSmoothingFactory.Default.Name=Smoothing {0}",
    "MSG.ExponentialSmoothingFactory.Name.Empty=Name is empty!",
    "# {0} - name",
    "MSG.ExponentialSmoothingFactory.Name.Exists=Name \"{0}\" already exists!",
    "MSG.ExponentialSmoothingFactory.No.Cells=At least one cell must be smoothed!",
    "MSG.ExponentialSmoothingFactory.Alpha.Empty=Field \"Alpha\" is empty!",
    "MSG.ExponentialSmoothingFactory.Alpha.Invalid=Value of \"Alpha\" must be in [0, 1]!"
})
public class ExponentialSmoothingFactory implements PropertyChangeListener {
    
    private final static boolean IS_MODAL = true;
    private final static int OPTION_TYPE = DialogDescriptor.OK_CANCEL_OPTION;
    private final static Object DEFAULT_OPTION = DialogDescriptor.OK_OPTION;
    
    private ModifiableTriangle triangle;
    private List<TriangleCoordiante> cells;
    
    private ExponentialSmoothingCreatorPanel panel;
    private DialogDescriptor dd; 
    private NotificationLineSupport nls;
    
    ExponentialSmoothingFactory(ModifiableTriangle triangle, List<TriangleCoordiante> cells, int visibleDigits) {
        this.triangle = triangle;
        this.cells = cells;
        createPanel(visibleDigits);
        createDialogDescriptor();
        nls = dd.createNotificationLineSupport();
    }
    
    private void createPanel(int visibleDigits) {
        double[] input = getInput();
        panel = new ExponentialSmoothingCreatorPanel(visibleDigits, input);
        setDefaultSmoothingName();
        panel.addPropertyChangeListener(this, SMOOTHING_NAME_PROPERTY, SMOOTHING_APPLIED_CELLS_PROPERTY, SMOOTHING_ALPHA_PROPARTY);
    }
    
    private double[] getInput() {
        TriangularData data = triangle.getTriangularData();
        return TriangleCoordiante.getValues(cells, data);
    }
    
    private void setDefaultSmoothingName() {
        int order = ClassCounterTriangleStackQuery.getCount(TriangleSmoothing.class, triangle);
        String name = Bundle.LBL_ExponentialSmoothingFactory_Default_Name(order + 1);
        panel.setSmoothingName(name);
    }
    
    private void createDialogDescriptor() {
        dd = new DialogDescriptor(panel, 
            Bundle.LBL_ExponentialSmoothingFactory_Dialog_Title(), 
            IS_MODAL, OPTION_TYPE, DEFAULT_OPTION, 
            null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        boolean valid = checkSmoothingName() && hasAppliedCells() && checkAlpha();
        dd.setValid(valid);
        if(valid)
            nls.clearMessages();
    }
    
    private boolean checkSmoothingName() {
        String name = panel.getSmoothingName();
        if(name==null || name.trim().length()==0) {
            nls.setErrorMessage(Bundle.MSG_ExponentialSmoothingFactory_Name_Empty());
            return false;
        }
        
        if(!SmoothingNameChecker.isValidName(triangle, name)) {
            nls.setErrorMessage(Bundle.MSG_ExponentialSmoothingFactory_Name_Exists(name));
            return false;
        }
        
        return true;
    }
    
    private boolean hasAppliedCells() {
        if(panel.hasApplied())
            return true;
        nls.setErrorMessage(Bundle.MSG_ExponentialSmoothingFactory_No_Cells());
        return false;
    }
    
    private boolean checkAlpha() {
        Double alpha = panel.getAlpha();
        if(alpha == null) {
            nls.setErrorMessage(Bundle.MSG_ExponentialSmoothingFactory_Alpha_Empty());
            return false;
        }
        
        double a = alpha.doubleValue();
        if(a < 0d || a > 1d) {
            nls.setErrorMessage(Bundle.MSG_ExponentialSmoothingFactory_Alpha_Invalid());
            return false;
        }
        
        return true;
    }
    
    Smoothing createSmoothing() {
        Object result = DialogDisplayer.getDefault().notify(dd);
        if(DialogDescriptor.OK_OPTION.equals(result))
            return buildSmoothing();
        return null;
    }
    
    private Smoothing buildSmoothing() {
        ExponentialSmoothing smoothing = initSmoothing();
        addSmoothingCells(smoothing);
        return smoothing;
    }
    
    private ExponentialSmoothing initSmoothing() {
        PersistentObject owner = triangle.getOwner();
        int order = triangle.getMaxModificationOrder() + 1;
        String name = panel.getSmoothingName();
        double alpha = panel.getAlpha();
        return new ExponentialSmoothing(owner, order, name, alpha);
    }
    
    private void addSmoothingCells(ExponentialSmoothing smoothing) {
        boolean[] applied = panel.getApplied();
        for(int i=0, size=cells.size(); i<size; i++)
            smoothing.addCell(new SmoothingCell(smoothing, cells.get(i), applied[i]));
    }
}
