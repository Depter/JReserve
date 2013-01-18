package org.jreserve.triangle.project;

import org.jreserve.project.system.container.AbstractProjectElementContainerInstaller;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.jreserve.triangle.util.ProjectDataContainerFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ProjectSystemCreationListener.class, position=100)
public class ProjectDataContainerInstaller extends AbstractProjectElementContainerInstaller {

    @Override
    protected ProjectDataElementContainerFactory getFactory() {
        return ProjectDataContainerFactory.getInstance();
    }
}
