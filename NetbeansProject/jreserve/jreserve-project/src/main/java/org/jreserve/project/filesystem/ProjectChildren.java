package org.jreserve.project.filesystem;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.project.filesystem.ProjectElement;
import org.jreserve.project.filesystem.ProjectElementListener;
import org.jreserve.project.filesystem.ProjectFileSystem;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectChildren extends Children.Keys<ProjectElement>{
    
    private final static Logger logger = Logging.getLogger(ProjectChildren.class.getName());
    
    private final ProjectElementListener changeListener = new ProjectElementListener() {

        @Override
        public void childAdded(ProjectElement element) {
            refreshKeys();
        }

        @Override
        public void childRemoved(ProjectElement element) {
            refreshKeys();
        }
    };
    
    private final ProjectElement root;

    public ProjectChildren(ProjectElement root) {
        this.root = root;
        this.root.addProjectElementListener(changeListener);
    }
    
    @Override
    protected void addNotify() {
        refreshKeys();
    }
    
    private void refreshKeys() {
        List<ProjectElement> children = getChildObjects();
        super.setKeys(children);
    }
    
    private List<ProjectElement> getChildObjects() {
        List<ProjectElement> dataObjects = new ArrayList<ProjectElement>();
        for(ProjectElement child : root.getChildren())
            dataObjects.add(child);
        return dataObjects;
    }
    
    @Override
    protected Node[] createNodes(ProjectElement child) {
        return new Node[]{child.createNodeDelegate()};
    }
}
