package org.jreserve.data.query;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.entities.ClaimValue;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractQuery {
    private final static String DATE_LITERAL = "'%1$tY-%1$tm-%1$td'";
    
    protected ClaimValue queryUniqueResult(Session session, DataCriteria criteria) {
        Query query = session.createQuery(createHQL(criteria));
        return (ClaimValue) query.uniqueResult();
    }
    
    protected List<ClaimValue> queryList(Session session, DataCriteria criteria) {
        Query query = session.createQuery(createHQL(criteria));
        return query.list();
    }
    
    protected String createHQL(DataCriteria criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM ").append(QueryUtil.getEntityName(ClaimValue.class)).append(" d");
        appendOwner(sb, criteria);
        return sb.toString();
    }
    
    private void appendOwner(StringBuilder sb, DataCriteria criteria) {
        sb.append(" WHERE d.dataType = '");
        sb.append(criteria.getOwnerId()).append('\'');
        appendFromAccidentDate(sb, criteria);
    }
    
    private void appendFromAccidentDate(StringBuilder sb, DataCriteria criteria) {
        Date from = criteria.getFromAccidentDate();
        if(from != null)
            sb.append(" AND d.accidentDate")
              .append(getStrEqt(criteria.getFromAccidentEqt()))
              .append(getDateLiteral(from));
        appendToAccidentDate(sb, criteria);
    }
    
    private String getStrEqt(DataCriteria.EQT eqt) {
        switch(eqt) {
            case GE:
                return " >= ";
            case GT:
                return " > ";
            case EQ:
                return " = ";
            case LT:
                return " < ";
            case LE:
                return " <= ";
            default:
                throw new IllegalArgumentException("Unknown EQT: "+eqt);
        }
    }
    
    private String getDateLiteral(Date date) {
        return String.format(DATE_LITERAL, date);    
    }
    
    private void appendToAccidentDate(StringBuilder sb, DataCriteria criteria) {
        Date to = criteria.getToAccidentDate();
        if(to != null)
            sb.append(" AND d.accidentDate")
              .append(getStrEqt(criteria.getToAccidentEqt()))
              .append(getDateLiteral(to));
        appendFromDevelopmentDate(sb, criteria);
    }
    
    private void appendFromDevelopmentDate(StringBuilder sb, DataCriteria criteria) {
        Date from = criteria.getFromDevelopmentDate();
        if(from != null)
            sb.append(" AND d.developmentDate")
              .append(getStrEqt(criteria.getFromDevelopmentEqt()))
              .append(getDateLiteral(from));
        appendToDevelopmentDate(sb, criteria);
    }
    
    private void appendToDevelopmentDate(StringBuilder sb, DataCriteria criteria) {
        Date to = criteria.getToDevelopmentDate();
        if(to != null)
            sb.append(" AND d.developmentDate")
              .append(getStrEqt(criteria.getToDevelopmentEqt()))
              .append(getDateLiteral(to));
    }
}
