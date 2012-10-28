package org.jreserve.triangle.guiutil.mvc2.view;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jreserve.data.Data;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.guiutil.mvc2.data.Layer;
import org.jreserve.triangle.guiutil.mvc2.model.TriangleModel;
import org.jreserve.triangle.guiutil.mvc2.model.TriangleTableModelConverter;

/**
 *
 * @author Peter Decsi
 */
public class TriangleTable extends JTable {

    private TriangleTableModelConverter model;
    private HashMap<Integer, DefaultRenderer> renderers = new HashMap<Integer, DefaultRenderer>();
    private DefaultRenderer defaultRenderer;
    private TableCellRenderer headerRenderer;
    
    public TriangleTable() {
        model = new TriangleTableModelConverter();
        setModel(model);
        initRenderers();
    }

    private void initRenderers() {
        headerRenderer = new HeaderRenderer();
        getTableHeader().setDefaultRenderer(headerRenderer);
        defaultRenderer = new DefaultRenderer();
        defaultRenderer.setModel(model);
    }
    
    public void setFractionDigits(int digits) {
        defaultRenderer.setFractionDigits(digits);
        for(DefaultRenderer renderer : renderers.values())
            renderer.setFractionDigits(digits);
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        model.setTriangleGeometry(geometry);
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return model.getTriangleGeometry();
    }
    
    public void setModelType(TriangleModel.ModelType type) {
        model.setModelType(type);
    }
    
    public Layer getLayer(int position) {
        return model.getLayer(position);
    }
    
    public void addLayer(Layer layer) {
        model.addLayer(layer);
    }
    
    public void addLayer(int position, Layer layer) {
        model.addLayer(position, layer);
    }
    
    public Layer setLayer(int position, Layer layer) {
        return model.setLayer(position, layer);
    }
    
    public void removeLayer(Layer layer) {
        model.removeLayer(layer);
    }
    
    public Layer removeLayer(int position) {
        return model.removeLayer(position);
    }
    
    public int getPosition(Layer layer) {
        return model.getPosition(layer);
    }
    
    public void setLayerRenderer(int layer, DefaultRenderer renderer) {
        if(renderer == null)
            renderers.remove(layer);
        else
            renderers.put(layer, renderer);
    }
    
    public boolean isCummulated() {
        return model.isCummulated();
    }
    
    public void setCummulated(boolean cummulated) {
        model.setCummulated(cummulated);
    }
    
    public List<Data> getDatas(int position) {
        return model.getDatas(position);
    }
    
    public void setDatas(int position, List<Data> data) {
        model.setDatas(position, data);
    }
    
    public double[][] flattenValues() {
        return model.flattenValues();
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        if(isPaintingForPrint())
            return prepareRendererForPrinting(row, column);
        return prepareRendererForScreen(row, column);
    } 
    
    private Component prepareRendererForPrinting(int row, int column) {
        Object value = getValueAt(row, column);
        int layer = model.getLayerOfLastValue();
        
        TableCellRenderer renderer = getRenderer(column, layer);
        return renderer.getTableCellRendererComponent(this, value,
                                                  false, false,
                                                  row, column);
    }
    
    private TableCellRenderer getRenderer(int column, int layer) {
        if(column == 0)
            return headerRenderer;
        TableCellRenderer renderer = renderers.get(layer);
        return renderer==null? defaultRenderer : renderer;
    }
    
    private Component prepareRendererForScreen(int row, int column) {
        Object value = getValueAt(row, column);
        int layer = model.getLayerOfLastValue();
        
        boolean isSelected = isCellSelected(row, column);
        boolean hasFocus = isCellFocosed(row, column);
        
        TableCellRenderer renderer = getRenderer(column, layer);
        return renderer.getTableCellRendererComponent(this, value,
                                                  isSelected, hasFocus,
                                                  row, column);
    }
    
    private boolean isCellFocosed(int row, int column) {
        return (selectionModel.getLeadSelectionIndex() == row) &&
               (columnModel.getSelectionModel().getLeadSelectionIndex() == column) &&
               isFocusOwner();
    }
}
