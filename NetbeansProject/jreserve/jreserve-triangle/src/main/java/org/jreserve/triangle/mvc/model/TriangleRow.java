package org.jreserve.triangle.mvc.model;

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
public class TriangleRow implements Comparable<TriangleRow> {
    
    private TriangleTable table;
    
    private final Date accidentBegin;
    private final Date accidentEnd;
    
    private List<AbstractCell> cells = new ArrayList<AbstractCell>();
    private int cellCount;
    
    TriangleRow(Date accidentBegin, Date accidentEnd) {
        this.accidentBegin = accidentBegin;
        this.accidentEnd = accidentEnd;
    }
    
    void setTable(TriangleTable table) {
        this.table = table;
    }
    
    public TriangleTable getTable() {
        return table;
    }
    
    public Date getAccidentBegin() {
        return accidentBegin;
    }
    
    public Date getAccidentEnd() {
        return accidentEnd;
    }
    
    void addCell(AbstractCell cell) {
        cells.add(cell);
        Collections.sort(cells);
        cellCount++;
        cell.setRow(this);
    }
    
    public int getCellCount() {
        return cellCount;
    }
    
    public AbstractCell getCell(int index) {
        if(index <0 || cellCount <= index)
            return null;
        return cells.get(index);
    }
    
    void setValues(List<Data> datas) {
        List<Data> rowData = getRowData(datas);
        for(AbstractCell cell : cells)
            if(cell instanceof ValueCell)
                ((ValueCell)cell).setValues(rowData);
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
    
    AbstractCell getPreviousCell(ValueCell cell) {
        int index = Collections.binarySearch(cells, cell);
        if(index > 0)
            return cells.get(index-1);
        return null;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleRow)
            return compareTo((TriangleRow)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(TriangleRow row) {
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
