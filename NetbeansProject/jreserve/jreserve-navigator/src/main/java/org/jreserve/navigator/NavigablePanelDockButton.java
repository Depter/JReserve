package org.jreserve.navigator;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.NavigablePanelDockButton.Dock=Dock",
    "LBL.NavigablePanelDockButton.UnDock=Float"
})
class NavigablePanelDockButton extends NavigablePanelButton {
    
    private boolean docked = true;
    
    public void setDocked(boolean docked) {
        this.docked = docked;
        super.repaint();
        setToolTip();
    }
    
    private void setToolTip() {
        if(docked)
            setToolTipText(Bundle.LBL_NavigablePanelDockButton_UnDock());
        else
            setToolTipText(Bundle.LBL_NavigablePanelDockButton_Dock());
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {
        docked = !docked;
        setToolTip();
        super.mouseClicked(e);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(super.getForeground());
        g2.setStroke(new BasicStroke(1));
        
        if(pressed)
            g2.translate(1, 1);
        
        g2.fillRect(2, 8, 6, 6);
        g2.drawLine(9, 6, 13, 2);
        
        if(docked) {
            paintDocked(g2);
        } else {
            paintUndocked(g2);
        }
        
        g2.dispose();
    }    
    
    private void paintDocked(Graphics2D g2) {
        g2.drawLine(11, 2, 13, 2);
        g2.drawLine(13, 2, 13, 4);
    }
    
    private void paintUndocked(Graphics2D g2) {
        g2.drawLine(9, 4, 9, 6);
        g2.drawLine(9, 6, 12, 6);
    }
}
