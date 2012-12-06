package org.jreserve.project.entities;

import org.jreserve.project.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ProjectTest {

    private final static String NAME = "Project";
    
    private ClaimType claimType;;
    private Project project;
    
    public ProjectTest() {
    }

    @Before
    public void setUp() {
        claimType = new ClaimType("ClaimType");
        project = new Project(NAME);
        claimType.addProject(project);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_NameNull() {
        new Project(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NameTooShort() {
        new Project("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NameTooLong() {
        new Project(TestUtil.TEXT_65);
    }
    
    @Test
    public void testGetId() {
        assertEquals(0, project.getId());
    }

    @Test
    public void testGetClaimType() {
        assertEquals(claimType, project.getClaimType());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, project.getName());
    }

    @Test
    public void testSetName() {
        project.setName("bela");
        assertEquals("bela", project.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_NameNull() {
        project.setName(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_NameTooShort() {
        project.setName("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_NameTooLong() {
        project.setName(TestUtil.TEXT_65);
    }

    @Test
    public void testGetDescription() {
        assertEquals(null, project.getDescription());
    }

    @Test
    public void testSetDescription() {
        String description = "This is the description!";
        project.setDescription(description);
        assertEquals(description, project.getDescription());
    }

    @Test
    public void testEquals() {
        Project p2 = null;
        assertFalse(project.equals(p2));
        
        p2 = new Project(NAME.toLowerCase());
        Project p3 = new Project(NAME.toUpperCase());
        assertTrue(p2.equals(p3));
        
        p2 = new Project(NAME+NAME);
        claimType.addProject(p2);
        assertFalse(project.equals(p2));
        
        p2 = new Project(NAME);
        new ClaimType("CT 2").addProject(p2);
        assertFalse(project.equals(p2));
    }

    @Test
    public void testToString() {
        String expected = String.format(
                "Project [%s; %s]", NAME, claimType.getName());
        assertEquals(expected, project.toString());
    }

}