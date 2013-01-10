package org.jreserve.triangle.smoothing.visual;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.SmoothingCreatorPanel.Name=Name:"
})
public abstract class SmoothingCreatorPanel extends JPanel {

    public final static String SMOOTHING_NAME_PROPERTY = "SMOOTHING_NAME";
    public final static String SMOOTHING_APPLIED_CELLS_PROPERTY = "SMOOTHING_APPLIED_CELLS";

    private JTextField nameText;
    private SmoothingTablePanel table;

    protected SmoothingCreatorPanel(int visibleDigits, double[] input) {
        initComponents(visibleDigits, input);
    }
    
    private void initComponents(int visibleDigits, double[] input) {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        setLayout(new BorderLayout(15, 15));
        add(createInputPanel(), BorderLayout.NORTH);
        initTable(visibleDigits, input);
        add(table, BorderLayout.CENTER);
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.BASELINE_LEADING;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        inputPanel.add(new JLabel(Bundle.LBL_SmoothingCreatorPanel_Name()), gc);
        
        nameText = new JTextField();
        nameText.getDocument().addDocumentListener(new NameListener());
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1d; gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        inputPanel.add(nameText, gc);
        
        addUserInputs(inputPanel, new UserInputListener());
        
        return inputPanel;
    }

    protected abstract void addUserInputs(JPanel panel, PropertyChangeListener listener);
    
    private void initTable(int visibleDigits, double[] input) {
        table = new SmoothingTablePanel();
        table.setVisibleDigits(visibleDigits);
        table.setInput(input);
        table.setSmoothed(getSmoothedValues(input));
        table.addChangeListener(new TableListener());
    }
    
    protected abstract double[] getSmoothedValues(double[] input);
    
    public void addPropertyChangeListener(PropertyChangeListener listener, String... properties) {
        for(String property : properties)
            super.addPropertyChangeListener(property, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener, String... properties) {
        for(String property : properties)
            super.removePropertyChangeListener(property, listener);
    }
    
    public String getSmoothingName() {
        return nameText.getText();
    }
    
    public void setSmoothingName(String name) {
        nameText.setText(name);
    }
    
    public boolean hasApplied() {
        return table.hasApplied();
    }
    
    public boolean[] getApplied() {
        return table.getApplied();
    }
    
    private class NameListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            putClientProperty(SMOOTHING_NAME_PROPERTY, nameText.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            putClientProperty(SMOOTHING_NAME_PROPERTY, nameText.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
    
    private class TableListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            putClientProperty(SMOOTHING_APPLIED_CELLS_PROPERTY, table.getApplied());
        }
    }    
    
    private class UserInputListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }
}
