package org.jreserve.audit.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class StringRenderer extends JTextArea implements TableCellRenderer {
    
    private final static int CHARACTERS_PER_TAB = 2;
    
    private Color bgFocused = UIManager.getColor("Table.focusCellBackground");
    private Color bgDropLocation = UIManager.getColor("Table.dropCellBackground");
    private Color fgFocused = UIManager.getColor("Table.focusCellForeground");
    private Color fgDropLocation = UIManager.getColor("Table.dropCellForeground");
    
    private Border focusedSelected = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
    private Border focusedNotSelected =  UIManager.getBorder("Table.focusCellHighlightBorder");
    private Border selected = createCellBorder();
    private Border notSelected = selected;
    
    private static Border createCellBorder() {
        Color color = UIManager.getColor("InternalFrame.borderDarkShadow");
        if(color == null)
            color = new Color(122, 138, 153);
        return BorderFactory.createMatteBorder(0, 0, 1, 1, color);
    }
    
    public StringRenderer() {
        setLineWrap(true);
        setTabSize(CHARACTERS_PER_TAB);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String str = value==null? "" : (String) value;
        setText(str);
        format(table, isSelected, hasFocus, row, column);
        return this;
    }
    
    private void format(JTable table, boolean isSelected, boolean hasFocus, int row, int column) {
        super.setBackground(getBackground(table, isSelected, hasFocus, row, column));
        super.setForeground(getForeground(table, isSelected, hasFocus, row, column));
        //super.setBorder(getBorder(isSelected, hasFocus));
        super.setFont(table.getFont());
    }
    
    private Color getBackground(JTable table,boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus && !isSelected && table.isCellEditable(row, column))
            return bgFocused;
        else if(isDropLocation(table, row, column)) 
            return bgDropLocation;
        else if(isSelected)
            return table.getSelectionBackground();
        else
            return table.getBackground();
    }
    
    private boolean isDropLocation(JTable table, int row, int column) {
        JTable.DropLocation dropLocation = table.getDropLocation();
        return dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column;
    }
    
    private Color getForeground(JTable table, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus && !isSelected && table.isCellEditable(row, column))
            return fgFocused;
        else if(isDropLocation(table, row, column))
            return fgDropLocation;
        else if(isSelected)
            return table.getSelectionForeground();
        else
            return table.getForeground();
    }
    
    private Border getBorder(boolean isSelected, boolean hasFocus) {
        if(hasFocus)
            return isSelected? 
                    focusedSelected : 
                    focusedNotSelected;
        else if(isSelected)
            return selected;
        else
            return notSelected;
    }
}
