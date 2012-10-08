package org.jreserve.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataRow implements Comparable<DataRow> {
    
    private DataTable table;
    
    private final Date accidentBegin;
    private final Date accidentEnd;
    
    private List<DataCell> cells = new ArrayList<DataCell>();
    private int cellCount;
    
    DataRow(Date accidentBegin, Date accidentEnd) {
        this.accidentBegin = accidentBegin;
        this.accidentEnd = accidentEnd;
    }
    
    void setTable(DataTable table) {
        this.table = table;
    }
    
    public DataTable getTable() {
        return table;
    }
    
    public Date getAccidentBegin() {
        return accidentBegin;
    }
    
    public Date getAccidentEnd() {
        return accidentEnd;
    }
    
    void addCell(DataCell cell) {
        cells.add(cell);
        Collections.sort(cells);
        cellCount++;
        cell.setRow(this);
    }
    
    public int getCellCount() {
        return cellCount;
    }
    
    public DataCell getCell(int index) {
        if(index <0 || cellCount <= index)
            return null;
        return cells.get(index);
    }
    
    void setValues(List<Data> datas) {
        List<Data> rowData = getRowData(datas);
        for(DataCell cell : cells)
            cell.setValues(rowData);
    }
    
    private List<Data> getRowData(List<Data> datas) {
        List<Data> rowData = new ArrayList<Data>();
        for(Data data : datas)
            if(myData(data))
                rowData.add(data);
        return rowData;
    }
    
    private boolean myData(Data data) {
        Date date = data.getAccidentDate();
        return !accidentBegin.after(date) &&
                accidentEnd.after(date);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof DataRow)
            return compareTo((DataRow)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(DataRow row) {
        return accidentBegin.compareTo(row.accidentBegin);
    }
    
    @Override
    public int hashCode() {
        return accidentBegin.hashCode();
    }
    
    @Override
    public String toString() {
        String format = "DataRow [%tF - %tF]";
        return String.format(format, accidentBegin, accidentEnd);
    }
}
