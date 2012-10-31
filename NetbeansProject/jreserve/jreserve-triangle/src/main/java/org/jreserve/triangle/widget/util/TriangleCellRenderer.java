package org.jreserve.triangle.widget.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.triangle.widget.data.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCellRenderer extends DefaultTableCellRenderer {

    private final static Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    
    private Map<Integer, Color> layerBgs = new HashMap<Integer, Color>();
    private Map<Integer, Color> layerFgs = new HashMap<Integer, Color>();
    
    protected DoubleRenderer valueRenderer = new DoubleRenderer();
    protected Color bgFocused = UIManager.getColor("Table.focusCellBackground");
    protected Color bgDropLocation = UIManager.getColor("Table.dropCellBackground");
    protected Color fgFocused = UIManager.getColor("Table.focusCellForeground");
    protected Color fgDropLocation = UIManager.getColor("Table.dropCellForeground");
    
    protected Border focusedSelected = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
    protected Border focusedNotSelected =  UIManager.getBorder("Table.focusCellHighlightBorder");
    protected Border selected = createCellBorder();
    protected Border notSelected = selected;
    
    public TriangleCellRenderer() {
    }
    
    public void setFractionDigits(int digits) {
        valueRenderer.setFractionDigits(digits);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(!(value instanceof TriangleCell)) {
            formatAsEmpty(table);
        } else {
            TriangleCell cell = (TriangleCell) value;
            formatCell(table, cell, isSelected, hasFocus, row, column);
        }
        return this;
    }
    
    private void formatAsEmpty(JTable table) {
        setBackground(getEmptyBackground(table));
        super.setBorder(EMPTY_BORDER);
        super.setText(null);
    }
    
    private Color getEmptyBackground(JTable table) {
        Color color = null;
        if(table != null && table.getParent() != null)
            color = table.getParent().getBackground();
        if(color == null)
            color = UIManager.getColor("Panel.background");
        return color;
    }
    
    protected void formatCell(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        super.setBackground(getBackground(table, cell, isSelected, hasFocus, row, column));
        super.setForeground(getForeground(table, cell, isSelected, hasFocus, row, column));
        super.setFont(getFont(table, cell, isSelected, hasFocus, row, column));
        super.setBorder(getBorder(table, cell, isSelected, hasFocus, row, column));
        super.setText(getValue(cell));
    }
    
    protected Color getBackground(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        Color color = null;

        if (hasFocus && !isSelected && table.isCellEditable(row, column))
            color = getBgFocused(table, cell, isSelected, hasFocus, row, column);
        
        if(color==null && isDropLocation(table, row, column)) { 
            color = getBgDropLocation(table, cell, isSelected, hasFocus, row, column);
            isSelected = true;
        }
        
        if(color==null && isSelected)
            color = getBgSelected(table, cell, isSelected, hasFocus, row, column);
        
        if(color == null) 
            color = getBgNotSelected(table, cell, isSelected, hasFocus, row, column);
        
        return color;
    }
    
    protected Color getBgFocused(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return bgFocused;
    }
    
    protected Color getBgDropLocation(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return bgDropLocation;
    }
    
    protected Color getBgSelected(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return table.getSelectionBackground();
    }
    
    protected Color getBgNotSelected(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        int layer = cell.getDisplayedLayer();
        Color color = layerBgs.get(layer);
        return color==null? table.getBackground() : color;
    }
    
    protected boolean isDropLocation(JTable table, int row, int column) {
        JTable.DropLocation dropLocation = table.getDropLocation();
        return dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column;
    }
    
    protected Color getForeground(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        Color color = null;
        
        if (hasFocus && !isSelected && table.isCellEditable(row, column))
            color = getFgFocused(table, cell, isSelected, hasFocus, row, column);
        
        if(color==null && isDropLocation(table, row, column)) { 
            color = getFgDropLocation(table, cell, isSelected, hasFocus, row, column);
            isSelected = true;
        }
        
        if(color==null && isSelected)
            color = getFgSelected(table, cell, isSelected, hasFocus, row, column);
        
        if(color == null) 
            color = getFgNotSelected(table, cell, isSelected, hasFocus, row, column);
        
        return color;
    }
    
    protected Color getFgFocused(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return fgFocused;
    }
    
    protected Color getFgDropLocation(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return fgDropLocation;
    }
    
    protected Color getFgSelected(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return table.getSelectionForeground();
    }
    
    protected Color getFgNotSelected(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        int layer = cell.getDisplayedLayer();
        Color color = layerFgs.get(layer);
        return color==null? table.getForeground() : color;
    }
    
    protected Font getFont(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return table.getFont();
    }
    
    protected Border getBorder(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        Border border = null;
        
        if(hasFocus) {
            if(isSelected)
                border = getFocusedSelectedBorder(table, cell, isSelected, hasFocus, row, column);
            if(border == null)
                border = getFocusedNotSelectedBorder(table, cell, isSelected, hasFocus, row, column);
        }
        
        if(border == null && isSelected)
            border = getSelectedBorder(table, cell, isSelected, hasFocus, row, column);
        
        if(border == null)
            border = getNotSelectedBorder(table, cell, isSelected, hasFocus, row, column);
        
        return border;
    }
    
    protected Border getFocusedSelectedBorder(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return focusedSelected;
    }
    
    protected Border getFocusedNotSelectedBorder(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return focusedNotSelected;
    }
    
    protected Border getSelectedBorder(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return selected;
    }
    
    protected Border getNotSelectedBorder(JTable table, TriangleCell cell, boolean isSelected, boolean hasFocus, int row, int column) {
        return notSelected;
    }
    
    protected String getValue(TriangleCell cell) {
        Double value = cell.getDisplayValue();
        if(value == null)
            return null;
        return valueRenderer.toString(value);
    }
    
    private static Border createCellBorder() {
        Color color = UIManager.getColor("InternalFrame.borderDarkShadow");
        if(color == null)
            color = new Color(122, 138, 153);
        return BorderFactory.createMatteBorder(0, 0, 1, 1, color);
    }
    
    void setLayerBackground(int layer, Color color) {
        if(color == null)
            layerBgs.remove(layer);
        else
            layerBgs.put(layer, color);
    }
    
    void setLayerForeground(int layer, Color color) {
        if(color == null)
            layerFgs.remove(layer);
        else
            layerFgs.put(layer, color);
    }
    
    void setLayerColor(int layer, Color bg, Color fg) {
        setLayerBackground(layer, fg);
        setLayerForeground(layer, fg);
    }
    
} 
