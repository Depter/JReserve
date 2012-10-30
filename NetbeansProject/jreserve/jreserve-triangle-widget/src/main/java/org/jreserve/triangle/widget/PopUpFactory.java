package org.jreserve.triangle.widget;

import javax.swing.JPopupMenu;
import org.jreserve.triangle.widget.view.TriangleTable;

/**
 *
 * @author Peter Decsi
 */
public interface PopUpFactory {
    
    public JPopupMenu createPopUp(TriangleTable table, int row, int column);
}
