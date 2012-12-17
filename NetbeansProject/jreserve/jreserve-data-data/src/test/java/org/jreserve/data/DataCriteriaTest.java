package org.jreserve.data;

import java.util.Date;
//import org.easymock.EasyMock;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DataCriteriaTest {

//    private final static String OWNER_ID = "OWNER_ID";
//    
//    private DataCriteria criteria;
//    
//    public DataCriteriaTest() {
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        ProjectDataType mock = EasyMock.createMock(ProjectDataType.class);
//        EasyMock.expect(mock.getId()).andReturn(OWNER_ID);
//        EasyMock.replay(mock);
//        criteria = new DataCriteria(mock);
//    }
//
//    @Test
//    public void testGetOwner() {
//        ProjectDataType pdt = criteria.getOwner();
//        assertEquals(OWNER_ID, pdt.getId());
//    }
//
//    @Test
//    public void testSetOwner() {
//        ProjectDataType mock = EasyMock.createMock(ProjectDataType.class);
//        EasyMock.expect(mock.getId()).andReturn("bela");
//        EasyMock.replay(mock);
//        
//        criteria.setOwner(mock);
//        ProjectDataType pdt = criteria.getOwner();
//        assertEquals("bela", pdt.getId());
//    }
//
//    @Test(expected=NullPointerException.class)
//    public void testSetOwner_Null() {
//        criteria.setOwner(null);
//    }
//
//    @Test
//    public void testGetOwnerId() {
//        assertEquals(OWNER_ID, criteria.getOwnerId());
//        
//        ProjectDataType mock = EasyMock.createMock(ProjectDataType.class);
//        EasyMock.expect(mock.getId()).andReturn("bela");
//        EasyMock.replay(mock);
//        criteria.setOwner(mock);
//        assertEquals("bela", criteria.getOwnerId());
//    }
//
//    @Test
//    public void testAccidentDate() {
//        assertEquals(null, criteria.getFromAccidentDate());
//        assertEquals(null, criteria.getToAccidentDate());
//        
//        criteria.setFromAccidentDate(new Date(100));
//        assertEquals(100, criteria.getFromAccidentDate().getTime());
//        
//        criteria.setToAccidentDate(new Date(200));
//        assertEquals(200, criteria.getToAccidentDate().getTime());
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void testAccidentDate_ToBeforeFrom() {
//        criteria.setFromAccidentDate(new Date(100));
//        criteria.setToAccidentDate(new Date(99));
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void testAccidentDate_FromAfterTo() {
//        criteria.setToAccidentDate(new Date(100));
//        criteria.setFromAccidentDate(new Date(101));
//    }
//
//    @Test
//    public void testFromAccidentEqt() {
//        assertEquals(DataCriteria.EQT.GE, criteria.getFromAccidentEqt());
//        criteria.setFromAccidentEqt(DataCriteria.EQT.EQ);
//        assertEquals(DataCriteria.EQT.EQ, criteria.getFromAccidentEqt());
//        criteria.setFromAccidentEqt(null);
//        assertEquals(DataCriteria.EQT.GE, criteria.getFromAccidentEqt());
//    }
//
//    @Test
//    public void testToAccidentEqt() {
//        assertEquals(DataCriteria.EQT.LE, criteria.getToAccidentEqt());
//        criteria.setToAccidentEqt(DataCriteria.EQT.EQ);
//        assertEquals(DataCriteria.EQT.EQ, criteria.getToAccidentEqt());
//        criteria.setToAccidentEqt(null);
//        assertEquals(DataCriteria.EQT.LE, criteria.getToAccidentEqt());
//    }
//
//    @Test
//    public void testDevelopmentDate() {
//        assertEquals(null, criteria.getFromDevelopmentDate());
//        assertEquals(null, criteria.getToDevelopmentDate());
//        
//        criteria.setFromDevelopmentDate(new Date(100));
//        assertEquals(100, criteria.getFromDevelopmentDate().getTime());
//        
//        criteria.setToDevelopmentDate(new Date(200));
//        assertEquals(200, criteria.getToDevelopmentDate().getTime());
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void testDevelopmentDate_ToBeforeFrom() {
//        criteria.setFromDevelopmentDate(new Date(100));
//        criteria.setToDevelopmentDate(new Date(99));
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void testDevelopmentDate_FromAfterTo() {
//        criteria.setToDevelopmentDate(new Date(100));
//        criteria.setFromDevelopmentDate(new Date(101));
//    }
//
//    @Test
//    public void testFromDevelopmentEqt() {
//        assertEquals(DataCriteria.EQT.GE, criteria.getFromDevelopmentEqt());
//        criteria.setFromDevelopmentEqt(DataCriteria.EQT.EQ);
//        assertEquals(DataCriteria.EQT.EQ, criteria.getFromDevelopmentEqt());
//        criteria.setFromDevelopmentEqt(null);
//        assertEquals(DataCriteria.EQT.GE, criteria.getFromDevelopmentEqt());
//    }
//
//    @Test
//    public void testToDevelopmentEqt() {
//        assertEquals(DataCriteria.EQT.LE, criteria.getToDevelopmentEqt());
//        criteria.setToDevelopmentEqt(DataCriteria.EQT.EQ);
//        assertEquals(DataCriteria.EQT.EQ, criteria.getToDevelopmentEqt());
//        criteria.setToDevelopmentEqt(null);
//        assertEquals(DataCriteria.EQT.LE, criteria.getToDevelopmentEqt());
//    }
    
//TODO uncomment when EasyMock is downloadable
}