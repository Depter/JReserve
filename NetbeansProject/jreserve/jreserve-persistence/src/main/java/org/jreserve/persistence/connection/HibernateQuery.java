package org.jreserve.persistence.connection;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jreserve.persistence.Query;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class HibernateQuery implements Query {

    private org.hibernate.Query query;
    
    HibernateQuery(org.hibernate.Query query) {
        this.query = query;
    }
    
    @Override
    public int executeUpdate() {
        return query.executeUpdate();
    }

    @Override
    public List getResultList() {
        return query.list();
    }

    @Override
    public Object getSingleResult() {
        return query.uniqueResult();
    }

    @Override
    public Query setFirstResult(int startPosition) {
        query.setFirstResult(startPosition);
        return this;
    }

    @Override
    public Query setFlushMode(Query.FlushMode flushMode) {
        switch(flushMode) {
            case AUTO:
                query.setFlushMode(org.hibernate.FlushMode.AUTO);
                break;
            case COMMIT:
                query.setFlushMode(org.hibernate.FlushMode.COMMIT);
                break;
            default:
                throw new IllegalArgumentException("Unknown FlushMode: "+flushMode);
        }
        return this;
    }

    @Override
    public Query setMaxResults(int maxResult) {
        query.setMaxResults(maxResult);
        return this;
    }

    @Override
    public Query setParameter(int position, Calendar value, Temporal temporalType) {
        return setParameter(position, value.getTime(), temporalType);
    }

    @Override
    public Query setParameter(int position, Date value, Temporal temporalType) {
        switch(temporalType) {
            case DATE:
                query.setDate(position, value);
                break;
            case TIME:
                query.setTime(position, value);
                break;
            case TIMESTAMP:
                query.setTimestamp(position, value);
            default:
                throw new IllegalArgumentException("Unknown temporal type: "+temporalType);
        }
        return this;
    }

    @Override
    public Query setParameter(int position, Object value) {
        query.setParameter(position, value);
        return this;
    }

    @Override
    public Query setParameter(String name, Object value) {
        query.setParameter(name, value);
        return this;
    }

    @Override
    public Query setParameter(String name, Calendar value, Temporal temporalType) {
        return setParameter(name, value.getTime(), temporalType);
    }

    @Override
    public Query setParameter(String name, Date value, Temporal temporalType) {
        switch(temporalType) {
            case DATE:
                query.setDate(name, value);
                break;
            case TIME:
                query.setTime(name, value);
                break;
            case TIMESTAMP:
                query.setTimestamp(name, value);
            default:
                throw new IllegalArgumentException("Unknown temporal type: "+temporalType);
        }
        return this;
    }
}
