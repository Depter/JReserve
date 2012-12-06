package org.jreserve.resources.layerexplorer;

import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FileNode extends AbstractNode {
    
    FileNode(FileObject file) {
        super(file.isFolder()? new FolderChildren(file) : new FileChildren(file));
        setDisplayName(file.getName());
    }
}
