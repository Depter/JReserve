package org.jreserve.navigator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import org.jreserve.navigator.undockabletopcomponent.DockTarget;
import org.jreserve.navigator.undockabletopcomponent.UndockedTopComponent;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NavigablePanel extends JPanel implements NavigableComponent, ActionListener, Lookup.Provider, DockTarget {

    private final static Color BACKGROUND = new Color(67, 196, 67);
    private final static Color FOREGROUND = Color.WHITE;
    private final static int BORDER_WIDTH = 2;
    
    private NavigableComponent parent;
    private Image icon;
    
    private JPanel contentPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    
    private JPanel buttonPanel;
    private NavigablePanelOpenButton openButton;
    private NavigablePanelDockButton dockButton;
    
    private JPanel userButtonPanel;
    private List<JComponent> userButtons = new ArrayList<JComponent>();
    
    private Color background = BACKGROUND;
    private Color foreground = FOREGROUND;
    private boolean opened = true;
    private Lookup lookup;
    
    private JComponent content;
    private boolean docked = true;
    
    private UndockedTopComponent tc = null;
    
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
        userButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        userButtonPanel.setOpaque(false);
        titlePanel.add(userButtonPanel, gc);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        dockButton = new NavigablePanelDockButton();
        dockButton.setForeground(foreground);
        dockButton.addActionListener(this);
        buttonPanel.add(dockButton);
        
        buttonPanel.add(Box.createHorizontalStrut(5));
        openButton = new NavigablePanelOpenButton();
        openButton.setForeground(foreground);
        openButton.addActionListener(this);
        buttonPanel.add(openButton);
        
        gc.gridx=3; gc.weightx=0d;
        titlePanel.add(buttonPanel, gc);
        
        titlePanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 2, 1));
        titlePanel.addMouseListener(new DblClickHandler());
    }
    
    public void setContent(JComponent component) {
        if(content != null)
            throw new IllegalStateException("Content already defined!");
        this.content = component;
        setLookup();
        dock(component);
    }
    
    private void setLookup() {
        if(content instanceof Lookup.Provider)
            lookup = ((Lookup.Provider)content).getLookup();
    }

    public void addUserTitleComponent(JComponent component) {
        component.setBackground(background);
        component.setForeground(foreground);
        userButtonPanel.add(component);
        userButtonPanel.add(Box.createHorizontalStrut(5));
        userButtons.add(component);
        
        userButtonPanel.revalidate();
        titlePanel.revalidate();
    }
    
    public void setDocked(boolean docked) {
        if(this.docked == docked)
            return;
        dockButton.setDocked(docked);
        if(docked) {
            tc.close();
        } else {
            undock();
        }
    }
    
    @Override
    public void dock(JComponent component) {
        if(content != component)
            throw new IllegalArgumentException("Wrong component!");
        this.docked = true;
        dockButton.setDocked(true);
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        
        tc = null;
    }
    
    public void undock() {
        if(content == null)
            throw new IllegalStateException("No component to undock!");
        dockButton.setDocked(false);
        contentPanel.remove(content);
        contentPanel.revalidate();
        docked = false;
        
        tc = UndockedTopComponent.create(getDisplayName(), content, this);
        
    }
    
    @Override
    public void setForeground(Color color) {
        this.foreground = color;
        if(titlePanel != null) {
            titlePanel.setForeground(color);
            titleLabel.setForeground(color);
            openButton.setForeground(color);
            dockButton.setForeground(color);
            setUserComponentsForeground(color);
        }
        super.setForeground(color);
    }
    
    private void setUserComponentsForeground(Color color) {
        for(JComponent comp : userButtons)
            comp.setForeground(color);
    }
    
    @Override
    public void setBackground(Color color) {
        this.background = color;
        if(titlePanel != null) {
            titlePanel.setBackground(color);
            titleLabel.setBackground(color);
            openButton.setBackground(color);
            dockButton.setBackground(color);
            setUserComponentsBackground(color);
        }
        setBorder(new LineBorder(color, BORDER_WIDTH, true));
        super.setBackground(color);
    }
    
    private void setUserComponentsBackground(Color color) {
        for(JComponent comp : userButtons)
            comp.setBackground(color);
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
        if(tc != null) {
            tc.requestActive();
        } else {
            if(parent != null)
                parent.navigateToChild(this);
        }
        setOpened(true);
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
        Object source = e.getSource();
        if(openButton == source)
            setOpened(!opened);
        else if(dockButton == source)
            setDocked(!docked);
    }
    
    public void setOpened(boolean opened) {
        this.opened = opened;
        openButton.setOpened(opened);
        contentPanel.setVisible(opened);
    }

    @Override
    public Lookup getLookup() {
        if(lookup == null)
            return Lookup.EMPTY;
        return lookup;
    }
    
    protected void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }
    
    private class DblClickHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2)
                setOpened(!opened);
        }
    }
}
