package org.jreserve.project.system;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultProjectNode extends AbstractNode {

    private ProjectElement element;
    
    public DefaultProjectNode(ProjectElement element) {
        super(new ProjectElementChildren(element), Lookups.proxy(element));
        this.element = element;
        Object value = element.getValue();
        setDisplayName(value==null? "null" : value.toString());
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
}
