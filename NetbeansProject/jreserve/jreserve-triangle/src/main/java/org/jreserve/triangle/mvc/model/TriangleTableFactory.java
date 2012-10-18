package org.jreserve.triangle.mvc.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTableFactory<V> {
    
    private Date accidentStart;
    private int accidentPeriods;
    private int accidentMonths;

    private Date developmentStart;
    private int developmentPeriods;
    private int developmentMonths;
    
    private Calendar calendar = Calendar.getInstance();
    private TriangleTable<V> table;
    private CellFactory<V> cellFactory;
    
    public TriangleTableFactory(TriangleGeometry geometry) {
        this(geometry, new TriangleCellFactory<V>());
    }
    
    public TriangleTableFactory(TriangleGeometry geometry, CellFactory<V> cellFactory) {
        setAccident(geometry);
        setDevelopment(geometry);
        setCellFactory(cellFactory);
    }
    
    private void setCellFactory(CellFactory<V> cellFactory) {
        if(cellFactory == null)
            throw new NullPointerException("CellFactory can not be null!");
        this.cellFactory = cellFactory;
        
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
    
    public TriangleTable<V> buildTable() {
        table = new TriangleTable<V>();
        buildRows();
        TriangleTable result = table;
        table = null;
        return result;
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
        TriangleRow<V> row = createRow();
        buildColumns(row, dummies);
        table.addRow(row);
    }
    
    private TriangleRow<V> createRow() {
        Date rowStart = calendar.getTime();
        calendar.add(Calendar.MONTH, accidentMonths);
        Date rowEnd = calendar.getTime();
        return new TriangleRow<V>(rowStart, rowEnd);
    }
    
    private void buildColumns(TriangleRow<V> row, List<DataCellDummy> dummies) {
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
        Date end = dummy.end;
        row.addCell(cellFactory.createCell(start, end));
    }
    
    private static class DataCellDummy {
        private Date start;
        private Date end;
        
        private DataCellDummy(Date start, Date end) {
            this.start = start;
            this.end = end;
        }
    }
}