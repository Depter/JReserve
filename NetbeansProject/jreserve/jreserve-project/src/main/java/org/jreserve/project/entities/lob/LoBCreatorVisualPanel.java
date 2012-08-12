package org.jreserve.project.entities.lob;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_LoBCreatorVisualPanel.title=Create LoB",
    "LBL_LoBCreatorVisualPanel.name=Name:"
})
class LoBCreatorVisualPanel extends JPanel {
    
    private JLabel nameLabel;
    private JTextField nameText;

    private DocumentListener listener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setNameProperty();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setNameProperty();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setNameProperty();
        }
    };
    
    LoBCreatorVisualPanel() {
        initComponents();
        nameText.getDocument().addDocumentListener(listener);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(getNamePanel(), BorderLayout.PAGE_START);
        add(new JPanel(), BorderLayout.CENTER);
    }
    
    private JPanel getNamePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        nameLabel = new JLabel(Bundle.LBL_LoBCreatorVisualPanel_name());
        panel.add(nameLabel, BorderLayout.LINE_START);
        
        nameText = new JTextField();
        panel.add(nameText, BorderLayout.CENTER);
        return panel;
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_LoBCreatorVisualPanel_title();
    }
    
    private void setNameProperty() {
        putClientProperty(LoBCreatorWizardPanel.LOB_NAME, getLoBName());
    }
    
    private String getLoBName() {
        String name = nameText.getText();
        if(name==null || name.trim().length()==0)
            return null;
        return name.trim();
    }
}
