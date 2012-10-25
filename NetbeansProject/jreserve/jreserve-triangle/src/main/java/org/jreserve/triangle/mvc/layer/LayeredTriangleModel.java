package org.jreserve.triangle.mvc.layer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.view.ColumnRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LayeredTriangleModel implements TableModel {
    
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    private List<Layer> layers  = new ArrayList<Layer>();
    private GeometryModel.ModelType modelType = GeometryModel.ModelType.DEVELOPMENT;
    private GeometryModel geometry = modelType.createModel();
    private ColumnRenderer columnRenderer;
    
    public int getLayerCount() {
        return layers.size();
    }
    
    public Layer getLayer(int position) {
        return layers.get(position);
    }
    
    public void addLayer(Layer layer) {
        addLayer(layers.size(), layer);
    }
    
    private void checkLayer(Layer layer) {
        if(layer == null)
            throw new NullPointerException("Layer can not be null!");
        if(layers.contains(layer))
            throw new IllegalArgumentException("Layer already added: "+layer);
    }
    
    public void addLayer(int index, Layer layer) {
        checkLayer(layer);
        layers.add(layer);
        fireLayerAdded(layer);
    }
    
    public void setLayer(int position, Layer layer) {
        checkLayer(layer);
        layers.set(position, layer);
        fireLayerAdded(layer);
    }
    
    public void removeLayer(int position) {
        Layer layer = layers.remove(position);
        fireLayerRemoved(layer);
    }
    
    public void removeLayer(Layer layer) {
        if(layers.remove(layer))
            fireLayerRemoved(layer);
    }
    
    public void setTriangleGeometry(TriangleGeometry geoemtry) {
        this.geometry.setTriangleGeometry(geoemtry);
        fireDataChanged();
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return this.geometry.getTriangleGeometry();
    }
    
    public void setModelType(GeometryModel.ModelType modelType) {
        if(this.modelType == modelType)
            return;
        this.modelType = modelType;
        initGeometryModel();
    }
    
    private void initGeometryModel() {
        TriangleGeometry tg = geometry.getTriangleGeometry();
        geometry = modelType.createModel();
        geometry.setTriangleGeometry(tg);
        fireDataChanged();
    }
    
    public GeometryModel.ModelType getModelType() {
        return modelType;
    }
    
    public void setCummulated(boolean cummulated) {
        geometry.setCummulated(cummulated);
        fireDataChanged();
    }
    
    public boolean isCummulated() {
        return geometry.isCummulated();
    }
    
    public void setColumnRenderer(ColumnRenderer renderer) {
        this.columnRenderer = renderer;
    }
    
    @Override
    public int getRowCount() {
        return geometry.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return geometry.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        Object name = geometry.getColumnTitle(column);
        if(columnRenderer == null)
            return name==null? "" : name.toString();
        return columnRenderer.getColumnName(name);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return geometry.getColumnTitleClass();
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0 || !geometry.hasCellAt(row, column))
            return false;
        return getEditableLayer() != null;
    }
    
    private Layer getEditableLayer() {
        for(Layer layer : layers)
            if(layer.isEditable())
                return layer;
        return null;
    }

    public double[][] flattenValues() {
        Flattener flattener = new Flattener();
        flattener.calculate();
        return flattener.values;
    }
    
    public boolean hasValueAt(int row, int column) {
        return geometry.hasCellAt(row, column);
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return geometry.getRowName(row);
        LayerCriteria criteria = geometry.createCriteria(row, column);
        return getValue(criteria);
    }
    
    private Object getValue(LayerCriteria criteria) {
        for(Layer layer : layers) {
            Object value = getValue(layer, criteria);
            if(value != null)
                return value;
        }
        return null;
    }
    
    private Object getValue(Layer layer, LayerCriteria criteria) {
        if(layer.isVisible())
            return layer.getValue(criteria);
        return null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Layer layer = getEditableLayer();
        if(layer != null) {
            LayerCriteria criteria = geometry.createCriteria(row, column);
            layer.setValue(criteria, value);
        }
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
    
    private void fireLayerAdded(Layer layer) {
        if(layer.isVisible())
            fireDataChanged();
    }
    
    private void fireLayerRemoved(Layer layer) {
        if(layer.isVisible())
            fireDataChanged();
    }
    
    private void fireDataChanged() {
        TableModelEvent evt = new TableModelEvent(this);
        fireEvent(evt);
    }
    
    private void fireEvent(TableModelEvent evt) {
        for(TableModelListener listener : new ArrayList<TableModelListener>(listeners))
            listener.tableChanged(evt);
    }

    
    private class Flattener {
    
        private double[][] values;
        private GeometryModel model;
        
        private Flattener() {
            model = GeometryModel.ModelType.DEVELOPMENT.createModel();
            model.setTriangleGeometry(getTriangleGeometry());
        }
        
        void calculate() {
            int rowCount = model.getRowCount();
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
            while(model.hasCellAt(row, count))
                count++;
            return count - 1;
        }
        
        private void calculateCell(int row, int column) {
            LayerCriteria criteria = geometry.createCriteria(row, column+1);
            double value = getValue(criteria);
            values[row][column] = value;
        }

        private double getValue(LayerCriteria criteria) {
            for(Layer layer : layers) {
                Object value = getValue(layer, criteria);
                if(value != null)
                    return (Double) value;
            }
            return Double.NaN;
        }

        private Object getValue(Layer layer, LayerCriteria criteria) {
            if(layer.isVisible())
                return layer.getValue(criteria);
            return null;
        }
    }
}
