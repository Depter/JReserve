package org.jreserve.triangle.guiutil.mvc2.data;

import java.util.Date;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 */
public class LayerCriteria {
    
    private Date accidentFrom;
    private Date accidentEnd;
    private Date developmentFrom;
    private Date developmentEnd;
    private int counter = -1;
    
    public LayerCriteria() {
    }

    public LayerCriteria setAccidentFrom(Date accidentFrom) {
        this.accidentFrom = accidentFrom;
        return this;
    }

    public Date getAccidentFrom() {
        return this.accidentFrom;
    }

    public LayerCriteria setAccidentEnd(Date accidentEnd) {
        this.accidentEnd = accidentEnd;
        return this;
    }

    public Date getAccidentEnd() {
        return this.accidentEnd;
    }

    public LayerCriteria setDevelopmentFrom(Date developmentFrom) {
        this.developmentFrom = developmentFrom;
        return this;
    }

    public Date getDevelopmentFrom() {
        return this.developmentFrom;
    }

    public LayerCriteria setDevelopmentEnd(Date developmentEnd) {
        this.developmentEnd = developmentEnd;
        return this;
    }

    public Date getDevelopmentEnd() {
        return this.developmentEnd;
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
    
    public boolean isValid() {
        if(accidentFrom==null || developmentEnd==null)
            return true;
        return developmentEnd.after(accidentFrom);
    }
    
    void incrementCounter() {
        counter++;
    }
    
    void resetCounter() {
        counter = -1;
    }
    
    public int getCounter() {
        return counter;
    }
}
