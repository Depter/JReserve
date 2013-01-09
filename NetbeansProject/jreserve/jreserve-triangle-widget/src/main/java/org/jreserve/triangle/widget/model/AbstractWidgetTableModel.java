package org.jreserve.triangle.widget.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import org.jreserve.triangle.TriangleCoordiante;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.comment.Commentable;
import org.jreserve.triangle.comment.TriangleComment;
import org.jreserve.triangle.widget.WidgetEditor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractWidgetTableModel extends AbstractTableModel implements WidgetTableModel, ChangeListener {

    protected TriangularData data;
    
    protected boolean cummulated;
    protected Commentable commentable;
    protected WidgetEditor editor;
    private double[][] values;
    
    public AbstractWidgetTableModel() {
    }
    
    public AbstractWidgetTableModel(TriangularData data) {
        initData(data);
    }
    
    @Override
    public void setData(TriangularData data) {
        if(this.data != null)
            this.data.removeChangeListener(this);
        initData(data);
        super.fireTableStructureChanged();
    }
    
    private void initData(TriangularData data) {
        this.data = data;
        if(this.data != null)
            this.data.addChangeListener(this);
        initValues();
    }
    
    private void initValues() {
        this.values = data==null? null : data.toArray();
        if(values != null && cummulated)
            TriangleUtil.cummulate(values);
    }
    
    @Override
    public TriangularData getData() {
        return data;
    }
    
    @Override
    public void setCummulated(boolean cummulated) {
        if(this.cummulated == cummulated)
            return;
        this.cummulated = cummulated;
        initValues();
        fireTableDataChanged();
    }
    
    @Override
    public boolean isCummulated() {
        return cummulated;
    }
    
    @Override
    public void setWidgetEditor(WidgetEditor editor) {
        this.editor = editor;
    }
    
    @Override
    public WidgetEditor getWidgetEditor() {
        return editor;
    }
    
    @Override
    public String getLayerId(int row, int column) {
        if(data == null || 
           row > data.getAccidentCount() || 
           --column >= data.getDevelopmentCount(row)) 
            return null;
        return data.getLayerTypeId(row, column);
    }
    
    @Override
    public int getRowCount() {
        if(data == null)
            return 0;
        else
            return data.getAccidentCount();
    }

    @Override
    public int getColumnCount() {
        if(data == null)
            return 0;
        else
            return data.getDevelopmentCount()+1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return getRowTitleClass();
        else
            return Double.class;
    }
    
    protected Class<?> getRowTitleClass() {
        return java.util.Date.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0 || getValueAt(row, column) == null || editor == null)
            return false;
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        return editor.isCellEditable(accident, development);
    }
    
    protected abstract int getAccident(int row, int column);

    protected abstract int getDevelopment(int row, int column);
    
    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return data.getAccidentName(row);
        int accident = getAccident(row, column-1);
        int development = getDevelopment(row, column-1);
        return getValue(accident, development);
    }
    
    protected abstract String getRowName(int row);
    
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
    public void setValueAt(Object value, int row, int column) {
        if(editor == null) 
            return;
        int accident = getAccident(row, column-1);
        int development = getDevelopment(row, column-1);
        if(isValidCoordiantes(accident, development))
            editor.setCellValue(this, accident, development, (Double) value);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        initValues();
        fireTableStructureChanged();
    }
    
    @Override
    public List<TriangleCoordiante> getCells(int[] rows, int[] columns) {
        List<TriangleCoordiante> cells = new ArrayList<TriangleCoordiante>();
        for(int row : rows)
            for(int column : columns)
                addCell(cells, row, column);
        return cells;
    }
    
    private void addCell(List<TriangleCoordiante> cells, int row, int column) {
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        if(isValidCoordiantes(accident, development))
            cells.add(new TriangleCoordiante(accident, development));
    }
    
    @Override
    public void setCommentable(Commentable commentable) {
        this.commentable = commentable;
    }
    
    @Override
    public Commentable getCommentable() {
        return this.commentable;
    }
    
    @Override
    public List<TriangleComment> getComments(int row, int column) {
        int accident = getAccident(row, column - 1);
        int development = getDevelopment(row, column - 1);
        if(commentable != null && isValidCoordiantes(accident, development))
            return queryComments(accident, development);
        return Collections.EMPTY_LIST;
    }
    
    private List<TriangleComment> queryComments(int accident, int development) {
        List<TriangleComment> comments = new ArrayList<TriangleComment>();
        for(TriangleComment comment : commentable.getComments())
            if(isCellComment(comment, accident, development))
                comments.add(comment);
        return comments;
    }
    
    private boolean isCellComment(TriangleComment comment, int accident, int development) {
        return comment.getAccidentPeriod() == accident &&
               comment.getDevelopmentPeriod() == development;
    }
}
