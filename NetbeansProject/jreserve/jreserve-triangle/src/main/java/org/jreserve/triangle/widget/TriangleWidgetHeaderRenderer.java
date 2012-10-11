package org.jreserve.triangle.widget;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Peter Decsi
 */
class TriangleWidgetHeaderRenderer extends JLabel implements TableCellRenderer {
    
    private final static Color BACKGROUND = UIManager.getColor("Panel.background");
    
    TriangleWidgetHeaderRenderer() {
        setBorder(BorderFactory.createRaisedBevelBorder());
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
