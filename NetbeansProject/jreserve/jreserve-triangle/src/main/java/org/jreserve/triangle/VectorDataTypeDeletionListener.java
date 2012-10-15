package org.jreserve.triangle;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;
import org.jreserve.triangle.entities.Vector;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ProjectSystemDeletionListener.class)
public class VectorDataTypeDeletionListener extends AbstractDataTypeDeletionListener {

    @Override
    protected List<ProjectElement> getProjectElements(ProjectDataContainer container, ProjectDataType dt) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ProjectElement<Vector> element : container.getProjectElements(Vector.class))
            if(dt.equals(element.getValue().getDataType()))
                result.add(element);
        return result;
    }
}
