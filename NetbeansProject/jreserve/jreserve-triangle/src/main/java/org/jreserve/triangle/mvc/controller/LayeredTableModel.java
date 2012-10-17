package org.jreserve.triangle.mvc.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.triangle.mvc.model.AbstractCell;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.jreserve.triangle.mvc.model.ValueCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LayeredTableModel implements TriangleTableModel {

    private TriangleTableModelFactory modelFactory;
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private List<TriangleTableModel> layers = new ArrayList<TriangleTableModel>();
    
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
        List<TriangleTableModel> oldList = new ArrayList<TriangleTableModel>(layers);
        createNewModels(oldList);
        fireChange();
    }
    
    private void createNewModels(List<TriangleTableModel> oldList) {
        layers.clear();
        for(TriangleTableModel oldModel : oldList)
            createNewModel(oldModel);
    }
    
    private void createNewModel(TriangleTableModel oldModel) {
        TriangleTable table = oldModel.getTable();
        TriangleTableModel model = modelFactory.createModel(table);
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
    public boolean hasValueAt(int row, int column) {
        return getCellAt(row, column) != null;
    }
    
    private boolean isValueCell(AbstractCell cell) {
        return cell != null && (cell instanceof ValueCell);
    }
    
    @Override
    public AbstractCell getCellAt(int row, int column) {
        for(int l=layers.size()-1; l>=0; l--) {
            AbstractCell cell = getCellAt(row, column, l);
            if(isValueCell(cell))
                return cell;
        }
        return null;
    }
    
    public AbstractCell getCellAt(int row, int colum, int layer) {
        TriangleTableModel model = layers.get(layer);
        return model.getCellAt(row, colum);
    }
    
    public void addTable(TriangleTable table) {
        TriangleTableModel model = modelFactory.createModel(table);
        layers.add(model);
        fireChange();
    }
    
    @Override
    public void setTable(TriangleTable table) {
        setTable(table, 0);
    }
    
    public void setTable(TriangleTable table, int layer) {
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
    
    private void setTableAt(TriangleTable table, int layer) {
        ensureLayerExists(layer);
        layers.get(layer).setTable(table);
        fireChange();
    }
    
    private void ensureLayerExists(int layer) {
        int count = layer - layers.size() + 1;
        for(int l=0; l<count; l++)
            layers.add(modelFactory.createModel(new TriangleTable()));
    }
    
    @Override
    public TriangleTable getTable() {
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
