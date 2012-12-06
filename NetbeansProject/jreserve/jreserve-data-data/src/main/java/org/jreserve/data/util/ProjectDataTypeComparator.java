package org.jreserve.data.util;

import java.util.Comparator;
import org.jreserve.data.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataTypeComparator implements Comparator<ProjectDataType>  {

    public static int compareDataTypes(ProjectDataType o1, ProjectDataType o2) {
        if(o1 == null)
            return o2==null? 0 : 1;
        return o2==null? -1 : o1.getDbId() - o2.getDbId();
    }
    
    @Override
    public int compare(ProjectDataType o1, ProjectDataType o2) {
        return compareDataTypes(o1, o2);
    }
}
