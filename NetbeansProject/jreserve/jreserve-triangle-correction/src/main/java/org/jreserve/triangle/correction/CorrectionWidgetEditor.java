package org.jreserve.triangle.correction;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleModification;
import org.jreserve.triangle.util.TriangleUtil;
import org.jreserve.triangle.visual.widget.TriangleWidgetProperties;
import org.jreserve.triangle.visual.widget.WidgetEditor;
import org.jreserve.triangle.visual.widget.WidgetTableModel;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@WidgetEditor.Registration(category="Triangle")
public class CorrectionWidgetEditor implements WidgetEditor {
    
    private Lookup lookup;

    public CorrectionWidgetEditor() {
    }
    
    @Override
    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }
    
    @Override
    public boolean setCellValue(WidgetTableModel model, int accident, int development, Double value) {
        ModifiableTriangle triangle = lookup(ModifiableTriangle.class);
        if(triangle == null) {
            return false;
        } else if(isCorrectionDeleted(model, accident, development, value)) {
            deleteCorrection(triangle, new TriangleCell(accident, development));
        } else {
            addCorrection(triangle, new TriangleCell(accident, development), value);
        }
        return false;
    }
    
    private <T> T lookup(Class<T> clazz) {
        if(lookup == null)
            return null;
        return lookup.lookup(clazz);
    }
    
    private boolean isCorrectionDeleted(WidgetTableModel model, int accident, int development, Double value) {
        if(value == null)
            return true;
        Double originalValue = (Double) model.getValueAt(accident, development + 1);
        return value==null ||
               value.equals(originalValue);
    }
    
    private void deleteCorrection(ModifiableTriangle triangle, TriangleCell cell) {
        TriangleModification mod = getCorrectionAt(triangle, cell);
        if(mod != null)
            triangle.removeModification(mod);
    }
    
    private TriangleModification getCorrectionAt(ModifiableTriangle triangle, TriangleCell cell) {
        for(TriangleModification mod : triangle.getModifications())
            if(isMyCorrection(mod, cell)) 
                return mod;
        return null;
    }
    
    private boolean isMyCorrection(TriangleModification mod, TriangleCell cell) {
        if(mod instanceof TriangleCorrection) {
            TriangleCell modCell = ((TriangleCorrection)mod).getTriangleCell();
            return cell.equals(modCell);
        }
        return false;
    }
    
    private void addCorrection(ModifiableTriangle triangle, TriangleCell cell, double value) {
        deleteCorrection(triangle, cell);
        TriangleCorrection correction = createCorrection(triangle, cell, value);
        triangle.addModification(correction);
    }
    
    private TriangleCorrection createCorrection(ModifiableTriangle triangle, TriangleCell cell, double value) {
        value = getCorrigatedValue(cell, value);
        int order = triangle.getMaxModificationOrder() + 1;
        return new TriangleCorrection(order, cell, value);
    }

    private double getCorrigatedValue(TriangleCell cell, double value) {
        if(cell.getDevelopment() > 0 && isCummulated()) {
            double prev = getPreviousModelValue(cell);
            if(!Double.isNaN(prev))
                value -= prev;
        }
        return value;
    }
    
    private boolean isCummulated() {
        TriangleWidgetProperties props = lookup(TriangleWidgetProperties.class);
        return props != null &&
               props.isCummualted();
    }
    
    private double getPreviousModelValue(TriangleCell cell) {
        TriangularData data = lookup(TriangularData.class);
        if(data == null)
            return Double.NaN;
        double[][] values = data.toArray();
        TriangleUtil.cummulate(values);
        return TriangleUtil.getValue(cell.getAccident(), cell.getDevelopment()-1, values);
    }
    
    @Override
    public boolean isCellEditable(int accident, int development) {
        if(lookup(ModifiableTriangle.class) == null)
            return false;
        TriangularData data = lookup(TriangularData.class);
        return data != null &&
               accident < data.getAccidentCount() &&
               development < data.getDevelopmentCount(accident);
    }
}
