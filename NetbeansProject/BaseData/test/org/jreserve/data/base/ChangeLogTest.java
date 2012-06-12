package org.jreserve.data.base;

import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChangeLogTest {
    
    private final static Date BEFORE = new Date();
    private final static String LOG = "This is a meaningful log.";
    
    private ChangeLog log;
    
    public ChangeLogTest() {
    }

    @Before
    public void setUp() {
        log = new ChangeLog(LOG);
    }

    @Test
    public void testGetId() {
        assertEquals(0, log.getId());
    }

    @Test
    public void testGetCreationTime() throws Exception {
        Date created = log.getCreationTime();
        assertTrue(BEFORE.before(created));
        //Give some time to get a date which is later than the creation date.
        Thread.sleep(10);
        Date after = new Date();
        assertTrue(after.after(created));
    }

    @Test
    public void testGetChange() {
        assertEquals(LOG, log.getChange());
    }

    @Test
    public void testSetProject() {
        assertEquals(null, log.getProject());
        Project project = new Project("Project", new Lob("LoB", "LoB"));
        log.setProject(project);
        assertEquals(project, log.getProject());
    }

    @Test
    public void testToString() {
        assertFalse(log.toString() == null);
    }

}