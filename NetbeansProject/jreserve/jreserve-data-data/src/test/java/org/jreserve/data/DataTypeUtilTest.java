package org.jreserve.data;

import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class DataTypeUtilTest {

    public DataTypeUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParse() {
    }

    @Test
    public void testGetDataType() {
        List<DataType> dts = DataTypeUtil.getDataTypes();
        
        assertEquals(1, dts.size());
    }

    @Test
    public void testDeleteDataType() {
    }

    @Test
    public void testCreateDataType() {
    }

    @Test
    public void testSetName() {
    }

    @Test
    public void testSetTriangle() {
    }

    @Test
    public void testGetDataTypes() {
    }

    @Test
    public void testSave() {
    }

    @Test
    public void testGetDefaultDataTypes() {
    }

}