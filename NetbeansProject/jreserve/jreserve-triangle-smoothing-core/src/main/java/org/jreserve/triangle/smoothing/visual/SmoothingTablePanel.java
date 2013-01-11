package org.jreserve.triangle.smoothing.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.localesettings.util.DecimalSpinner;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SmoothingTablePanel.Decimals=Decimals:"
})
public class SmoothingTablePanel extends JPanel implements ChangeListener {
    
    private DecimalSpinner spinner;
    private SmoothingTable table;
    
    public SmoothingTablePanel() {
        initPanel();
        setPreferredSize(new java.awt.Dimension(250, 150));
    }
    
    private void initPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(Bundle.LBL_SmoothingTablePanel_Decimals());
        northPanel.add(label, BorderLayout.LINE_START);
        spinner = new DecimalSpinner();
        spinner.addChangeListener(this);
        northPanel.add(spinner, BorderLayout.CENTER);
        northPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(northPanel, BorderLayout.NORTH);
        
        table = new SmoothingTable();
        table.setDigits(spinner.getIntValue());
        table.setFillsViewportHeight(true);
        table.setEventSource(this);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        add(scroll, BorderLayout.CENTER);
        setBorder(new LineBorder(Color.BLACK, 1, true));
    }

    public void setVisibleDigits(int visibleDigits) {
        if(visibleDigits < 0) 
            visibleDigits = 0;
        spinner.setValue(visibleDigits);
        table.setDigits(visibleDigits);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        table.setDigits(spinner.getIntValue());
    }
    
    public boolean hasApplied() {
        return table.hasApplied();
    }
    
    public boolean[] getApplied() {
        int size = table.getRowCount();
        boolean[] applied = new boolean[size];
        for(int i=0; i<size; i++)
            applied[i] = table.isApplied(i);
        return applied;
    }
    
    public void setInputValues(double[] input) {
        table.setInputValues(input);
    }
    
    public double[] getInputValues() {
        return table.getInputValues();
    }
    
    public void setSmoothedValues(double[] smoothed) {
        table.setSmoothedValues(smoothed);
    }
    
    public void addChangeListener(ChangeListener listener) {
        table.addChangeListener(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        table.removeChangeListener(listener);
    }
    
    void setEventSource(Object source) {
        table.setEventSource(source);
    }
}
