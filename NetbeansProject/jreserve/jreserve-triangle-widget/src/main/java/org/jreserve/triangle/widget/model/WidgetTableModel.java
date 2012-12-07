package org.jreserve.triangle.widget.model;

import javax.swing.table.TableModel;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetTableModel extends TableModel {

    public void setData(TriangularData data);
}
