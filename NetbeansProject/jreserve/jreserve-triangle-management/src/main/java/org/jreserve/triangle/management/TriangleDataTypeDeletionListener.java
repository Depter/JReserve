package org.jreserve.triangle.management;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;
import org.jreserve.triangle.entities.Triangle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ProjectSystemDeletionListener.class)
public class TriangleDataTypeDeletionListener extends AbstractDataTypeDeletionListener {

    @Override
    protected List<ProjectElement> getProjectElements(ProjectDataContainer container, ProjectDataType dt) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ProjectElement<Triangle> element : container.getProjectElements(Triangle.class))
            if(dt.equals(element.getValue().getDataType()))
                result.add(element);
        return result;
    }

}
