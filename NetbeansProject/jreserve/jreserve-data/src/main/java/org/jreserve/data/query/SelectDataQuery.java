package org.jreserve.data.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.AbstractData;
import org.jreserve.data.entities.DataFactory;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SelectDataQuery<E extends AbstractData, O extends PersistentObject, V> extends AbstractQuery<E> implements DataQuery<O, List<Data<O, V>>> {
    
    private DataFactory<O, E, V> dataFactory;

    public SelectDataQuery(Class<E> entityClass, DataFactory<O, E, V> dataFactory) {
        super(entityClass);
        this.dataFactory = dataFactory;
    }
    
    @Override
    public List<Data<O, V>> query(Session session, DataCriteria<O> criteria) {
        List<Data<O, V>> result = new ArrayList<Data<O, V>>();
        for(E value : queryList(session, criteria))
            result.add(dataFactory.createData(value));
        return result;
    }
//    
//    @Override
//    public List<Data<T, Double>> query(Session session, DataCriteria<T> criteria) {
//        String sql = buildCriteria(SQL, criteria);
//        logger.log(Level.FINER, "Query ClaimValues: {0}", sql);
//        Query query = session.createQuery(sql);
//        return getData(query.list());
//    }
//
//    private List<Data<Double>> getData(List<Object[]> records) {
//        List<Data<Double>> data = new ArrayList<Data<Double>>(records.size());
//        for(Object[] record : records)
//            data.add(getData(record));
//        return data;
//    }
//    
//    private Data<Double> getData(Object[] record) {
//        ProjectDataType dt = getDataType(record);
//        Date accident = (Date) record[COL_ACCIDENT];
//        Date development = (Date) record[COL_DEVELOPMENT];
//        double value = getValue(record);
//        return new Data<Double>(dt, accident, development, value);
//    }
//    
//    private ProjectDataType getDataType(Object[] record) {
//        return (ProjectDataType) record[COL_DT];
//    }
//    
//    private double getValue(Object[] record) {
//        Number id = (Number) record[COL_VALÜE];
//        return id.doubleValue();
//    }
}