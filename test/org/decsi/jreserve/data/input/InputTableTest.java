package org.decsi.jreserve.data.input;

import org.decsi.jreserve.data.DataType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputTableTest {
    
    private final static DataType TYPE = DataType.PAID;
    private final static int CLAIM_TYPE = 1;
    private InputTable table;
    
    public InputTableTest() {
    }

    @Before
    public void setUp() {
        table = new InputTable(CLAIM_TYPE, TYPE);
    }

    @Test
    public void testGetType() {
        assertEquals(TYPE, table.getType());
    }

    @Test
    public void testGetClaimTypeId() {
        assertEquals(CLAIM_TYPE, table.getClaimTypeId());
    }

    @Test
    public void testGetRecordCount() {
        assertEquals(0, table.getRecordCount());
        
        InputRecord record = new InputRecord(InputRecordTest.ACCIDENT, InputRecordTest.DEVELOPMENT, 1d);
        assertTrue(table.addRecord(record));
        assertEquals(1, table.getRecordCount());
        assertFalse(table.addRecord(record));
        assertEquals(1, table.getRecordCount());
        
        assertTrue(table.removeRecord(record));
        assertEquals(0, table.getRecordCount());
    }

    @Test
    public void testEquals_Object() {
        assertFalse(table.equals(null));
        assertFalse(table.equals(new Object()));
    }

    @Test
    public void testEquals_InputTable() {
        InputTable t2 = new InputTable(CLAIM_TYPE, TYPE);
        assertTrue(table.equals(t2));
        assertTrue(t2.equals(table));
        
        t2 = new InputTable(2, TYPE);
        assertFalse(table.equals(t2));
        assertFalse(t2.equals(table));
        
        t2 = new InputTable(CLAIM_TYPE, DataType.INCURED);
        assertFalse(table.equals(t2));
        assertFalse(t2.equals(table));
    }
}