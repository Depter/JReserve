package org.jreserve.triangle.mvc.layer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractGeometryModelTest {
    
    private final static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    private final static Date AC_START = getDate("1997-01-01");
    private final static int AC_PERIODS = 8;
    private final static int AC_MONTHS = 12;
    private final static Date DEV_START = getDate("1997-01-01");
    private final static int DEV_PERIODS = 8;
    private final static int DEV_MONTHS = 12;
    
    private static Date getDate(String date) {
        try {
            return DF.parse(date);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(date, ex);
        }
    }
    
    private AbstractGeometryModelImpl model;
    private TriangleGeometry geometry;
    
    public AbstractGeometryModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        geometry = new TriangleGeometry(AC_START, AC_PERIODS, AC_MONTHS, DEV_START, DEV_PERIODS, DEV_MONTHS);
        model = new AbstractGeometryModelImpl(geometry);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getRowCount method, of class AbstractGeometryModel.
     */
    @Test
    public void testGetRowCount() {
        assertEquals(AC_PERIODS, model.getRowCount());
    }

    /**
     * Test of getColumnCount method, of class AbstractGeometryModel.
     */
    @Test
    public void testGetColumnCount() {
        //First column is the row titles
        assertEquals(DEV_PERIODS+1, model.getColumnCount());
    }

    /**
     * Test of getRowName method, of class AbstractGeometryModel.
     */
    @Test
    public void testGetRowName() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(AC_START);
        
        for(int r=0; r<AC_PERIODS; r++) {
            Date date = calendar.getTime();
            assertEquals("At row: "+r, date, model.getRowName(r));
            calendar.add(Calendar.MONTH, AC_MONTHS);
        }
    }

    /**
     * Test of getAccidentBegin method, of class AbstractGeometryModel.
     */
    @Test
    public void testGetAccidentBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(AC_START);
        
        for(int r=0; r<AC_PERIODS; r++) {
            Date date = calendar.getTime();
            assertEquals("At row: "+r, date, model.getAccidentBegin(r));
            calendar.add(Calendar.MONTH, AC_MONTHS);
        }
    }

    /**
     * Test of getAccidentEnd method, of class AbstractGeometryModel.
     */
    @Test
    public void testGetAccidentEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(AC_START);
        calendar.add(Calendar.MONTH, AC_MONTHS * AC_PERIODS);
        
        Date date = calendar.getTime();
        assertEquals(date, model.getAccidentEnd());
    }

    /**
     * Test of getDevelopmentEnd method, of class AbstractGeometryModel.
     */
    @Test
    public void testGetDevelopmentEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(AC_START);
        calendar.add(Calendar.MONTH, DEV_MONTHS * DEV_PERIODS);
        
        Date date = calendar.getTime();
        assertEquals(date, model.getDevelopmentEnd());
    }

    private class AbstractGeometryModelImpl extends AbstractGeometryModel {
        
        private AbstractGeometryModelImpl(TriangleGeometry geometry) {
            super.setTriangleGeometry(geometry);
        }

        @Override
        public Object getColumnTitle(int column) {
            return column;
        }

        @Override
        public Class getColumnTitleClass() {
            return Integer.class;
        }

        @Override
        public boolean hasCellAt(int row, int column) {
            return false;
        }
    }
}
