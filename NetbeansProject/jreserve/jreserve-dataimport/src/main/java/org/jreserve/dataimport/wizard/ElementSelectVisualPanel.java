package org.jreserve.dataimport.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import org.jreserve.dataimport.DataImportWizard;
import org.jreserve.resources.LabeledListPanel;
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
    "LBL.ElementSelectVisualPanel.name=Import method"
})
class ElementSelectVisualPanel extends JPanel {

    private DataImportWizard lastWizard;
    
    private LabeledListPanel wizardPanel;
    private JTextArea descriptionText;
    private Result<DataImportWizard> wizardResult;
    private DataImportWizardChildren wizards = new DataImportWizardChildren();
    
    ElementSelectVisualPanel() {
        initComponents();
        bindWizardPanel();
        wizardPanel.setChilden(new DataImportWizardChildren());
        initSelectedWizard();
        setName(Bundle.LBL_ElementSelectVisualPanel_name());
    }
    
    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new BorderLayout(15, 15));
        initCategoryPanel();
        initInfoText();
    }
    
    private void initCategoryPanel() {
        wizardPanel = new LabeledListPanel("Import method:");
        Dimension size = new Dimension(150, 300);
        wizardPanel.setPreferredSize(size);
        wizardPanel.setMinimumSize(size);
        add(wizardPanel, BorderLayout.CENTER);
    }
    
    private void initInfoText() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Description"), BorderLayout.PAGE_START);
        
        descriptionText = new JTextArea();
        descriptionText.setEditable(false);
        descriptionText.setColumns(20);
        descriptionText.setRows(5);
        descriptionText.setMinimumSize(new Dimension(0, 100));
        
        panel.add(new JScrollPane(descriptionText), BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);
    }
    
    private void bindWizardPanel() {
        wizardResult = wizardPanel.getLookup().lookupResult(DataImportWizard.class);
        wizardResult.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent le) {
                wizardSelected();
            }
        });
    }
    
    private void wizardSelected() {
        wizardSelected(getWizard());
    }
    
    private void wizardSelected(DataImportWizard wizard) {
        lastWizard = wizard;
        putClientProperty(ElementSelectWizardPanel.DATA_IMPORT_WIZARD, wizard);
        setDescription(wizard);
    }
    
    private DataImportWizard getWizard() {
        return wizardPanel.getLookup().lookup(DataImportWizard.class);
    }
    
    private void setDescription(DataImportWizard wizard) {
        String description = wizard==null? null : wizard.getDescription();
        descriptionText.setText(description);
    }
    
    private void initSelectedWizard() {
        if(lastWizard == null)
            return;
        wizardPanel.selectNodes(wizards.getNode(lastWizard));
        wizardSelected();
    }
    
}
