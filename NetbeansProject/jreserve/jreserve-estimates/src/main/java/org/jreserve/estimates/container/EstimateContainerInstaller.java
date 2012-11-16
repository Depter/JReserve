package org.jreserve.estimates.container;

import org.jreserve.estimates.EstimateContainerFactory;
import org.jreserve.project.system.container.AbstractProjectElementContainerInstaller;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ProjectSystemCreationListener.class, position=200)
public class EstimateContainerInstaller extends AbstractProjectElementContainerInstaller {

    @Override
    protected ProjectDataElementContainerFactory getFactory() {
        return EstimateContainerFactory.getInstance();
    }
}
