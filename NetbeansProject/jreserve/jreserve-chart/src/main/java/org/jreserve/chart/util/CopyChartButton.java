package org.jreserve.chart.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartPanel;
import org.jreserve.navigator.NavigablePanelButton;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.CopyChartButton.Tooltip=Copy to clipboard"
})
public class CopyChartButton extends NavigablePanelButton implements ActionListener {
    
    private ChartPanel panel;
    
    public CopyChartButton(ChartPanel panel) {
        this.panel = panel;
        super.addActionListener(this);
        super.setToolTipText(Bundle.LBL_CopyChartButton_Tooltip());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(super.getForeground());
        g2.setStroke(new BasicStroke(1));
        
        if(pressed)
            g2.translate(1, 1);
        
        g2.fillRect(2, 5, 3, 9);
        g2.fillRect(5, 12, 4, 2);
        g2.fillRect(6, 2, 8, 9);
        
        g2.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.doCopy();
    }
}
