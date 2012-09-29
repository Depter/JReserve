package org.jreserve.data.projectdatatype;

import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
class ProjectDatTypeProjectElement extends ProjectElement<ProjectDataType> {

    ProjectDatTypeProjectElement(ProjectDataType dataType) {
        super(dataType);
        super.addToLookup(new PersistentDeletable(this));
    }

    @Override
    public Node createNodeDelegate() {
        return null;
    }

    @Override
    public boolean isVisible() {
        return false;
    }    
}
