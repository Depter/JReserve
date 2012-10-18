package org.jreserve.triangle.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LayeredTableModel<V> implements TriangleTableModel<V> {

    private TriangleTableModelFactory modelFactory;
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private List<TriangleTableModel<V>> layers = new ArrayList<TriangleTableModel<V>>();
    
    public LayeredTableModel(TriangleTableModelFactory modelFactory) {
        checkModelFactory(modelFactory);
    }
    
    private void checkModelFactory(TriangleTableModelFactory modelFactory) {
        if(modelFactory == null)
            throw new NullPointerException("TriangleTableModelFactory can not be null!");
        this.modelFactory = modelFactory;
    }

    public void setModelType(TriangleTableModelFactory factory) {
        checkModelFactory(factory);
        List<TriangleTableModel<V>> oldList = new ArrayList<TriangleTableModel<V>>(layers);
        createNewModels(oldList);
        fireChange();
    }
    
    private void createNewModels(List<TriangleTableModel<V>> oldList) {
        layers.clear();
        for(TriangleTableModel<V> oldModel : oldList)
            createNewModel(oldModel);
    }
    
    private void createNewModel(TriangleTableModel<V> oldModel) {
        TriangleTable<V> table = oldModel.getTable();
        TriangleTableModel<V> model = modelFactory.createModel(table);
        layers.add(model);
    }
    
    @Override
    public int getRowCount() {
        if(layers.isEmpty())
            return 0;
        return layers.get(0).getRowCount();
    }

    @Override
    public int getColumnCount() {
        if(layers.isEmpty())
            return 0;
        return layers.get(0).getColumnCount();
    }

    @Override
    public Object getRowTitle(int row) {
        return layers.get(0).getRowTitle(row);
    }

    @Override
    public Object getColumnTitle(int column) {
        return layers.get(0).getColumnTitle(column);
    }

    @Override
    public boolean hasCellAt(int row, int column) {
        if(layers.isEmpty())
            return false;
        return layers.get(0).hasCellAt(row, column);
    }
    
    @Override
    public TriangleCell<V> getCellAt(int row, int column) {
        for(int l=layers.size()-1; l>=0; l--) {
            TriangleCell<V> cell = getCellAt(row, column, l);
            if(cell != null && cell.getValue() != null)
                return cell;
        }
        return null;
    }
    
    public TriangleCell<V> getCellAt(int row, int colum, int layer) {
        TriangleTableModel<V> model = layers.get(layer);
        return model.getCellAt(row, colum);
    }
    
    public void addTable(TriangleTable<V> table) {
        TriangleTableModel<V> model = modelFactory.createModel(table);
        layers.add(model);
        fireChange();
    }
    
    @Override
    public void setTable(TriangleTable<V> table) {
        setTable(table, 0);
    }
    
    public void setTable(TriangleTable<V> table, int layer) {
        if(table == null) {
            removeTable(layer);
        } else {
            setTableAt(table, layer);
        }
    } 
    
    public void removeTable(int layer) {
        if(layer >= 0 && layer < layers.size()) {
            layers.remove(layer);
            fireChange();
        }
    }
    
    private void setTableAt(TriangleTable<V> table, int layer) {
        ensureLayerExists(layer);
        layers.get(layer).setTable(table);
        fireChange();
    }
    
    private void ensureLayerExists(int layer) {
        int count = layer - layers.size() + 1;
        for(int l=0; l<count; l++)
            layers.add(modelFactory.createModel(new TriangleTable<V>()));
    }
    
    @Override
    public TriangleTable<V> getTable() {
        if(layers.isEmpty())
            return null;
        return layers.get(0).getTable();
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
}
