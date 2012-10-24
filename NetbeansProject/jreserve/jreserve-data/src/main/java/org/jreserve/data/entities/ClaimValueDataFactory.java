package org.jreserve.data.entities;

import java.util.Date;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimValueDataFactory<O extends PersistentObject> implements DataFactory<O, ClaimValue, Double> {

    private O owner;
    
    public ClaimValueDataFactory(O owner) {
        this.owner = owner;
    }
    
    @Override
    public Data<O, Double> createData(ClaimValue entity) {
        Date accident = entity.getAccidentDate();
        Date development = entity.getDevelopmentDate();
        double value = entity.getClaimValue();
        return new Data<O, Double>(owner, accident, development, value);
    }

}
