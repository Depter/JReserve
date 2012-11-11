package org.jreserve.navigator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 */
public class NavigableTopComponent extends TopComponent implements NavigableComponent {

    private final static int BORDER_WIDHT = 15;
    private final static int COMPONENT_SPACING = 5;
    private final static int SCROLL_INCREMENT = 20;
    
    private Lookup lookup;
    private NavigableComponent parent;
    private List<NavigableComponent> components = new ArrayList<NavigableComponent>();
    
    protected JPanel contentPanel;
    protected JScrollPane scroller;
    
    public NavigableTopComponent(List<NavigableComponent> components) {
        addComponents(components);
        createLookup();
        initPanel();
    }
    
    private void addComponents(List<NavigableComponent> components) {
        for(NavigableComponent comp : components) {
            comp.setParent(this);
            this.components.add(comp);
        }
    }
    
    private void createLookup() {
        List<Lookup> lkps = new ArrayList<Lookup>();
        lkps.add(Lookups.fixed(this));
        for(NavigableComponent comp : components) {
            if(comp instanceof Lookup.Provider)
                lkps.add(((Lookup.Provider) comp).getLookup());
        }
        lookup = new ProxyLookup(lkps.toArray(new Lookup[0]));
    }
    
    private void initPanel() {
        contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0; 
        gc.weightx=1d;gc.weighty=0d;
        gc.anchor=GridBagConstraints.NORTH;
        gc.fill=GridBagConstraints.HORIZONTAL;
        
        for(int i=0, size=components.size(); i<size; i++) {
            if(i == 1)
                gc.insets = new Insets(COMPONENT_SPACING, 0, 0, 0);
            contentPanel.add(components.get(i).getComponent(), gc);
            gc.gridy++;
        }
        
        gc.weighty=1d;
        gc.fill=GridBagConstraints.BOTH;
        contentPanel.add(Box.createGlue(), gc);
        
        contentPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_WIDHT, BORDER_WIDHT, BORDER_WIDHT, BORDER_WIDHT));
        scroller = new JScrollPane(contentPanel);
        scroller.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scroller.getVerticalScrollBar().setBlockIncrement(SCROLL_INCREMENT);

        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
    }
    
    @Override
    public List<NavigableComponent> getChildren() {
        return new ArrayList<NavigableComponent>(components);
    }

    @Override
    public void navigateTo() {
        if(parent != null)
            parent.navigateTo();
        requestActive();
    }

    @Override
    public void navigateToChild(NavigableComponent component) {
        if(components.contains(component)) {
            this.navigateTo();
            Rectangle rect = component.getComponent().getBounds();
            contentPanel.scrollRectToVisible(rect);
        }
    }

    @Override
    public void setParent(NavigableComponent parent) {
        this.parent = parent;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
