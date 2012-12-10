package org.jreserve.triangle.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultWidgetRenderer extends JLabel implements TableCellRenderer {

    protected final static Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    
    protected Color bgFocused = UIManager.getColor("Table.focusCellBackground");
    protected Color bgDropLocation = UIManager.getColor("Table.dropCellBackground");
    protected Color fgFocused = UIManager.getColor("Table.focusCellForeground");
    protected Color fgDropLocation = UIManager.getColor("Table.dropCellForeground");
    
    protected final DoubleRenderer valueRenderer = new DoubleRenderer();
    protected Border focusedSelected = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
    protected Border focusedNotSelected =  UIManager.getBorder("Table.focusCellHighlightBorder");
    protected Border selected = createCellBorder();
    protected Border notSelected = selected;
    protected boolean hasRemark;
    
    private static Border createCellBorder() {
        Color color = UIManager.getColor("InternalFrame.borderDarkShadow");
        if(color == null)
            color = new Color(122, 138, 153);
        return BorderFactory.createMatteBorder(0, 0, 1, 1, color);
    }
    
    public DefaultWidgetRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value == null) {
            formatAsEmpty(table);
        } else {
            formatAsFilled(table, value, isSelected, hasFocus, row, column);
        }
        return this;
    }
    
    protected void formatAsEmpty(JTable table) {
        setBackground(getEmptyBackground(table));
        super.setBorder(EMPTY_BORDER);
        super.setText(null);
        hasRemark = false;
    }
    
    private Color getEmptyBackground(JTable table) {
        Color color = null;
        if(table != null && table.getParent() != null)
            color = table.getParent().getBackground();
        if(color == null)
            color = UIManager.getColor("Panel.background");
        return color;
    }
    
    private void formatAsFilled(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.setText(getStringValue(table, value));
        super.setBackground(getBackground(table, value, isSelected, hasFocus, row, column));
        super.setForeground(getForeground(table, value, isSelected, hasFocus, row, column));
        super.setFont(getFont(table, value, isSelected, hasFocus, row, column));
        super.setBorder(getBorder(table, value, isSelected, hasFocus, row, column));
    }
    
    protected String getStringValue(JTable table, Object value) {
        if(value == null) return null;
        if(value instanceof Double)
            return getStringValue(table, (Double) value);
        return ""+value;
    }
    
    private String getStringValue(JTable table, Double value) {
        int decimals = (Integer) table.getClientProperty(TriangleWidget.VISIBLE_DECIMALS_PROPERTY);
        if(valueRenderer.getFractionDigits() != decimals)
            valueRenderer.setFractionDigits(decimals);
        return valueRenderer.toString(value);
    }
    
    protected Color getBackground(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus && !isSelected && table.isCellEditable(row, column))
            return getBgFocused(table);
        else if(isDropLocation(table, row, column)) 
            return getBgDropLocation(table);
        else if(isSelected)
            return getBgSelected(table);
        else
            return getBgNotSelected(table);
    }
    
    protected Color getBgFocused(JTable table) {
        return bgFocused;
    }
    
    protected boolean isDropLocation(JTable table, int row, int column) {
        JTable.DropLocation dropLocation = table.getDropLocation();
        return dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column;
    }
    
    protected Color getBgDropLocation(JTable table) {
        return bgDropLocation;
    }
    
    protected Color getBgSelected(JTable table) {
        return table.getSelectionBackground();
    }
    
    protected Color getBgNotSelected(JTable table) {
        return table.getBackground();
    }
    
    protected Color getForeground(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus && !isSelected && table.isCellEditable(row, column))
            return getFgFocused(table);
        else if(isDropLocation(table, row, column))
            return getFgDropLocation(table);
        else if(isSelected)
            return getFgSelected(table);
        else
            return getFgNotSelected(table);
    }
    
    protected Color getFgFocused(JTable table) {
        return fgFocused;
    }
    
    protected Color getFgDropLocation(JTable table) {
        return fgDropLocation;
    }
    
    protected Color getFgSelected(JTable table) {
        return table.getSelectionForeground();
    }
    
    protected Color getFgNotSelected(JTable table) {
        return table.getForeground();
    }
    
    protected Font getFont(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return table.getFont();
    }
    
    protected Border getBorder(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(hasFocus)
            return isSelected? 
                    getFocusedSelectedBorder(table) : 
                    getFocusedNotSelectedBorder(table);
        else if(isSelected)
            return getSelectedBorder(table);
        else
            return getNotSelectedBorder(table);
    }
    
    protected Border getFocusedSelectedBorder(JTable table) {
        return focusedSelected;
    }
    
    protected Border getFocusedNotSelectedBorder(JTable table) {
        return focusedNotSelected;
    }
    
    protected Border getSelectedBorder(JTable table) {
        return selected;
    }
    
    protected Border getNotSelectedBorder(JTable table) {
        return notSelected;
    }
}
