package org.jreserve.triangle.mvc2.view;

import javax.swing.JPopupMenu;

/**
 *
 * @author Peter Decsi
 */
public interface PopUpFactory {
    
    public JPopupMenu createPopUp(TriangleTable table, int row, int column);
}
