package org.jreserve.data.container;

import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.container.AbstractProjectElementContainerLoader;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(500)
public class ProjectDataContainerLoader extends AbstractProjectElementContainerLoader {

    @Override
    protected ProjectDataElementContainerFactory getFactory() {
        return ProjectDataContainerFactoy.getInstance();
    }
}
