package org.jreserve.triangle.visual.widget;

import java.util.List;
import javax.swing.table.TableModel;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleComment;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetTableModel extends TableModel {

    public void setLookup(Lookup lookup);
    
    public List<TriangleComment> getComments(int row, int column);
    
    public List<TriangleCell> getCells(int[] rows, int[] columns);
    
    public String getLayerId(int row, int column);
    
    public @interface Registration {
        public int position() default Integer.MAX_VALUE;
        public String displayName();
        public String icon();
    }
}
