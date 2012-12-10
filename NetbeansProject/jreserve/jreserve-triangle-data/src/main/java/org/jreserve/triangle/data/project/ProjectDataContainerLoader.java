package org.jreserve.triangle.data.project;

import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.container.AbstractProjectElementContainerLoader;
import org.jreserve.project.system.container.ProjectDataElementContainerFactory;
import org.jreserve.triangle.data.factories.ProjectDataContainerFactoy;

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
