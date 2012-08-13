package org.jreserve.project.system.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.Deletable;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL_DeleteDialog.title=Delete",
    "LBL_DeleteDialog.button.delete=Delete",
    "LBL_DeleteDialog.button.deleteall=Delete All",
    "LBL_DeleteDialog.button.discardall=Discard All",
    "LBL_DeleteDialog.button.cancel=Cancel"
})
class DeleteDialog extends JDialog implements ExplorerManager.Provider {

    private final static int MARGIN = 15;
    private final ExplorerManager em = new ExplorerManager();
    
    private ListView listView;
    private JButton delete = new JButton(Bundle.LBL_DeleteDialog_button_delete());
    private JButton deleteAll = new JButton(Bundle.LBL_DeleteDialog_button_deleteall());
    private JButton discardAll = new JButton(Bundle.LBL_DeleteDialog_button_discardall());
    private JButton cancel = new JButton(Bundle.LBL_DeleteDialog_button_cancel());
    
    DeleteDialog(List<Deletable> context) {
        super(WindowManager.getDefault().getMainWindow(), Bundle.LBL_DeleteDialog_title());
        initContents(context);
        centerDialog();
    }
    
    private void initContents(List<Deletable> context) {
        JPanel content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout(MARGIN, MARGIN));
        content.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
        content.add(createListView(context), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.LINE_END);
        pack();
    }
    
    private ListView createListView(List<Deletable> context) {
        listView = new ListView();
        listView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        listView.setPreferredSize(new Dimension(200, 150));
        listView.setMinimumSize(new Dimension(200, 150));
        em.setRootContext(new AbstractNode(new DeleteChildren(context)));
        return listView;
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
        gc.gridy = 1;
        gc.insets = new Insets(5, 0, 0, 0);
        panel.add(deleteAll, gc);
        gc.gridy = 2;
        panel.add(discardAll, gc);
        gc.gridy = 4;
        panel.add(cancel, gc);
        
        gc.gridy = 3; gc.weighty = 1d;
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

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    private static class DeleteChildren extends Children.Keys<Deletable> {
        
        private List<Deletable> children = new ArrayList<Deletable>();
        
        DeleteChildren(List<Deletable> children) {
            this.children.addAll(children);
        }
        
        @Override
        protected void addNotify() {
            setKeys(children);
        }
        
        @Override
        protected Node[] createNodes(Deletable t) {
            return new Node[]{t.getNode()};
        }
    
    }
}
