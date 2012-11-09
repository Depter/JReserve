package org.jreserve.navigator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author Peter Decsi
 */
class NavigablePanelButton extends JLabel implements MouseListener {
    
    private final static Color FOREGROUND = Color.WHITE;
    private final static int BORDER_WIDTH = 1;
    private final static Dimension SIZE = new Dimension(16, 16);
    
    private Border border = BorderFactory.createLineBorder(FOREGROUND, BORDER_WIDTH);
    private Border emptyBorder = BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
    private boolean opened = true;
    protected boolean pressed = false;
    
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private String actionCommand = "NavigablePanelButton";
    
    NavigablePanelButton() {
        super("");
        setVerticalTextPosition(JLabel.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        initButton();
    }

    private void initButton() {
        setOpaque(false);
        super.setForeground(FOREGROUND);
        super.setBorder(emptyBorder);
        addMouseListener(this);
        setRequestFocusEnabled(false);
    }   
    
    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        border = BorderFactory.createLineBorder(color, BORDER_WIDTH);
    }

    @Override
    public boolean isFocusTraversable() {
        return isRequestFocusEnabled();
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {
        opened = !opened;
        fireActionEvent();
    }
    
    @Override 
    public void mousePressed(MouseEvent e) {
        pressed = true;
        super.repaint();
    }
    
    @Override 
    public void mouseReleased(MouseEvent e) {
        pressed = false;
        super.repaint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(border);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(emptyBorder);
    }

    @Override
    public Dimension getSize() {
        return SIZE;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return SIZE;
    }
    
    @Override
    public Dimension getMaximumSize() {
        return SIZE;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }
    
    public void addActionListener(ActionListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireActionEvent() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_FIRST, actionCommand);
        for(ActionListener listener : new ArrayList<ActionListener>(listeners))
            listener.actionPerformed(evt);
    }
    
    @Override
    public void updateUI() {
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintBorder(g);
    }
}