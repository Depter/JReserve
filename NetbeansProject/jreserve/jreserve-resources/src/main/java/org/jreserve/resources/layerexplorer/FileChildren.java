package org.jreserve.resources.layerexplorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.openide.filesystems.*;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FileChildren extends Children.Keys<String> implements FileChangeListener {

    private FileObject file;

    FileChildren(FileObject file) {
        this.file = file;
    }
    
    @Override
    protected Node[] createNodes(String t) {
        return new Node[] {new AttributeNode(file, t)};
    }

    @Override
    protected void addNotify() {
        attributesChanged();
    }

    private void attributesChanged() {
        List<String> names = new ArrayList<String>();
        Enumeration<String> attrs = file.getAttributes();
        while(attrs.hasMoreElements())
            names.add(attrs.nextElement());
        Collections.sort(names);
        setKeys(names);
    }
    
    @Override
    public void fileFolderCreated(FileEvent fe) {
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
    }

    @Override
    public void fileChanged(FileEvent fe) {
        attributesChanged();
    }

    @Override
    public void fileDeleted(FileEvent fe) {
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
        attributesChanged();
    }

}
