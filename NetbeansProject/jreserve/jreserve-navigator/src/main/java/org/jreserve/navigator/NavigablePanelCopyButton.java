package org.jreserve.navigator;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.NavigablePanelCopyButton.Copy=Copy to clipboard"
})
public class NavigablePanelCopyButton extends NavigablePanelButton {
    
    public NavigablePanelCopyButton(ActionListener listener) {
        super.addActionListener(listener);
        super.setToolTipText(Bundle.LBL_NavigablePanelCopyButton_Copy());
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

}
