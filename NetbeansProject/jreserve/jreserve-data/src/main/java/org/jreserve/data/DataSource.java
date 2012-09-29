package org.jreserve.data;

import java.util.List;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.data.query.*;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataSource {
    
    private Session session;
    
    /**
     * Opens the datasource. Before executing any works, this method should be called
     */
    public void open() {
        if(session != null)
            return;
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    public void commit() {
        if(session != null)
            session.comitTransaction();
        session = null;
    }
    
    public void rollBack() {
        if(session != null)
            session.rollBackTransaction();
        session = null;
    }
    
    public Session getSession() {
        return session;
    }
    
    /**
     * Returns the existing {@link ProjectDataType ProjectDataTypes} for the 
     * given {@link Project project}.
     */
    public List<ProjectDataType> getDataTypes(Project project) {
        DataQuery<List<ProjectDataType>> query = new DistinctDataTypesQuery();
        return query.query(session, new Criteria(project));
    }
    
    /**
     * Deletes all data for the given criteria.
     */
    public int clearData(Criteria criteria) {
        DataQuery<Integer> query = new ClearDataQuery();
        return query.query(session, criteria);
    }
    
    /**
     * Returns all data for the given criteria.
     */
    public List<Data> getData(Criteria criteria) {
        DataQuery<List<Data>> query = new SelectDataQuery();
        return query.query(session, criteria);
    }
    
    /**
     * Saves the given data. If a datapoint for the given dates and
     * data type is already exists, the old value is overwritten.
     */
    public void saveData(List<Data> data) {
        AddDataQuery query = new AddDataQuery();
        query.add(session, data);
    }
    
    /**
     * Deletes the given data.
     */
    public void deleteData(List<Data> data) {
        DeleteDataQuery query = new DeleteDataQuery();
        query.delete(session, data);
    }
}
