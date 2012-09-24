package org.jreserve.project.system.newdialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import org.jreserve.project.system.management.ElementCreatorWizard;
import org.jreserve.project.system.management.ElementCreatorWizard.Category;
import org.jreserve.project.system.util.ElementCategoryUtil;
import org.jreserve.resources.LabeledListPanel;
import org.openide.nodes.Children;
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
    
    private static Category lastCategory = null;
    private static ElementCreatorWizard lastWizard = null;
    
    private LabeledListPanel categoryPanel;
    private LabeledListPanel elementPanel;
    private JTextArea descriptionText;
    
    private Result<Category> categoryResult;
    private Result<ElementCreatorWizard> creatorResult;
    
    ElementSelectVisualPanel() {
        initComponents();
        bindPanels();
        categoryPanel.setChilden(ElementCategoryUtil.getChildren());
        initSelectedElementCategory();
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
        
        descriptionText = new JTextArea();
        descriptionText.setEditable(false);
        descriptionText.setColumns(20);
        descriptionText.setRows(5);
        descriptionText.setMinimumSize(new Dimension(0, 100));
        
        panel.add(new JScrollPane(descriptionText), BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);
    }
    
    private void bindPanels() {
        bindCategoryPanel();
        bindCreatorPanel();
    }
    
    private void bindCategoryPanel() {
        categoryResult = categoryPanel.getLookup().lookupResult(Category.class);
        categoryResult.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent le) {
                categorySelected();
            }
        });
    }
    
    private void categorySelected() {
        lastCategory = getElementCategory();
        Children children = lastCategory==null? Children.LEAF : new ElementCategoryChildren(lastCategory);
        elementPanel.setChilden(children);
    }
    
    private Category getElementCategory() {
        return categoryPanel.getLookup().lookup(Category.class);
    }
    
    private void bindCreatorPanel() {
        creatorResult = elementPanel.getLookup().lookupResult(ElementCreatorWizard.class);
        creatorResult.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent le) {
                elementCreatorWizardSelected();
            }
        });
    }
    
    private void elementCreatorWizardSelected() {
        elementCreatorWizardSelected(getElementCreatorWizard());
    }
    
    private void elementCreatorWizardSelected(ElementCreatorWizard wizard) {
        lastWizard = wizard;
        putClientProperty(ElementSelectWizardPanel.ELEMENT_CREATOR_WIZARD, wizard);
        setDescription(wizard);
    }
    
    private ElementCreatorWizard getElementCreatorWizard() {
        return elementPanel.getLookup().lookup(ElementCreatorWizard.class);
    }
    
    private void setDescription(ElementCreatorWizard wizard) {
        String description = wizard==null? null : wizard.getDescription();
        descriptionText.setText(description);
    }
    
    private void initSelectedElementCategory() {
        if(lastCategory == null)
            return;
        categoryPanel.selectNode(lastCategory.name());
        initSelectedWizard();
    }
    
    private void initSelectedWizard() {
        if(lastWizard == null)
            return;
        elementPanel.selectNode(lastWizard.getClass().getName());
        elementCreatorWizardSelected(lastWizard);
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ElementSelectVisualPanel_name();
    }
}
