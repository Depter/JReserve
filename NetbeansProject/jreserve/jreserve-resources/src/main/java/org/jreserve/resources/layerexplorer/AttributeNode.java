package org.jreserve.resources.layerexplorer;

import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AttributeNode extends AbstractNode {

    public AttributeNode(FileObject file, String attribute) {
        super(Children.LEAF);
        Object value = file.getAttribute(attribute);
        String str = value==null? "null" : value.toString();
        setDisplayName(String.format("%s = %s", attribute, str));
    }
}
