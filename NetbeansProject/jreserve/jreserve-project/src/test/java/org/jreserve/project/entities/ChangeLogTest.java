package org.jreserve.project.entities;

import java.util.Date;
import org.jreserve.project.entities.ChangeLog.Type;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ChangeLogTest {

    private final static String MSG = "Important message.";
    
    private Project project;
    private ChangeLog log;
    
    public ChangeLogTest() {
    }

    @Before
    public void setUp() {
        project = new Project("Project");
        log = new ChangeLog(project, Type.PROJECT, MSG);
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