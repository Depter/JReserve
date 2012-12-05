package org.jreserve.data.util;

import org.jreserve.data.util.ProjectDataTypeComparatorTest.DummyDT;
import org.jreserve.project.system.ProjectElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDataTypeElementComparatorTest {

    private ProjectDataTypeElementComparator comparator;
    
    public ProjectDataTypeElementComparatorTest() {
    }

    @Before
    public void setUp() throws Exception {
        comparator = new ProjectDataTypeElementComparator();
    }

    @Test
    public void testCompare() {
        ProjectElement e1 = new ProjectElement(new DummyDT(100));
        ProjectElement e2 = new ProjectElement(new DummyDT(100));
        
        assertEquals(0, comparator.compare(e1, e2));
        assertEquals(0, comparator.compare(e2, e1));
        assertEquals(0, comparator.compare(null, null));
        
        e2 = new ProjectElement(new DummyDT(200));
        assertTrue(comparator.compare(e1, e2) < 0);
        assertTrue(comparator.compare(e2, e1) > 0);
        
        assertTrue(comparator.compare(e1, null) < 0);
        assertTrue(comparator.compare(null, e1) > 0);        
    }

}