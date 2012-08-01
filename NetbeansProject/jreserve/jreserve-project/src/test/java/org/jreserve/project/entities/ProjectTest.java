package org.jreserve.project.entities;

import org.jreserve.project.TestUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 */
public class ProjectTest {

    private final static String NAME = "Project";
    private final static ClaimType claimType = new ClaimType("ClaimType");
    
    private Project project;
    
    public ProjectTest() {
    }

    @Before
    public void setUp() {
        project = new Project(claimType, NAME);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_ClaimTypeNull() {
        new Project(null, NAME);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_NameNull() {
        new Project(claimType, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NameTooShort() {
        new Project(claimType, "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NameTooLong() {
        new Project(claimType, TestUtil.TEXT_65);
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
    public void testGetChanges() {
        assertTrue(project.getChanges().isEmpty());
        ChangeLog log = new ChangeLog("This is the change!");
        project.addChange(log);
        assertEquals(1, project.getChanges().size());
        assertEquals(project, log.getProject());
    }

    @Test
    public void testEquals() {
        Project p2 = null;
        assertFalse(project.equals(p2));
        
        p2 = new Project(claimType, NAME.toLowerCase());
        assertTrue(project.equals(p2));
        
        p2 = new Project(claimType, NAME+NAME);
        assertFalse(project.equals(p2));
        
        p2 = new Project(new ClaimType("CT 2"), NAME);
        assertFalse(project.equals(p2));
    }

    @Test
    public void testToString() {
        String expected = String.format(
                "Project [%s; %s]", NAME, claimType.getName());
        assertEquals(expected, project.toString());
    }

}