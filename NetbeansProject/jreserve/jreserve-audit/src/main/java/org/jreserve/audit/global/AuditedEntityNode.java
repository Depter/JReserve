package org.jreserve.audit.global;

import java.awt.Image;
import org.jreserve.audit.AuditedEntity;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AuditedEntityNode extends AbstractNode {

    private AuditedEntity entity;
    
    AuditedEntityNode(AuditedEntity entity) {
        super(Children.create(new AuditedEntityChildFactory(entity), true), Lookups.fixed(entity.getEntity()));
        setDisplayName(entity.getDisplayName());
    }

    @Override
    public Image getIcon(int type) {
        return entity.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    
}
