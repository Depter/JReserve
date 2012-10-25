package org.jreserve.triangle.mvc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jreserve.triangle.mvc.controller.CalendarTriangleTableModel;
import org.jreserve.triangle.mvc.controller.DevelopmentTriangleTableModel;
import org.jreserve.triangle.mvc.controller.LayeredTableModel;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportTableModel.Periods=Periods"
})
class TriangleTableModel implements TableModel, ChangeListener {

    static enum ModelType {
        DEVELOPMENT,
        CALENDAR
    };
    
    private final static double MISSING_VALUE = 0d;

    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    
    private ModelType type = ModelType.DEVELOPMENT; 
    private LayeredTableModel<Double> model = new LayeredTableModel<Double>(DevelopmentTriangleTableModel.FACTORY);
    private boolean isCummulated = false;
    private ColumnRenderer columnRenderer = new DefaultColumnRenderer();
    private int editingLayer = -1;
    
    TriangleTableModel() {
        model.addChangeListener(this);
    }
    
    @Override
    public int getRowCount() {
        int v = model.getRowCount();
        return v;
    }

    @Override
    public int getColumnCount() {
        int v = model.getColumnCount() + 1;
        return v;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ImportTableModel_Periods();
        return columnRenderer.getColumnName(model.getColumnTitle(column-1));
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return Date.class;
        return Double.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return model.getRowTitle(row);
        TriangleCell<Double> cell = model.getCellAt(row, column-1);
        return getCellValue(cell);
    }
    
    private Double getCellValue(TriangleCell<Double> cell) {
        if(cell == null)
            return null;
        if(isCummulated)
            return getCummulatedCellValue(cell);
        return getUncummulatedValue(cell);
    }
    
    private double getCummulatedCellValue(TriangleCell<Double> cell) {
        double value = 0d;
        while(cell != null) {
            double d = getUncummulatedValue(cell);
            if(Double.isNaN(d))
                break;
            value += d;
            cell = cell.getPreviousCell();
        }
        return value;
    }
    
    private double getUncummulatedValue(TriangleCell<Double> cell) {
        Double value = cell.getValue();
        if(value == null)
            return MISSING_VALUE;
        return value;
    }
    
    public boolean hasCellAt(int row, int column) {
        if(column == 0)
            return true;
        return model.hasCellAt(row, column-1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0 || editingLayer < 0)
            return false;
        return model.getCellAt(row, column-1, editingLayer) != null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        TriangleCell<Double> cell = model.getCellAt(row, column-1, editingLayer);
        cell.setValue((Double) value);
        fireCellChanged(row, column);
    }

    private void fireCellChanged(int row, int column) {
        TableModelEvent evt = new TableModelEvent(this, row, row, column);
        fireTableModelEvent(evt);
    }
    
    @Override
    public void addTableModelListener(TableModelListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireTableStructureChanged();
    }    
    
    private void fireTableStructureChanged() {
        TableModelEvent evt = new TableModelEvent(this, TableModelEvent.HEADER_ROW);
        fireTableModelEvent(evt);
    }
    
    private void fireTableModelEvent(TableModelEvent evt) {
        for(TableModelListener listener : new ArrayList<TableModelListener>(listeners))
            listener.tableChanged(evt);
    }
    
    public void setCummulated(boolean cummulated) {
        if(this.isCummulated != cummulated) {
            this.isCummulated = cummulated;
            fireRowsChanged();
        }
    }
    
    public boolean isCummulated() {
        return isCummulated;
    }
    
    protected void fireRowsChanged() {
        TableModelEvent evt = new TableModelEvent(this);
        fireTableModelEvent(evt);
    }
    
    public void setModelType(ModelType type) {
        if(type == null)
            throw new NullPointerException("ModelType can not be null!");
        if(this.type != type) {
            this.type = type;
            changeModelType();
        }
    }
    
    private void changeModelType() {
        switch(type) {
            case CALENDAR:
                model.setModelType(CalendarTriangleTableModel.FACTORY);
                break;
            case DEVELOPMENT:
                model.setModelType(DevelopmentTriangleTableModel.FACTORY);
                break;
            default:
                throw new IllegalArgumentException("Unknown model type: "+type);
        }
    }
    
    public TriangleTable getTableAt(int layer) {
        return model.get
    }
    
    public void addTable(TriangleTable table) {
        model.addTable(table);
    }
    
    public void setTable(TriangleTable table, int layer) {
        model.setTable(table, layer);
    } 
    
    public void removeTable(int layer) {
        model.removeTable(layer);
    }
    
    public void setColumnRenderer(ColumnRenderer renderer) {
        if(renderer == null)
            renderer = new DefaultColumnRenderer();
        this.columnRenderer = renderer;
        fireTableStructureChanged();
    }
    
    private static class DefaultColumnRenderer implements ColumnRenderer {
        @Override
        public String getColumnName(Object title) {
            return ""+title;
        }
    }
}
