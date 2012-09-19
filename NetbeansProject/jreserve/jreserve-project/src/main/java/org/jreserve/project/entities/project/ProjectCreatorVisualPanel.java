package org.jreserve.project.entities.project;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.system.RootElement;
import org.jreserve.project.system.visual.ProjectElementComboBox;
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
    "LBL.ProjectCreatorVisualPanel.title=Create Project",
    "LBL.ProjectCreatorVisualPanel.lob=Line of business:",
    "LBL.ProjectCreatorVisualPanel.caimtype=Claim type:",
    "LBL.ProjectCreatorVisualPanel.name=Name:",
    "LBL.ProjectCreatorVisualPanel.description=Description:"
})
class ProjectCreatorVisualPanel extends JPanel {

    private ProjectElementComboBox lobCombo;
    private Result<org.jreserve.project.system.ProjectElement> lobResult;
    private ProjectElementComboBox claimTypeCombo;
    private Result<org.jreserve.project.system.ProjectElement> ctResult;
    private JTextField nameText;
    private JTextArea descriptionText;
    
    ProjectCreatorVisualPanel() {
        initComponents();
        addListeners();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        add(getInputPanel(), BorderLayout.PAGE_START);
        add(getDescriptionPanel(), BorderLayout.CENTER);
    }
    
    private JPanel getInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0d; gc.weighty = 0d;
        gc.gridx=0; gc.gridy=0;
        gc.insets = new Insets(0, 0, 5, 5);
        JLabel label = new JLabel(Bundle.LBL_ProjectCreatorVisualPanel_lob());
        panel.add(label, gc);
        
        gc.gridy=1;
        label = new JLabel(Bundle.LBL_ProjectCreatorVisualPanel_caimtype());
        panel.add(label, gc);
        
        gc.gridy=2;
        gc.insets = new Insets(0, 0, 0, 5);
        label = new JLabel(Bundle.LBL_ProjectCreatorVisualPanel_name());
        panel.add(label, gc);
        
        gc.gridx=1; gc.gridy=0;
        gc.insets = new Insets(0, 0, 5, 0);
        lobCombo = new ProjectElementComboBox(getLoBElements());
        lobCombo.setPreferredSize(new Dimension(150, 20));
        panel.add(lobCombo, gc);
        
        gc.gridy=1;
        claimTypeCombo = new ProjectElementComboBox();
        claimTypeCombo.setPreferredSize(new Dimension(150, 20));
        panel.add(claimTypeCombo, gc);
        
        gc.gridy=2;
        gc.insets = new Insets(0, 0, 0, 0);
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(150, 20));
        panel.add(nameText, gc);
        
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1d; gc.weighty = 1d;
        gc.gridx=2; gc.gridy=0;
        gc.gridheight=3;
        panel.add(Box.createGlue(), gc);
        
        return panel;
    }
    
    private List<org.jreserve.project.system.ProjectElement> getLoBElements() {
        return RootElement.getDefault().getChildren(LoB.class);
    }
    
    private JPanel getDescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(Bundle.LBL_ProjectCreatorVisualPanel_description());
        panel.add(label, BorderLayout.PAGE_START);
        
        descriptionText = new JTextArea();
        panel.add(new JScrollPane(descriptionText), BorderLayout.CENTER);
        return panel;
    }
    
    private void addListeners() {
        addLoBListener();
        addClaimTypeListener();
        new NameListener(nameText, ProjectCreatorWizardPanel.NAME_VALUE);
        new NameListener(descriptionText, ProjectCreatorWizardPanel.DESCRIPTION_VALUE);
    }

    private void addLoBListener() {
        Lookup lkp = lobCombo.getLookup();
        lobResult = lkp.lookupResult(org.jreserve.project.system.ProjectElement.class);
        lobResult.addLookupListener(new LoBListener());
    }

    private void addClaimTypeListener() {
        Lookup lkp = claimTypeCombo.getLookup();
        ctResult = lkp.lookupResult(org.jreserve.project.system.ProjectElement.class);
        ctResult.addLookupListener(new ClaimTypeListener());
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ProjectCreatorVisualPanel_title();
    }
    
    void setClaimType(ClaimType ct) {
        setLoB(ct.getLoB());
        claimTypeCombo.setSelectedItem(ct);
    }
    
    void setLoB(LoB lob) {
        lobCombo.setSelectedItem(lob);
    }
    
    private <T> T getResult(Result<T> result) {
        Object[] elements = result.allInstances().toArray();
        if(elements.length == 0)
            return null;
        return (T) elements[0];
    }
    
    private class LoBListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            org.jreserve.project.system.ProjectElement element = getResult(lobResult);
            setClaimTypes(element);
            putClientProperty(ProjectCreatorWizardPanel.LOB_VALUE, element.getValue());
        }
    
        private void setClaimTypes(org.jreserve.project.system.ProjectElement element) {
            if(element == null)
                claimTypeCombo.setElements(new ArrayList<org.jreserve.project.system.ProjectElement>());
            else
                claimTypeCombo.setElements(element.getChildren(ClaimType.class));
            putClientProperty(ProjectCreatorWizardPanel.CLAIM_TYPE_VALUE, null);
            putClientProperty(ProjectCreatorWizardPanel.PROJECT_ELEMENT_VALUE, null);
        }
    }
    
    private class ClaimTypeListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            org.jreserve.project.system.ProjectElement element = getResult(ctResult);
            if(element == null)
                removeClaimTypeProperties();
            else
                setClaimTypeProperties(element);
        }
    
        private void removeClaimTypeProperties() {
            putClientProperty(ProjectCreatorWizardPanel.CLAIM_TYPE_VALUE, null);
            putClientProperty(ProjectCreatorWizardPanel.PROJECT_ELEMENT_VALUE, null);
        }
        
        private void setClaimTypeProperties(org.jreserve.project.system.ProjectElement element) {
            putClientProperty(ProjectCreatorWizardPanel.CLAIM_TYPE_VALUE, element.getValue());
            putClientProperty(ProjectCreatorWizardPanel.PROJECT_ELEMENT_VALUE, element);
        }
    }
    
    private class NameListener implements DocumentListener {

        private JTextComponent component;
        private String propertyName;
        
        private NameListener(JTextComponent component, String propertyName) {
            this.propertyName = propertyName;
            this.component = component;
            this.component.getDocument().addDocumentListener(this);
        }
        
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
            String text = component.getText();
            if(text == null || text.trim().length() == 0)
                text = null;
            putClientProperty(propertyName, text==null? null : text.trim());
        }
    }
    
}
