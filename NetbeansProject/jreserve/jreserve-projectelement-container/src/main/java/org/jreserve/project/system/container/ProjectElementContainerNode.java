package org.jreserve.project.system.container;

import java.awt.Image;
import org.jreserve.project.system.DefaultProjectNode;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectElementContainerNode extends DefaultProjectNode {
    
    private Image img;
    
    ProjectElementContainerNode(ProjectElementContainerElement element, Image img) {
        super(element);
        this.img = img;
        for(String path : element.getActionPathes())
            addActionPath(path);
    }
    
        
    @Override
    public Image getIcon(int type) {
        return img==null? super.getIcon(type) : img;
    }
}
