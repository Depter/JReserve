package org.jreserve.project.entities;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.HibernateUtil;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Peter Decsi
 */
public class PersistenceTest {
    private final static String DERBY_SHUTDOWN_STATE = "08006";
    private final static String DELETE_IDS = String.format("DELETE FROM %s.%s",
              EntityRegistration.SCHEMA, EntityRegistration.TABLE);
    
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
    
    @After
    public void tearDown() {
        if(session.isOpen())
            session.getTransaction().rollback();
    }
    
    @Test
    public void testCreateLob() {
        session.beginTransaction();
        
        LoB lob1 = new LoB("Dummy Lob 1");
        LoB lob2 = new LoB("Dummy Lob 2");
        session.save(lob1);
        session.save(lob2);
        assertTrue(0 < lob1.getId());
        assertTrue(0 < lob2.getId());
        
        List<LoB> lobs = session.createQuery("from LoB").list();
        assertEquals(2, lobs.size());
        
        for(LoB lob : lobs)
            session.delete(lob);
        
        lobs = session.createQuery("from LoB").list();
        assertTrue(lobs.isEmpty());
            
        session.getTransaction().commit();
    }
    
    @Test
    public void testClaimType() {
        session.beginTransaction();

        LoB lob = new LoB("LOB");
        ClaimType claimType = new ClaimType("ClaimType");
        lob.addClaimType(claimType);
            
        session.persist(lob);
        session.persist(claimType);
        assertTrue(0 < lob.getId());
        assertTrue(0 < claimType.getId());

        session.delete(lob);
        List claimTypes = session.createQuery("from ClaimType").list();
        assertTrue(claimTypes.isEmpty());
            
        List lobs = session.createQuery("from LoB").list();
        assertTrue(lobs.isEmpty());
            
        session.getTransaction().commit();
    }
    
    @Test(expected=Exception.class)
    public void testClaimType_NoLob() {
        session.beginTransaction();

        ClaimType claimType = new ClaimType("ClaimType");
        session.save(claimType);
        session.getTransaction().commit();
    }
    
    @Test
    public void testProject() throws Exception {
        session.beginTransaction();

        LoB lob = new LoB("LOB");
        ClaimType claimType = new ClaimType("ClaimType");
        Project project = new Project("Project");
        
        lob.addClaimType(claimType);
        claimType.addProject(project);
        
        session.persist(lob);
        session.persist(claimType);
        session.persist(project);
        assertTrue(0 < lob.getId());
        assertTrue(0 < claimType.getId());
        assertTrue(0 < project.getId());
        List projects = session.createQuery("from Project").list();
        assertFalse(projects.isEmpty());
        
        session.delete(lob);
        projects = session.createQuery("from Project").list();
        assertTrue(projects.isEmpty());
        
        session.getTransaction().commit();
    }
    
    @Test(expected=Exception.class)
    public void testProject_NoClaimType() {
        session.beginTransaction();
        
        Project project = new Project("Project");
        session.save(project);
        session.getTransaction().commit();
    }
    
    @Test
    public void testChangeLog() {
        session.beginTransaction();
        
        LoB lob = new LoB("LoB");
        ClaimType claimType = new ClaimType("ClaimType");
        Project project = new Project("Project");
        
        lob.addClaimType(claimType);
        claimType.addProject(project);
        
        ChangeLog log = new ChangeLog(project, ChangeLog.Type.PROJECT, "Changed!");
        
        session.persist(lob);
        session.persist(claimType);
        session.persist(project);
        session.persist(log);
        
        assertTrue(0 < log.getId());
        List logs = session.createQuery("from ChangeLog").list();
        assertFalse(logs.isEmpty());
        
        session.delete(log);
        logs = session.createQuery("from ChangeLog").list();
        assertTrue(logs.isEmpty());
        
        session.getTransaction().commit();
    }
}