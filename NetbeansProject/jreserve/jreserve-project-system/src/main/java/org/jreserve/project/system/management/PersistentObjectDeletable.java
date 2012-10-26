package org.jreserve.project.system.management;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistentObjectDeletable<T extends PersistentObject> extends PersistentDeletable<T> {

    private String hql;
    
    public PersistentObjectDeletable(ProjectElement<T> element, String name) {
        super(element);
        this.hql = String.format("DELETE FROM %s WHERE id = :id", name);
    }
    
    @Override
    protected Query createQuery(Session session, T entity) {
        Query query = session.createQuery(hql);
        query.setString("id", entity.getId());
        return query;
    }

}
