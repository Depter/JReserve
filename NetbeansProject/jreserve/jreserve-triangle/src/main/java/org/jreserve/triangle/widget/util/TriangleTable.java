package org.jreserve.triangle.widget.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.resources.ActionUtil;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.PopUpFactory;
import org.jreserve.triangle.widget.TriangleWidget.TriangleWidgetListener;
import org.jreserve.triangle.widget.data.TriangleCell;
import org.jreserve.triangle.widget.model.TriangleModel;
import org.jreserve.triangle.widget.model.TriangleModel.ModelType;
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
    
    private TriangleModel.ModelType type;
    private TriangleModel model;
    private TriangleCellRenderer renderer;
    private TableCellRenderer headerRenderer;
    private DoubleEditor editor;
    private String popUpActionPath;
    //private PopUpFactory popUpFactory;
    
    private InstanceContent content = new InstanceContent();
    private Lookup lookup = new AbstractLookup(content);
    
    public TriangleTable() {
        this(ModelType.DEVELOPMENT);
    }

    public TriangleTable(ModelType type) {
        this.type = type!=null? type : ModelType.DEVELOPMENT;
        createModel();
        createRenderers();
        formatTable();
        setListeners();
    }
    
    private void createModel() {
        model = type.createModel();
        setModel(model);
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
    
    public void setModelType(ModelType type) {
        this.type = type!=null? type : ModelType.DEVELOPMENT;
        initModel();
    }
    
    private void initModel() {
        TriangleModel newModel = type.createModel();
        newModel.copyStateFrom(model);
        this.model = newModel;
        setModel(newModel);
    }
    
    public void setFractionDigits(int digits) {
        renderer.setFractionDigits(digits);
    }
    
    public ModelType getModelType() {
        return type;
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
    
    public void addValueLayer(List<Data<PersistentObject, Double>> datas) {
        model.addValues(datas);
    }
    
    public void setValueLayer(int layer, List<Data<PersistentObject, Double>> datas) {
        model.setValues(layer, datas);
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
    
    public <T extends PersistentObject> List<Data<T, Double>> getLayer(T owner, int layerIndex) {
        return model.getLayer(owner, layerIndex);
    }
    
    public double[][] flatten() {
        TriangleCell[][] cells = model.getCells();
        Flattener flattener = new Flattener(cells);
        return flattener.flatten();
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    
    
    private static class Flattener {
        
        private TriangleCell[][] cells;
        
        private Flattener(TriangleCell[][] cells) {
            this.cells = cells;
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
            int cCount = row.length;
            for(int c=row.length-1; c>=0; c--)
                if(row[c] != null)
                    return c;
            return -1;
        }
        
        private double getCellValue(TriangleCell cell) {
            if(cell == null)
                return Double.NaN;
            Double value = cell.getDisplayValue();
            return value==null? Double.NaN : value;
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
                if(action instanceof Presenter.Popup)
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
