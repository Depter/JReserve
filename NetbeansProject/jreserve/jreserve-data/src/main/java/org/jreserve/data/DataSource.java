package org.jreserve.data;

import java.util.List;
import org.jreserve.data.query.*;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ClaimType;

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
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    public void commit() {
        if(session != null)
            session.comitTransaction();
    }
    
    public void rollBack() {
        if(session != null)
            session.rollBackTransaction();
    }
    
    /**
     * Returns the existing {@link DataType DataTypes} for the give
     * {@link ClaimType ClaimType}.
     */
    public List<DataType> getDataTypes(ClaimType ct) {
        DataQuery<List<DataType>> query = new DistinctDataTypesQuery();
        return query.query(session, new Criteria(ct));
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
     * Saves the given datas. If a datapoint for the given dates and
     * data type is already exists, the old value is preserved.
     */
    public void addIfNotExists(ClaimType ct, List<Data> data) {
        AddDataQuery query = new AddDataQuery(false);
        query.add(session, ct, data);
    }
    
    /**
     * Saves the given datas. If a datapoint for the given dates and
     * data type is already exists it will be overwritten with the
     * new value.
     */
    public void overwriteIfExists(ClaimType ct, List<Data> data) {
        AddDataQuery query = new AddDataQuery(true);
        query.add(session, ct, data);
    }
}
