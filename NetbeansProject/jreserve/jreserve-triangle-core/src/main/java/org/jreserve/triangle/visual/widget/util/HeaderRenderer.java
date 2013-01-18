package org.jreserve.triangle.visual.widget.util;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;
import org.jreserve.localesettings.util.LocaleSettings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HeaderRenderer extends JLabel implements TableCellRenderer {
    
    private final static Color BACKGROUND = UIManager.getColor("Panel.background");
    
    private DateFormat df = LocaleSettings.getDateFormat();
    
    public HeaderRenderer() {
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(BACKGROUND);
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Date)
            setText(df.format((Date) value));
        else
            setText(""+value);
        return this;
    }
}
