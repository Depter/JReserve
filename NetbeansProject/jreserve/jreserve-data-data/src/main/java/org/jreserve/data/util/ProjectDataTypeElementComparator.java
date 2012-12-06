package org.jreserve.data.util;

import java.util.Comparator;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDataTypeElementComparator implements Comparator<ProjectElement> {

    @Override
    public int compare(ProjectElement o1, ProjectElement o2) {
        if(o1==null)
            return o2==null? 0 : 1;
        if(o2==null)
            return -1;
        ProjectDataType d1 = (ProjectDataType) o1.getValue();
        ProjectDataType d2 = (ProjectDataType) o2.getValue();
        return ProjectDataTypeComparator.compareDataTypes(d1, d2);
    }
}
