package org.jreserve.triangle.smoothing.geometric;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.triangle.smoothing.visual.SmoothingTablePanel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.GeometricSmoothingCreateDialog.Name=Name:"
})
class GeometricSmoothingCreateDialog extends JPanel {
    
    final static String SMOOTHING_NAME_PROPERTY = "SMOOTHING_NAME";
    final static String SMOOTHING_CELLS_PROPERTY = "SMOOTHING_CELLS";
    
    private GeometricSmoothing smoothing = new GeometricSmoothing();
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private JTextField nameText;
    private SmoothingTablePanel table;
    
    GeometricSmoothingCreateDialog(int visibleDigits, double[] input) {
        initComponents(visibleDigits, input);
    }
    
    private void initComponents(int visibleDigits, double[] input) {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        setLayout(new BorderLayout(15, 15));
        add(createNamePanel(), BorderLayout.NORTH);
        initTable(visibleDigits, input);
        add(table, BorderLayout.CENTER);
    }
    
    private JPanel createNamePanel() {
        JPanel namePanel = new JPanel(new BorderLayout(5, 5));
        namePanel.add(new JLabel(Bundle.LBL_GeometricSmoothingCreateDialog_Name()), BorderLayout.WEST);
        initNameText();
        namePanel.add(nameText, BorderLayout.CENTER);
        return namePanel;
    }
    
    private void initNameText() {
        nameText = new JTextField();
        nameText.getDocument().addDocumentListener(new NameListener());
    }
    
    private void initTable(int visibleDigits, double[] input) {
        table = new SmoothingTablePanel();
        table.setVisibleDigits(visibleDigits);
        table.setInput(input);
        table.setSmoothed(smoothing.smooth(input));
        table.addChangeListener(new TableListener());
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    String getSmoothingName() {
        return nameText.getText();
    }
    
    void setSmoothingName(String name) {
        nameText.setText(name);
    }
    
    boolean hasApplied() {
        return table.hasApplied();
    }
    
    boolean[] getApplied() {
        return table.getApplied();
    }
    
    private class TableListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
    
    private class NameListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
}
