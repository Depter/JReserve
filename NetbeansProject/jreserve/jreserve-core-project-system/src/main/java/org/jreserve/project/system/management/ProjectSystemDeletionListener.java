package org.jreserve.project.system.management;

import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.util.ProjectSystemListener;

/**
 *
 * @author Peter Decsi
 */
public interface ProjectSystemDeletionListener extends ProjectSystemListener {

    public void deleted(ProjectElement parent, ProjectElement child);
}
