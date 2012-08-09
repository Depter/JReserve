package org.jreserve.project.system.newdialog;

import org.jreserve.project.system.ElementCreatorWizard;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ElementCategoryChildren extends Children.Keys<FileObject>{
    
    private ElementCategory category;

    ElementCategoryChildren(ElementCategory category) {
        this.category = category;
    }

    @Override
    protected void addNotify() {
        setKeys(category.getElementCreators());
    }

    @Override
    protected Node[] createNodes(FileObject t) {
        AbstractNode node = new AbstractNode(LEAF, Lookups.singleton(getWizard(t)));
        node.setDisplayName(t.getName());
        setIcon(node, t);
        setDisplayName(node, t);
        return new Node[]{node};
    }
    
    private ElementCreatorWizard getWizard(FileObject file) {
        try {
            DataObject data = DataObject.find(file);
            InstanceCookie cookie = data.getLookup().lookup(InstanceCookie.class);
            return (ElementCreatorWizard) cookie.instanceCreate();
        } catch (Exception ex) {
            //TODO make log
            return null;
        }
    }
    
    private void setIcon(AbstractNode node, FileObject file) {
        String iconBase = (String) file.getAttribute("iconBase");
        if(iconBase == null)
            return ;
        node.setIconBaseWithExtension(iconBase);
    }
    
    private void setDisplayName(AbstractNode node, FileObject file) {
        String name = (String) file.getAttribute("displayName");
        if(name == null)
            name = file.getName();
        node.setDisplayName(name);
    }
}
