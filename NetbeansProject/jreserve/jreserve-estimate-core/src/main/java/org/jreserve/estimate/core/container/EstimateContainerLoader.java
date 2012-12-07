package org.jreserve.estimate.core.container;

import org.jreserve.estimate.core.container.factory.EstimateContainerFactory;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.container.AbstractProjectElementContainerLoader;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(600)
public class EstimateContainerLoader extends AbstractProjectElementContainerLoader {

    @Override
    protected ProjectDataElementContainerFactory getFactory() {
        return EstimateContainerFactory.getInstance();
    }
}
