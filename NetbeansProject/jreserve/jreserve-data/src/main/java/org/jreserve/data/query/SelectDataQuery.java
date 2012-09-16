package org.jreserve.data.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Criteria;
import org.jreserve.data.Data;
import org.jreserve.data.DataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SelectDataQuery extends AbstractQuery implements DataQuery<List<Data>> {
    
    private final static int COL_DT = 0;
    private final static int COL_ACCIDENT = 1;
    private final static int COL_DEVELOPMENT = 2;
    private final static int COL_VALÜE = 3;
    
    private final static String SQL = "SELECT "+
            "c.dataTypeId, c.accidentDate, c.developmentDate, c.claimValue "+
            "FROM ClaimValue c";
    
    @Override
    public List<Data> query(Session session, Criteria criteria) {
        String sql = buildCriteria(SQL, criteria);
        Query query = session.createQuery(sql);
        return getData(query.getResultList());
    }

    private List<Data> getData(List<Object[]> records) {
        List<Data> data = new ArrayList<Data>(records.size());
        for(Object[] record : records)
            data.add(getData(record));
        return data;
    }
    
    private Data getData(Object[] record) {
        return new Data()
            .setDataType(getDataType(record))
            .setAccidentDate((Date) record[COL_ACCIDENT])
            .setDevelopmentDate((Date) record[COL_DEVELOPMENT])
            .setValue(getValue(record));
    }
    
    private DataType getDataType(Object[] record) {
        Number id = (Number) record[COL_DT];
        return DataType.parse(id.intValue());
    }
    
    private double getValue(Object[] record) {
        Number id = (Number) record[COL_VALÜE];
        return id.doubleValue();
    }
}
