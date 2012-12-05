package org.jreserve.data.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.projectdatatype.ProjectDatTypeProjectElement;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataTypeFactory extends SessionTask.AbstractTask<List<ProjectElement<ProjectDataType>>>{

    private final static Logger logger = Logger.getLogger(ProjectDataTypeFactory.class.getName());
    
    public static List<DataTypeDummy> getDefaultDummies() {
        List<DataTypeDummy> result = new ArrayList<DataTypeDummy>();
        for(DataType dt : DataTypeUtil.getDataTypes())
            result.add(new DataTypeDummy(dt));
        return result;
    }
    
    private final List<DataTypeDummy> dummies;
    private final ClaimType claimType;
    
    public ProjectDataTypeFactory(ClaimType claimType, DataTypeDummy[] dummies) {
        this(claimType, Arrays.asList(dummies));
    }
    
    public ProjectDataTypeFactory(ClaimType claimType, List<DataTypeDummy> dummies) {
        this.claimType = claimType;
        this.dummies = new ArrayList<DataTypeDummy>(dummies);
        Collections.sort(dummies);
    }

    @Override
    public void doWork(Session session) throws Exception {
        result = new ArrayList<ProjectElement<ProjectDataType>>();
        for(DataTypeDummy dummy : dummies)
            result.add(createDataType(session, dummy));
    }

    private ProjectElement<ProjectDataType> createDataType(Session session, DataTypeDummy dummy) {
        ProjectDataType dt = dummy.createDataType(claimType);
        session.persist(dt);
        logger.log(Level.INFO, "Data type created: \"{0}\"", dt);
        return new ProjectDatTypeProjectElement(dt);
    }
    
    public static class DataTypeDummy implements Comparable<DataTypeDummy>{
        private final int dbId;
        private final String name;
        private final boolean isTriangle;
        
        public DataTypeDummy(int dbId, String name, boolean isTriangle) {
            this.dbId = dbId;
            this.name = name;
            this.isTriangle = isTriangle;
        }
        
        private DataTypeDummy(DataType dt) {
            this.dbId = dt.getDbId();
            this.name = dt.getName();
            this.isTriangle = dt.isTriangle();
        }
        
        private ProjectDataType createDataType(ClaimType claimType) {
            return new ProjectDataType(claimType, dbId, name, isTriangle);
        }

        public int getDbId() {
            return dbId;
        }

        public boolean isTriangle() {
            return isTriangle;
        }

        public String getName() {
            return name;
        }
        
        @Override
        public int compareTo(DataTypeDummy o) {
            if(o == null)
                return -1;
            return dbId - o.dbId;
        }
    }
}
