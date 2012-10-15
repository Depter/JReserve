package org.jreserve.data.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTableFactory {
    
    private Date accidentStart;
    private int accidentPeriods;
    private int accidentMonths;

    private Date developmentStart;
    private int developmentPeriods;
    private int developmentMonths;
    
    private Calendar calendar = Calendar.getInstance();
    private TriangleTable table = new TriangleTable();
    private boolean built = false;
    
    public TriangleTableFactory(TriangleGeometry geometry) {
        setAccident(geometry);
        setDevelopment(geometry);
    }
    
    private void setAccident(TriangleGeometry geometry) {
        this.accidentStart = geometry.getAccidentStart();
        this.accidentPeriods = geometry.getAccidentPeriods();
        this.accidentMonths = geometry.getMonthInAccident();
    }
    
    private void setDevelopment(TriangleGeometry geometry) {
        this.developmentStart = geometry.getDevelopmentStart();
        this.developmentPeriods = geometry.getDevelopmentPeriods();
        this.developmentMonths = geometry.getMonthInDevelopment();
    }
    
    public TriangleTable buildTable() {
        checkState();
        buildRows();
        return table;
    }
    
    private void checkState() {
        if(built)
            throw new IllegalStateException("Data already built!");
    }
    
    private void buildRows() {
        List<DataCellDummy> dummies = getCellDummies();
        calendar.setTime(accidentStart);
        for(int p=0; p<accidentPeriods; p++)
            buildRow(dummies);
    }
    
    private List<DataCellDummy> getCellDummies() {
        List<DataCellDummy> dummies = new ArrayList<DataCellDummy>();
        calendar.setTime(developmentStart);
        for(int p=0; p<developmentPeriods; p++)
            dummies.add(buildDummyColumn());
        return dummies;
    }
    
    private DataCellDummy buildDummyColumn() {
        Date start = calendar.getTime();
        calendar.add(Calendar.MONTH, developmentMonths);
        Date end = calendar.getTime();
        return new DataCellDummy(start, end);
    }
    
    private void buildRow(List<DataCellDummy> dummies) {
        TriangleRow row = createRow();
        buildColumns(row, dummies);
        table.addRow(row);
    }
    
    private TriangleRow createRow() {
        Date rowStart = calendar.getTime();
        calendar.add(Calendar.MONTH, accidentMonths);
        Date rowEnd = calendar.getTime();
        return new TriangleRow(rowStart, rowEnd);
    }
    
    private void buildColumns(TriangleRow row, List<DataCellDummy> dummies) {
        for(DataCellDummy dummy : dummies)
            if(containsDummy(row, dummy))
                addCell(row, dummy);
    }
    
    private boolean containsDummy(TriangleRow row, DataCellDummy dummy) {
        Date dummyEnd = dummy.end;
        Date rowBegin = row.getAccidentBegin();
        return dummyEnd.after(rowBegin);
    }
    
    private void addCell(TriangleRow row, DataCellDummy dummy) {
        Date start = dummy.start;
        row.addCell(new TriangleCell(start, dummy.end));
    }
    
    private static class DataCellDummy {
        private Date start;
        private Date end;
        
        private DataCellDummy(Date start, Date end) {
            this.start = start;
            this.end = end;
        }
    }
//    
//    public void setValues(List<Data> datas) {
//        table.setValues(datas);
//    }
}