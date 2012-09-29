package org.jreserve.project.system;

import java.util.ArrayList;
import java.util.List;
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
            setKeys(getVisibleChildren());
        }

        @Override
        public void childRemoved(ProjectElement child) {
            setKeys(getVisibleChildren());
        }

        @Override
        public void childrenChanged() {
            setKeys(getVisibleChildren());
        }
    };
    
    public ProjectElementChildren(ProjectElement element) {
        this.element = element;
        element.addProjectElementListener(listener);
    }

    @Override
    protected void addNotify() {
        setKeys(getVisibleChildren());
    }
    
    private List<ProjectElement> getVisibleChildren() {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ProjectElement child : (List<ProjectElement>) element.getChildren())
            if(child.isVisible())
                result.add(child);
        return result;
    }
    
    @Override
    protected Node[] createNodes(ProjectElement t) {
        return new Node[]{t.createNodeDelegate()};
    }
}
