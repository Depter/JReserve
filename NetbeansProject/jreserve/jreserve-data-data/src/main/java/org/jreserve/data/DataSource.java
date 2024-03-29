package org.jreserve.data;

import java.util.List;
import org.hibernate.Session;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.query.*;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataSource {
    
    private Session session;
    
    public DataSource() {
    }
    
    public DataSource(Session session) {
        this.session = session;
    }
    
    /**
     * Opens the datasource. Before executing any works, this method should be called
     */
    public void open() {
        if(session != null)
            return;
        session = SessionFactory.openSession();
        session.beginTransaction();
    }
    
    public void commit() {
        if(session != null) {
            session.getTransaction().commit();
            session.close();
        }
        session = null;
    }
    
    public void rollBack() {
        if(session != null) {
            session.getTransaction().rollback();
            session.close();
        }
        session = null;
    }
    
    public Session getSession() {
        return session;
    }
    
    /**
     * Returns the existing {@link ProjectDataType ProjectDataTypes} for the 
     * given {@link Project project}.
     */
    public List<ProjectDataType> getDataTypes(ClaimType claimType) {
        DistinctDataTypesQuery query = new DistinctDataTypesQuery();
        return query.query(session, claimType);
    }
    
    /**
     * Deletes all data for the given criteria.
     */
    public int clearData(DataCriteria criteria) {
        ClearDataQuery query = new ClearDataQuery();
        return query.query(session, criteria);
    }
    
    /**
     * Returns all data for the given criteria.
     */
    public List<ClaimValue> getClaimData(DataCriteria criteria) {
        SelectDataQuery query = new SelectDataQuery();
        return query.query(session, criteria);
    }
    
    /**
     * Saves the given data. If a datapoint for the given dates and
     * data type is already exists, the old value is overwritten.
     */
    public void saveClaimData(List<ClaimValue> data) {
        AddDataQuery query = new AddDataQuery();
        query.add(session, data);
    }
    
    /**
     * Deletes the given data.
     */
    public void deleteData(List<ClaimValue> data) {
        DeleteDataQuery query = new DeleteDataQuery();
        query.delete(session, data);
    }
}
