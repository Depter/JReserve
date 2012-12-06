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
    "LBL.NavigablePanelOpenButton.Open=Show",
    "LBL.NavigablePanelOpenButton.Close=Hide"
})
class NavigablePanelOpenButton extends NavigablePanelButton {
    
    private boolean opened = true;
    
    public void setOpened(boolean opened) {
        this.opened = opened;
        super.repaint();
        setToolTip();
    }
    
    private void setToolTip() {
        if(opened)
            setToolTipText(Bundle.LBL_NavigablePanelOpenButton_Close());
        else
            setToolTipText(Bundle.LBL_NavigablePanelOpenButton_Open());
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {
        opened = !opened;
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
        
        g2.drawLine(3, 7, 12, 7);
        g2.drawLine(3, 8, 12, 8);
        if(!opened) {
            g2.drawLine(7, 3, 7, 12);
            g2.drawLine(8, 3, 8, 12);
        }
        g2.dispose();
    }    
}
