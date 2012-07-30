package org.jreserve.project.entities.input;

import org.hibernate.Session;
import org.jreserve.project.entities.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 */
public class DataTypeTest {
    
    private final static String NAME = "Paid";
    
    private static Session session;
    private DataType dataType;
    
    public DataTypeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        session.close();
    }
    
    @Before
    public void setUp() {
        dataType = new DataType(NAME);
    }

    @Test
    public void testGetId() {
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, dataType.getName());
    }

    @Test
    public void testSetName() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testToString() {
    }

}