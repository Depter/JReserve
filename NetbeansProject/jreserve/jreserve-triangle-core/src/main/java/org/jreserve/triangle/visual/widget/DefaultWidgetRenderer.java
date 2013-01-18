package org.jreserve.triangle.visual.widget;

import java.awt.*;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.triangle.entities.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultWidgetRenderer extends JLabel implements TableCellRenderer {
    
    public static @interface WidgetRendererRegistration {
        public String layerId();
    }
    
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
    protected Color COLOR_REMARK = Color.RED;
    protected int REMARK_SIZE = 7;
    protected int REMARK_MAX_ROW_WIDTH = 40;
    protected String REMARK_TITLE = "%tF / %s:";
    
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
        createToolTip(table, row, column);
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
    
    
    protected void createToolTip(JTable table, int row, int column) {
        java.util.List<TriangleComment> list = getComments(table, row, column);
        if((hasRemark = !list.isEmpty()))
            setToolTipText(renderComments(list));
        else
            setToolTipText(null);
    }
    
    private List<TriangleComment> getComments(JTable table, int row, int column) {
        TableModel model = table.getModel();
        if(model instanceof WidgetTableModel)
            return ((WidgetTableModel) model).getComments(row, column);
        return java.util.Collections.EMPTY_LIST;
    }
    
    protected String renderComments(List<TriangleComment> comments) {
        StringBuilder sb = new StringBuilder("<html>");
        for(int i=0, size=comments.size(); i<size; i++) {
            if(i > 0) 
                sb.append("<br><br>");
            appendComment(sb, comments.get(i));
        }
        return sb.append("</html>").toString();
    }

    private void appendComment(StringBuilder sb, TriangleComment comment) {
        sb.append("<b>").append(getTitle(comment)).append("</b><br>");
        String commentText = comment.getCommentText();
        if(commentText!=null && commentText.length() > 0)
            appendMessage(sb, commentText.toCharArray());
    }

    private String getTitle(TriangleComment comment) {
        java.util.Date date = comment.getCreationDate();
        String user = comment.getUserName();
        return String.format(REMARK_TITLE, date, user);
    }

    private void appendMessage(StringBuilder sb, char[] comment) {
        int count = 0;
        for(int i=0, size=comment.length; i<size; i++) {
            char c = comment[i];

            if(count >= REMARK_MAX_ROW_WIDTH && i<(size-1) && (Character.isSpaceChar(c) || (count - REMARK_MAX_ROW_WIDTH) > 10)) {
                sb.append("<br>");
                count=0;
            } else {
                sb.append(c);
                count++;
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(hasRemark)
            paintRemark(g);
    }
    
    private void paintRemark(Graphics g) {
        int size = getTriangleSize();
        Polygon triangle = createTriangle(size);
        
        Color oldColor = g.getColor();
        g.setColor(COLOR_REMARK);
        g.fillPolygon(triangle);
        g.setColor(oldColor);
    }
    
    private int getTriangleSize() {
        java.awt.Dimension dim = super.getSize();
        int size = REMARK_SIZE;
        if(size > dim.width)
            size = dim.width;
        if(size > dim.height)
            size = dim.height;
        return size;
    }
    
    private Polygon createTriangle(int size) {
        java.awt.Dimension dim = getSize();
        int x[] = {dim.width - size, dim.width, dim.width     };
        int y[] = {0, 0, size};
        return new Polygon(x, y, 3);
    }    
}
