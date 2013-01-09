package org.jreserve.triangle.correction;

import java.util.Collection;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.ModifiedTriangularData;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.widget.WidgetEditor;
import org.jreserve.triangle.widget.model.WidgetTableModel;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@WidgetEditor.Registration(category="Triangle")
public class CorrectionWidgetEditor implements WidgetEditor {
    
    private ModifiableTriangle triangle;

    public CorrectionWidgetEditor() {
        new TriangleListener();
    }
    
    @Override
    public boolean setCellValue(WidgetTableModel model, int accident, int development, Double value) {
        Double originalValue = (Double) model.getValueAt(accident, development+1);
        if(value==null || value.equals(originalValue))
            deleteCorrection(accident, development);
        else
            addCorrection(model, accident, development, value);
        return true;
    }
    
    private void deleteCorrection(int accident, int development) {
        ModifiedTriangularData mod = getCorrectionAt(accident, development);
        if(mod != null)
            triangle.removeModification(mod);
    }
    
    private ModifiedTriangularData getCorrectionAt(int accident, int development) {
        for(ModifiedTriangularData mod : triangle.getModifications())
            if(isMyCorrection(mod, accident, development))
                return mod;
        return null;
    }
    
    private boolean isMyCorrection(ModifiedTriangularData mod, int accident, int development) {
        if(!(mod instanceof TriangleCorrectionModification))
            return false;
        TriangleCorrectionModification corr = (TriangleCorrectionModification) mod;
        return corr.myCell(accident, development);
    }
    
    private void addCorrection(WidgetTableModel model, int accident, int development, double value) {
        deleteCorrection(accident, development);
        TriangleCorrection correction = createCorrection(model, accident, development, value);
        TriangleCorrectionModification modification = new TriangleCorrectionModification(correction);
        triangle.addModification(modification);
    }
    
    private TriangleCorrection createCorrection(WidgetTableModel model, int accident, int development, double value) {
        value = getCorrigatedValue(model, accident, development, value);
        int order = triangle.getMaxModificationOrder() + 1;
        String ownerId = triangle.getOwner().getId();
        return new TriangleCorrection(ownerId, order, accident, development, value);
    }

    private double getCorrigatedValue(WidgetTableModel model, int accident, int development, double value) {
        if(development > 0 && model.isCummulated()) {
            double prev = getPreviousModelValue(model, accident, development);
            if(!Double.isNaN(prev))
                value -= prev;
        }
        return value;
    }
    
    private double getPreviousModelValue(WidgetTableModel model, int accident, int development) {
        double[][] values = model.getData().toArray();
        TriangleUtil.cummulate(values);
        return values[accident][development-1];
    }
    
    @Override
    public boolean isCellEditable(int accident, int development) {
        return triangle != null;
    }
    
    private class TriangleListener implements LookupListener {
        
        private Result<ModifiableTriangle> result;

        private TriangleListener() {
            result = Utilities.actionsGlobalContext().lookupResult(ModifiableTriangle.class);
            result.addLookupListener(this);
            resultChanged(null);
        }
        
        @Override
        public void resultChanged(LookupEvent le) {
            triangle = null;
            Collection<? extends ModifiableTriangle> items = result.allInstances();
            if(!items.isEmpty())
                triangle = items.iterator().next();
        }
    }
}
