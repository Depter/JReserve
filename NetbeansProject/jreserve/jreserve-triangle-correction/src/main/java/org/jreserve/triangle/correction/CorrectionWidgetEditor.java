package org.jreserve.triangle.correction;

import java.util.Collection;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.widget.WidgetEditor;
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

    private final static Double EPSILON = 0.0000000001;
    
    private ModifiableTriangle triangle;

    public CorrectionWidgetEditor() {
        new TriangleListener();
    }
    
    @Override
    public boolean setCellValue(int row, int column, double value) {
        if(Math.abs(value) < EPSILON)
            deleteCorrection(row, column);
        else
            addCorrection(row, column, value);
        return true;
    }
    
    private void deleteCorrection(int row, int column) {
    }
    
    private void addCorrection(int row, int column, double value) {
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
