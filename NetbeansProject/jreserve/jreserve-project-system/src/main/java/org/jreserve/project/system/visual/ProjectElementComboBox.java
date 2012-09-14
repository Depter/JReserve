package org.jreserve.project.system.visual;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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
    
    public void addActionListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        comboBox.removeActionListener(listener);
    }
    
    private class ProjectElementChildren extends Children.Keys<ProjectElement> {
        
        @Override
        protected void addNotify() {
            setKeys(elements);
        }
        
        @Override
        protected Node[] createNodes(ProjectElement element) {
            return new Node[] {element.createNodeDelegate()};
        }
    }
}