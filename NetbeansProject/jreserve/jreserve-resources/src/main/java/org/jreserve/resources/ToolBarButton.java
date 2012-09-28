package org.jreserve.resources;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ToolBarButton extends JButton implements MouseListener {
        
    public ToolBarButton(Icon icon) {
        super(icon);
        initButton();
    }
    
    public ToolBarButton(String text, Icon icon) {
        super(text, icon);
        initButton();
    }

    public ToolBarButton() {
        initButton();
    }

    public ToolBarButton(Action action) {
        super(action);
        super.setText(null);
        initButton();
    }

    private void initButton() {
        addMouseListener(this);
        setRequestFocusEnabled(false);
        setBorderPainted(false);
        setMargin(new Insets(1, 1, 1, 1));
    }

    @Override
    public boolean isFocusTraversable() {
        return isRequestFocusEnabled();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorderPainted(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorderPainted(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
    
}
