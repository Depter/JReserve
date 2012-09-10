package org.jreserve.project.system.management;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistentSavable extends AbstractSavable implements Icon {
    
    public static enum Order {
        UPDATE_SAVE {
            @Override
            protected void execute(PersistentSavable ps) {
                ps.updateEntities();
                ps.saveEntities();
            }
        },
        SAVE_UPDATE {
            @Override
            protected void execute(PersistentSavable ps) {
                ps.saveEntities();
                ps.updateEntities();
            }
        };
        
        protected abstract void execute(PersistentSavable ps);
    };
    
    private final static Logger logger = Logging.getLogger(PersistentSavable.class.getName());
    
    private ProjectElement element;
    private Node elementNode;
    private Icon nodeIcon;
    private Order order = Order.SAVE_UPDATE;
    
    private List<Object> toUpdate = new ArrayList<Object>();
    private List<Object> toSave = new ArrayList<Object>();
    protected Session session;
    
    public PersistentSavable(ProjectElement element, Order order) {
        this(element);
        if(order != null)
            this.order = order;
    }
    
    public PersistentSavable(ProjectElement element) {
        this.element = element;
        this.elementNode = element.createNodeDelegate();
        this.nodeIcon = getNodeIcon();
        toUpdate.add(element.getValue());
        register();
    }
    
    private Icon getNodeIcon() {
        Image img = elementNode.getIcon(BeanInfo.ICON_COLOR_16x16);
        if(img == null)
            return null;
        return new ImageIcon(img);
    }
    
    public void setUpdates(Object... updates) {
        toUpdate.clear();
        addUpdates(updates);
    }
    
    public void addUpdates(Object... updates) {
        toUpdate.addAll(Arrays.asList(updates));
    }
    
    public void setSaves(Object... saves) {
        toSave.clear();
        addUpdates(saves);
    }
    
    public void addSaves(Object... saves) {
        toSave.addAll(Arrays.asList(saves));
    }
    
    @Override
    protected String findDisplayName() {
        return elementNode.getDisplayName();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if(nodeIcon != null)
            nodeIcon.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return nodeIcon==null? 0 : nodeIcon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return nodeIcon==null? 0 : nodeIcon.getIconHeight();
    }

    @Override
    protected void handleSave() throws IOException {
        try {
            initSession();
            order.execute(this);
            session.comitTransaction();
        } catch (Exception ex) {
            session.rollBackTransaction();
            logger.error(ex, "Unable to save entities, transaction is rolled back.");
            throw new IOException(ex);
        } finally {
            element.removeFromLookup(this);
            unregister();
        }
    }

    private void initSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    protected void updateEntities() {
        for(Object o : toUpdate) {
            logger.debug("Updating entity: %s", o);
            session.update(o);
        }
    }
    
    protected void saveEntities() {
        for(Object o : toSave) {
            logger.debug("Saving entity: %s", o);
            session.persist(o);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof PersistentSavable)
            return ((PersistentSavable)o).element.equals(element);
        return false;
    }
    
    @Override
    public int hashCode() {
        return element.hashCode();
    }
}
