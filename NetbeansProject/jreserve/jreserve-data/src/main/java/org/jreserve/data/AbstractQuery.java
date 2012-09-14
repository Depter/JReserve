package org.jreserve.data;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractQuery {

    
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
        sb.append(" AND c.accidentDate >= ").append(Criteria.getDateLiteral(from));
    }

    private void addToDate(StringBuilder sb, Date to) {
        if(to == null)
            return;
        sb.append(" AND c.developmentDate >= ").append(Criteria.getDateLiteral(to));
    }
    
}
