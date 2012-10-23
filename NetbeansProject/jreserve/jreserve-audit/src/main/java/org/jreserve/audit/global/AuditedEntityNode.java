package org.jreserve.audit.global;

import java.awt.Image;
import javax.swing.Action;
import org.jreserve.audit.Auditable;
import org.jreserve.audit.AuditedEntity;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AuditedEntityNode extends AbstractNode {

    private static Lookup getLookup(AuditedEntity entity) {
        Object value = entity.getEntity();
        if(value == null)
            return Lookup.EMPTY;
        return Lookups.fixed(new NodeAuditable());
    }
    
    private AuditedEntity entity;
    
    AuditedEntityNode(AuditedEntity entity) {
        super(Children.create(new AuditedEntityChildFactory(entity), true), getLookup(entity));
        this.entity = entity;
        setDisplayName(entity.getDisplayName());
        initAuditable();
    }
    
    private void initAuditable() {
        NodeAuditable auditable = getLookup().lookup(NodeAuditable.class);
        if(auditable != null)
            auditable.setNode(this);
    }
    
    void refreshChildren() {
        AuditedEntityChildFactory factory = new AuditedEntityChildFactory(entity);
        super.setChildren(Children.create(factory, true));
    }
    
    @Override
    public Image getIcon(int type) {
        Image img = entity.getImage();
        return img!=null? img : super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{new RefreshAction(this)};
    }
    
    
    
    static class NodeAuditable implements Auditable {
        
        private AuditedEntityNode node;
        private String path = null;
        
        private NodeAuditable() {
        }

        @Override
        public Object getAuditableEntity() {
            return node.entity.getEntity();
        }
        
        void setNode(AuditedEntityNode node) {
            this.node = node;
        }
        
        AuditedEntityNode getNode() {
            return this.node;
        }
        
        private void buildPath(AuditedEntityNode node) {
            path = "";
            while(node.entity.getEntity() != null) {
                if(path.length() > 0)
                    path = "/"+path;
                path = node.getDisplayName() + path;
                node = (AuditedEntityNode) node.getParentNode();
            }
        }

        @Override
        public String getDisplayName() {
            if(path == null)
                buildPath(node);
            return path;
        }
        
    }
    
}
