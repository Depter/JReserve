package org.jreserve.data.query;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.entities.AbstractData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractQuery<T extends AbstractData> {
    private final static String DATE_LITERAL = "'%1$tY-%1$tm-%1$td'";
    
    private final Class<T> entityClass;
    
    protected AbstractQuery(Class<T> entityClass) {
        if(entityClass == null)
            throw new NullPointerException("Entity class can not be null!");
        this.entityClass = entityClass;
    }
    
    protected T queryUniqueResult(Session session, DataCriteria criteria) {
        Query query = session.createQuery(createHQL(criteria));
        return (T) query.uniqueResult();
    }
    
    protected List<T> queryList(Session session, DataCriteria criteria) {
        Query query = session.createQuery(createHQL(criteria));
        return query.list();
    }
    
//    protected Criteria buildQuery(Session session, DataCriteria criteria) {
//        Criteria hCrit = session.createCriteria(entityClass);
//        appendOwner(hCrit, criteria);
//        return hCrit;
//    }
//    
//    private void appendOwner(Criteria hCrit, DataCriteria criteria) {
//        String ownerId = criteria.getOwnerId();
//        hCrit.add(restrict("ownerId", ownerId, DataCriteria.EQT.EQ));
//        appendFromAccidentDate(hCrit, criteria);
//    }
//    
//    private Criterion restrict(String property, Object value, DataCriteria.EQT eqt) {
//        switch(eqt) {
//            case GE:
//                return Restrictions.ge(property, value);
//            case GT:
//                return Restrictions.gt(property, value);
//            case EQ:
//                return Restrictions.eq(property, value);
//            case LT:
//                return Restrictions.lt(property, value);
//            case LE:
//                return Restrictions.le(property, value);
//            default:
//                throw new IllegalArgumentException("Unknown EQT: "+eqt);
//        }
//    }
//    
//    private void appendFromAccidentDate(Criteria hCrit, DataCriteria criteria) {
//        Date from = criteria.getFromAccidentDate();
//        if(from != null)
//            hCrit.add(restrict("accidentDate", from, criteria.getFromAccidentEqt()));
//        appendToAccidentDate(hCrit, criteria);
//    }
//
//    private void appendToAccidentDate(Criteria hCrit, DataCriteria criteria) {
//        Date to = criteria.getToAccidentDate();
//        if(to != null)
//            hCrit.add(restrict("accidentDate", to, criteria.getToAccidentEqt()));
//        appendFromDevelopmentDate(hCrit, criteria);
//    }
//    
//    private void appendFromDevelopmentDate(Criteria hCrit, DataCriteria criteria) {
//        Date from = criteria.getFromDevelopmentDate();
//        if(from != null)
//            hCrit.add(restrict("developmentDate", from, criteria.getFromDevelopmentEqt()));
//        appendToDevelopmentDate(hCrit, criteria);
//    }
//
//    private void appendToDevelopmentDate(Criteria hCrit, DataCriteria criteria) {
//        Date to = criteria.getToDevelopmentDate();
//        if(to != null)
//            hCrit.add(restrict("developmentDate", to, criteria.getToDevelopmentEqt()));
//    }
    
    protected String createHQL(DataCriteria criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM ").append(QueryUtil.getEntityName(entityClass)).append(" d");
        appendOwner(sb, criteria);
        return sb.toString();
    }
    
    private void appendOwner(StringBuilder sb, DataCriteria criteria) {
        sb.append(" WHERE d.ownerId = '");
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
