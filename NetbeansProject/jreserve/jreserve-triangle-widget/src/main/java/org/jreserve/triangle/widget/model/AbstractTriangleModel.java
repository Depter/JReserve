package org.jreserve.triangle.widget.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleModel extends AbstractTableModel implements TriangleModel {
    
    protected TriangleGeometry geometry;
    protected boolean cummulated;
    
    protected AxisModel rowModel = AxisModel.EMPTY;
    protected AxisModel columnModel = AxisModel.EMPTY;
    
    protected List<List<WidgetData<Double>>> values = new ArrayList<List<WidgetData<Double>>>();
    
    protected List<WidgetData<Comment>> comments = new ArrayList<WidgetData<Comment>>();
    private List<TriangleWidgetListener> listeners = new ArrayList<TriangleWidgetListener>();
    
    private TriangleCell[][] cells = new TriangleCell[0][];
    
    private int editableLayer = -1;
    
    @Override
    public AxisModel getRowModel() {
        return rowModel;
    }

    @Override
    public AxisModel getColumnModel() {
        return columnModel;
    }
    
    @Override
    public void setCummulated(boolean isCummulated) {
        if(this.cummulated != isCummulated) {
            this.cummulated = isCummulated;
            
            if(cummulated)
                TriangleCellUtil.cummulate(cells);
            else
                TriangleCellUtil.deCummulate(cells);
        }
        fireTableDataChanged();
        fireTriangleValuesChanged();
    }
    
    @Override
    public boolean isCummulated() {
        return cummulated;
    }

    @Override
    public TriangleGeometry getTriangleGeometry() {
        return geometry;
    }

    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
        this.geometry = geometry==null? null : geometry.copy();
        createAxises();
        resetCells();
        fireTableStructureChanged();
        fireTriangleStructureChanged();
    }
    
    private void createAxises() {
        rowModel = geometry==null? AxisModel.EMPTY : createRowModel(geometry);
        columnModel = geometry==null? AxisModel.EMPTY : createColumnModel(geometry);
    }
    
    protected AxisModel createRowModel(TriangleGeometry geometry) {
        int size = geometry.getAccidentPeriods();
        return new AxisModel.NumberAxisModel(size);
    }
    
    protected AxisModel createColumnModel(TriangleGeometry geometry) {
        int size = geometry.getDevelopmentPeriods();
        return new AxisModel.NumberAxisModel(size);
    }
    
    private void resetCells() {
        cells = createCells();
        fillCellValues();
        fillCellComments();
    }
    
    protected abstract TriangleCell[][] createCells();

    private void fillCellValues() {
        TriangleCellUtil.setCellValues(cells, values);
        if(cummulated)
            TriangleCellUtil.cummulate(cells);
    }
    
    private void fillCellComments() {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                fillCellComments(cell);
    }

    private void fillCellComments(TriangleCell cell) {
        if(cell != null)
            for(WidgetData<Comment> comment : comments)
                cell.addComment(comment);
    }
    
    @Override
    public TriangleCell[][] getCells() {
        return cells;
    }

    @Override
    public void addValues(List<WidgetData<Double>> datas) {
        checkDatas(datas);
        values.add(datas);
        refillCellValues();
        fireTriangleValuesChanged();
    }
    
    private void refillCellValues() {
        fillCellValues();
        fireTableDataChanged();
    }
    
    private void checkDatas(List datas) {
        if(datas == null)
            throw new NullPointerException("Can not add null values!");
        for(Object o : datas)
            checkData(o);
    }
    
    private void checkData(Object o) {
        if(o == null)
            throw new IllegalArgumentException("List contains null values!");
        if(((WidgetData)o).getValue() == null)
            throw new IllegalArgumentException("List contains data elements, with null values!");
    }

    @Override
    public void addValues(int layer, List<WidgetData<Double>> datas) {
        checkDatas(datas);
        ensureLayerExists(layer-1);
        values.add(layer, datas);
        refillCellValues();
        fireTriangleValuesChanged();
    }
    
    private void ensureLayerExists(int layer) {
        for(int i=values.size(); i<=layer; i++)
            values.add(Collections.EMPTY_LIST);
    }

    @Override
    public void setValues(int layer, List<WidgetData<Double>> datas) {
        checkDatas(datas);
        ensureLayerExists(layer);
        values.set(layer, datas);
        refillCellValues();
        fireTriangleValuesChanged();
    }
    
    @Override
    public List<List<WidgetData<Double>>> getValues() {
        return values;
    }

    @Override
    public List<WidgetData<Double>> getValues(int layer) {
        return new ArrayList<WidgetData<Double>>(values.get(layer));
    }
    
    @Override
    public TriangleCell getCellAt(Date accident, Date development) {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                if(cell!= null && cell.acceptsDates(accident, development))
                    return cell;
        return null;
    }

    @Override
    public void removeValues(int layer) {
        values.remove(layer);
        refillCellValues();
        fireTriangleValuesChanged();
    }
    
    @Override
    public void setComments(List<WidgetData<Comment>> comments) {
        checkDatas(comments);
        this.comments.clear();
        this.comments.addAll(comments);
        refillCellComments();
    }

    @Override
    public void addComments(List<WidgetData<Comment>> comments) {
        checkDatas(comments);
        this.comments.addAll(comments);
        refillCellComments();
    }
    
    private void refillCellComments() {
        clearCellComments();
        fillCellComments();
        fireTableDataChanged();
    }
    
    private void clearCellComments() {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                if(cell != null)
                    cell.clearComments();
    }

    @Override
    public void addComment(WidgetData<Comment> comment) {
        checkData(comment);
        comments.add(comment);
        refillCellComments();
        fireCommentsChanged();
    }

    @Override
    public List<WidgetData<Comment>> getComments() {
        return new ArrayList<WidgetData<Comment>>(comments);
    }

    @Override
    public void removeComment(WidgetData<Comment> comment) {
        comments.remove(comment);
        refillCellComments();
        fireCommentsChanged();
    }
    
    @Override
    public void setEditableLayer(int layer) {
        if(layer < -1)
            layer = -1;
        this.editableLayer = layer;
    }
    
    @Override
    public int getEditableLayer() {
        return editableLayer;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return String.class;
        return TriangleCell.class;
    }

    @Override
    public int getColumnCount() {
        return getColumnModel().size() + 1;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "";
        return getColumnModel().getTitle(column - 1);
    }

    @Override
    public int getRowCount() {
        return getRowModel().size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return getRowModel().getTitle(row);
        return getCellAt(row, column - 1);
    }

    private TriangleCell getCellAt(int row, int column) {
        if(cells == null || cells.length <= row)
            return null;
        TriangleCell[] rCells = cells[row];
        
        if(rCells == null || rCells.length <= column)
            return null;
        return rCells[column];
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0 || editableLayer < 0)
            return false;
        TriangleCell cell = getCellAt(row, column -1);
        return cell != null;// && cell.getLayerCount() > editableLayer;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if(column == 0 || editableLayer < 0)
            return;
        TriangleCell cell = getCellAt(row, column - 1);
        if(cell != null)// && cell.getLayerCount() > editableLayer)
            editValue(cell, (Double) value);
    }
    
    private void editValue(TriangleCell cell, Double value) {
        Double old = cell.getValueAt(editableLayer);
        TriangleCellUtil.setValue(cell, editableLayer, value);
        values.set(editableLayer, TriangleCellUtil.extractValues(cells, editableLayer));
        fireTableCellUpdated(editableLayer, editableLayer);
        fireEdited(cell, old, value);
    }
    
    private void fireEdited(TriangleCell cell, Double old, Double current) {
        for(TriangleWidgetListener l : new ArrayList<TriangleWidgetListener>(listeners))
            l.cellEdited(cell, editableLayer, old, current);
    }
    
    private void fireCommentsChanged() {
        for(TriangleWidgetListener l : new ArrayList<TriangleWidgetListener>(listeners))
            l.commentsChanged();
    }
    
    private void fireTriangleValuesChanged() {
        for(TriangleWidgetListener l : new ArrayList<TriangleWidgetListener>(listeners))
            l.valuesChanged();
    }
    
    private void fireTriangleStructureChanged() {
        for(TriangleWidgetListener l : new ArrayList<TriangleWidgetListener>(listeners))
            l.structureChanged();
    }

    @Override
    public void addTriangleWidgetListener(TriangleWidgetListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    @Override
    public void removeTriangleWidgetListener(TriangleWidgetListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public List<TriangleWidgetListener> getTriangleWidgetListeners() {
        return new ArrayList<TriangleWidgetListener>(listeners);
    }
    
    @Override
    public void copyStateFrom(TriangleModel model) {
        for(List<WidgetData<Double>> datas : model.getValues())
            addValues(datas);
        addComments(model.getComments());
        setCummulated(model.isCummulated());
        setTriangleGeometry(model.getTriangleGeometry());
        setEditableLayer(model.getEditableLayer());
        
        listeners.clear();
        listeners.addAll(model.getTriangleWidgetListeners());
    }
}
