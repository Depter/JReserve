package org.jreserve.data.base;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTest {
    
    private final static String NAME = "Triangle";
    private final static short START_YEAR = 2000;
    private final static short START_MONTH = 0;
    private final static short END_YEAR = 2008;
    private final static short END_MONTH = 0;
    
    private Lob lob;
    private ClaimType ct;
    private DataType paidType;
    
    private Triangle triangle;
    
    public TriangleTest() {
    }

    @Before
    public void setUp() {
        lob = new Lob("LoB", "LoB");
        ct = new ClaimType("CT", "CT");
        lob.addClaimType(ct);
        paidType = new DataType("Paid", true);
        
        triangle = new Triangle(NAME, ct, paidType);
        triangle.setStart(START_YEAR, START_MONTH);
        triangle.setEnd(END_YEAR, END_MONTH);
    }

    @Test
    public void testGetId() {
        assertEquals(0, triangle.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, triangle.getName());
        triangle.setName("Bela");
        assertEquals("Bela", triangle.getName());
    }

    @Test
    public void testGetProject() {
        assertEquals(null, triangle.getProject());
        Project project = new Project("Project", lob);
        triangle.setProject(project);
        assertEquals(project, triangle.getProject());
    }

    @Test
    public void testGetLob() {
        assertEquals(lob, triangle.getLob());
    }

    @Test
    public void testGetClaimType() {
        assertEquals(ct, triangle.getClaimType());
        Lob lob2 = new Lob("Bela", "Bela");
        ClaimType ct2 = new ClaimType("CT2", "CT2");
        lob2.addClaimType(ct2);
        triangle.setClaimType(ct2);
        
        assertEquals(ct2.getName(), triangle.getClaimType().getName());
        assertEquals(lob2.getName(), triangle.getLob().getName());
    }

    @Test
    public void testGetDataType() {
        
    }

    @Test
    public void testSetDataType() {
    }

    @Test
    public void testSetStart() {
    }

    @Test
    public void testGetStartYear() {
    }

    @Test
    public void testSetStartYear() {
    }

    @Test
    public void testGetStartMonth() {
    }

    @Test
    public void testSetStartMonth() {
    }

    @Test
    public void testGetMonthInDevelopment() {
    }

    @Test
    public void testSetMonthInDevelopment() {
    }

    @Test
    public void testGetMonthInAcciddent() {
    }

    @Test
    public void testSetMonthInAcciddent() {
    }

    @Test
    public void testSetEnd() {
    }

    @Test
    public void testGetEndYear() {
    }

    @Test
    public void testSetEndYear() {
    }

    @Test
    public void testGetEndMonth() {
    }

    @Test
    public void testSetEndMonth() {
    }

    @Test
    public void testGetCells() {
    }

    @Test
    public void testAddCell() {
    }

    @Test
    public void testRemoveCell() {
    }

    @Test
    public void testGetComments() {
    }

    @Test
    public void testAddComment() {
    }

    @Test
    public void testRemoveComment() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testToString() {
    }

}