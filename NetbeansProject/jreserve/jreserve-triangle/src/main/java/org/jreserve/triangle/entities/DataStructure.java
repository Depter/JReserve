package org.jreserve.triangle.entities;

import org.jreserve.data.ProjectData;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataStructure extends PersistentObject, ProjectData {

    public ProjectDataType getDataType();
    
}
