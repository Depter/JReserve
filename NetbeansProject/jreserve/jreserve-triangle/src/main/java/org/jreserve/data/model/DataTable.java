package org.jreserve.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataTable {

    private List<DataRow> rows = new ArrayList<DataRow>();
    private int rowCount = 0;
    private int columnCount = 0;
    
    DataTable() {
    }
    
    void addRow(DataRow row) {
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
    
    public DataRow getRow(int row) {
        if(row < 0 || rowCount <= row)
            return null;
        return rows.get(row);
    }
    
    public DataCell getCell(int row, int column) {
        DataRow r = getRow(row);
        return r==null? null : r.getCell(column);
    }
    
    void setValues(List<Data> datas) {
        for(DataRow row : rows)
            row.setValues(datas);
    }
}
