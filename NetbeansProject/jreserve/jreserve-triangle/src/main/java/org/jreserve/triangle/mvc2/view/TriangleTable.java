package org.jreserve.triangle.mvc2.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc2.data.TriangleCell;
import org.jreserve.triangle.mvc2.model.TriangleModel;
import org.jreserve.triangle.mvc2.model.TriangleModel.ModelType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTable extends JTable {

    private TriangleModel.ModelType type;
    private TriangleModel model;
    private TriangleCellRenderer renderer;
    
    public TriangleTable() {
        this(ModelType.DEVELOPMENT);
    }

    public TriangleTable(ModelType type) {
        this.type = type!=null? type : ModelType.DEVELOPMENT;
        createModel();
        createRenderers();
        formatTable();
    }
    
    private void createModel() {
        model = type.createModel();
        setModel(model);
    }
    
    private void createRenderers() {
        TableCellRenderer header = getTableHeader().getDefaultRenderer();
        setDefaultRenderer(String.class, header);
        renderer = new TriangleCellRenderer();
        setDefaultRenderer(TriangleCell.class, renderer);
    }
    
    private void formatTable() {
        setShowGrid(false);
        super.setRowMargin(0);
        //super.setce
    }
    
    public void setModelType(ModelType type) {
        this.type = type!=null? type : ModelType.DEVELOPMENT;
        initModel();
    }
    
    private void initModel() {
        TriangleModel newModel = type.createModel();
        
        for(List<Data<PersistentObject, Double>> datas : model.getValues())
            newModel.addValues(datas);
        newModel.addComments(model.getComments());
        newModel.setCummulated(model.isCummulated());
        newModel.setTriangleGeometry(model.getTriangleGeometry());
        newModel.setEditableLayer(model.getEditableLayer());
        
        this.model = newModel;
        setModel(newModel);
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
    
    public void setFractionDigits(int digits) {
        renderer.setFractionDigits(digits);
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
    
    public double[][] flatten() {
        TriangleCell[][] cells = model.getCells();
        Flattener flattener = new Flattener(cells);
        return flattener.flatten();
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
            Double value = cell.getValue();
            return value==null? Double.NaN : value;
        }
    }
}
