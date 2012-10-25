package org.jreserve.triangle.mvc.layer;

import java.util.Date;

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
    
    
    
}
