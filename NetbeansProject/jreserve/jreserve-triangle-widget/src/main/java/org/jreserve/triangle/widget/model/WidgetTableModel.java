package org.jreserve.triangle.widget.model;

import java.util.List;
import javax.swing.table.TableModel;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.widget.WidgetCell;
import org.jreserve.triangle.widget.WidgetEditor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetTableModel extends TableModel {

    public void setData(TriangularData data);

    public TriangularData getData();
    
    public void setCummulated(boolean cummulated);
    
    public boolean isCummulated();
    
    public void setWidgetEditor(WidgetEditor editor);
    
    public WidgetEditor getWidgetEditor();
    
    public String getLayerId(int row, int column);
    
    public List<WidgetCell> getCells(int[] rows, int[] columns);
    
    public @interface Registration {
        public int position() default Integer.MAX_VALUE;
        public String displayName();
        public String icon();
    }

}
