package org.jreserve.triangle.mvc.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jreserve.data.Data;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.data.DefaultLayerModel;
import org.jreserve.triangle.mvc.data.Layer;
import org.jreserve.triangle.mvc.data.LayerCriteria;
import org.jreserve.triangle.mvc.data.TriangleLayerModel;

/**
 *
 * @author Peter Decsi
 */
public class TriangleTableModelConverter implements TableModel {

    private TriangleModel.ModelType type = TriangleModel.ModelType.DEVELOPMENT;
    private TriangleModel triangleModel = type.createModel();
    private TriangleLayerModel layerModel = new DefaultLayerModel();
    private int lastValue = -1;
    
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    
    @Override
    public int getRowCount() {
        return triangleModel.getRowModel().size();
    }

    @Override
    public int getColumnCount() {
        return triangleModel.getColumnModel().size()+1;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "";
        return triangleModel.getColumnModel().getTitle(column-1);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0)
            return false;
        return layerModel.isCellEditable(createCriteria(row, column));
    }
    
    public LayerCriteria createCellCriteria(int row, int column) {
        return triangleModel.createCellCriteria(row, column - 1);
    }
    
    private LayerCriteria createCriteria(int row, int column) {
        return triangleModel.createCriteria(row, column - 1);
    }

    public boolean hasValueAt(int row, int column) {
        if(column == 0)
            return 0 <= row && row <= triangleModel.getRowModel().size();
        return triangleModel.hasValueAt(row, column - 1);
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0) {
            lastValue = -1;
            return triangleModel.getRowModel().getTitle(row);
        } else {
            return getLayerValue(row, column);
        }
    }
    
    private Object getLayerValue(int row, int column) {
        LayerCriteria criteria = createCriteria(row, column);
        Object result = layerModel.getValueAt(criteria);
        lastValue = criteria.getCounter();
        return result;
    }
    
    public int getLayerOfLastValue() {
        return lastValue;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if(column > 0) {
            layerModel.setValueAt(value, createCriteria(row, column));
            fireCellChanged(row, column);
        }
    }
    
    private void fireCellChanged(int row, int column) {
        TableModelEvent evt = new TableModelEvent(this, row, row, column, TableModelEvent.UPDATE);
        fireEvent(evt);
    }
    
    private void fireEvent(TableModelEvent evt) {
        for(TableModelListener listener : new ArrayList<TableModelListener>(listeners))
            listener.tableChanged(evt);
    }
    
    private void fireStructureChanged() {
        TableModelEvent evt = new TableModelEvent(this, TableModelEvent.HEADER_ROW);
        fireEvent(evt);
    }
    
    private void fireDataChanged() {
        TableModelEvent evt = new TableModelEvent(this);
        fireEvent(evt);
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
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        triangleModel.setTriangleGeometry(geometry);
        fireStructureChanged();
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return triangleModel.getTriangleGeometry();
    }
    
    public void setModelType(TriangleModel.ModelType type) {
        if(type == null)
            type = TriangleModel.ModelType.DEVELOPMENT;
        TriangleGeometry geometry = triangleModel.getTriangleGeometry();
        boolean cummulated = triangleModel.isCummulated();
        initModelType(type, geometry, cummulated);
    }
    
    private void initModelType(TriangleModel.ModelType type, TriangleGeometry geometry, boolean cummulated) {
        this.type = type;
        triangleModel = type.createModel();
        triangleModel.setTriangleGeometry(geometry);
        triangleModel.setCummulated(cummulated);
        fireStructureChanged();
    }
    
    public boolean isCummulated() {
        return triangleModel.isCummulated();
    }
    
    public void setCummulated(boolean cummulated) {
        triangleModel.setCummulated(cummulated);
        fireDataChanged();
    }
    
    public List<Data> getDatas(int position) {
        return getLayer(position).getDatas();
    }
    
    public void setDatas(int position, List<Data> data) {
        getLayer(position).setDatas(data);
        fireDataChanged();
    }
    
    public Layer getLayer(int position) {
        return layerModel.get(position);
    }
    
    public void addLayer(Layer layer) {
        layerModel.add(layer);
        if(layer.isLayerVisible())
            fireDataChanged();
    }
    
    public void addLayer(int position, Layer layer) {
        layerModel.add(position, layer);
        if(layer.isLayerVisible())
            fireDataChanged();
    }
    
    public Layer setLayer(int position, Layer layer) {
        Layer removed = layerModel.set(position, layer);
        if(layer.isLayerVisible())
            fireDataChanged();
        return removed;
    }
    
    public void removeLayer(Layer layer) {
        layerModel.remove(layer);
        if(layer.isLayerVisible())
            fireDataChanged();
    }
    
    public Layer removeLayer(int position) {
        Layer removed = layerModel.remove(position);
        if(removed.isLayerVisible())
            fireDataChanged();
        return removed;
    }
    
    public int getPosition(Layer layer) {
        return layerModel.getPosition(layer);
    }
    
    public double[][] flattenValues() {
        Flattener flattener = new Flattener();
        flattener.calculate();
        return flattener.values;
    }
    
    private class Flattener {
    
        private double[][] values;
        private TriangleModel model;
        
        private Flattener() {
            model = TriangleModel.ModelType.DEVELOPMENT.createModel();
            model.setTriangleGeometry(triangleModel.getTriangleGeometry());
        }
        
        void calculate() {
            int rowCount = model.getRowModel().size();
            values = new double[rowCount][];
            for(int r=0; r<rowCount; r++)
                calculateRow(r);
        }
        
        private void calculateRow(int row) {
            int cCount = getColumnCount(row);
            values[row] = new double[cCount];
            for(int c=0; c<cCount; c++)
                calculateCell(row, c);
        }
        
        private int getColumnCount(int row) {
            int count = 1;
            while(model.hasValueAt(row, count))
                count++;
            return count - 1;
        }
        
        private void calculateCell(int row, int column) {
            LayerCriteria criteria = model.createCriteria(row, column+1);
            double value = getValue(layerModel.getValueAt(criteria));
            values[row][column] = value;
        }

        private double getValue(List<Data> datas) {
            if(datas == null)
                return Double.NaN;
            
            double sum = 0d;
            for(Data data : datas) {
                double d = getValue(data);
                if(Double.isNaN(d))
                    return Double.NaN;
                sum += d;
            }
            return sum;
        }
        
        private double getValue(Data data) {
            Object value = data.getValue();
            if(value instanceof Double)
                return (Double) value;
            return 0d;
        }
    }
}
