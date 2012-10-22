package org.jreserve.audit.global;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.audit.util.AuditorRegistry;
import org.jreserve.persistence.SessionFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AuditedEntityChildFactory extends ChildFactory<AuditedEntity> {

    private final static Logger logger = Logger.getLogger(AuditedEntityChildFactory.class.getName());
    
    private Object parent;
    private Session session;
    
    AuditedEntityChildFactory(AuditedEntity parent) {
        this.parent = parent.getEntity();
    }

    @Override
    protected boolean createKeys(List<AuditedEntity> list) {
        if(!SessionFactory.isConnected())
            return true;
        return queryKeys(list);
    }
    
    private boolean queryKeys(List<AuditedEntity> list) {
        try {
            AuditReader reader = createReader();
            for(Auditor auditor : AuditorRegistry.getAuditors(parent))
                list.addAll(auditor.getAuditedEntities(reader, parent));
            return true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to query audited entities for entity: {0}", parent);
            return false;
        } finally {
            closeSession();
        }
    }
    
    private AuditReader createReader() {
        session = SessionFactory.openSession();
        return AuditReaderFactory.get(session);
    }

    private void closeSession() {
        if(session != null) {
            session.close();
            session = null;
        }
    }
    
    @Override
    protected Node createNodeForKey(AuditedEntity key) {
        return new AuditedEntityNode(key);
    }
}
