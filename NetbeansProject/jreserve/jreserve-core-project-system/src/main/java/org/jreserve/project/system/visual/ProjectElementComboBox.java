package org.jreserve.project.system.visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ActionMap;
import javax.swing.JPanel;
import org.jreserve.project.system.ProjectElement;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ChoiceView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElementComboBox extends JPanel implements ExplorerManager.Provider, Lookup.Provider {
    
    private final ExplorerManager em = new ExplorerManager();
    private Lookup lookup;
    private ChoiceView comboBox;
    private List<ProjectElement> elements = new ArrayList<ProjectElement>();
    
    private Map<Class, Result> results = new HashMap<Class, Result>();
    
    public ProjectElementComboBox() {
        this(new ArrayList<ProjectElement>());
    }
    
    public ProjectElementComboBox(List<ProjectElement> elements) {
        this.elements.addAll(elements);
        initComponents();
        em.setRootContext(new AbstractNode(new ProjectElementChildren()));
        lookup = ExplorerUtils.createLookup(em, new ActionMap());
    }
   
    private void initComponents() {
        setLayout(new BorderLayout());
        comboBox = new ChoiceView();
        add(comboBox, BorderLayout.CENTER);
        comboBox.setEditable(false);
    }
    
    @Override
    public int getBaseline(int width, int height) {
        Dimension size = comboBox.getPreferredSize();     
        Component c = comboBox.getEditor().getEditorComponent();
        int bs = c.getBaseline(size.width, size.height);
        int textY = comboBox.getY();
        return textY + bs;
    }
    
    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior() {
        Component c = comboBox.getEditor().getEditorComponent();
        return comboBox.getBaselineResizeBehavior();
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    public void setElements(List<ProjectElement> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
        em.setRootContext(new AbstractNode(new ProjectElementChildren()));
    }
    
    public ProjectElement getSelectedItem() {
        return lookup.lookup(ProjectElement.class);
    }
    
    public void setSelectedItem(Object o) {
        Node[] nodes = getNodeFor(o);
        try {
            em.setSelectedNodes(nodes);
            if(nodes.length == 0)
                em.setExploredContext(em.getRootContext());
        } catch (Exception ex) {}
    }
    
    private Node[] getNodeFor(Object o) {
        if(o == null)
            return new Node[0];
        for(Node node : em.getRootContext().getChildren().getNodes())
            if(containsObject(node, o))
                return new Node[]{node};
        return new Node[0];
    }
    
    private boolean containsObject(Node node, Object o) {
        Object n = node.getLookup().lookup(o.getClass());
        return n!=null && o.equals(n);
    }
    
    public <T> void addLookupListener(Class<T> c, LookupListener listener) {
        Result<T> result = results.get(c);
        if(result == null) {
            result = getLookup().lookupResult(c);
            results.put(c, result);
        }
        result.addLookupListener(listener);
    }
    
    public <T> void removeLookupListener(Class<T> c, LookupListener listener) {
        Result<T> result = results.get(c);
        if(result != null)
            result.removeLookupListener(listener);
    }
    
    public <T> T getSelectedItem(Class<T> c) {
        return getLookup().lookup(c);
    }
    
    private class ProjectElementChildren extends Children.Keys<ProjectElement> {
        
        private ProjectElementChildren() {
            setKeys(elements);
        }
        
        @Override
        protected Node[] createNodes(ProjectElement element) {
            return new Node[] {element.createNodeDelegate()};
        }
    }
}
