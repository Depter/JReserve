package org.jreserve.triangle.widget.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.data.Data;
import org.jreserve.data.DataComment;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.data.TriangleCell;

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
    
    protected List<List<Data<PersistentObject, Double>>> values = new ArrayList<List<Data<PersistentObject, Double>>>();
    protected List<Data<PersistentObject, DataComment>> comments = new ArrayList<Data<PersistentObject, DataComment>>();
    
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
                cummulate();
            else
                deCummulate();
        }
        fireTableDataChanged();
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
    }
    
    private void createAxises() {
        rowModel = geometry==null? AxisModel.EMPTY : createRowModel(geometry);
        columnModel = geometry==null? AxisModel.EMPTY : createColumnModel(geometry);
    }
    
    protected AxisModel createRowModel(TriangleGeometry geometry) {
        int size = geometry.getAccidentPeriods();
        return new NumberAxisModel(size);
    }
    
    protected AxisModel createColumnModel(TriangleGeometry geometry) {
        int size = geometry.getDevelopmentPeriods();
        return new NumberAxisModel(size);
    }
    
    private void resetCells() {
        cells = createCells();
        fillCellValues();
        fillCellComments();
    }
    
    protected abstract TriangleCell[][] createCells();

    private void fillCellValues() {
        for(TriangleCell[] row : cells)
            setCellValues(row);
        if(cummulated)
            cummulate();
    }
    
    private void setCellValues(TriangleCell[] row) {
        for(TriangleCell cell : row)
            if(cell != null)
                cell.setValues(values);
    }
    
    private void cummulate() {
        for(TriangleCell[] row : cells)
            cummulateRow(row);
    }
    
    private void cummulateRow(TriangleCell[] row) {
        TriangleCell prev = null;
        for(TriangleCell cell : row) {
            if(cell != null && prev != null)
                cell.cummulate(prev);
            prev = cell;
        }
    }
    
    private void deCummulate() {
        for(TriangleCell[] row : cells)
            deCummulateRow(row);
    }
    
    private void deCummulateRow(TriangleCell[] row) {
        for(int i=row.length-1; i>0; i--) {
            TriangleCell cell = row[i];
            TriangleCell prev = row[i-1];
            if(cell != null && prev != null)
                cell.deCummulate(prev);
        }
    }
    
    private void fillCellComments() {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                fillCellComments(cell);
    }

    private void fillCellComments(TriangleCell cell) {
        for(Data<? extends PersistentObject, DataComment> comment : comments)
            cell.addComment(comment);
    }
    
    @Override
    public TriangleCell[][] getCells() {
        return cells;
    }

    @Override
    public void addValues(List<Data<PersistentObject, Double>> datas) {
        checkDatas(datas);
        values.add(datas);
        refillCellValues();
    }
    
    private void refillCellValues() {
        clearCellValues();
        fillCellValues();
        fireTableDataChanged();
    }
    
    private void clearCellValues() {
        for(TriangleCell[] row : cells)
            for(TriangleCell cell : row)
                if(cell != null)
                    cell.clearValue();
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
        if(((Data)o).getValue() == null)
            throw new IllegalArgumentException("List contains data elements, with null values!");
    }

    @Override
    public void addValues(int layer, List<Data<PersistentObject, Double>> datas) {
        checkDatas(datas);
        values.add(layer, datas);
        refillCellValues();
    }

    @Override
    public void setValues(int layer, List<Data<PersistentObject, Double>> datas) {
        checkDatas(datas);
        values.set(layer, datas);
        refillCellValues();
    }
    
    @Override
    public List<List<Data<PersistentObject, Double>>> getValues() {
        return values;
    }

    @Override
    public List<Data<PersistentObject, Double>> getValues(int layer) {
        return values.get(layer);
    }

    @Override
    public void removeValues(int layer) {
        values.remove(layer);
        refillCellValues();
    }

    @Override
    public void addComments(List<Data<PersistentObject, DataComment>> comments) {
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
    public void addComment(Data<PersistentObject, DataComment> comment) {
        checkData(comment);
        comments.add(comment);
        refillCellComments();
    }

    @Override
    public List<Data<PersistentObject, DataComment>> getComments() {
        return new ArrayList<Data<PersistentObject, DataComment>>(comments);
    }

    @Override
    public void removeComment(Data<PersistentObject, DataComment> comment) {
        comments.remove(comment);
        refillCellComments();
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
        return cell != null &&
               cell.getLayerCount() > editableLayer;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if(column == 0 || editableLayer < 0)
            return;
        TriangleCell cell = getCellAt(row, column - 1);
        if(cell != null && cell.getLayerCount() > editableLayer)
            cell.setValue(editableLayer, (Double) value);
        fireTableCellUpdated(row, column);
    }        
}
