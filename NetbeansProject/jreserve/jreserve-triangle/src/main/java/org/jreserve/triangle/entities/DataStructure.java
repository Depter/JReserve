package org.jreserve.triangle.entities;

import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.util.ProjectData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataStructure extends PersistentObject, ProjectData {

    public ProjectDataType getDataType();
    
}
