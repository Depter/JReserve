package org.jreserve.data.base;

import javax.persistence.EntityManager;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistenceTest {
    
    private static EntityManager em;
    
    public PersistenceTest() {
    }

    @Before
    public void setUp() throws Exception {
        em = EMFactory.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        EMFactory.shutDownDb();
    }
    
    @Test
    public void testPersitenceLob_ClaimType() {
        try {
            em.getTransaction().begin();
        
            Lob lob = new Lob();
            lob.setName("MTPL");
            lob.setLongName("Motor Third Party Liability");
            em.persist(lob);
            assertTrue(em.contains(lob));
            
            ClaimType bi = new ClaimType();
            bi.setName("BI");
            bi.setLongName("Bodily Injury");
            lob.addClaimType(bi);
            em.persist(bi);
            assertTrue(em.contains(bi));
            
            ClaimType md = new ClaimType();
            md.setName("MD");
            md.setLongName("Material Damage");
            em.persist(md);
            lob.addClaimType(md);
            assertTrue(em.contains(md));
            
            DataType dt = new DataType("Incurred", true);
            em.persist(dt);
            DataType ep = new DataType("Earned premium", false);
            em.persist(ep);
            
            em.remove(lob);
            assertFalse(em.contains(md));
            assertFalse(em.contains(bi));
            assertFalse(em.contains(lob));
            
            em.remove(ep);
            em.remove(dt);
            assertFalse(em.contains(ep));
            assertFalse(em.contains(dt));
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception in testPersistence!");
        }
    }
    
    @Test
    public void testPersistenceProject() {
        try {
            em.getTransaction().begin();

            Lob lob = new Lob();
            lob.setName("MTPL");
            lob.setLongName("Motor Third Party Liability");
            em.persist(lob);
            assertTrue(em.contains(lob));
            
            Project project = new Project("Test Project", lob);
            em.persist(project);
            assertTrue(em.contains(project));

            em.remove(project);
            assertFalse(em.contains(project));
            assertTrue(em.contains(lob));
            
            em.remove(lob);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception in testPersistence!");
        }
    }
    
    @Test
    public void testPersistenceTriangle() {
        try {
            em.getTransaction().begin();

            Lob lob = new Lob("TL", "Test Lob");
            ClaimType ct = new ClaimType("TCT", "Test Claim Type");
            lob.addClaimType(ct);
            Project project = new Project("Test Project", lob);
            DataType dt = new DataType("Paid", true);
            
            em.persist(lob);
            em.persist(project);
            em.persist(dt);
            
            Triangle triangle = new Triangle("Paid", ct, dt);
            project.addTriangle(triangle);
            em.persist(triangle);
            
            em.remove(project);
            assertFalse(em.contains(project));
            assertFalse(em.contains(triangle));
            
            em.remove(lob);
            em.remove(dt);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception in testPersistence!");
        }
    }
    
    @Test
    public void testPersistenceTriangleCells() {
        try {
            em.getTransaction().begin();

            Lob lob = new Lob("TL", "Test Lob");
            ClaimType ct = new ClaimType("TCT", "Test Claim Type");
            lob.addClaimType(ct);
            Project project = new Project("Test Project", lob);
            DataType dt = new DataType("Paid", true);
            Triangle triangle = new Triangle("Paid", ct, dt);
            project.addTriangle(triangle);
            
            em.persist(lob);
            em.persist(project);
            em.persist(dt);
            assertTrue(em.contains(triangle));
            
            //em.persist(triangle);
            
            em.remove(project);
            assertFalse(em.contains(project));
            assertFalse(em.contains(triangle));
            
            em.remove(lob);
            em.remove(dt);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception in testPersistence!");
        }
    }
    
}