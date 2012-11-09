package org.jreserve.navigator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import org.jreserve.navigator.undockabletopcomponent.DockTarget;
import org.jreserve.navigator.undockabletopcomponent.UndockDialog;
import org.jreserve.navigator.undockabletopcomponent.UndockedTopComponent;
import org.openide.util.Exceptions;
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
    private NavigablePanelOpenButton openButton;
    private NavigablePanelDockButton dockButton;
    
    private Color background = BACKGROUND;
    private Color foreground = FOREGROUND;
    private boolean opened = true;
    private Lookup lookup;
    
    private JComponent content;
    private boolean docked = true;
    
    //private UndockedTopComponent tc = null;
    private Dialog undockedDialog = null;
    
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
        dockButton = new NavigablePanelDockButton();
        dockButton.setForeground(foreground);
        dockButton.addActionListener(this);
        titlePanel.add(dockButton, gc);
        
        gc.gridx=3;
        titlePanel.add(Box.createHorizontalStrut(5), gc);
        
        gc.gridx=4;
        openButton = new NavigablePanelOpenButton();
        openButton.setForeground(foreground);
        openButton.addActionListener(this);
        titlePanel.add(openButton, gc);
        
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

    public void setDocked(boolean docked) {
        if(this.docked == docked)
            return;
        dockButton.setDocked(docked);
        if(docked) {
            undockedDialog.dispose();
            //tc.close();
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
        
        undockedDialog = null;
        //tc = null;
    }
    
    public void undock() {
        if(content == null)
            throw new IllegalStateException("No component to undock!");
        dockButton.setDocked(false);
        contentPanel.remove(content);
        contentPanel.revalidate();
        docked = false;
        
        undockedDialog = UndockDialog.createDialog(getDisplayName(), content, this);
        undockedDialog.setVisible(true);
        //tc = UndockedTopComponent.create(getDisplayName(), content, this);
        
    }
    
    @Override
    public void setForeground(Color color) {
        if(titlePanel != null) {
            titleLabel.setForeground(color);
            openButton.setForeground(color);
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
