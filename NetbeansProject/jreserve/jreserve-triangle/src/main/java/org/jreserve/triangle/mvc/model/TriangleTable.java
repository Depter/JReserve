package org.jreserve.triangle.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTable {

    private List<TriangleRow> rows = new ArrayList<TriangleRow>();
    private int rowCount = 0;
    private int columnCount = 0;
    
    TriangleTable() {
    }
    
    void addRow(TriangleRow row) {
        rows.add(row);
        row.setTable(this);
        Collections.sort(rows);
        rowCount++;
        setColumnCount(row.getCellCount());
    }
    
    private void setColumnCount(int cellCount) {
        if(cellCount > columnCount)
            columnCount = cellCount;
    }
    
    public int getRowCount() {
        return rowCount;
    }
    
    public int getColumnCount() {
        return columnCount;
    }
    
    public int getColumnCount(int row) {
        return rows.get(row).getCellCount();
    }
    
    public TriangleRow getRow(int row) {
        if(row < 0 || rowCount <= row)
            return null;
        return rows.get(row);
    }
    
    public AbstractCell getCell(int row, int column) {
        TriangleRow r = getRow(row);
        return r==null? null : r.getCell(column);
    }
    
    public void setValues(List<Data> datas) {
        for(TriangleRow row : rows)
            row.setValues(datas);
    }
}
