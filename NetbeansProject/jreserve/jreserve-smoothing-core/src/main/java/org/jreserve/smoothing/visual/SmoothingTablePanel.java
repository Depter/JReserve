package org.jreserve.smoothing.visual;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    }
    
    private void initPanel() {
        setLayout(new BorderLayout(5, 5));
        
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(Bundle.LBL_SmoothingTablePanel_Decimals());
        northPanel.add(label, BorderLayout.LINE_START);
        spinner = new DecimalSpinner();
        northPanel.add(spinner, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
        
        table = new SmoothingTable();
        table.setDigits(spinner.getIntValue());
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        table.setDigits(spinner.getIntValue());
    }
    
    public boolean[] getApplied() {
        int size = table.getRowCount();
        boolean[] applied = new boolean[size];
        for(int i=0; i<size; i++)
            applied[i] = table.isApplied(i);
        return applied;
    }
    
    public void setInput(double[] input) {
        table.setInput(input);
    }
    
    public void setSmoothed(double[] smoothed) {
        table.setSmoothed(smoothed);
    }
}
