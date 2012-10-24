package org.jreserve.data;

import java.util.List;
import org.hibernate.Session;
import org.jreserve.data.entities.*;
import org.jreserve.data.query.*;
import org.jreserve.persistence.PersistentObject;
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
        ClearDataQuery query = new ClearDataQuery(ClaimValue.class);
        return query.query(session, criteria);
    }
    
    /**
     * Returns all data for the given criteria.
     */
    public List<Data<ProjectDataType, Double>> getClaimData(DataCriteria<ProjectDataType> criteria) {
        ClaimValueDataFactory<ProjectDataType> dataFactory = new ClaimValueDataFactory<ProjectDataType>(criteria.getOwner());
        SelectDataQuery<ClaimValue, ProjectDataType, Double> query = new SelectDataQuery<ClaimValue, ProjectDataType, Double>(ClaimValue.class, dataFactory);
        return query.query(session, criteria);
    }
    
    /**
     * Saves the given data. If a datapoint for the given dates and
     * data type is already exists, the old value is overwritten.
     */
    public void saveClaimData(List<Data<ProjectDataType, Double>> data) {
        AddDataQuery query = new AddDataQuery();
        query.add(session, data);
    }
    
    /**
     * Deletes the given data.
     */
    public void deleteData(List<Data<ProjectDataType, Double>> data) {
        DeleteDataQuery query = new DeleteDataQuery();
        query.delete(session, data);
    }
    
    /**
     * Returns the data for the given criteria.
     */
    public <T extends PersistentObject> List<Data<T, Double>> getCorrections(DataCriteria<T> criteria) {
        DataCorrectionFactory<T> factory = new DataCorrectionFactory<T>(criteria.getOwner());
        SelectDataQuery<DataCorrection, T, Double> query = new SelectDataQuery<DataCorrection, T, Double>(DataCorrection.class, factory);
        return query.query(session, criteria);
    }
    
}
