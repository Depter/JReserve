package org.jreserve.triangle.widget;

import javax.swing.table.TableModel;
import org.jreserve.data.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
interface TriangleModel extends TableModel {

    public void setDataTable(TriangleTable table);
    
    public void setCummulated(boolean cummulated);

    public boolean hasValueAt(int row, int column);
}
