package org.jreserve.project.system.newdialog;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.util.ElementCategoryUtil;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
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
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int dif = comparePosition(o1, o2);
            return dif!=0? dif : compareNames(o1, o2);
        }
        
        private int comparePosition(FileObject f1, FileObject f2) {
            int p1 = (Integer) f1.getAttribute(ElementCategoryUtil.CATEGORY_ELEMENT_POSITION);
            int p2 = (Integer) f2.getAttribute(ElementCategoryUtil.CATEGORY_ELEMENT_POSITION);
            return p1-p2;
        }
        
        private int compareNames(FileObject f1, FileObject f2) {
            String n1 = (String) f1.getAttribute(ElementCategoryUtil.CATEGORY_ELEMENT_NAME);
            String n2 = (String) f2.getAttribute(ElementCategoryUtil.CATEGORY_ELEMENT_NAME);
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    private Category category;

    ElementCategoryChildren(Category category) {
        this.category = category;
    }

    @Override
    protected void addNotify() {
        List<FileObject> creators = ElementCategoryUtil.getElementCreators(category);
        Collections.sort(creators, COMPARATOR);
        setKeys(creators);
    }

    @Override
    protected Node[] createNodes(FileObject t) {
        ElementCreatorWizard wizard = getWizard(t);
        AbstractNode node = new AbstractNode(LEAF, Lookups.singleton(wizard));
        node.setName(wizard.getClass().getName());
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
        String iconBase = (String) file.getAttribute(ElementCategoryUtil.CATEGORY_ELEMENT_ICON);
        if(iconBase == null)
            return ;
        node.setIconBaseWithExtension(iconBase);
    }
    
    private void setDisplayName(AbstractNode node, FileObject file) {
        String name = (String) file.getAttribute(ElementCategoryUtil.CATEGORY_ELEMENT_NAME);
        if(name == null)
            name = file.getName();
        node.setDisplayName(name);
    }
}
