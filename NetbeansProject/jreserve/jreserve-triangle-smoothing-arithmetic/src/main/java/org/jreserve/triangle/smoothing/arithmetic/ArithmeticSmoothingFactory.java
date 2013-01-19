package org.jreserve.triangle.smoothing.arithmetic;

import org.jreserve.triangle.smoothing.arithmetic.entities.ArithmeticSmoothing;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.SmoothingNameChecker;
import org.jreserve.triangle.smoothing.TriangleSmoothing;
import static org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel.SMOOTHING_APPLIED_CELLS_PROPERTY;
import static org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel.SMOOTHING_NAME_PROPERTY;
import org.jreserve.triangle.util.ClassCounterTriangleStackQuery;
import org.jreserve.triangle.util.TriangleUtil;
import org.jreserve.triangle.visual.widget.TriangleWidgetProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ArithmeticSmoothingFactory.Dialog.Title=Geometric smoothing",
    "# {0} - smoothing id",
    "LBL.ArithmeticSmoothingFactory.Default.Name=Smoothing {0}",
    "MSG.ArithmeticSmoothingFactory.Name.Empty=Name is empty!",
    "# {0} - name",
    "MSG.ArithmeticSmoothingFactory.Name.Exists=Name \"{0}\" already exists!",
    "MSG.ArithmeticSmoothingFactory.No.Cells=At least one cell must be smoothed!"
})
class ArithmeticSmoothingFactory implements PropertyChangeListener {
    
    private final static boolean IS_MODAL = true;
    private final static int OPTION_TYPE = DialogDescriptor.OK_CANCEL_OPTION;
    private final static Object DEFAULT_OPTION = DialogDescriptor.OK_OPTION;
    
    private ModifiableTriangle triangle;
    private TriangularData source;
    private List<TriangleCell> cells;
    
    private ArithmeticSmoothingCreatePanel panel;
    private DialogDescriptor dd; 
    private NotificationLineSupport nls;
    
    ArithmeticSmoothingFactory(Lookup lookup) {
        this.triangle = lookup.lookup(ModifiableTriangle.class);
        this.cells = new ArrayList<TriangleCell>(lookup.lookupAll(TriangleCell.class));
        this.source = lookup.lookup(TriangularData.class);
        Collections.sort(cells);
        createPanel(lookup);
        createDialogDescriptor();
        nls = dd.createNotificationLineSupport();
    }
    
    private void createPanel(Lookup lookup) {
        double[] input = TriangleUtil.getValues(cells, source);
        panel = new ArithmeticSmoothingCreatePanel(getVisibleDigits(lookup), input);
        setDefaultSmoothingName();
        panel.addPropertyChangeListener(this, SMOOTHING_NAME_PROPERTY, SMOOTHING_APPLIED_CELLS_PROPERTY);
    }
    
    private int getVisibleDigits(Lookup lookup) {
        TriangleWidgetProperties props = lookup.lookup(TriangleWidgetProperties.class);
        if(props == null)
            return 0;
        return props.getVisibleDigits();
    }
    
    private void setDefaultSmoothingName() {
        int order = ClassCounterTriangleStackQuery.getCount(TriangleSmoothing.class, triangle);
        String name = Bundle.LBL_ArithmeticSmoothingFactory_Default_Name(order + 1);
        panel.setSmoothingName(name);
    }
    
    private void createDialogDescriptor() {
        dd = new DialogDescriptor(panel, 
            Bundle.LBL_ArithmeticSmoothingFactory_Dialog_Title(), 
            IS_MODAL, OPTION_TYPE, DEFAULT_OPTION, 
            null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        boolean valid = checkSmoothingName() && hasAppliedCells();
        dd.setValid(valid);
        if(valid)
            nls.clearMessages();
    }
    
    private boolean checkSmoothingName() {
        String name = panel.getSmoothingName();
        if(name==null || name.trim().length()==0) {
            nls.setErrorMessage(Bundle.MSG_ArithmeticSmoothingFactory_Name_Empty());
            return false;
        }
        
        if(!SmoothingNameChecker.isValidName(triangle, name)) {
            nls.setErrorMessage(Bundle.MSG_ArithmeticSmoothingFactory_Name_Exists(name));
            return false;
        }
        
        return true;
    }
    
    private boolean hasAppliedCells() {
        if(panel.hasApplied())
            return true;
        nls.setErrorMessage(Bundle.MSG_ArithmeticSmoothingFactory_No_Cells());
        return false;
    }
    
    Smoothing createSmoothing() {
        Object result = DialogDisplayer.getDefault().notify(dd);
        if(DialogDescriptor.OK_OPTION.equals(result))
            return buildSmoothing();
        return null;
    }
    
    private Smoothing buildSmoothing() {
        ArithmeticSmoothing smoothing = initSmoothing();
        addSmoothingCells(smoothing);
        return smoothing;
    }
    
    private ArithmeticSmoothing initSmoothing() {
        int order = triangle.getMaxModificationOrder() + 1;
        String name = panel.getSmoothingName();
        return new ArithmeticSmoothing(order, name);
    }
    
    private void addSmoothingCells(ArithmeticSmoothing smoothing) {
        boolean[] applied = panel.getApplied();
        for(int i=0, size=cells.size(); i<size; i++)
            smoothing.addCell(new SmoothingCell(smoothing, cells.get(i), applied[i]));
    }
}
