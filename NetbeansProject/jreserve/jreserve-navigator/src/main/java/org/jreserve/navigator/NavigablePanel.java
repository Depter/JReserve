package org.jreserve.navigator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NavigablePanel extends JPanel implements NavigableComponent, ActionListener {

    private final static Color BACKGROUND = new Color(67, 196, 67);
    private final static Color FOREGROUND = Color.WHITE;
    private final static int BORDER_WIDTH = 2;
    
    private NavigableComponent parent;
    private Image icon;
    
    private JPanel contentPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private NavigablePanelButton button;
    
    private Color background = BACKGROUND;
    private Color foreground = FOREGROUND;
    private boolean opened = true;
    
    public NavigablePanel() {
    }
    
    public NavigablePanel(String displayName, Image icon) {
        super.setName(displayName);
        this.icon = icon;
        initPanel();
    }
    
    private void initPanel() {
        setLayout(new BorderLayout());
        initTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setBorder(new LineBorder(background, BORDER_WIDTH, true));
    }
    
    private void initTitlePanel() {
        titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(background);
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx=0d; gc.weighty=0d;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 0, 5);
        
        titleLabel = new JLabel(getName());
        if(icon != null)
            titleLabel.setIcon(new ImageIcon(icon));
        titleLabel.setForeground(foreground);
        Font font = titleLabel.getFont();
        titleLabel.setFont(font.deriveFont(Font.BOLD));
        titlePanel.add(titleLabel, gc);
        
        gc.gridx=1; gc.weightx=1d;
        gc.anchor=GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 0, 0);
        titlePanel.add(Box.createHorizontalGlue(), gc);
        
        gc.gridx=2; gc.weightx=0d;
        button = new NavigablePanelButton();
        button.setForeground(foreground);
        button.addActionListener(this);
        titlePanel.add(button, gc);
        
        titlePanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 2, 1));
    }
    
    public void setContent(JComponent component) {
        contentPanel.removeAll();
        contentPanel.add(component, BorderLayout.CENTER);
    }
    
    @Override
    public void setForeground(Color color) {
        if(titlePanel != null) {
            titleLabel.setForeground(color);
            button.setForeground(color);
        }
        super.setForeground(color);
    }
    
    @Override
    public void setBackground(Color color) {
        if(titlePanel != null)
            titlePanel.setBackground(color);
        setBorder(new LineBorder(color, BORDER_WIDTH, true));
        super.setBackground(color);
    }
    
    @Override
    public Image getIcon() {
        return icon;
    }

    @Override
    public String getDisplayName() {
        return super.getName();
    }

    @Override
    public List<NavigableComponent> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void navigateTo() {
        if(parent != null)
            parent.navigateToChild(this);
    }

    @Override
    public void setParent(NavigableComponent parent) {
        this.parent = parent;
    }

    @Override
    public void navigateToChild(NavigableComponent component) {
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(button == e.getSource()) {
            opened = !opened;
            contentPanel.setVisible(opened);
        }
    }
}
