package org.jreserve.project.system;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectChildren extends Children.Keys<ProjectElement> {

    private final ProjectElement element;
    
    public ProjectChildren(ProjectElement element) {
        this.element = element;
    }

    @Override
    protected void addNotify() {
        super.setKeys(element.getChildren());
    }
    
    @Override
    protected Node[] createNodes(ProjectElement t) {
        return new Node[]{t.createNodeDelegate()};
    }

}
