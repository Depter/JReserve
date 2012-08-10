package org.jreserve.project.system.util;

import java.awt.Image;
import java.util.*;
import org.jreserve.project.system.ElementCreatorWizard.Category;
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
public class ElementCategoryUtil {
    
    public final static String CATEGORY_ELEMENT_NAME = "displayName";
    public final static String CATEGORY_ELEMENT_ICON = "iconBase";
    
    private final static String CATEGORY_ROOT = "jreserve/new-wizard/";
    private static final Map<Category, String> roots = new EnumMap<Category, String>(Category.class);
    private static final Map<Category, String> names = new EnumMap<Category, String>(Category.class);
    
    static {
        roots.put(Category.PROJECT, CATEGORY_ROOT+"project");
        names.put(Category.PROJECT, Bundle.LBL_ElementCategory_project());
        
        roots.put(Category.DATA, CATEGORY_ROOT+"data");
        names.put(Category.DATA, Bundle.LBL_ElementCategory_data());
        
        roots.put(Category.ESTIMATE, CATEGORY_ROOT+"estimate");
        names.put(Category.ESTIMATE, Bundle.LBL_ElementCategory_estimate());
        
        roots.put(Category.BOOTSTRAP, CATEGORY_ROOT+"bootstrap");
        names.put(Category.BOOTSTRAP, Bundle.LBL_ElementCategory_bootstrap());
    }
    
    public static String getRootName(Category category) {
        return roots.get(category);
    }
    
    public static List<FileObject> getElementCreators(Category category) {
        FileObject root = FileUtil.getConfigFile(roots.get(category));
        Enumeration<? extends FileObject> children = root.getData(false);
        List<FileObject> files = new ArrayList<FileObject>();
        while(children.hasMoreElements())
            files.add(children.nextElement());
        return files;
    }
    
    public static Children getChildren() {
        return new AllChildren();
    }
    
    private static class AllChildren extends Children.Keys<Category> {
        
        @Override
        protected void addNotify() {
            setKeys(Category.values());
        }
        
        @Override
        protected Node[] createNodes(Category t) {
            return new Node[]{new ElementCategoryNode(t)};
        }
    
    }
    
    private static class ElementCategoryNode extends AbstractNode {
        
        ElementCategoryNode(Category category) {
            super(Children.LEAF, Lookups.singleton(category));
            setDisplayName(names.get(category));
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
