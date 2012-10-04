package org.jreserve.data.container;

import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.ProjectDataContainerNode.Name=Data"
})
public class ProjectDataContainerElement extends ProjectElement<ProjectDataContainer> {

    private final static int POSITION = 200;
    
    public ProjectDataContainerElement(ProjectDataContainer container) {
        super(container);
        properties.put(NAME_PROPERTY, Bundle.LBL_ProjectDataContainerNode_Name());
    }

    @Override
    public Node createNodeDelegate() {
        return new ProjectDataContainerNode(this);
    }
    
    @Override
    public int getPosition() {
        return POSITION;
    }
}
