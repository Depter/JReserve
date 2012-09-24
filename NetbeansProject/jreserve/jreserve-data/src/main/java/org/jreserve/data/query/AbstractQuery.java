package org.jreserve.data.query;

import java.util.Date;
import org.jreserve.data.Criteria;
import org.jreserve.data.entities.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractQuery {

    private final static String DATE_LITERAL = "{d '%1$tY-%1$tm-%1$td'}";
    
    protected String buildCriteria(String begin, Criteria criteria) {
        StringBuilder sb = new StringBuilder(begin);
        sb.append(" WHERE c.projectId = ").append(criteria.getProject().getId());
        addDataType(sb, criteria.getDataType());
        addFromAccidentDate(sb, criteria.getFromAccidentDate());
        addToAccidentDate(sb, criteria.getToAccidentDate());
        addFromDevelopmentDate(sb, criteria.getFromDevelopmentDate());
        addToDevelopmentDate(sb, criteria.getToDevelopmentDate());
        return sb.toString();
    }
    
    private void addDataType(StringBuilder sb, ProjectDataType dt) {
        if(dt == null)
            return;
        sb.append(" AND c.dataType.id = ").append(dt.getId());
    }

    private void addFromAccidentDate(StringBuilder sb, Date from) {
        if(from == null)
            return;
        sb.append(" AND c.accidentDate >= ").append(getDateLiteral(from));
    }
    
    private String getDateLiteral(Date date) {
        if(date == null)
            return null;
        return String.format(DATE_LITERAL, date);
    }    

    private void addToAccidentDate(StringBuilder sb, Date to) {
        if(to == null)
            return;
        sb.append(" AND c.accidentDate >= ").append(getDateLiteral(to));
    }

    private void addFromDevelopmentDate(StringBuilder sb, Date from) {
        if(from == null)
            return;
        sb.append(" AND c.developmentDate >= ").append(getDateLiteral(from));
    }

    private void addToDevelopmentDate(StringBuilder sb, Date to) {
        if(to == null)
            return;
        sb.append(" AND c.developmentDate >= ").append(getDateLiteral(to));
    }
}
