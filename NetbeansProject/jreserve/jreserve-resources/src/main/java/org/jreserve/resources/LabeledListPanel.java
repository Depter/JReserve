package org.jreserve.resources;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import javax.swing.ActionMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LabeledListPanel extends JPanel implements ExplorerManager.Provider {
        
    private final ExplorerManager em = new ExplorerManager();
    private Lookup lookup;
    private JLabel label;
    private ListView list;

    public LabeledListPanel(String title) {
        initComponents(title);
        em.setRootContext(new AbstractNode(Children.LEAF));
    }

    private void initComponents(String title) {
        setLayout(new BorderLayout());
        initLabel(title);
        initList();
    }

    private void initLabel(String title) {
        label = new JLabel(title);
        add(label, BorderLayout.PAGE_START);
    }

    private void initList() {
        list = new ListView();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        add(list, BorderLayout.CENTER);
        lookup = ExplorerUtils.createLookup(em, new ActionMap());
    }

    public void setChilden(Children children) {
        em.setRootContext(new AbstractNode(children));
    }

    public void selectNode(String name) {
        Node node = em.getRootContext().getChildren().findChild(name);
        if(node != null)
            selectNodes(node);
        else
            selectNodes();
    }

    public void selectNodes(Node... nodes) {
        try {
            em.setSelectedNodes(nodes);
        } catch (PropertyVetoException ex) {
        }
    }

    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
}

