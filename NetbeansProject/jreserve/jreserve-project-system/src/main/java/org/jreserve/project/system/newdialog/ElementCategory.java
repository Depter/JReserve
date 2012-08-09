package org.jreserve.project.system.newdialog;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.jreserve.resources.images.ImageResources;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL_ElementCategory_project=Project",
    "LBL_ElementCategory_data=Data",
    "LBL_ElementCategory_estimate=Estimate",
    "LBL_ElementCategory_bootstrap=Bootstrap"
})
public enum ElementCategory {
    
    PROJECT("jreserve/new-wizard/project", Bundle.LBL_ElementCategory_project()),
    DATA("jreserve/new-wizard/data", Bundle.LBL_ElementCategory_data()),
    ESTIMATE("jreserve/new-wizard/estimate", Bundle.LBL_ElementCategory_estimate()),
    BOOTSTRAP("jreserve/new-wizard/bootstrap", Bundle.LBL_ElementCategory_bootstrap());
    
    private final String rootName;
    private final String name;
    
    private ElementCategory(String rootName, String name) {
        this.rootName = rootName;
        this.name = name;
    }
    
    Node createRepresentingNode() {
        return new ElementCategoryNode(this);
    }
    
    List<FileObject> getElementCreators() {
        FileObject root = FileUtil.getConfigFile(rootName);
        Enumeration<? extends FileObject> children = root.getData(false);
        List<FileObject> files = new ArrayList<FileObject>();
        while(children.hasMoreElements())
            files.add(children.nextElement());
        return files;
    }
    
    
    static Children getChildren() {
        return new AllChildren();
    }
    
    private static class AllChildren extends Children.Keys<ElementCategory> {
        
        @Override
        protected void addNotify() {
            setKeys(ElementCategory.values());
        }
        
        @Override
        protected Node[] createNodes(ElementCategory t) {
            return new Node[]{new ElementCategoryNode(t)};
        }
    
    }
    
    private static class ElementCategoryNode extends AbstractNode {
        
        ElementCategoryNode(ElementCategory category) {
            super(Children.LEAF, Lookups.singleton(category));
            setDisplayName(category.name);
        }

        @Override
        public Image getIcon(int type) {
            return ImageResources.folderOpened().getImage();
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
        
        
    }
}
