package org.jreserve.triangle.mvc.view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class TriangleWidgetHeaderRenderer extends JLabel implements TableCellRenderer {
    
    private final static Color BACKGROUND = UIManager.getColor("Panel.background");
    
    TriangleWidgetHeaderRenderer() {
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(BACKGROUND);
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(""+value);
        return this;
    }
}
