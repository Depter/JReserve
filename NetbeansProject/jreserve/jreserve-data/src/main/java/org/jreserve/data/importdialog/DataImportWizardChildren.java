package org.jreserve.data.importdialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.data.DataImportWizard;
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
class DataImportWizardChildren extends Children.Keys<FileObject>{
    
    private final static String ROOT = "JReserve/ImportWizard";
    private final static String POSITION_ATTRIBUTE = "position";
    private final static String NAME_ATTRIBUTE = "displayName";
    private final static String ICON_ATTRIBUTE = "iconBase";
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int dif = comparePosition(o1, o2);
            return dif!=0? dif : compareNames(o1, o2);
        }
        
        private int comparePosition(FileObject f1, FileObject f2) {
            int p1 = (Integer) f1.getAttribute(POSITION_ATTRIBUTE);
            int p2 = (Integer) f2.getAttribute(POSITION_ATTRIBUTE);
            return p1-p2;
        }
        
        private int compareNames(FileObject f1, FileObject f2) {
            String n1 = (String) f1.getAttribute(NAME_ATTRIBUTE);
            String n2 = (String) f2.getAttribute(NAME_ATTRIBUTE);
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    private List<WizardNode> wizardNodes = new ArrayList<WizardNode>();
    
    DataImportWizardChildren() {
    }

    @Override
    protected void addNotify() {
        List<FileObject> files = getLoaderFiles();
        Collections.sort(files, COMPARATOR);
        super.setKeys(files);
    }
    
    private List<FileObject> getLoaderFiles() {
        FileObject root = FileUtil.getConfigFile(ROOT);
        if(root == null)
            return Collections.EMPTY_LIST;
        return (List<FileObject>) Collections.list(root.getData(false));
    }
    
    @Override
    protected Node[] createNodes(FileObject t) {
        DataImportWizard wizard = getWizard(t);
        if(wizard == null)
            return new Node[]{};
        return createNode(wizard, t);
    }
    
    private DataImportWizard getWizard(FileObject file) {
        try {
            DataObject data = DataObject.find(file);
            InstanceCookie cookie = data.getLookup().lookup(InstanceCookie.class);
            return (DataImportWizard) cookie.instanceCreate();
        } catch (Exception ex) {
            //TODO make log
            return null;
        }
    }
    
    private Node[] createNode(DataImportWizard wizard, FileObject file) {
        WizardNode node = new WizardNode(wizard);
        node.setDisplayName((String) file.getAttribute(NAME_ATTRIBUTE));
        setIcon(node, file);
        return new Node[]{node};
    }
    
    private void setIcon(WizardNode node, FileObject file) {
        String iconBase = (String) file.getAttribute(ICON_ATTRIBUTE);
        if(iconBase == null)
            return ;
        node.setIconBaseWithExtension(iconBase);
    }

    Node getNode(DataImportWizard wizard) {
        for(Node node : getNodes(true))
            if(containsWizard(node, wizard))
                return node;
        return null;
    }
    
    private boolean containsWizard(Node node, DataImportWizard wizard) {
        if(node instanceof WizardNode)
            return ((WizardNode)node).wizard.equals(wizard);
        return false;
    }
    
    private class WizardNode extends AbstractNode {
    
        private DataImportWizard wizard;
        
        private WizardNode(DataImportWizard wizard) {
            super(Children.LEAF, Lookups.fixed(wizard));
            this.wizard = wizard;
        } 
    }
}
