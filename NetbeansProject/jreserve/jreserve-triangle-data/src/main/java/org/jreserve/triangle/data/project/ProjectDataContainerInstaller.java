package org.jreserve.triangle.data.project;

import org.jreserve.project.system.container.AbstractProjectElementContainerInstaller;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.jreserve.triangle.data.factories.ProjectDataContainerFactoy;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ProjectSystemCreationListener.class, position=100)
public class ProjectDataContainerInstaller extends AbstractProjectElementContainerInstaller {

    @Override
    protected ProjectDataElementContainerFactory getFactory() {
        return ProjectDataContainerFactoy.getInstance();
    }
}
