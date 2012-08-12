package org.jreserve.project.system;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElementChildren extends Children.Keys<ProjectElement> {

    private final ProjectElement element;
    
    private ProjectElementListener listener = new ProjectElementListener() {
        @Override
        public void childAdded(ProjectElement child) {
            setKeys(element.getChildren());
        }

        @Override
        public void childRemoved(ProjectElement child) {
            setKeys(element.getChildren());
        }

        @Override
        public void childrenChanged() {
            setKeys(element.getChildren());
        }
    };
    
    public ProjectElementChildren(ProjectElement element) {
        this.element = element;
        element.addProjectElementListener(listener);
    }

    @Override
    protected void addNotify() {
        setKeys(element.getChildren());
    }
    
    @Override
    protected Node[] createNodes(ProjectElement t) {
        return new Node[]{t.createNodeDelegate()};
    }

}
