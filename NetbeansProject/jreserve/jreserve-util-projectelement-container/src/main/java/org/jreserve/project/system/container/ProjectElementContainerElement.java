package org.jreserve.project.system.container;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jreserve.project.system.ProjectElement;
import org.openide.nodes.Node;


/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectElementContainerElement extends ProjectElement<ProjectElementContainer>{
    
    private int position;
    private Image img;
    private List<String> actionPathes = new ArrayList<String>();
    
    public ProjectElementContainerElement(ProjectElementContainer container, String name) {
        super(container);
        properties.put(NAME_PROPERTY, name);
        this.position = container.getPosition();
    }
    
    void setNodeImage(Image img) {
        this.img = img;
    }
    
    void addActionPathes(String... actionPathes) {
        if(actionPathes != null)
            this.actionPathes.addAll(Arrays.asList(actionPathes));
    }
    
    List<String> getActionPathes() {
        return actionPathes;
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ProjectElementContainerNode(this, img);
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public String toString() {
        return String.format(
            "ProjectElementContainer [%s]",
            getProperty(NAME_PROPERTY));
    }
}
