package org.jreserve.data.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.Criteria;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SelectDataQuery extends AbstractQuery implements DataQuery<List<Data<Double>>> {
    
    private final static Logger logger = Logger.getLogger(SelectDataQuery.class.getName());
    
    private final static int COL_DT = 0;
    private final static int COL_ACCIDENT = 1;
    private final static int COL_DEVELOPMENT = 2;
    private final static int COL_VALÜE = 3;
    
    private final static String SQL = 
        "SELECT c.dataType, c.accidentDate, c.developmentDate, c.claimValue " +
        "FROM ClaimValue c";
    
    @Override
    public List<Data<Double>> query(Session session, Criteria criteria) {
        String sql = buildCriteria(SQL, criteria);
        logger.log(Level.FINER, "Query ClaimValues: {0}", sql);
        Query query = session.createQuery(sql);
        return getData(query.list());
    }

    private List<Data<Double>> getData(List<Object[]> records) {
        List<Data<Double>> data = new ArrayList<Data<Double>>(records.size());
        for(Object[] record : records)
            data.add(getData(record));
        return data;
    }
    
    private Data<Double> getData(Object[] record) {
        ProjectDataType dt = getDataType(record);
        Date accident = (Date) record[COL_ACCIDENT];
        Date development = (Date) record[COL_DEVELOPMENT];
        double value = getValue(record);
        return new Data<Double>(dt, accident, development, value);
    }
    
    private ProjectDataType getDataType(Object[] record) {
        return (ProjectDataType) record[COL_DT];
    }
    
    private double getValue(Object[] record) {
        Number id = (Number) record[COL_VALÜE];
        return id.doubleValue();
    }
}