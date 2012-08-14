package org.jreserve.project.system.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.*;
import org.jreserve.project.system.management.Deletable;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL_DeleteDialog.title=Delete",
    "LBL_DeleteDialog.button.delete=Delete",
    "LBL_DeleteDialog.button.deleteall=Delete All",
    "LBL_DeleteDialog.button.cancel=Cancel"
})
class DeleteDialog extends JDialog implements ExplorerManager.Provider {

    private final static int MARGIN = 15;
    private final ExplorerManager em = new ExplorerManager();
    
    private final List<Deletable> context;
    private final DeleteChildren children;
    private ListView listView;
    private JButton delete = new JButton(Bundle.LBL_DeleteDialog_button_delete());
    private JButton deleteAll = new JButton(Bundle.LBL_DeleteDialog_button_deleteall());
    private JButton cancel = new JButton(Bundle.LBL_DeleteDialog_button_cancel());
    private Lookup lookup;
    private Result<Deletable> selection;
    private JProgressBar pBar = new JProgressBar();
    
    DeleteDialog(List<Deletable> context) {
        super(WindowManager.getDefault().getMainWindow(), Bundle.LBL_DeleteDialog_title());
        this.context = new ArrayList<Deletable>(context);
        children = new DeleteChildren();
        initContents();
        centerDialog();
        addListeners();
    }
    
    private void initContents() {
        JPanel content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout(MARGIN, MARGIN));
        content.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
        content.add(createListView(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.LINE_END);
        content.add(pBar, BorderLayout.PAGE_END);
        pBar.setVisible(false);
        pack();
    }
    
    private ListView createListView() {
        listView = new ListView();
        listView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        listView.setPreferredSize(new Dimension(200, 120));
        listView.setMinimumSize(new Dimension(200, 120));
        initExplorerManager();
        return listView;
    }
    
    private void initExplorerManager() {
        em.setRootContext(new AbstractNode(children));
        lookup = ExplorerUtils.createLookup(em, new ActionMap());
        selection = lookup.lookupResult(Deletable.class);
        selection.addLookupListener(new SelectionListener());
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 1d;
        gc.weighty = 0d;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0;
        panel.add(delete, gc);
        delete.setEnabled(false);
        
        gc.gridy = 1;
        gc.insets = new Insets(5, 0, 0, 0);
        panel.add(deleteAll, gc);
        
        gc.gridy = 3;
        panel.add(cancel, gc);
        
        gc.gridy = 2; gc.weighty = 1d;
        gc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createHorizontalBox(), gc);
        
        return panel;
    }
    
    private void centerDialog() {
         Point location = getParent().getLocation();
         Dimension size = getParent().getSize();
         Dimension mySize = getSize();
         
         int x = location.x + (size.width - mySize.width) / 2;
         int y = location.y + (size.height - mySize.height) / 2;
         setLocation(x, y);
    }
    
    private void addListeners() {
        ActionListener listener = new ButtonListener();
        delete.addActionListener(listener);
        deleteAll.addActionListener(listener);
        cancel.addActionListener(listener);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    private void delete(Collection<? extends Deletable> deletables) {
        setWorkingStatus(true);
        new DeleteWorker(deletables).execute();
    }
    
    private void setWorkingStatus(boolean isWorking) {
        setDefaultCloseOperation(isWorking? DO_NOTHING_ON_CLOSE : DISPOSE_ON_CLOSE);
        delete.setEnabled(!isWorking);
        deleteAll.setEnabled(!isWorking);
        cancel.setEnabled(!isWorking);
        pBar.setVisible(isWorking);
        pBar.setIndeterminate(isWorking);
    }
    
    private class DeleteChildren extends Children.Keys<Deletable> {
        
        DeleteChildren() {
        }
        
        @Override
        protected void addNotify() {
            setKeys(context);
        }
        
        private void refreshKeys() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    synchronized(context) {
                        setKeys(context);
                    }
                }
            });
        }
        
        @Override
        protected Node[] createNodes(Deletable t) {
            return new Node[]{new DeletableNode(t)};
        }
    }
    
    private static class DeletableNode extends AbstractNode {
    
        private Node nodeDelegate;
        
        private DeletableNode(Deletable deletable) {
            super(Children.LEAF, Lookups.singleton(deletable));
            nodeDelegate = deletable.getNode();
        }

        @Override
        public Image getIcon(int type) {
            return nodeDelegate.getIcon(type);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return nodeDelegate.getOpenedIcon(type);
        }

        @Override
        public String getDisplayName() {
            return nodeDelegate.getDisplayName();
        }
    }
    
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(source == cancel) {
                dispose();
            } else if(source == delete) {
                delete(selection.allInstances());
            } else if(source == deleteAll) {
                delete(context);
            }
        }
    }
    
    private class SelectionListener implements LookupListener {
        @Override
        public void resultChanged(LookupEvent le) {
            int count = selection.allInstances().size();
            delete.setEnabled(count > 0);
        }
    }
    
    private class DeleteWorker extends SwingWorker<Void, Void> {
        
        private final List<Deletable> deletables;
        
        private DeleteWorker(Collection<? extends Deletable> deletables) {
            this.deletables = new ArrayList<Deletable>(deletables);
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            for(Deletable deletable : deletables)
                delete(deletable);
            return null;
        }
        
        private void delete(Deletable deletable) throws Exception {
            deletable.delete();
            removeNode(deletable);
        }
        
        private void removeNode(Deletable deletable) {
            synchronized(context) {
                context.remove(deletable);
            }
            children.refreshKeys();
        }
        
        @Override
        protected void done() {
            setWorkingStatus(false);
            if(context.isEmpty())
                dispose();
        }
    }
}
