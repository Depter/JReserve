package org.jreserve.data.query;

import java.util.Date;
import org.jreserve.data.Criteria;
import org.jreserve.data.DataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractQuery {

    private final static String DATE_LITERAL = "{d '%1$tY-%1$tm-%1$td'}";
    
    protected String buildCriteria(String begin, Criteria criteria) {
        StringBuilder sb = new StringBuilder(begin);
        sb.append(" WHERE c.claimTypeId = ").append(criteria.getClaimType().getId());
        addDataType(sb, criteria.getDataType());
        addFromDate(sb, criteria.getFromDate());
        addToDate(sb, criteria.getToDate());
        return sb.toString();
    }
    
    private void addDataType(StringBuilder sb, DataType dt) {
        if(dt == null)
            return;
        sb.append(" AND c.dataTypeId = ").append(dt.getDbId());
    }

    private void addFromDate(StringBuilder sb, Date from) {
        if(from == null)
            return;
        sb.append(" AND c.accidentDate >= ").append(getDateLiteral(from));
    }

    private void addToDate(StringBuilder sb, Date to) {
        if(to == null)
            return;
        sb.append(" AND c.developmentDate >= ").append(getDateLiteral(to));
    }
    
    private String getDateLiteral(Date date) {
        if(date == null)
            return null;
        return String.format(DATE_LITERAL, date);
    }
    
}
