package org.jreserve.project.entities;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.project.HibernateUtil;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class PersistenceTest {
    private final static String DERBY_SHUTDOWN_STATE = "08006";
    private Session session;
    
    public PersistenceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        createDatabase();
        HibernateUtil.open();
    }

    private static void createDatabase() throws Exception {
        System.setProperty("derby.stream.error.field", "java.lang.System.err");
        //System.setProperty("derby.stream.error.logSeverityLevel", "60000");
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        DriverManager.getConnection("jdbc:derby:memory:TestDb;create=true").close();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        HibernateUtil.close();
        dropDatabase();
    }
    
    private static void dropDatabase() throws SQLException {
        try {
            DriverManager.getConnection("jdbc:derby:memory:TestDb;drop=true");
        } catch (SQLException ex) {
            if(!DERBY_SHUTDOWN_STATE.equals(ex.getSQLState()))
                throw ex;
        }
    }
    
    @Before
    public void setUp() {
        session = HibernateUtil.getCurrentSession();
    }
    
    @Test
    public void testDataType() throws Exception {
        session.beginTransaction();
        try {
            DataType dt1 = new DataType("DataType 1");
            DataType dt2 = new DataType("DataType 2");
            
            session.save(dt1);
            session.save(dt2);
            assertEquals(1, dt1.getId());
            assertEquals(2, dt2.getId());
        
            List<DataType> dts = session.createQuery("from DataType").list();
            assertEquals(2, dts.size());
        
            for(DataType dt : dts)
                session.delete(dt);
        
            dts = session.createQuery("from DataType").list();
            assertTrue(dts.isEmpty());
            
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        }
    }
    
    @Test
    public void testCreateLob() throws Exception {
        session.beginTransaction();
        try {
            LoB lob1 = new LoB("Dummy Lob 1");
            LoB lob2 = new LoB("Dummy Lob 2");
            session.save(lob1);
            session.save(lob2);
            assertEquals(1, lob1.getId());
            assertEquals(2, lob2.getId());
        
            List<LoB> lobs = session.createQuery("from LoB").list();
            assertEquals(2, lobs.size());
        
            for(LoB lob : lobs)
                session.delete(lob);
        
            lobs = session.createQuery("from LoB").list();
            assertTrue(lobs.isEmpty());
            
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        }
    }
}