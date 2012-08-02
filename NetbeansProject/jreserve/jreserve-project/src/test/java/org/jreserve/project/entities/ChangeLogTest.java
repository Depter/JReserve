package org.jreserve.project.entities;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 */
public class ChangeLogTest {

    private final static String MSG = "Important message.";
    
    private ChangeLog log;
    
    public ChangeLogTest() {
    }

    @Before
    public void setUp() {
        log = new ChangeLog(MSG);
    }

    @Test
    public void testGetId() {
        assertEquals(0, log.getId());
    }

    @Test
    public void testGetProject() {
        assertEquals(null, log.getProject());
    }
    
    @Test
    public void testSetProject() {
        Project project = new Project("Project");
        log.setProject(project);
        assertEquals(project, log.getProject());
    }
    
    @Test(expected=NullPointerException.class)
    public void testSetProject_NullProject() {
        log.setProject(null);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testSetProject_ProjectAlreadySet() {
        try {
            log.setProject(new Project("Project 1"));
            log.setProject(new Project("Project 2"));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Test
    public void testGetCreationTime() {
        try {Thread.sleep(100);} catch (InterruptedException ex) {}
        Date date = new Date();
        Date creation = log.getCreationTime();
        assertTrue(date.after(creation));
    }

    @Test
    public void testGetLogMessage() {
        assertEquals(MSG, log.getLogMessage());
    }

    @Test
    public void testToString() {
        String expected = String.format(
            "%1$s (%2$tF %2$tT) [%3$s]: %4$s",
            System.getProperty("user.name"), log.getCreationTime(), 
            log.getProject(), MSG);
        assertEquals(expected, log.toString());
    }
}