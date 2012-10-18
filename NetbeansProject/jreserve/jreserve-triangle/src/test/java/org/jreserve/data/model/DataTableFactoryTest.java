package org.jreserve.data.model;

import org.jreserve.triangle.mvc.model.TriangleRow;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.jreserve.triangle.mvc.model.TriangleTableFactory;
import java.util.Calendar;
import java.util.Date;
import org.jreserve.triangle.TestUtil;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.model.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DataTableFactoryTest {

    private Date ACCIDENT_BEGIN = TestUtil.getDate("1997-1-1");
    private Date DEVELOPMENT_BEGIN = TestUtil.getDate("1997-1-1");
    
    public DataTableFactoryTest() {
    }

    @Test
    public void testBuildData_8x8() {
        TriangleTableFactory factory = getFactory(8, 12, 8, 12);
        TriangleTable table = factory.buildTable();
        assertEquals(8, table.getRowCount());
        assertEquals(8, table.getColumnCount());
        
        int year = 1997;
        for(int r=0; r<8; r++) {
            TriangleRow row = table.getRow(r);
            assertEquals(getDate(year+r, 1), row.getAccidentBegin());
            assertEquals(getDate(year+1+r, 1), row.getAccidentEnd());
            assertEquals(8-r, row.getCellCount());
            
            for(int c=1; c<=(8-r); c++) {
                TriangleCell cell = row.getCell(c-1);
                assertEquals(getDate(year+r+c-1, 1), cell.getDevelopmentBegin());
                assertEquals(getDate(year+r+c, 1), cell.getDevelopmentEnd());
            }
        }
    }

    private Date getDate(int year, int month) {
        String str = String.format("%d-%d-1", year, month);
        return TestUtil.getDate(str);
    }
    
    private TriangleTableFactory getFactory(int aPeriods, int aMonths, int dPeriods, int dMonths) {
        TriangleGeometry geometry = getGeometry(aPeriods, aMonths, dPeriods, dMonths);
        return new TriangleTableFactory(geometry);
    }
    
    private TriangleGeometry getGeometry(int aPeriods, int aMonths, int dPeriods, int dMonths) {
        return new TriangleGeometry(ACCIDENT_BEGIN, aPeriods, aMonths, DEVELOPMENT_BEGIN, dPeriods, dMonths);
    }

    @Test
    public void testBuildData_5x5() {
        TriangleTableFactory factory = getFactory(5, 18, 5, 18);
        TriangleTable table = factory.buildTable();
        assertEquals(5, table.getRowCount());
        assertEquals(5, table.getColumnCount());
        
        Calendar rowCalendar = Calendar.getInstance();
        rowCalendar.setTime(ACCIDENT_BEGIN);
        Calendar cellCalendar = Calendar.getInstance();
        cellCalendar.setTime(ACCIDENT_BEGIN);
        
        for(int r=0; r<5; r++) {
            TriangleRow row = table.getRow(r);
            assertEquals(rowCalendar.getTime(), row.getAccidentBegin());
            
            rowCalendar.add(Calendar.MONTH, 18);
            assertEquals(rowCalendar.getTime(), row.getAccidentEnd());
            
            assertEquals(5-r, row.getCellCount());
            
            cellCalendar.setTime(row.getAccidentBegin());
            for(int c=1; c<=(5-r); c++) {
                TriangleCell cell = row.getCell(c-1);
                assertEquals(cellCalendar.getTime(), cell.getDevelopmentBegin());
                cellCalendar.add(Calendar.MONTH, 18);
                assertEquals(cellCalendar.getTime(), cell.getDevelopmentEnd());
            }
        }
    }

    @Test
    public void testBuildData_8x32() {
        TriangleTableFactory factory = getFactory(8, 12, 32, 3);
        TriangleTable table = factory.buildTable();
        assertEquals(8, table.getRowCount());
        assertEquals(32, table.getColumnCount());
        
        Calendar rowCalendar = Calendar.getInstance();
        rowCalendar.setTime(ACCIDENT_BEGIN);
        Calendar cellCalendar = Calendar.getInstance();
        cellCalendar.setTime(ACCIDENT_BEGIN);
        
        for(int r=0; r<8; r++) {
            TriangleRow row = table.getRow(r);
            assertEquals(rowCalendar.getTime(), row.getAccidentBegin());
            
            rowCalendar.add(Calendar.MONTH, 12);
            assertEquals(rowCalendar.getTime(), row.getAccidentEnd());
            
            assertEquals(32-r*4, row.getCellCount());
            
            cellCalendar.setTime(row.getAccidentBegin());
            for(int c=1; c<=(32-r*4); c++) {
                TriangleCell cell = row.getCell(c-1);
                assertEquals(cellCalendar.getTime(), cell.getDevelopmentBegin());
                cellCalendar.add(Calendar.MONTH, 3);
                assertEquals(cellCalendar.getTime(), cell.getDevelopmentEnd());
            }
        }
    }
}