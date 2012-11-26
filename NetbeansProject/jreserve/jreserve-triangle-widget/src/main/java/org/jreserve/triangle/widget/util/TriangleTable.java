package org.jreserve.triangle.widget.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import org.jreserve.resources.ActionUtil;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleModel;
import org.jreserve.triangle.widget.TriangleWidgetListener;
import org.jreserve.triangle.widget.WidgetData;
import org.jreserve.triangle.widget.model.AbstractTriangleTableModel;
import org.jreserve.triangle.widget.model.TriangleTableModel;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTable extends JTable implements Lookup.Provider {

    private final static int DISMISS_DELAY = 10 * 60 * 1000;
    
    private TriangleTableModel model = new AbstractTriangleTableModel();
    private TriangleCellRenderer renderer;
    private TableCellRenderer headerRenderer;
    private DoubleEditor editor;
    private String popUpActionPath;
    
    private InstanceContent content = new InstanceContent();
    private Lookup lookup = new AbstractLookup(content);
    
    public TriangleTable() {
        super.setModel(model);
        createRenderers();
        formatTable();
        setListeners();
    }
    
    private void createRenderers() {
        headerRenderer = new HeaderRenderer();
        setDefaultRenderer(String.class, headerRenderer);
        getTableHeader().setDefaultRenderer(headerRenderer);
        
        renderer = new TriangleCellRenderer();
        setDefaultRenderer(TriangleCell.class, renderer);
        
        editor = new DoubleEditor();
        setDefaultEditor(TriangleCell.class, editor);
    }
    
    private void formatTable() {
        setShowGrid(false);
        setRowMargin(0);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(true);
        setCellSelectionEnabled(true);
        
    }
    
    private void setListeners() {
        addMouseListener(new MouseHandler());
        
        CellSelectionListener listener = new CellSelectionListener();
        getSelectionModel().addListSelectionListener(listener);
        getColumnModel().getSelectionModel().addListSelectionListener(listener);
    }
    
    public void setPopUpActionPath(String path) {
        if(path == null || path.trim().length()==0)
            this.popUpActionPath = null;
        else
            this.popUpActionPath = path;
    }
    
    public void setFractionDigits(int digits) {
        renderer.setFractionDigits(digits);
    }
    
    public void setTriangleModel(TriangleModel triangleModel) {
        this.model.setTriangleModel(triangleModel);
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        model.setTriangleGeometry(geometry);
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return model.getTriangleGeometry();
    }
    
    public void setCummulated(boolean cummulated) {
        model.setCummulated(cummulated);
    }
    
    public boolean isCummulated() {
        return model.isCummulated();
    }
    
    public void addTriangleWidgetListener(TriangleWidgetListener listener) {
        model.addTriangleWidgetListener(listener);
    }
    
    public void removeTriangleWidgetListener(TriangleWidgetListener listener) {
        model.removeTriangleWidgetListener(listener);
    }
    
    public void setManualEvents(boolean manualEvents) {
        model.setManualEvents(manualEvents);
    }
    
    public boolean isManualEvents() {
        return model.isManualEvents();
    }
    
    public void fireTriangleStructureChanged() {
        model.fireTriangleStructureChanged();
    }

    public void fireEdited(TriangleCell cell, Double old, Double current) {
        model.fireEdited(cell, old, current);
    }
    
    public void fireTriangleValuesChanged() {
        model.fireTriangleValuesChanged();
    }
    
    public void fireCommentsChanged() {
        model.fireCommentsChanged();
    }
    
    public void addValueLayer(List<WidgetData<Double>> datas) {
        model.addValues(datas);
    }
    
    public void setValueLayer(int layer, List<WidgetData<Double>> datas) {
        model.setValues(layer, datas);
    }
    
    public List<WidgetData<Double>> getValueLayer(int layer) {
        return model.getValues(layer);
    }
    
    public List<TriangleCell> getCells() {
        List<TriangleCell> cells = new ArrayList<TriangleCell>();
        for(TriangleCell[] row : model.getCells())
            for(TriangleCell cell : row)
                if(cell != null)
                    cells.add(cell);
        return cells;
    }
    
    public TriangleCell[][] getCellArray() {
        return model.getCells();
    }
    
    public TriangleCell getCellAt(Date accident, Date development) {
        return model.getCellAt(accident, development);
    }
    
    public void removeComment(WidgetData<Comment> comments) {
        model.removeComment(comments);
    }
    
    public void setComments(List<WidgetData<Comment>> comments) {
        model.setComments(comments);
    }
    
    public void addComments(List<WidgetData<Comment>> comments) {
        model.addComments(comments);
    }
    
    public void addComment(WidgetData<Comment> comment) {
        model.addComment(comment);
    }
    
    public List<WidgetData<Comment>> getComments() {
        return model.getComments();
    }
    
    public void setLayerBackground(int layer, Color color) {
        renderer.setLayerBackground(layer, color);
    }
    
    public void setLayerForeground(int layer, Color color) {
        renderer.setLayerForeground(layer, color);
    }
    
    public void setLayerColor(int layer, Color bg, Color fg) {
        renderer.setLayerColor(layer, bg, fg);
    }
    
    public void setEditableLayer(int layer) {
        model.setEditableLayer(layer);
    }
    
    public double[][] flatten() {
        TriangleCell[][] cells = model.getCells();
        Flattener flattener = new Flattener(cells);
        return flattener.flatten();
    }
    
    public double[][] flattenLayer(int layer) {
        TriangleCell[][] cells = model.getCells();
        Flattener flattener = new Flattener(cells, layer);
        return flattener.flatten();
    }

    public boolean[][] flattenExclusions() {
        TriangleCell[][] cells = model.getCells();
        ExclusionFlattener flattener = new ExclusionFlattener(cells);
        return flattener.flatten();
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    public String getClipboardString() {
        int columns = model.getColumnCount();
        StringBuilder sb = new StringBuilder();
        appendHeader(columns, sb);
        appendTable(columns, sb);
        return sb.toString();
    }
    
    private void appendHeader(int columns, StringBuilder sb) {
        for(int c=0; c<columns; c++) {
            if(c!=0) sb.append('\t');
            sb.append(model.getColumnName(c));
        }
    }
    
    private void appendTable(int columns, StringBuilder sb) {
        for(int r=0, rows=model.getRowCount(); r<rows; r++) {
            sb.append('\n');
            appendRow(columns, r, sb);
        }
    }
    
    private void appendRow(int columns, int row, StringBuilder sb) {
        for(int c=0; c<columns; c++) {
            if(c != 0) sb.append('\t');
            Object value = model.getValueAt(row, c);
            appendValue(c, value, sb);
        }
    } 
    
    private void appendValue(int column, Object value, StringBuilder sb) {
        if(value == null)
            return;
        if(column == 0) {
            sb.append(value);
        } else {
            String str = renderer.getSimpleValue((TriangleCell) value);
            if(str != null)
                sb.append(str.trim());
        }
    }
    
    private static class Flattener {
        
        private int layer;
        private TriangleCell[][] cells;
        
        private Flattener(TriangleCell[][] cells) {
            this(cells, -1);
        }
        
        private Flattener(TriangleCell[][] cells, int layer) {
            this.cells = cells;
            this.layer = layer;
        }
        
        double[][] flatten() {
            int rCount = cells.length;
            double[][] result = new double[rCount][];
            for(int r=0; r<rCount; r++)
                result[r] = createRow(cells[r]);
            return result;
        }
        
        private double[] createRow(TriangleCell[] cells) {
            int lastIndex = getLastIndex(cells);
            if(lastIndex < 0) return new double[0];
            
            double[] result = new double[lastIndex+1];
            for(int c=0; c<=lastIndex; c++)
                result[c] = getCellValue(cells[c]);
            return result;
        } 
        
        private int getLastIndex(TriangleCell[] row) {
            for(int c=row.length-1; c>=0; c--)
                if(row[c] != null)
                    return c;
            return -1;
        }
        
        private double getCellValue(TriangleCell cell) {
            if(cell == null)
                return Double.NaN;
            Double value = layer<0? cell.getDisplayValue() : cell.getValueAt(layer);
            return value==null? Double.NaN : value;
        }
    }
    
    private static class ExclusionFlattener {
        
        private TriangleCell[][] cells;
        
        private ExclusionFlattener(TriangleCell[][] cells) {
            this.cells = cells;
        }
        
        boolean[][] flatten() {
            int rCount = cells.length;
            boolean[][] result = new boolean[rCount][];
            for(int r=0; r<rCount; r++)
                result[r] = createRow(cells[r]);
            return result;
        }
        
        private boolean[] createRow(TriangleCell[] cells) {
            int lastIndex = getLastIndex(cells);
            if(lastIndex < 0) return new boolean[0];
            
            boolean[] result = new boolean[lastIndex+1];
            for(int c=0; c<=lastIndex; c++)
                result[c] = getCellValue(cells[c]);
            return result;
        } 
        
        private int getLastIndex(TriangleCell[] row) {
            for(int c=row.length-1; c>=0; c--)
                if(row[c] != null)
                    return c;
            return -1;
        }
        
        private boolean getCellValue(TriangleCell cell) {
            if(cell == null)
                return false;
            return !cell.isExcluded();
        }
    }
    
    private class MouseHandler extends MouseAdapter {

        private int originalDismissDelay;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger())
                showPopUp(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(popUpActionPath!=null && e.isPopupTrigger())
                showPopUp(e);
        }
        
        private void showPopUp(MouseEvent e) {
            JPopupMenu menu = createPopUp();
            Point p = e.getPoint();
            menu.show(TriangleTable.this, p.x, p.y);
        }
        
        private JPopupMenu createPopUp() {
            JPopupMenu popUp = new JPopupMenu();
            for(Action action : ActionUtil.actionsForPath(popUpActionPath)) {
            //for(Action action : Utilities.actionsForPath(popUpActionPath)) {
                if(action == null)
                    popUp.add(new JSeparator());
                else if(action instanceof Presenter.Popup)
                    popUp.add(((Presenter.Popup)action).getPopupPresenter());
                else
                    popUp.add(action);
            }
            return popUp;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ToolTipManager mg = ToolTipManager.sharedInstance();
            originalDismissDelay = mg.getDismissDelay();
            mg.setDismissDelay(DISMISS_DELAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(originalDismissDelay == 0)
                originalDismissDelay = 5 * 1000;
            ToolTipManager.sharedInstance().setDismissDelay(originalDismissDelay);
        }
        
        
    }

    private class CellSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            clearContent();
            for(TriangleCell cell : getSelectedCells())
                content.add(cell);
        }
        
        private List<TriangleCell> getSelectedCells() {
            int rows[] = getSelectedRows();
            int cells[] = getSelectedColumns();
            return getSelectedCells(rows, cells);
        }
        
        private List<TriangleCell> getSelectedCells(int[] rows, int[] columns) {
            List<TriangleCell> cells = new ArrayList<TriangleCell>();
            for(int column : columns)
                if(column != 0)
                    addSelectedCells(cells, column, rows);
            return cells;
        }
        
        private void addSelectedCells(List<TriangleCell> cells, int column, int[] rows) {
            for(int row : rows) {
                TriangleCell cell = (TriangleCell) model.getValueAt(row, column);
                if(cell != null)
                    cells.add(cell);
            }
        }
        
        private void clearContent() {
            List<TriangleCell> elements = new ArrayList<TriangleCell>(lookup.lookupAll(TriangleCell.class));
            for(TriangleCell cell : elements)
                content.remove(cell);
        }
    }

}
