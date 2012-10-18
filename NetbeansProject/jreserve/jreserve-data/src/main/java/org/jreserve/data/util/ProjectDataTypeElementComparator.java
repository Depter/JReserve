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
        ProjectDataType d1 = (ProjectDataType) o1.getValue();
        ProjectDataType d2 = (ProjectDataType) o1.getValue();
        return d1.getDbId() - d2.getDbId();
    }

}
