package org.jreserve.triangle.visual.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleComment;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractWidgetTableModel extends AbstractTableModel implements WidgetTableModel {
    
    protected Lookup lookup;
    private Result<TriangularData> dataResult;
    private LookupListener dataListener = new DataListener();
    
    private ChangeListener changeListener = new DataChangeListener();
    protected TriangularData data = TriangularData.EMPTY;
    
    protected boolean cummulated = false;
    private TriangleWidgetProperties properties;
    private ChangeListener propertyListener = new PropertyListener();
    
    @Override
    public void setLookup(Lookup lookup) {
        clearState();
        if(lookup != null)
            initState(lookup);
        fireTableStructureChanged();
    }
    
    private void clearState() {
        clearData();
        clearProperties();
        clearLookup();
    }
    
    private void clearData() {
        data.removeChangeListener(changeListener);
        this.data = TriangularData.EMPTY;
    }
    
    private void clearProperties() {
        if(properties != null) {
            properties.removeChangeListener(propertyListener);
            properties = null;
        }
        cummulated = false;
    }
    
    private void clearLookup() {
        if(this.dataResult != null)
            this.dataResult.removeLookupListener(dataListener);
        this.dataResult = null;
        this.lookup = null;
    }
    
    private void initState(Lookup lookup) {
        this.lookup = lookup;
        initDataState();
        initProperties();
    }
    
    private void initDataState() {
        this.dataResult = lookup.lookupResult(TriangularData.class);
        this.dataResult.addLookupListener(dataListener);
        initData(lookup.lookup(TriangularData.class));
    }
    
    private void initData(TriangularData data) {
        this.data = data==null? TriangularData.EMPTY : data;
        this.data.addChangeListener(changeListener);
    }
    
    private void initProperties() {
        properties = lookup.lookup(TriangleWidgetProperties.class);
        if(properties != null) {
            properties.addChangeListener(propertyListener);
            this.cummulated = properties.isCummualted();
        }
    }
    
    @Override
    public int getRowCount() {
        return data.getAccidentCount();
    }

    @Override
    public int getColumnCount() {
        return data.getDevelopmentCount() + 1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return java.util.Date.class;
        return Double.class;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return getRowName(row);
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        return getValue(accident, development);
    }

    protected Object getRowName(int row) {
        return data.getAccidentName(row);
    }
    
    protected abstract int getAccident(int valueRow, int valueColumn);
    
    protected abstract int getDevelopment(int valueRow, int valueColumn);
    
    private Double getValue(int accident, int development) {
        if(cummulated)
            return getCummulatedData(accident, development);
        else
            return getData(accident, development);
    }
    
    private Double getCummulatedData(int accident, int development) {
        Double sum = null;
        for(int d=development; d>=0; d--) {
            Double value = getData(accident, d);
            if(value == null)
                return sum;
            if(Double.isNaN(value))
                return d==development? Double.NaN : sum;
            sum = sum==null? value : sum+value;
        }
        return sum;
    }
    
    private Double getData(int accident, int development) {
        if(isValidCoordiantes(accident, development))
            return data.getValue(accident, development);
        return null;
    }
    
    private boolean isValidCoordiantes(int accident, int development) {
        return accident >= 0 && development >= 0 &&
               accident < data.getAccidentCount() &&
               development < data.getDevelopmentCount(accident);
    }

    @Override
    public List<TriangleComment> getComments(int row, int column) {
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        return data.getComments(accident, development);
    }

    @Override
    public List<TriangleCell> getCells(int[] rows, int[] columns) {
        List<TriangleCell> cells = new ArrayList<TriangleCell>();
        for(int row : rows)
            for(int column : columns)
                addCellIfValid(cells, row, column);
        Collections.sort(cells);
        return cells;
    }
    
    private void addCellIfValid(List<TriangleCell> cells, int row, int column) {
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        if(isValidCoordiantes(accident, development))
            cells.add(new TriangleCell(accident, development));
    }

    @Override
    public String getLayerId(int row, int column) {
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        return data.getLayerTypeId(accident, development);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        return column > 0 &&
               isValidCoordiantes(accident, development) &&
               lookup.lookup(WidgetEditor.class) != null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        WidgetEditor editor = lookup.lookup(WidgetEditor.class);
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        editor.setCellValue(this, accident, development, (Double) value);
    }
    
    private class DataListener implements LookupListener {
        @Override
        public void resultChanged(LookupEvent le) {
            data.removeChangeListener(changeListener);
            initData(lookup.lookup(TriangularData.class));
            fireTableStructureChanged();
        }
    }
    
    private class DataChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireTableStructureChanged();
        }
    }
    
    private class PropertyListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if(cummulated != properties.isCummualted()) {
                cummulated = !cummulated;
                fireTableDataChanged();
            }
        }
    }
}
