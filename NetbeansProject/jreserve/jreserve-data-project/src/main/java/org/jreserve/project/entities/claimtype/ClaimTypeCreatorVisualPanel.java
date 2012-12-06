package org.jreserve.project.entities.claimtype;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.RootElement;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ChoiceView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL_ClaimTypeCreatorVisualPanel.title=Create Claim Type",
    "LBL_ClaimTypeCreatorVisualPanel.lob=LoB:",
    "LBL_ClaimTypeCreatorVisualPanel.name=Name:"
})
class ClaimTypeCreatorVisualPanel extends JPanel implements ExplorerManager.Provider {
    
    private final static int MARGIN = 15;
    
    private final ExplorerManager em = new ExplorerManager();
    private ChoiceView lobCombo;
    private JTextField nameText;
    
    private Lookup lookup;
    private Result<LoB> selection;
    
    ClaimTypeCreatorVisualPanel() {
        initContents();
        initExplorerManager();
    }

    private void initContents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0d; gc.weighty = 0d;
        gc.insets = new Insets(0, 0, MARGIN, 5);
        gc.gridx = 0; gc.gridy = 0;
        add(new JLabel(Bundle.LBL_ClaimTypeCreatorVisualPanel_lob()), gc);
        
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 0, 5);
        add(new JLabel(Bundle.LBL_ClaimTypeCreatorVisualPanel_name()), gc);
        
        gc.gridx = 1; gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, MARGIN, 0);
        gc.weightx = 1d;
        lobCombo = new ChoiceView();
        lobCombo.setEditable(false);
        add(lobCombo, gc);
        
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        nameText = new JTextField(20);
        nameText.getDocument().addDocumentListener(new NameListener());
        add(nameText, gc);
        
        gc.gridx = 0; gc.gridy = 2;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1d;
        add(Box.createVerticalBox(), gc);
        
        setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
    }
    
    private void initExplorerManager() {
        em.setRootContext(new AbstractNode(new LoBChildren()));
        em.getRootContext().setDisplayName("ROOT");
        lookup = ExplorerUtils.createLookup(em, new ActionMap());
        selection = lookup.lookupResult(LoB.class);
        selection.addLookupListener(new LoBListener());
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ClaimTypeCreatorVisualPanel_title();
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    void setLoB(LoB lob) {
        Node node = getNodeForLoB(lob);
        try {
            if(node != null)
                em.setSelectedNodes(new Node[]{node});
        } catch (Exception ex) {}
    }
    
    private Node getNodeForLoB(LoB lob) {
        for(Node node : em.getRootContext().getChildren().getNodes())
            if(containsLoB(node, lob))
                return node;
        return null;
    }
    
    private boolean containsLoB(Node node, LoB lob) {
        LoB n = node.getLookup().lookup(LoB.class);
        return n!=null && lob.equals(n);
    }
    
    private class LoBChildren extends Children.Keys<ProjectElement> {
        
        LoBChildren() {
        }
        
        @Override
        protected void addNotify() {
            List<ProjectElement> lobs = RootElement.getDefault().getChildren(LoB.class);
            super.setKeys(lobs);
        }
        
        @Override
        protected Node[] createNodes(ProjectElement t) {
            return new Node[]{t.createNodeDelegate()};
        }
    }
    
    private class LoBListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            putClientProperty(ClaimTypeCreatorWizardPanel.LOB_VALUE, 
                    lookup.lookup(LoB.class));
            putClientProperty(ClaimTypeCreatorWizardPanel.PROJECT_ELEMENT_VALUE, 
                    lookup.lookup(ProjectElement.class));
        }
    }
    
    private class NameListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            nameChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            nameChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            nameChanged();
        }
    
        private void nameChanged() {
            String name = nameText.getText();
            if(name == null || name.trim().length() == 0)
                name = null;
            putClientProperty(ClaimTypeCreatorWizardPanel.NAME_VALUE, name==null? null : name.trim());
        }
    }
}
