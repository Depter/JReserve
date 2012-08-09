package org.jreserve.project.system.newdialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.border.LineBorder;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL_ElementSelectVisualPanel_name=Select element",
    "LBL_ElementSelectVisualPanel_category=Category:",
    "LBL_ElementSelectVisualPanel_element=Element:",
    "LBL_ElementSelectVisualPanel_description=Description:"
})
class ElementSelectVisualPanel extends JPanel {
    
    private LabeledListPanel categoryPanel;
    private LabeledListPanel elementPanel;
    private JTextArea infoText;
    
    private Result<ElementCategory> categoryResult;
    
    ElementSelectVisualPanel() {
        initComponents();
        bindPanels();
        categoryPanel.setChilden(ElementCategory.getChildren());
    }
    
    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new BorderLayout(15, 15));
        initCategoryPanel();
        initElementPanel();
        initInfoText();
    }
    
    private void initCategoryPanel() {
        categoryPanel = new LabeledListPanel(Bundle.LBL_ElementSelectVisualPanel_category());
        Dimension size = new Dimension(150, 300);
        categoryPanel.setPreferredSize(size);
        categoryPanel.setMinimumSize(size);
        add(categoryPanel, BorderLayout.LINE_START);
    }
    
    private void initElementPanel() {
        elementPanel = new LabeledListPanel(Bundle.LBL_ElementSelectVisualPanel_element());
        Dimension size = new Dimension(400, 300);
        elementPanel.setPreferredSize(size);
        add(elementPanel, BorderLayout.CENTER);
    }
    
    private void initInfoText() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(Bundle.LBL_ElementSelectVisualPanel_description()), BorderLayout.PAGE_START);
        
        infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setColumns(20);
        infoText.setRows(5);
        infoText.setMinimumSize(new Dimension(0, 100));
        
        panel.add(new JScrollPane(infoText), BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);
    }
    
    private void bindPanels() {
        bindCategoryPanel();
    }
    
    private void bindCategoryPanel() {
        categoryResult = categoryPanel.getLookup().lookupResult(ElementCategory.class);
        categoryResult.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent le) {
                categorySelected();
            }
        });
    }
    
    private void categorySelected() {
        ElementCategory category = getElementCategory();
        Children children = category==null? Children.LEAF : new ElementCategoryChildren(category);
        elementPanel.setChilden(children);
    }
    
    private ElementCategory getElementCategory() {
        return categoryPanel.getLookup().lookup(ElementCategory.class);
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ElementSelectVisualPanel_name();
    }
    
    private static class LabeledListPanel extends JPanel implements ExplorerManager.Provider {
        
        private final ExplorerManager em = new ExplorerManager();
        private Lookup lookup;
        
        private JLabel label;
        private ListView list;

        LabeledListPanel(String title) {
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
        
        void setChilden(Children children) {
            em.setRootContext(new AbstractNode(children));
        }
        
        Lookup getLookup() {
            return lookup;
        }
        
        @Override
        public ExplorerManager getExplorerManager() {
            return em;
        }
    
    }

}
