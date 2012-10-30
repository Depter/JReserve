package org.jreserve.triangle.widget;

import org.jreserve.triangle.widget.util.TriangleTable;
import javax.swing.JPopupMenu;

/**
 *
 * @author Peter Decsi
 */
public interface PopUpFactory {
    
    public JPopupMenu createPopUp(TriangleTable table, int row, int column);
}
