package org.jreserve.data.util;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.util.ProjectDataTypeFactory.DataTypeDummy;
import org.jreserve.project.entities.ClaimType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDataTypeFactoryTest {

    private ProjectDataTypeFactory factory;
    private List<DataTypeDummy> dummies;
    
    public ProjectDataTypeFactoryTest() {
    }

    @Before
    public void setUp() throws Exception {
        createDummies();
        factory = new ProjectDataTypeFactory(new ClaimType("CT"), dummies);
    }
    
    private void createDummies() {
        dummies = new ArrayList<DataTypeDummy>(1);
        dummies.addAll(ProjectDataTypeFactory.getDefaultDummies());
    }

    @Test
    public void testGetDefaultDummies() {
        List<DataTypeDummy> ds = ProjectDataTypeFactory.getDefaultDummies();
        List<DataType> dts = DataTypeUtil.getDataTypes();
        assertEquals(dts.size(), ds.size());
        
        for(int i=0, size=dts.size(); i<size; i++) {
            DataType dt = dts.get(i);
            DataTypeDummy d = ds.get(i);
            
            assertEquals(dt.getDbId(), d.getDbId());
            assertEquals(dt.getName(), d.getName());
            assertEquals(dt.isTriangle(), d.isTriangle());
        }
    }

    @Test
    public void testDoWork() throws Exception {
    }

}