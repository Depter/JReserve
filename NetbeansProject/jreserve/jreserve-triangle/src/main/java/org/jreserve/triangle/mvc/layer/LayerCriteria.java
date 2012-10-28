package org.jreserve.triangle.mvc.layer;

import java.util.Date;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LayerCriteria {
    
    private Date accidentFrom;
    private Date accidentEnd;
    private Date developmentFrom;
    private Date developmentEnd;
    
    public LayerCriteria() {
    }

    public LayerCriteria setAccidentEnd(Date accidentEnd) {
        this.accidentEnd = accidentEnd;
        return this;
    }

    public LayerCriteria setAccidentFrom(Date accidentFrom) {
        this.accidentFrom = accidentFrom;
        return this;
    }

    public LayerCriteria setDevelopmentEnd(Date developmentEnd) {
        this.developmentEnd = developmentEnd;
        return this;
    }

    public LayerCriteria setDevelopmentFrom(Date developmentFrom) {
        this.developmentFrom = developmentFrom;
        return this;
    }
    
    public boolean acceptsData(Data data) {
        return withinAccident(data.getAccidentDate()) && 
               withinDevelopment(data.getDevelopmentDate());
    }
    
    private boolean withinAccident(Date accident) {
        if(accidentFrom != null && accident.before(accidentFrom))
            return false;
        if(accidentEnd != null && !accident.before(accidentEnd))
            return false;
        return true;
    }
    
    private boolean withinDevelopment(Date development) {
        if(developmentFrom != null && development.before(developmentFrom))
            return false;
        if(developmentEnd != null && !development.before(developmentEnd))
            return false;
        return true;
    }
}
