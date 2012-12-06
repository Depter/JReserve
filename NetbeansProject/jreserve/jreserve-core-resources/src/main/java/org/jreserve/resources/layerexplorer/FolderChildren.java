package org.jreserve.resources.layerexplorer;

import org.openide.filesystems.*;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FolderChildren extends Children.Keys<FileObject> implements FileChangeListener {

    private FileObject root;
    
    FolderChildren(FileObject root) {
        this.root = root;
    }

    @Override
    protected void addNotify() {
        childrenChanged();
    }
    
    @Override
    protected Node[] createNodes(FileObject t) {
        return new Node[]{new FileNode(t)};
    }

    private void childrenChanged() {
        setKeys(root.getChildren());
    }
    
    @Override
    public void fileFolderCreated(FileEvent fe) {
        childrenChanged();
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        childrenChanged();
    }

    @Override
    public void fileChanged(FileEvent fe) {
    }

    @Override
    public void fileDeleted(FileEvent fe) {
        childrenChanged();
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
    }
}
