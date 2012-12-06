package org.jreserve.resources;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ToolBarToggleButton extends JToggleButton implements MouseListener, ChangeListener {
        
    public ToolBarToggleButton(Icon icon) {
        super(icon);
        getModel().addChangeListener(this);
        initButton();
    }
    
    public ToolBarToggleButton(String text, Icon icon) {
        super(text, icon);
        initButton();
    }

    public ToolBarToggleButton() {
        initButton();
    }

    public ToolBarToggleButton(Action action) {
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
        if(!isSelected())
            setBorderPainted(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!isSelected())
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

    @Override
    public void stateChanged(ChangeEvent e) {
        boolean selected = getModel().isSelected();
        setBorderPainted(selected);
    }
}
