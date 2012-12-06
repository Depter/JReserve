package org.jreserve.navigator;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
        scroller.addComponentListener(new ResizeListener());
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
        contentPanel = new JPanel(new NtcLayout(COMPONENT_SPACING));
        
        for(int i=0, size=components.size(); i<size; i++)
            contentPanel.add(components.get(i).getComponent());
        
        contentPanel.add(Box.createVerticalGlue());
        
        contentPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_WIDHT, BORDER_WIDHT, BORDER_WIDHT, BORDER_WIDHT));
        scroller = new JScrollPane(contentPanel);
        scroller.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scroller.getVerticalScrollBar().setBlockIncrement(SCROLL_INCREMENT);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
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
    
    @Override
    public void parentClosed() {
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }
    
    @Override
    protected void componentClosed() {
        super.componentClosed();
        for(NavigableComponent comp : components)
            comp.parentClosed();
    }
    
    private void setContentSize() {
        int barWidth = scroller.getHorizontalScrollBar().getWidth();
        int width = scroller.getWidth() - barWidth;
        int height = contentPanel.getPreferredSize().height;
        contentPanel.setPreferredSize(new Dimension(width, height));
        scroller.revalidate();
    }
    
    private class ResizeListener extends ComponentAdapter {
        
        private boolean firstCall = true;
        
        @Override
        public void componentResized(ComponentEvent e) {
            if(firstCall) {
                firstCall = false;
            } else {
                setContentSize();
            }
        }
   
    }
}
