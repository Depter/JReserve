package org.jreserve.project.system.visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import org.netbeans.api.actions.Savable;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CloseConfirmDialog extends JDialog implements ActionListener {
    
    private ListView savableList;
    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton discardAllButton;
    private JButton saveAllButton;

    private final ExplorerManager em = new ExplorerManager();
    private Lookup lookup;
    protected java.util.List<Savable> savables = new ArrayList<Savable>();
    
    public CloseConfirmDialog(Lookup lookup, String title) {
        super(WindowManager.getDefault().getMainWindow(), title, true);
        this.lookup = lookup;
        initComponents();
        centerDialog();
    }
    
    private void initComponents() {
        JPanel panel = new ContentPanel();
        panel.setLayout(new BorderLayout(15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        buttonPanel = new javax.swing.JPanel();
        saveAllButton = new javax.swing.JButton();
        discardAllButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        savableList = new ListView();
        savableList.setBorder(new LineBorder(Color.BLACK, 1, true));
        panel.add(savableList, BorderLayout.CENTER);

        buttonPanel.setLayout(new GridBagLayout());

        saveAllButton.setText(org.openide.util.NbBundle.getMessage(CloseConfirmDialog.class, "LBL.CloseConfirmForm.Button.Save")); // NOI18N
        saveAllButton.addActionListener(this);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.insets = new java.awt.Insets(0, 0, 5, 0);
        buttonPanel.add(saveAllButton, gc);

        discardAllButton.setText(org.openide.util.NbBundle.getMessage(CloseConfirmDialog.class, "LBL.CloseConfirmForm.Button.Discard")); // NOI18N
        discardAllButton.addActionListener(this);
        gc.gridy = 1;
        buttonPanel.add(discardAllButton, gc);
        
        gc = new java.awt.GridBagConstraints();
        gc.gridy = 2;
        gc.fill = java.awt.GridBagConstraints.VERTICAL;
        gc.weighty=1d;
        buttonPanel.add(Box.createVerticalGlue(), gc);

        cancelButton.setText(org.openide.util.NbBundle.getMessage(CloseConfirmDialog.class, "LBL.CloseConfirmForm.Button.Cancel")); // NOI18N
        cancelButton.addActionListener(this);
        gc.gridy = 3;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.weighty=0d;
        buttonPanel.add(cancelButton, gc);

        panel.add(buttonPanel, java.awt.BorderLayout.LINE_END);
        
        JPanel contents = (JPanel) getContentPane();
        contents.setLayout(new BorderLayout());
        contents.add(panel, BorderLayout.CENTER);
        pack();
        setPreferredSize(new Dimension(300, 170));
    }
    
    private void centerDialog() {
        Window owner = getOwner();
        Point p = owner.getLocation();
        Dimension ownerSize = owner.getSize();
        Dimension size = getSize();
        int x = p.x + (ownerSize.width - size.width) / 2;
        int y = p.y + (ownerSize.height - size.height) / 2;
        setLocation(x, y);
    }

    @Override
    public void setVisible(boolean visible) {
        savables = new ArrayList<Savable>(lookup.lookupAll(Savable.class));
        if(savables.isEmpty())
            return;
        em.setRootContext(new AbstractNode(new SavableChildren()));
        super.setVisible(visible);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(saveAllButton == source) {
            save();
        } else if(discardAllButton == source) {
            discard();
        } else if(cancelButton == source) {
            dispose();
        }
    }
    
    private void save() {
        try {
            for(Node node : getSelectedNodes())
                save(((SavableNode) node).savable);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            reinitDialog();
        }
    }
    
    private Node[] getSelectedNodes() {
        Node[] selected = em.getSelectedNodes();
        if(selected.length > 0)
            return selected;
        return em.getRootContext().getChildren().getNodes();
    }
    
    protected void save(Savable savable) throws IOException {
        savable.save();
        savables.remove(savable);
    }
    
    private void reinitDialog() {
        if(savables.isEmpty()) {
            dispose();
        } else {
            em.setRootContext(new AbstractNode(new SavableChildren()));
        }
    }
    
    private void discard() {
        for(Node node : getSelectedNodes())
            discard(((SavableNode)node).savable);
        reinitDialog();
    }
    
    protected void discard(Savable savable) {
        savables.remove(savable);
    }
   
    private class SavableChildren extends Children.Keys<Savable> {

        @Override
        protected void addNotify() {
            setKeys(savables);
        }
        
        @Override
        protected Node[] createNodes(Savable t) {
            return new Node[]{new SavableNode(t)};
        }
    }
    
    private static class SavableNode extends AbstractNode {
        
        private Image img;
        private Savable savable;
        
        private SavableNode(Savable t) {
            super(Children.LEAF, Lookups.singleton(t));
            this.savable = t;
            setDisplayName(t.toString());
            if(t instanceof Icon)
                img = ImageUtilities.icon2Image((Icon) t);
        }

        @Override
        public Image getIcon(int type) {
            if(img != null)
                return img;
            return super.getIcon(type);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }
    
    private class ContentPanel extends JPanel implements ExplorerManager.Provider {
        @Override
        public ExplorerManager getExplorerManager() {
            return em;
        }
    }

}
