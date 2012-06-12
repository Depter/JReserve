package org.jreserve.data.base;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectTest {
    
    private final static String NAME = "Project";
    
    private Lob lob;
    private ClaimType ct;
    private DataType paidType;
    private DataType premiumType;
    
    private Project project;
    private Vector vector;
    private Triangle triangle;
    private ChangeLog log;
    
    public ProjectTest() {
    }

    @Before
    public void setUp() {
        lob = new Lob("Lob", "Lob");
        ct = new ClaimType("CT", "CT");
        lob.addClaimType(ct);
        paidType = new DataType("DT", true);
        premiumType = new DataType("DT", true);
        
        project = new Project(NAME, lob);
        vector = new Vector("Vector", ct, premiumType);
        triangle = new Triangle("Triangle", ct, paidType);
        log = new ChangeLog("Change");
    }

    @Test
    public void testGetId() {
        assertEquals(0, project.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, project.getName());
        project.setName("Bela");
        assertEquals("Bela", project.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(null, project.getDescription());
        
        String description = "This is a description!";
        project.setDescription(description);
        assertEquals(description, project.getDescription());
        
        project.setDescription(null);
        assertEquals(null, project.getDescription());
    }

    @Test
    public void testGetLob() {
        assertEquals(lob, project.getLob());
        
        String name2 = "Lob2";
        Lob lob2 = new Lob(name2, name2);
        project.setLob(lob2);
        assertEquals(lob2.getName(), project.getLob().getName());
        
    }

    @Test
    public void testGetVectors() {
        assertTrue(project.getVectors().isEmpty());
    }

    @Test
    public void testAddVector() {
        project.addVector(vector);
        assertEquals(1, project.getVectors().size());
        assertEquals(project, vector.getProject());
    }

    @Test
    public void testRemoveVector() {
        project.addVector(vector);
        project.removeVector(vector);
        assertTrue(project.getVectors().isEmpty());
        assertEquals(null, vector.getProject());
    }

    @Test
    public void testGetTriangles() {
        assertTrue(project.getTriangles().isEmpty());
    }

    @Test
    public void testAddTriangle() {
        project.addTriangle(triangle);
        assertEquals(1, project.getTriangles().size());
        assertEquals(project, triangle.getProject());
    }

    @Test
    public void testRemoveTriangle() {
        project.addTriangle(triangle);
        project.removeTriangle(triangle);
        assertTrue(project.getTriangles().isEmpty());
        assertEquals(null, triangle.getProject());
    }

    @Test
    public void testGetChangeLogs() {
        assertTrue(project.getChangeLogs().isEmpty());
    }

    @Test
    public void testAddChangeLog() {
        project.addChangeLog(log);
        assertEquals(1, project.getChangeLogs().size());
        assertEquals(project, log.getProject());
    }

    @Test
    public void testToString() {
        String expected = "Project ["+NAME+"; 0]";
        assertEquals(expected, project.toString());
    }
}