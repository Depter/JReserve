package org.jreserve.data.util;

import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.ClaimType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDataTypeComparatorTest {

    private ProjectDataTypeComparator comparator;
    
    public ProjectDataTypeComparatorTest() {
    }

    @Before
    public void setUp() throws Exception {
        comparator = new ProjectDataTypeComparator();
    }

    @Test
    public void testCompare() {
        DummyDT dt1 = new DummyDT(100);
        DummyDT dt2 = new DummyDT(100);
        
        assertEquals(0, comparator.compare(dt1, dt2));
        assertEquals(0, comparator.compare(dt2, dt1));
        assertEquals(0, comparator.compare(null, null));
        
        dt2 = new DummyDT(200);
        assertTrue(comparator.compare(dt1, dt2) < 0);
        assertTrue(comparator.compare(dt2, dt1) > 0);
        
        assertTrue(comparator.compare(dt1, null) < 0);
        assertTrue(comparator.compare(null, dt1) > 0);
    }
    
    static class DummyDT extends ProjectDataType {

        private final int dbId;
        
        DummyDT(int dbId) {
            this.dbId = dbId;
        }
        
        @Override
        public ClaimType getClaimType() {
            return null;
        }

        @Override
        public int getDbId() {
            return dbId;
        }

        @Override
        public String toString() {
            return String.format("DummyDT [%d]", dbId);
        }
        
        
    }
}